package com.example.to_dolistapp.presentation.todolist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.data.mappers.toTodoEntity
import com.example.domain.common.SortOrder
import com.example.domain.models.Todo
import com.example.to_dolistapp.R
import com.example.to_dolistapp.databinding.FragmentTodoListBinding
import com.example.to_dolistapp.presentation.base.BaseFragment
import com.example.to_dolistapp.utils.TodoAlarmManager
import com.example.to_dolistapp.utils.collectOnLifecycle
import com.example.to_dolistapp.utils.onQueryTextChanged
import com.example.to_dolistapp.utils.showToast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

@AndroidEntryPoint
class TodoListFragment :
    BaseFragment<FragmentTodoListBinding>(FragmentTodoListBinding::inflate),
    TodoListAdapter.OnTodoClickListener {

    private val viewModel: TodoListViewModel by viewModels()
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TodoListAdapter(this)
    }
    private val navController by lazy(LazyThreadSafetyMode.NONE) {
        findNavController()
    }
    private var todoList: List<Todo> = emptyList()
    private var searchView: SearchView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        collectOnLifecycle(viewModel.allTodoList) { todoList ->
            this.todoList = todoList
            adapter.submitList(todoList)
            binding.imageTodoList.visibility =
                if (todoList.isEmpty()) View.VISIBLE else View.INVISIBLE
        }

        binding.apply {
            todosRecyclerView.adapter = adapter

            addTodoFab.setOnClickListener {
                navController.navigate(R.id.action_todoListFragment_to_addTodoFragment)
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val todo = adapter.currentList[position]
                onDeleteTodo(todo)
            }
        }).attachToRecyclerView(binding.todosRecyclerView)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard(requireActivity())
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val searchQuery = viewModel.searchQuery.value
        if (searchQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView?.setQuery(searchQuery, false)
        }

        searchView?.onQueryTextChanged { text ->
            viewModel.searchQuery.value = text
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_by_name -> viewModel.onSortedOrderSelected(SortOrder.BY_NAME)
            R.id.action_sort_by_date_created -> viewModel.onSortedOrderSelected(SortOrder.BY_DATE)
            R.id.action_delete_all -> deleteAllTodo()
            R.id.action_delete_all_completed -> deleteAllCompletedTodo()
            R.id.action_add_reminder_todo -> addReminderOfUncompletedTodo()
            R.id.action_cancel_reminder -> cancelReminder(requireContext())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cancelReminder(context: Context) {
        if (TodoAlarmManager.isAlarmSet(context)) {
            TodoAlarmManager.cancelReminder(context)
            showToast(R.string.reminder_canceled)
            return
        }
        showToast(R.string.you_dont_have_reminder)
    }

    private fun addReminderOfUncompletedTodo() {
        viewModel.viewModelScope.launch {
            viewModel.getAllUncompleted().collect { todoList ->
                if (todoList.isNotEmpty()) {
                    val materialTimePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText(R.string.select_time_for_reminder)
                        .build()

                    materialTimePicker.addOnPositiveButtonClickListener {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.MILLISECOND, 0)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MINUTE, materialTimePicker.minute)
                        calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.hour)

                        TodoAlarmManager.createAlarm(requireActivity(), calendar)
                        showToast(
                            "You will get a reminder in ${
                                DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.time)
                            }",
                            Toast.LENGTH_LONG
                        )
                    }
                    materialTimePicker.show(parentFragmentManager, "time-picker")
                } else {
                    showToast(R.string.you_dont_have_uncompleted_todos)
                }
                this.coroutineContext.job.cancel()
            }
        }
    }

    private fun createAlertDialog(message: String, positiveButtonClickListener: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.confirm_action)
            .setMessage(message)
            .setPositiveButton("Confirm") { dialog, _ ->
                positiveButtonClickListener.invoke()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    private fun deleteAllTodo() {
        if (todoList.isNotEmpty()) {
            createAlertDialog("Delete all to-do's?") {
                viewModel.deleteAll()
            }
        } else {
            showToast(R.string.you_dont_have_todos)
        }
    }

    private fun deleteAllCompletedTodo() {
        viewModel.viewModelScope.launch {
            viewModel.getAllCompleted().collect { todoList ->
                if (todoList.isNotEmpty()) {
                    createAlertDialog("Delete all completed to-do's?") {
                        viewModel.deleteAllCompleted()
                    }
                } else {
                    showToast(R.string.you_dont_have_completed_todos)
                }
                this.coroutineContext.job.cancel()
            }
        }
    }

    override fun onTodoClick(todo: Todo) {
        navController.navigate(
            TodoListFragmentDirections.actionTodoListFragmentToUpdateTodoFragment(
                todo.toTodoEntity()
            )
        )
    }

    override fun onDeleteTodo(todo: Todo) {
        viewModel.delete(todo)
        Snackbar.make(requireView(), "To-do deleted", Snackbar.LENGTH_LONG).apply {
            setAction("Undo") {
                viewModel.insert(todo)
            }
            show()
        }
    }

    override fun onCheckBoxClick(todo: Todo, isCompleted: Boolean) {
        viewModel.onTodoCheckedChanged(todo, isCompleted)
    }

    override fun onDestroyView() {
        searchView?.setOnQueryTextListener(null)
        super.onDestroyView()
    }
}
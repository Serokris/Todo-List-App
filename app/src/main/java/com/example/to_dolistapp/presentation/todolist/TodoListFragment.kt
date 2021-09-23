package com.example.to_dolistapp.presentation.todolist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolistapp.R
import com.example.to_dolistapp.data.source.local.SortOrder
import com.example.to_dolistapp.databinding.FragmentTodoListBinding
import com.example.to_dolistapp.domain.models.Todo
import com.example.to_dolistapp.utils.TodoAlarmManager
import com.example.to_dolistapp.utils.observeOnce
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*

@AndroidEntryPoint
class TodoListFragment : Fragment(), TodoListAdapter.OnTodoClickListener {

    private val viewModel: TodoListViewModel by viewModels()
    private lateinit var adapter: TodoListAdapter
    private lateinit var navController: NavController
    private lateinit var binding: FragmentTodoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        adapter = TodoListAdapter(this)

        viewModel.getAllTodo.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.imageTodoList.visibility =
                if (list.isEmpty()) View.VISIBLE else View.INVISIBLE
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

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null) {
                    runQuery(text)
                }
                return true
            }
        })
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
            Toast.makeText(context, R.string.reminder_canceled, Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(context, R.string.you_dont_have_reminder, Toast.LENGTH_SHORT).show()
    }

    private fun runQuery(query: String) {
        val searchQuery = "%$query%"
        viewModel.databaseSearch(searchQuery).observe(viewLifecycleOwner, { tasks ->
            adapter.submitList(tasks)
        })
    }

    private fun addReminderOfUncompletedTodo() {
        viewModel.getAllUncompleted().observeOnce { list ->
            if (list.isNotEmpty()) {
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
                    Toast.makeText(
                        context,
                        "You will get a reminder in ${
                            DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.time)
                        }",
                        Toast.LENGTH_LONG
                    ).show()
                }
                materialTimePicker.show(requireFragmentManager(), "time-picker")
            } else {
                Toast.makeText(
                    context,
                    R.string.you_dont_have_uncompleted_todos,
                    Toast.LENGTH_SHORT
                ).show()
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
        if (viewModel.getAllTodo.value?.isNotEmpty() == true) {
            createAlertDialog("Delete all to-do's?") {
                viewModel.deleteAll()
            }
        } else {
            Toast.makeText(requireContext(), R.string.you_dont_have_todos, Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteAllCompletedTodo() {
        viewModel.getAllCompleted().observeOnce { list ->
            if (list.isNotEmpty()) {
                createAlertDialog("Delete all completed to-do's?") {
                    viewModel.deleteAllCompleted()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.you_dont_have_completed_todos,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onTodoClick(todo: Todo) {
        navController.navigate(
            TodoListFragmentDirections.actionTodoListFragmentToUpdateTodoFragment(
                todo
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
}
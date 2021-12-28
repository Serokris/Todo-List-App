package com.example.to_dolistapp.presentation.updatetodo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.common.TodoAddUpdateEvent
import com.example.domain.models.Todo
import com.example.to_dolistapp.R
import com.example.to_dolistapp.databinding.FragmentUpdateTodoBinding
import com.example.to_dolistapp.presentation.base.BaseFragment
import com.example.to_dolistapp.utils.collectLatestOnLifecycle
import com.example.to_dolistapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateTodoFragment :
    BaseFragment<FragmentUpdateTodoBinding>(FragmentUpdateTodoBinding::inflate) {

    private val viewModel: UpdateTodoViewModel by viewModels()
    private val safeArgs: UpdateTodoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
        initViews()
    }

    private fun subscribeUi() {
        collectLatestOnLifecycle(viewModel.todoUpdateEvent) { todoUpdateEvent ->
            when (todoUpdateEvent) {
                is TodoAddUpdateEvent.Success -> {
                    showToast(todoUpdateEvent.message.toString())
                    findNavController().navigate(R.id.action_updateTodoFragment_to_todoListFragment)
                }
                is TodoAddUpdateEvent.Error -> {
                    showToast(todoUpdateEvent.message.toString())
                }
                TodoAddUpdateEvent.Empty -> {
                }
            }
        }
    }

    private fun initViews() {
        binding.apply {
            newTodoDescriptionEdt.setText(safeArgs.todo.description)

            updateButton.setOnClickListener {
                val todo = Todo(
                    safeArgs.todo.id,
                    newTodoDescriptionEdt.text.toString().trim(),
                    safeArgs.todo.isCompleted
                )
                viewModel.update(todo)
            }
        }
    }
}
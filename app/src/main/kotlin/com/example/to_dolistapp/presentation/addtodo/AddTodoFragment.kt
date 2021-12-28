package com.example.to_dolistapp.presentation.addtodo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.common.TodoAddUpdateEvent
import com.example.domain.models.Todo
import com.example.to_dolistapp.R
import com.example.to_dolistapp.databinding.FragmentAddTodoBinding
import com.example.to_dolistapp.presentation.base.BaseFragment
import com.example.to_dolistapp.utils.collectLatestOnLifecycle
import com.example.to_dolistapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTodoFragment :
    BaseFragment<FragmentAddTodoBinding>(FragmentAddTodoBinding::inflate) {

    private val viewModel: AddTodoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
        initViews()
    }

    private fun subscribeUi() {
        collectLatestOnLifecycle(viewModel.todoInsertionEvent) { todoAddEvent ->
            when (todoAddEvent) {
                is TodoAddUpdateEvent.Success -> {
                    showToast(todoAddEvent.message.toString())
                    findNavController().navigate(R.id.action_addTodoFragment_to_todoListFragment)
                }
                is TodoAddUpdateEvent.Error -> {
                    showToast(todoAddEvent.message.toString())
                }
                TodoAddUpdateEvent.Empty -> {
                }
            }
        }
    }

    private fun initViews() {
        binding.apply {
            addTodoButton.setOnClickListener {
                val todo = Todo(
                    0,
                    todoDescriptionEdt.text.toString().trim(),
                    false
                )
                viewModel.insert(todo)
            }
        }
    }
}
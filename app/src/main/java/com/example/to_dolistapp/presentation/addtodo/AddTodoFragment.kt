package com.example.to_dolistapp.presentation.addtodo

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.to_dolistapp.R
import com.example.to_dolistapp.databinding.FragmentAddTodoBinding
import com.example.to_dolistapp.presentation.base.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTodoFragment :
    BaseBindingFragment<FragmentAddTodoBinding>(FragmentAddTodoBinding::inflate) {

    private val viewModel: AddTodoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            addTodoButton.setOnClickListener {
                if (TextUtils.isEmpty(todoDescriptionEdt.text)) {
                    Toast.makeText(
                        requireContext(),
                        R.string.field_must_not_be_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val todoDescription = todoDescriptionEdt.text.toString().trim()

                val todo = com.example.domain.models.Todo(
                    0,
                    todoDescription,
                    false
                )

                viewModel.insert(todo)
                Toast.makeText(
                    requireContext(),
                    R.string.todo_successfully_added,
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(R.id.action_addTodoFragment_to_todoListFragment)
            }
        }
    }
}
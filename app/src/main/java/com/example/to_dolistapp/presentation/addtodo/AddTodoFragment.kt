package com.example.to_dolistapp.presentation.addtodo

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.to_dolistapp.R
import com.example.to_dolistapp.databinding.FragmentAddTodoBinding
import com.example.to_dolistapp.domain.models.Todo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTodoFragment : Fragment() {

    private val viewModel: AddTodoViewModel by viewModels()
    private lateinit var binding: FragmentAddTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTodoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

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

                val todo = Todo(
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
                navController.navigate(R.id.action_addTodoFragment_to_todoListFragment)
            }
        }
    }
}
package com.example.to_dolistapp.presentation.updatetodo

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
import com.example.to_dolistapp.databinding.FragmentUpdateTodoBinding
import com.example.to_dolistapp.domain.models.Todo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateTodoFragment : Fragment() {

    private val viewModel: UpdateTodoViewModel by viewModels()
    private lateinit var binding: FragmentUpdateTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateTodoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = UpdateTodoFragmentArgs.fromBundle(requireArguments())
        val navController = findNavController()

        binding.apply {
            newTodoDescriptionEdt.setText(args.todo.description)

            updateButton.setOnClickListener {
                if (TextUtils.isEmpty(newTodoDescriptionEdt.text)) {
                    Toast.makeText(
                        requireContext(),
                        R.string.field_must_not_be_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val todoDescription = newTodoDescriptionEdt.text.toString().trim()

                val todo = Todo(
                    args.todo.id,
                    todoDescription,
                    args.todo.isCompleted
                )

                viewModel.update(todo)
                Toast.makeText(
                    requireContext(),
                    R.string.todo_successfully_updated,
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate(R.id.action_updateTodoFragment_to_todoListFragment)
            }
        }
    }
}
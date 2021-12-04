package com.example.to_dolistapp.presentation.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Todo
import com.example.to_dolistapp.databinding.TodoItemLayoutBinding

class TodoListAdapter(val listener: OnTodoClickListener) :
    ListAdapter<Todo, TodoListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TodoItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(
        private val binding: TodoItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentTodoItem: Todo? = null

        init {
            binding.apply {
                root.setOnClickListener {
                    currentTodoItem?.let { todo ->
                        listener.onTodoClick(todo)
                    }
                }
                checkboxCompleted.setOnClickListener {
                    currentTodoItem?.let { todo ->
                        listener.onCheckBoxClick(todo, checkboxCompleted.isChecked)
                    }
                }
                deleteTodoButton.setOnClickListener {
                    currentTodoItem?.let { todo ->
                        listener.onDeleteTodo(todo)
                    }
                }
            }
        }

        fun bind(todo: Todo) {
            currentTodoItem = todo

            binding.apply {
                checkboxCompleted.isChecked = todo.isCompleted
                todoDescription.text = todo.description
                todoDescription.paint.isStrikeThruText = todo.isCompleted
                timestamp.text = todo.createdDateFormatted
            }
        }
    }

    interface OnTodoClickListener {
        fun onTodoClick(todo: Todo)
        fun onDeleteTodo(todo: Todo)
        fun onCheckBoxClick(todo: Todo, isCompleted: Boolean)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
    }
}
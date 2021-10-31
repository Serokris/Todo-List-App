package com.example.to_dolistapp.presentation.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Todo
import com.example.to_dolistapp.databinding.TodoItemLayoutBinding

class TodoListAdapter(val listener: OnTodoClickListener)
    : ListAdapter<Todo, TodoListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TodoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: TodoItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                checkboxCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todo = getItem(position)
                        listener.onCheckBoxClick(todo, checkboxCompleted.isChecked)
                    }
                }
                deleteTodoButton.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todo = getItem(position)
                        listener.onDeleteTodo(todo)
                    }
                }
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todo = getItem(position)
                        listener.onTodoClick(todo)
                    }
                }
            }
        }

        fun bind(todo: Todo) {
            binding.checkboxCompleted.isChecked = todo.isCompleted
            binding.todoDescription.text = todo.description
            binding.todoDescription.paint.isStrikeThruText = todo.isCompleted
            binding.timestamp.text = todo.createdDateFormatted
            binding.executePendingBindings()
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
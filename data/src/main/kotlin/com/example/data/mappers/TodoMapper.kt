package com.example.data.mappers

import com.example.data.models.TodoEntity
import com.example.domain.models.Todo

fun Todo.toTodoEntity(): TodoEntity = TodoEntity(id, description, isCompleted)

fun TodoEntity.toTodo(): Todo = Todo(id, description, isCompleted)
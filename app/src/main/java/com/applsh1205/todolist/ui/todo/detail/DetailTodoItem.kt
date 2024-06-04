package com.applsh1205.todolist.ui.todo.detail

data class DetailTodoItem(
    val id: String,
    val content: String,
    val completed: Boolean,
    val createdAt: String
)
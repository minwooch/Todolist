package com.applsh1205.todolist.ui.todo.detail

import androidx.compose.runtime.Immutable

@Immutable
data class DetailState(
    val todo: DetailTodoItem,
    val showTodoEdit: Boolean = false,
    val todoText: String = ""
)
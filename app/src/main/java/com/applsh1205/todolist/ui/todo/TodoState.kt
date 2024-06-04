package com.applsh1205.todolist.ui.todo

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Immutable
data class TodoState(
    val incompleteTodos: Flow<PagingData<TodoItem>> = emptyFlow(),
    val completedTodos: Flow<PagingData<TodoItem>> = emptyFlow(),
    val showTodoAdd: Boolean = false,
    val todoText: String = ""
)

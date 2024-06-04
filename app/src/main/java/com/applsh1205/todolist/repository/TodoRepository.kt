package com.applsh1205.todolist.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodo(id: String): Flow<Todo>
    fun getIncompleteTodos(): Flow<PagingData<Todo>>
    fun getCompletedTodos(): Flow<PagingData<Todo>>
    suspend fun addTodo(content: String)
    suspend fun setTodoCompleted(id: String)
    suspend fun removeTodoCompleted(id: String)
    suspend fun editTodo(id: String, content: String)
}
package com.applsh1205.todolist.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.applsh1205.todolist.database.TodoDao
import com.applsh1205.todolist.database.TodoEntity
import com.applsh1205.todolist.di.IDProvider
import com.applsh1205.todolist.di.LocalDateTimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class DefaultTodoRepository(
    private val todoDao: TodoDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val localDateTimeProvider: LocalDateTimeProvider,
    private val idProvider: IDProvider
) : TodoRepository {

    override fun getTodo(id: String): Flow<Todo> {
        return todoDao.getTodo(id)
            .map {
                Todo(
                    id = it.id,
                    content = it.content,
                    completedAt = it.completedAt,
                    createdAt = it.createdAt,
                )
            }
    }

    override fun getIncompleteTodos(): Flow<PagingData<Todo>> {
        return Pager(
            config = PagingConfig(pageSize = 30)
        ) {
            todoDao.getIncompleteTodos()
        }.flow.map { pagingData ->
            pagingData.map {
                Todo(
                    id = it.id,
                    content = it.content,
                    completedAt = it.completedAt,
                    createdAt = it.createdAt,
                )
            }
        }
    }

    override fun getCompletedTodos(): Flow<PagingData<Todo>> {
        return Pager(
            config = PagingConfig(pageSize = 30)
        ) {
            todoDao.getCompletedTodos()
        }.flow.map { pagingData ->
            pagingData.map {
                Todo(
                    id = it.id,
                    content = it.content,
                    completedAt = it.completedAt,
                    createdAt = it.createdAt,
                )
            }
        }
    }

    override suspend fun addTodo(content: String) {
        withContext(ioDispatcher) {
            val now = localDateTimeProvider.now()
            val id = idProvider.id()
            todoDao.insert(
                TodoEntity(id, content, null, now)
            )
        }
    }

    override suspend fun setTodoCompleted(id: String) {
        withContext(ioDispatcher) {
            val now = localDateTimeProvider.now()

            todoDao.setCompleted(id, now)
        }
    }

    override suspend fun removeTodoCompleted(id: String) {
        withContext(ioDispatcher) {
            val now = localDateTimeProvider.now()

            todoDao.setCompleted(id, null)
        }
    }

    override suspend fun editTodo(id: String, content: String) {
        withContext(ioDispatcher) {
            todoDao.updateContent(id, content)
        }
    }
}
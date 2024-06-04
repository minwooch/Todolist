package com.applsh1205.todolist.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
abstract class TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(link: TodoEntity)

    @Query("SELECT * FROM todos WHERE id = :id")
    abstract fun getTodo(id: String): Flow<TodoEntity>

    @Query("SELECT * FROM todos ORDER BY createdAt desc")
    abstract fun getTodos(): PagingSource<Int, TodoEntity>

    @Query("SELECT * FROM todos WHERE completedAt IS NOT NULL ORDER BY createdAt desc")
    abstract fun getCompletedTodos(): PagingSource<Int, TodoEntity>

    @Query("SELECT * FROM todos WHERE completedAt IS NULL ORDER BY createdAt desc")
    abstract fun getIncompleteTodos(): PagingSource<Int, TodoEntity>

    @Query("UPDATE todos SET completedAt = :completedAt WHERE id = :id")
    abstract fun setCompleted(id: String, completedAt: LocalDateTime?)

    @Query("UPDATE todos SET content = :content WHERE id = :id")
    abstract fun updateContent(id: String, content: String)
}
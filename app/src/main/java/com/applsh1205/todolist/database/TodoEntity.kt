package com.applsh1205.todolist.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "todos",
    indices = [
        Index("createdAt")
    ]
)
data class TodoEntity(
    @PrimaryKey
    val id: String,
    val content: String,
    val completedAt: LocalDateTime?,
    val createdAt: LocalDateTime,
)
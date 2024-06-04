package com.applsh1205.todolist.repository

import java.time.LocalDateTime

data class Todo(
    val id: String,
    val content: String,
    val completedAt: LocalDateTime?,
    val createdAt: LocalDateTime
)
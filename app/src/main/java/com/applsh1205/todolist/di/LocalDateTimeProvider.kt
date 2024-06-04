package com.applsh1205.todolist.di

import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

interface LocalDateTimeProvider {
    fun now(): LocalDateTime
}

@Singleton
class DefaultLocalDateTimeProvider @Inject constructor() : LocalDateTimeProvider {
    override fun now(): LocalDateTime {
        return LocalDateTime.now()
    }
}
package com.applsh1205.todolist.di

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface IDProvider {
    fun id(): String
}

@Singleton
class DefaultIDProvider @Inject constructor() : IDProvider {
    override fun id(): String {
        return UUID.randomUUID().toString()
    }
}

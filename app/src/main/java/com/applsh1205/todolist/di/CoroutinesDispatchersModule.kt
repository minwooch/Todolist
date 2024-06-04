package com.applsh1205.todolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesDispatchersModule {

    @Provides
    @DispatchersIO
    fun provideDispatchersIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}

@Qualifier
annotation class DispatchersIO

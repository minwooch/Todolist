package com.applsh1205.todolist.di

import android.content.Context
import androidx.room.Room
import com.applsh1205.todolist.database.AppDatabase
import com.applsh1205.todolist.repository.DefaultTodoRepository
import com.applsh1205.todolist.repository.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SingletonModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Todo-DB.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTodoRepository(
        appDatabase: AppDatabase,
        @DispatchersIO ioDispatcher: CoroutineDispatcher,
        localDateTimeProvider: LocalDateTimeProvider,
        idProvider: IDProvider
    ): TodoRepository {
        return DefaultTodoRepository(
            appDatabase.todoDao(),
            ioDispatcher,
            localDateTimeProvider,
            idProvider
        )
    }

}

@InstallIn(SingletonComponent::class)
@Module
abstract class BindsSingletonModule {
    @Singleton
    @Binds
    abstract fun provideLocalDateTimeProvider(localDateTimeProvider: DefaultLocalDateTimeProvider): LocalDateTimeProvider

    @Singleton
    @Binds
    abstract fun provideIdProvider(idProvider: DefaultIDProvider): IDProvider

}
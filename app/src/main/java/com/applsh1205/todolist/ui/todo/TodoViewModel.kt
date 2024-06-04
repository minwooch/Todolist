package com.applsh1205.todolist.ui.todo

import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.applsh1205.todolist.BaseViewModel
import com.applsh1205.todolist.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : BaseViewModel<TodoState, TodoEffect>(TodoState()) {

    init {
        val incompleteTodos = todoRepository.getIncompleteTodos()
            .map { pagingData ->
                pagingData.map {
                    TodoItem(
                        id = it.id,
                        content = it.content,
                        completed = false
                    )
                }
            }

        val completedTodos = todoRepository.getCompletedTodos()
            .map { pagingData ->
                pagingData.map {
                    TodoItem(
                        id = it.id,
                        content = it.content,
                        completed = true
                    )
                }
            }

        setState {
            copy(
                incompleteTodos = incompleteTodos,
                completedTodos = completedTodos
            )
        }
    }

    fun hideTodoAdd() {
        setState {
            copy(
                showTodoAdd = false,
                todoText = ""
            )
        }
    }

    fun showTodoAdd() {
        setState {
            copy(
                showTodoAdd = true,
                todoText = ""
            )
        }
    }

    fun setTodoText(value: String) {
        setState {
            copy(todoText = value)
        }
    }

    fun addTodo() {
        viewModelScope.launch {
            val value = state.value.todoText
            if (value.isNotBlank()) {
                todoRepository.addTodo(value)
                provideEffect(TodoEffect.TodoAddDone)
                hideTodoAdd()
            }
        }
    }

    fun setTodoCompleted(id: String, completed: Boolean) {
        viewModelScope.launch {
            if (completed) {
                todoRepository.setTodoCompleted(id)
            } else {
                todoRepository.removeTodoCompleted(id)
            }
        }
    }
}
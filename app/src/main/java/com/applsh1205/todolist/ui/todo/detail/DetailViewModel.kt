package com.applsh1205.todolist.ui.todo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.applsh1205.todolist.BaseViewModel
import com.applsh1205.todolist.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val todoRepository: TodoRepository,
) : BaseViewModel<DetailState, Unit>(
    DetailState(DetailTodoItem("", "", false, ""))
) {

    private val id = savedStateHandle.get<String>("id")!!

    init {
        val todo = todoRepository
            .getTodo(id)
            .map {
                val createdAt =
                    it.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                DetailTodoItem(
                    id = it.id,
                    content = it.content,
                    completed = it.completedAt != null,
                    createdAt = createdAt
                )
            }

        whileStateSubscribed(todo) {
            setState { copy(todo = it) }
        }
    }

    fun setTodoCompleted() {
        viewModelScope.launch {
            if (state.value.todo.completed) {
                todoRepository.removeTodoCompleted(id)
            } else {
                todoRepository.setTodoCompleted(id)
            }
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
                todoRepository.editTodo(id, value)
            }
            hideTodoEdit()
        }
    }

    fun hideTodoEdit() {
        setState {
            copy(
                showTodoEdit = false,
            )
        }
    }

    fun showTodoEdit() {
        setState {
            copy(
                showTodoEdit = true,
                todoText = state.value.todo.content
            )
        }
    }
}
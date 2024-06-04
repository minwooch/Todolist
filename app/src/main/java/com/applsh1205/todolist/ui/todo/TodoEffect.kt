package com.applsh1205.todolist.ui.todo

sealed class TodoEffect {

    data object TodoAddDone : TodoEffect()

}
package com.applsh1205.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.applsh1205.todolist.ui.theme.TodolistTheme
import com.applsh1205.todolist.ui.todo.TodoScreen
import com.applsh1205.todolist.ui.todo.detail.DetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TodolistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoNavHost()
                }
            }
        }
    }
}

@Composable
fun TodoNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destination.Home.name
    ) {
        composable(Destination.Home.route) {
            TodoScreen(
                onClickTodo = { navController.navigate(Destination.Detail.path(it)) }
            )
        }

        composable(
            route = Destination.Detail.route,
            arguments = Destination.Detail.arguments
        ) {

            DetailScreen(
                onClickBack = { navController.navigateUp() }
            )
        }
    }
}

sealed class Destination(val name: String) {
    open val route: String = name

    data object Home : Destination("home")
    data object Detail : Destination("detail") {
        const val ID = "id"

        val arguments = listOf(
            navArgument(ID) {
                defaultValue = ""
            }
        )

        override val route = "$name?$ID={$ID}"

        fun path(id: String): String {
            return "$name?$ID=$id"
        }

    }

}
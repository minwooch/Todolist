package com.applsh1205.todolist.ui.todo.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.applsh1205.todolist.ui.todo.TodoInputScreen

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column {
            DetailBar(
                onClickBack = onClickBack
            )

            DetailContent(
                item = state.todo,
                onClickCompletedState = {
                    viewModel.setTodoCompleted()
                },
                onClickDetail = { viewModel.showTodoEdit() }
            )
        }

        if (state.showTodoEdit) {
            TodoInputScreen(
                textValue = state.todoText,
                onTextChanged = { viewModel.setTodoText(it) },
                onEnd = { viewModel.addTodo() },
                hide = { viewModel.hideTodoEdit() }
            )
        }
    }

}

@Composable
fun DetailBar(
    onClickBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        IconButton(onClick = { onClickBack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }
}

@Composable
fun DetailContent(
    item: DetailTodoItem,
    onClickCompletedState: () -> Unit,
    onClickDetail: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClickDetail()
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    modifier = Modifier
                        .size(32.dp),
                    onClick = {
                        onClickCompletedState()
                    }
                ) {
                    if (item.completed) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize(),
                            imageVector = Icons.Rounded.CheckCircleOutline,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize(),
                            imageVector = Icons.Rounded.RadioButtonUnchecked,
                            contentDescription = null
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    fontSize = 20.sp,
                    text = item.content
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(8.dp),
            text = item.createdAt
        )

    }
}

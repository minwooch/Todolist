package com.applsh1205.todolist.ui.todo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.applsh1205.todolist.ConsumeEffect
import com.applsh1205.todolist.IntWrapper
import com.applsh1205.todolist.R
import kotlinx.coroutines.flow.Flow

@Composable
fun TodoScreen(
    viewModel: TodoViewModel = hiltViewModel(),
    onClickTodo: (String) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ConsumeEffect(viewModel) {
        when (it) {
            TodoEffect.TodoAddDone -> {

            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        TodoList(
            incompleteTodos = state.incompleteTodos,
            completedTodos = state.completedTodos,
            onClickCompletedState = { id, completed ->
                viewModel.setTodoCompleted(id, completed)
            },
            onClickTodo = onClickTodo
        )

        if (!state.showTodoAdd) {
            TodoAddButton(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),

                onClick = {
                    viewModel.showTodoAdd()
                })
        } else {
            TodoInputScreen(
                textValue = state.todoText,
                onTextChanged = { viewModel.setTodoText(it) },
                onEnd = { viewModel.addTodo() },
                hide = { viewModel.hideTodoAdd() }
            )
        }
    }

}

@Composable
fun TodoList(
    incompleteTodos: Flow<PagingData<TodoItem>>,
    completedTodos: Flow<PagingData<TodoItem>>,
    onClickCompletedState: (id: String, completed: Boolean) -> Unit,
    onClickTodo: (id: String) -> Unit
) {
    val incompleteItems = incompleteTodos.collectAsLazyPagingItems()
    val completedItems = completedTodos.collectAsLazyPagingItems()

    LazyColumn {

        items(count = incompleteItems.itemCount, key = {
            val item = incompleteItems.peek(it)
            if (item == null) {
                IntWrapper(it)
            } else {
                item.id + ".incomplete"
            }
        }) {
            val item = incompleteItems[it]
            if (item != null) {
                TodoCell(
                    item = item,
                    onClickCompletedState = onClickCompletedState,
                    onClickTodo = onClickTodo
                )

                if (it < incompleteItems.itemCount - 1) {
                    HorizontalDivider()
                }

            }
        }

        if (completedItems.itemCount > 0) {

            item {
                TodoCompleteDivider()
            }

            items(count = completedItems.itemCount, key = {
                val item = completedItems.peek(it)
                if (item == null) {
                    IntWrapper(it)
                } else {
                    item.id + ".completed"
                }
            }) {
                val item = completedItems[it]
                if (item != null) {
                    TodoCell(
                        item = item,
                        onClickCompletedState = onClickCompletedState,
                        onClickTodo = onClickTodo
                    )

                    if (it < completedItems.itemCount - 1) {
                        HorizontalDivider()
                    }

                }
            }
        }

    }
}

@Composable
fun TodoCell(
    item: TodoItem,
    onClickCompletedState: (id: String, completed: Boolean) -> Unit,
    onClickTodo: (id: String) -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickTodo(item.id)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    onClickCompletedState(item.id, !item.completed)
                }
            ) {
                if (item.completed) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircleOutline,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.RadioButtonUnchecked,
                        contentDescription = null
                    )
                }
            }

            Text(text = item.content)
        }
    }

}

@Composable
fun TodoCompleteDivider() {
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .width(16.dp)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                text = stringResource(R.string.todo_completed)
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun TodoAddButton(
    modifier: Modifier, onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier, onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TodoInputScreen(
    textValue: String, onTextChanged: (String) -> Unit, onEnd: () -> Unit, hide: () -> Unit
) {

    var textFieldFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val clearFocus = remember(focusRequester, focusManager) {
        {
            focusRequester.freeFocus()
            focusManager.clearFocus()
        }
    }

    BackHandler(textFieldFocused || WindowInsets.isImeVisible) {
        clearFocus()
        hide()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = hide
            )
    ) {
        TodoInputField(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding(),
            textValue = textValue,
            onTextChanged = onTextChanged,
            onEnd = onEnd,
            onFocusChanged = {
                textFieldFocused = it.isFocused
            },
            focusRequester = focusRequester,
        )
    }
}

@Composable
fun TodoInputField(
    modifier: Modifier = Modifier,
    textValue: String,
    onTextChanged: (String) -> Unit,
    onEnd: () -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    focusRequester: FocusRequester,
) {

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged(onFocusChanged)
            .focusRequester(focusRequester),
        value = textValue,
        onValueChange = onTextChanged,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onEnd() }
        )
    )

}
package com.applsh1205.todolist

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imeAnimationSource
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope

data class IntWrapper(val value: Int)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun keyboardClosed(): State<Boolean> {
    val density = LocalDensity.current

    val sourceBottom = WindowInsets.imeAnimationSource.getBottom(density)
    val targetBottom = WindowInsets.imeAnimationTarget.getBottom(density)

    val closed = sourceBottom > 0 && sourceBottom > targetBottom

    return rememberUpdatedState(newValue = closed)
}

@Composable
fun <STATE, EFFECT> ConsumeEffect(
    viewModel: BaseViewModel<STATE, EFFECT>,
    consumer: suspend CoroutineScope.(EFFECT) -> Unit
) {

    val effect by viewModel.effect.collectAsStateWithLifecycle()
    LaunchedEffect(effect) {
        val e = effect
        if (e != null) {
            consumer(e)
            viewModel.consumeEffect()
        }
    }
}
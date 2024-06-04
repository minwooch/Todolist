package com.applsh1205.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, EFFECT>(
    initialState: STATE
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    private val _effect: MutableStateFlow<EFFECT?> = MutableStateFlow(null)

    val state = _state.asStateFlow()
    private val stateSubscriptionCount = SharingStarted.WhileSubscribed(5000L)
        .command(_state.subscriptionCount)
        .distinctUntilChanged()

    fun whileStateSubscribed(f: suspend () -> Unit) {
        viewModelScope.launch {
            stateSubscriptionCount.collectLatest {
                when (it) {
                    SharingCommand.START -> {
                        f()
                    }

                    SharingCommand.STOP -> {}
                    SharingCommand.STOP_AND_RESET_REPLAY_CACHE -> {}
                }
            }
        }
    }

    fun <FlowType> whileStateSubscribed(flow: Flow<FlowType>, f: suspend (FlowType) -> Unit) {
        viewModelScope.launch {
            stateSubscriptionCount.collectLatest {
                when (it) {
                    SharingCommand.START -> {
                        flow.collect(f)
                    }

                    SharingCommand.STOP -> {}
                    SharingCommand.STOP_AND_RESET_REPLAY_CACHE -> {}
                }
            }
        }
    }


    val effect = _effect.asStateFlow()

    protected fun setState(newState: STATE.() -> STATE) {
        _state.update(newState)
    }

    fun provideEffect(newEffect: EFFECT) {
        _effect.update { newEffect }
    }

    fun consumeEffect() {
        _effect.update { null }
    }

}
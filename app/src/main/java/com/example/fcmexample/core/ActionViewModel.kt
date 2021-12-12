package com.example.fcmexample.core

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class ActionViewModel<S, A>(initialState: S) : StateViewModel<S>(initialState) {
    private val pendingActions = MutableSharedFlow<A>()

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                collectAction(action)
            }
        }
    }

    abstract fun collectAction(action: A)

    fun submitAction(action: A) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}
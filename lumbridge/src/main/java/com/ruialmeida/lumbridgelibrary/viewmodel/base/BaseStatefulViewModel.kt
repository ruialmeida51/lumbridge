package com.ruialmeida.lumbridgelibrary.viewmodel.base

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseStatefulViewModel<State> : ViewModel() {

    private var currentState: State? = null

    private val stateLive = MutableLiveData<State>()

    init {
        setup()
    }

    abstract fun setup()
    abstract fun getDefaultState(): State

    protected fun updateState(newState: State) {
        currentState = newState

        if (Looper.getMainLooper().isCurrentThread) {
            //This means we are on the UI thread.
            stateLive.value = newState
        } else {
            //Non UI thread.
            stateLive.postValue(newState)
        }
    }

    //Exposed functions / parameters.
    fun getCurrentStateLive(): LiveData<State> = stateLive
    fun getCurrentState(): State = currentState ?: getDefaultState()
}
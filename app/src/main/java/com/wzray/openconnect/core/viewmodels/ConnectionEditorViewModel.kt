package com.wzray.openconnect.core.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.*
import androidx.lifecycle.viewModelScope
import com.wzray.openconnect.Application
import com.wzray.openconnect.core.data.model.Connection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConnectionEditorViewModel (c: Connection) : ViewModel() {
    private val dao = Application.db.connectionsDao()

    private val connectionFlow = MutableStateFlow(c)
    val connection = connectionFlow.asStateFlow()


    fun updateConnection(c: Connection) {
        viewModelScope.launch {
            connectionFlow.update { c }
            withContext(Dispatchers.IO) {
                dao.update(c)
            }
        }
    }
//    companion object {
//        fun provideFactory(
//            connection: Connection
//        ): Factory = object : Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return ConnectionEditorViewModel(connection) as T
//            }
//        }
//    }
}
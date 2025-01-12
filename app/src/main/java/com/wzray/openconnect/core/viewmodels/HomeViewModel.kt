package com.wzray.openconnect.core.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wzray.openconnect.Application
import com.wzray.openconnect.core.data.model.Connection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface HomeUiState {
    val connections: List<Connection>

    data class ConnectionList(
        override val connections: List<Connection>
    ) : HomeUiState

    data class SelectionList(
        override val connections: List<Connection>,
        val selectedConnections: List<Connection>
    ) : HomeUiState
}

private data class ViewModelState(
    val connections: List<Connection>? = null,
    val selectedConnections: List<Connection>? = null
) {
    fun toUiState() = if (selectedConnections == null)
        HomeUiState.ConnectionList(connections ?: listOf())
    else
        HomeUiState.SelectionList(connections ?: listOf(), selectedConnections)
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private var vmState = MutableStateFlow(ViewModelState())
    var uiState: StateFlow<HomeUiState> = vmState.map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, vmState.value.toUiState())

    private val dao = Application.db.connectionsDao()

    init {
        viewModelScope.launch {
            dao.getAll().collect { c ->
                vmState.update {
                    it.copy(connections = c)
                }
            }
        }
    }

    fun connect(connection: Connection) {
        Application.vpnManager.establish(connection)
    }

    fun openSettings(connection: Connection) {

    }

    fun toggleSelection(connection: Connection) {
        if (vmState.value.selectedConnections == null)
            vmState.update {
                it.copy(selectedConnections = listOf())
            }
        if (!vmState.value.selectedConnections?.contains(connection)!!)
            vmState.update {
                it.copy(selectedConnections = it.selectedConnections?.plus(connection))
            }
        else
            vmState.update {
                it.copy(selectedConnections = it.selectedConnections?.minus(connection))
            }

        if (vmState.value.selectedConnections?.isEmpty()!!)
            vmState.update {
                it.copy(selectedConnections = null)
            }

    }

    fun deleteSelected() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val db = Application.db
                val dao = db.connectionsDao()

                vmState.value.selectedConnections?.forEach { e ->
                    dao.delete(e)
                }
                vmState.update { it.copy(selectedConnections = null) }
            }
        }
    }

    fun selectAll() = viewModelScope.launch {
        vmState.value.connections?.forEach { c ->
            if (!vmState.value.selectedConnections?.contains(c)!!)
                vmState.update {
                    it.copy(selectedConnections = it.selectedConnections?.plus(c))
                }
        }
    }

    fun copySelected() {
        Log.e(TAG, "Copy selected is not yet implemented.")
    }

    companion object {
        private const val TAG = "OpenConnect/HomeViewModel"
    }
}
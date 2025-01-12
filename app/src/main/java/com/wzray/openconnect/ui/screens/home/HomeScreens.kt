package com.wzray.openconnect.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.ramcosta.composedestinations.generated.destinations.ConnectionEditorScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wzray.openconnect.Application
import com.wzray.openconnect.core.data.model.Connection
import com.wzray.openconnect.ui.components.ConnectionElement
import com.wzray.openconnect.util.applicationScope
import com.wzray.openconnect.core.viewmodels.HomeUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun MultiUseFab() { // TODO: you don't have to tell me that this is bad
    val vpnManager = Application.vpnManager

    if (vpnManager.isConnected)
        FloatingActionButton(
            onClick = { vpnManager.disconnect() },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Stop, "Stop the VPN")
        }
    else
        FloatingActionButton(
            onClick = {
                Application.applicationScope.launch {
                    val dao = Application.db.connectionsDao()
                    dao.insert(Connection("New connection", "", "", ""))
                }
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, "Add a new connection")
        }
}

@Composable
fun HomeScreen(
    onConnectionClick: (Connection) -> Unit,
    onLongClick: (Connection) -> Unit,
    uiStateFlow: StateFlow<HomeUiState>,
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator,
) {
    val uiState by uiStateFlow.collectAsState()

    Scaffold(
        topBar = { HomeTopBar() },
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MultiUseFab()
            }
        }
    ) { padding ->
        val haptics = LocalHapticFeedback.current
        Box(modifier = Modifier.padding(top = padding.calculateTopPadding())) {
            LazyColumn {
                items(uiState.connections) {
                    ConnectionElement(
                        onClick = { onConnectionClick(it) },
                        onLongClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onLongClick(it)
                        },
                        onSettingsClick = { navigator.navigate(ConnectionEditorScreenDestination(it)) },
                        connectionName = it.name
                    )
                }
            }
        }
    }
}
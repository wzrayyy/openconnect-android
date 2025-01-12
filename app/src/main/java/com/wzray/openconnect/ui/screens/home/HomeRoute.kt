package com.wzray.openconnect.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ramcosta.composedestinations.annotation.Destination
import com.wzray.openconnect.core.viewmodels.HomeUiState
import com.wzray.openconnect.core.viewmodels.HomeViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.wzray.openconnect.ui.MainGraph
import com.wzray.openconnect.ui.RootDestinationsNavigator

@Destination<MainGraph>(
    start = true,
)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    outerPadding: PaddingValues,
    navigator: RootDestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    when (uiState) {
        is HomeUiState.ConnectionList ->
            HomeScreen(
                { viewModel.connect(it) },
                { viewModel.toggleSelection(it) },
                viewModel.uiState,
                outerPadding,
                navigator
            )

        is HomeUiState.SelectionList -> HomeScreenSelection(
            onChangeState = { viewModel.toggleSelection(it) },
            onDeleteClick = { viewModel.deleteSelected() },
            onSelectAllClick = { viewModel.selectAll() },
            onCopyClick = { viewModel.copySelected() },
            uiStateFlow = viewModel.uiState,
            paddingValues = outerPadding
        )
    }
}
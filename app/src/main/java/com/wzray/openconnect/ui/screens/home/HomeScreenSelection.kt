package com.wzray.openconnect.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import com.wzray.openconnect.R
import com.wzray.openconnect.core.data.model.Connection
import com.wzray.openconnect.ui.components.ConnectionElementSelected
import com.wzray.openconnect.core.viewmodels.HomeUiState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SelectionTopBar(
    count: Int,
    onDeleteClick: () -> Unit,
    onSelectAllClick: () -> Unit,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {
        Text(
            pluralStringResource(
                R.plurals.selection_top_bar_count_selected,
                count,
                count
            )
        )
    }
) {
    HomeTopBar(modifier, title) {
        if (count == 1)
            IconButton(onClick = onCopyClick) {
                Icon(imageVector = Icons.Filled.ContentCopy, "Duplicate")
            }

        IconButton(onClick = onSelectAllClick) {
            Icon(imageVector = Icons.Filled.SelectAll, "Select all")
        }

        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Filled.Delete, "Select all")
        }
    }
}

@Composable
fun HomeScreenSelection(
    onChangeState: (Connection) -> Unit,
    onDeleteClick: () -> Unit,
    onSelectAllClick: () -> Unit,
    onCopyClick: () -> Unit,
    uiStateFlow: StateFlow<HomeUiState>,
    paddingValues: PaddingValues,
) {
    val uiState by uiStateFlow.collectAsState()

    Scaffold(
        topBar = { SelectionTopBar(
            (uiState as HomeUiState.SelectionList).selectedConnections.count(),
            onDeleteClick = onDeleteClick,
            onSelectAllClick = onSelectAllClick,
            onCopyClick = onCopyClick,
        ) },
        modifier = Modifier.padding(paddingValues),
    ) { padding ->
        Box(modifier = Modifier.padding(top = padding.calculateTopPadding())) {
            LazyColumn {
                items(uiState.connections) {
                    ConnectionElementSelected(
                        onChangeCallback = { onChangeState(it) },
                        isEnabled = (uiState as HomeUiState.SelectionList)
                            .selectedConnections.contains(it),
                        connectionName = it.name
                    )
                }
            }
        }
    }
}

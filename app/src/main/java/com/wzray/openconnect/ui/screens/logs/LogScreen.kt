package com.wzray.openconnect.ui.screens.logs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wzray.openconnect.R
import com.wzray.openconnect.ui.components.CheckboxText
import com.wzray.openconnect.ui.components.SimpleIconButton
import com.wzray.openconnect.ui.components.SingleSelectDialog
import com.wzray.openconnect.util.Settings
import com.wzray.openconnect.core.viewmodels.LogLevel
import com.wzray.openconnect.core.viewmodels.UIState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onPauseClick: () -> Unit,
    onShareClick: () -> Unit,
    onClearClick: () -> Unit,
    isPaused: Boolean,
    dialogState: MutableState<Boolean>,

    ) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val followLogs by Settings.followLogs.asState()
    var isDialogOpen by dialogState

    TopAppBar(
        title = { Text(stringResource(R.string.logs)) },
        actions = {
            SimpleIconButton(
                onClick = onPauseClick,
                icon = if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                contentDescription = stringResource(R.string.pause_logs)
            )
            SimpleIconButton(
                onClick = onClearClick,
                icon = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.clear_logs)
            )
            Box {
                SimpleIconButton(
                    onClick = { isMenuExpanded = !isMenuExpanded },
                    icon = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.more_options)
                )
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            CheckboxText(
                                stringResource(R.string.follow_logs),
                                followLogs
                            )
                        },
                        onClick = { Settings.followLogs.set(!followLogs); isMenuExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.log_level)) },
                        onClick = { isMenuExpanded = false; isDialogOpen = true }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.export_logs)) },
                        onClick = onShareClick
                    )
                }
            }
        },
    )
}

@Composable
fun LogScreen(
    onPauseClick: () -> Unit,
    onShareClick: () -> Unit,
    onSetLogLevel: (LogLevel) -> Unit,
    onClearClick: () -> Unit,
    uiStateFlow: StateFlow<UIState>,
    padding: PaddingValues
) {
    val uiState by uiStateFlow.collectAsState()
    val dialogState = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                onPauseClick = onPauseClick,
                onShareClick = onShareClick,
                onClearClick = onClearClick,
                isPaused = uiState.isPaused,
                dialogState = dialogState,
            )
        },
        modifier = Modifier.padding(padding)
    ) { innerPadding ->
        val followLogs by Settings.followLogs.asState()
        var isDialogOpen by dialogState
        val scrollState = rememberScrollState()

        if (isDialogOpen)
            SingleSelectDialog(
                title = stringResource(R.string.log_level),
                submitButtonText = stringResource(R.string.submit),
                dismissButtonText = stringResource(R.string.cancel),
                options = LogLevel.entries.associateWith { it.fullName },
                defaultSelected = uiState.logLevel,
                onSubmitButtonClick = onSetLogLevel,
                onDismissRequest = { isDialogOpen = false },
            )

        SelectionContainer(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (followLogs) LaunchedEffect(uiState.logList.size) {
                if (uiState.logList.isNotEmpty()) scrollState.scrollTo(scrollState.maxValue)
            }

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
            ) {
                uiState.logList.toList().forEach {
                    Row {
                        Text(
                            text = it,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
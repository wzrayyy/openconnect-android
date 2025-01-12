package com.wzray.openconnect.ui.screens.logs

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.wzray.openconnect.ui.MainGraph
import com.wzray.openconnect.core.viewmodels.LogViewModel

@Destination<MainGraph>
@Composable
fun LogRoute(
    viewModel: LogViewModel = hiltViewModel(),
    outerPadding: PaddingValues,
) {
    val ctx = LocalContext.current // TODO: remove this
    val makeToast: (String) -> Unit = {
        Toast.makeText(ctx, it, Toast.LENGTH_SHORT).show()
    }
    LogScreen( // TODO: remove dependency on UIState and provide all values individually
        onPauseClick = { viewModel.pauseLog() },
        onShareClick = { makeToast("onShareClick") },
        onSetLogLevel = { viewModel.setLogLevel(it) },
        onClearClick = { viewModel.clearLog() },
        uiStateFlow = viewModel.uiState,
        padding = outerPadding
    )
}
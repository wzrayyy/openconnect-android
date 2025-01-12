package com.wzray.openconnect.ui.screens.home

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.RootGraph
import com.wzray.openconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = { Text(stringResource(R.string.app_name)) },
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(title = title, modifier = modifier, actions = actions)
}
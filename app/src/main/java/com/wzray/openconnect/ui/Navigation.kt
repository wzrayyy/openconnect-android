package com.wzray.openconnect.ui

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.annotation.RootGraph
import com.wzray.openconnect.ui.theme.Fade

@NavGraph<RootGraph>
annotation class ConnectionEditorGraph

@NavHostGraph(
    route = "main",
    defaultTransitions = Fade::class
)
annotation class MainGraph
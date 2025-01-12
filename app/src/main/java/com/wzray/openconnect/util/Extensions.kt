package com.wzray.openconnect.util

import com.wzray.openconnect.Application
import kotlinx.coroutines.CoroutineScope

fun String.toFormalCase() = this.lowercase().replaceFirstChar { it.uppercaseChar() }

val Any.applicationScope: CoroutineScope
    get() = Application.coroutineScope


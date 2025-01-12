package com.wzray.openconnect.core.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.LinkedList
import javax.inject.Inject

enum class LogLevel(val shortName: Char, val fullName: String) {
    VERBOSE('V', "Verbose"),
    DEBUG('D', "Debug"),
    INFO('I', "Info"),
    WARN('W', "Warn"),
    ERROR('E', "Error"),
    FATAL('F', "Fatal")
}

data class UIState(
    val isPaused: Boolean = false,
    val logLevel: LogLevel = LogLevel.ERROR,
    val logList: List<String> = listOf() // TODO: proper structure for log lines
)

@HiltViewModel
class LogViewModel @Inject constructor() : ViewModel() {
    private var logCollectorJob: Job
    private val backlog = LinkedList<String>()
    private var mutableUiState = MutableStateFlow(UIState())
    val uiState = mutableUiState.asStateFlow()

    init {
        logCollectorJob = viewModelScope.launch {
            collectLogs()
        }
    }

    fun pauseLog() {
        mutableUiState.update {
            it.copy(isPaused = !mutableUiState.value.isPaused, logList = it.logList + backlog)
        }
        backlog.clear()
    }

    fun clearLog() {
        mutableUiState.update {
            it.copy(logList = listOf())
        }
        backlog.clear()
    }

    fun setLogLevel(logLevel: LogLevel) {
        logCollectorJob.cancel()
        backlog.clear()
        mutableUiState.update {
            it.copy(logLevel = logLevel, logList = listOf())
        }
        logCollectorJob = viewModelScope.launch {
            collectLogs()
        }
    }

    private suspend fun collectLogs() = withContext(Dispatchers.IO) {
        val builder = ProcessBuilder(
            "logcat", "-b", "all", "*:${uiState.value.logLevel.shortName}"
        )
        builder.environment()["LC_ALL"] = "C"
        var process: Process? = null
        try {
            process = try {
                builder.start()
            } catch (e: IOException) {
                Log.e(TAG, Log.getStackTraceString(e))
                return@withContext
            }

            val stdout =
                BufferedReader(InputStreamReader(process!!.inputStream, StandardCharsets.UTF_8))

            while (isActive) {
                val line = stdout.readLine() ?: break
                if (uiState.value.isPaused) backlog.add(line)
                else mutableUiState.update {
                    it.copy(logList = it.logList + line)
                }
            }
        } finally {
            process?.destroy()
        }
    }

    companion object {
        private const val TAG = "OpenConnect/LogViewModel"
    }
}
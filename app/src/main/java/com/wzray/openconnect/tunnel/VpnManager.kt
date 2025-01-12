package com.wzray.openconnect.tunnel

import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.wzray.openconnect.core.data.model.Connection
import com.wzray.openconnect.util.applicationScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class VpnManager(private val context: Context) {
    var activityCallback: ((ActivityResult) -> Unit)? = null
        private set

    var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private var openConnect: OpenConnect? = null
    private var openConnectThread: Thread? = null

    fun disconnect() = applicationScope.launch {
        openConnect?.cancel()
        openConnectThread?.join(1000)
    }

    val isConnected: Boolean
        get() = openConnect != null || openConnectThread != null

    fun establish(connection: Connection) {
        if (isConnected)
            disconnect()

        openConnect = OpenConnect(connection)
        openConnectThread = Thread(openConnect)

        activityCallback = {
            openConnectThread?.start()
        }

        val intent = VpnService.prepare(context)
        if (intent != null) {
            Log.d(TAG, "VPN permission not granted, requesting")
            try {
                activityResultLauncher!!.launch(intent)
            } catch (e: Exception) { // we want to have control over what gets thrown
                throw Exception("Missing VPN permission")
            }
        } else
            openConnectThread?.start()
    }

    fun reconnect(name: String) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "OpenConnect/VpnManager"
    }
}
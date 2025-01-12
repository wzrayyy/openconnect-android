package com.wzray.openconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.wzray.openconnect.ui.OpenConnectApp
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Application.vpnManager.activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Application.vpnManager.activityCallback?.invoke(it)
            }

        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            OpenConnectApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Application.vpnManager.activityResultLauncher = null
    }
}
package com.wzray.openconnect.ui.screens

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.wzray.openconnect.R
import com.wzray.openconnect.ui.MainGraph
import com.wzray.openconnect.util.Settings
import com.wzray.openconnect.util.UiMode
import com.wzray.openconnect.util.toFormalCase
import me.zhanghai.compose.preference.ListPreference
import me.zhanghai.compose.preference.PreferenceCategory
import me.zhanghai.compose.preference.SwitchPreference

@Destination<MainGraph>
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(outerPadding: PaddingValues) {
    Scaffold(
        modifier = Modifier.padding(outerPadding),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.settings)) }) }

    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val uiModeToString = UiMode.entries.associateBy { it.toString().toFormalCase() }
            val uiMode by Settings.uiMode.asState()
            val dynamicColors by Settings.useDynamicColor.asState()

            PreferenceCategory(
                title = { Text(stringResource(R.string.look_and_feel)) }
            )

            ListPreference(
                value = uiMode.toString().toFormalCase(),
                onValueChange = {
                    Settings.uiMode.set(
                        uiModeToString[it]
                    )
                },
                values = uiModeToString.keys.toList(),
                title = { Text(stringResource(R.string.app_theme)) },
                summary = { Text(stringResource(R.string.app_theme_summary)) },
                icon = {
                    Icon(
                        when (uiMode) {
                            UiMode.DARK -> Icons.Outlined.DarkMode
                            UiMode.LIGHT -> Icons.Outlined.LightMode
                            UiMode.AUTO -> Icons.Outlined.FormatPaint
                        }, null
                    )
                },
            )

            SwitchPreference(
                value = dynamicColors && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S),
                onValueChange = { Settings.useDynamicColor.set(it) },
                icon = { Icon(Icons.Outlined.Palette, null) },
                title = { Text(stringResource(R.string.use_dynamic_colors)) },
                summary = { Text(stringResource(R.string.use_dynamic_colors_summary)) },
                enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            )
        }
    }
}
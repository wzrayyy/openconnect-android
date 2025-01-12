package com.wzray.openconnect.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wzray.openconnect.ui.theme.OpenConnectTheme
import me.zhanghai.compose.preference.CheckboxPreference
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.TwoTargetIconButtonPreference
import me.zhanghai.compose.preference.rememberPreferenceState

// TwoTargetIconButtonPreference specialization for connection list
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConnectionElement(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onSettingsClick: () -> Unit,
    connectionName: String
) {
    TwoTargetIconButtonPreference(
        title = { Text(connectionName) },
        modifier = Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick),
        onIconButtonClick = onSettingsClick,
        iconButtonIcon = {
            Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConnectionElementSelected(
    onChangeCallback: () -> Unit,
    isEnabled: Boolean,
    connectionName: String
) {
    CheckboxPreference(
        value = isEnabled,
        title = { Text(connectionName) },
        onValueChange = {
            onChangeCallback.invoke()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ConnectionElementPreview() {
    OpenConnectTheme {
        Surface {
            Column {
                ConnectionElement({}, {}, {}, "Menu element")
                ConnectionElementSelected({}, false, "Not selected element")
                ConnectionElementSelected({}, true, "Example selected")
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ConnectionElementPreviewDark() {
    OpenConnectTheme {
        Surface {
            Column {
                ConnectionElement({}, {}, {}, "Menu element")
                ConnectionElementSelected({}, false, "Not selected element")
                ConnectionElementSelected({}, true, "Selected element")
            }
        }
    }
}

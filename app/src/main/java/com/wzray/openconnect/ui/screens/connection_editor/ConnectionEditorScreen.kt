package com.wzray.openconnect.ui.screens.connection_editor

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.wzray.openconnect.R
import com.wzray.openconnect.core.data.model.Connection
import com.wzray.openconnect.core.viewmodels.ConnectionEditorViewModel
import com.wzray.openconnect.ui.ConnectionEditorGraph
import com.wzray.openconnect.ui.theme.SlideEnd
import me.zhanghai.compose.preference.ListPreference
import me.zhanghai.compose.preference.PreferenceCategory
import me.zhanghai.compose.preference.TextFieldPreference
import me.zhanghai.compose.preference.preferenceCategory
import me.zhanghai.compose.preference.textFieldPreference

//private val protocolToString = mapOf<Protocols, @receiver:StringRes Int>(
//    Protocols.ANYCONNECT to R.string.home
//)

private fun LazyListScope.textFieldPreference(
    key: String,
    @StringRes title: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    item(key = key, contentType = "TextFieldPreference") {
        TextFieldPreference(
            value = value,
            summary = { Text(value) },
            title = { Text(stringResource(title)) },
            onValueChange = onValueChange,
            textToValue = { it },
            modifier = modifier
        )
    }
}

private fun LazyListScope.passwordTextFieldPreference(
    key: String,
    @StringRes title: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    item(key = key, contentType = "TextFieldPreference") {
        TextFieldPreference(
            value = value,
            summary = { Text("•".repeat(value.length)) },
            title = { Text(stringResource(title)) },
            onValueChange = onValueChange,
            textToValue = { it },
            modifier = modifier
        )
    }
}

@Destination<ConnectionEditorGraph>(start = true, style = SlideEnd::class)
@Composable
fun ConnectionEditorScreen(
    initialConnection: Connection,
) {
    val viewModel = remember { ConnectionEditorViewModel(initialConnection) }
    val connection by viewModel.connection.collectAsState()

    Scaffold { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            preferenceCategory(
                key = "cat_server",
                title = { Text(stringResource(R.string.server)) })

            textFieldPreference(
                key = "connection_name",
                value = connection.name,
                title = R.string.profile_name,
                onValueChange = {
                    viewModel.updateConnection(connection.copy(name = it))
                },
            )

            textFieldPreference(
                key = "server_address",
                value = connection.url,
                title = R.string.server_address,
                onValueChange = {
                    viewModel.updateConnection(connection.copy(url = it))
                }
            )

            preferenceCategory(
                key = "cat_auth",
                title = { Text(stringResource(R.string.authentication)) })

            textFieldPreference(
                key = "username",
                value = connection.username,
                title = R.string.username,
                onValueChange = {
                    viewModel.updateConnection(connection.copy(username = it))
                },
            )

            passwordTextFieldPreference(
                key = "password",
                value = connection.password,
                title = R.string.password,
                onValueChange = {
                    viewModel.updateConnection(connection.copy(password = it))
                },
            )
        }

    }
}
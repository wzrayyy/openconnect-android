package com.wzray.openconnect.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wzray.openconnect.ui.theme.OpenConnectTheme

@Composable
fun CheckboxText(
    text: String,
    checked: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)
        Spacer(Modifier.width(36.dp))
        Checkbox(checked, null)
    }
}

@Preview(showBackground = true)
@Composable
private fun UiPreview() {
    val enabled by remember { mutableStateOf(true) }
    OpenConnectTheme {
        CheckboxText("Preview", enabled)
    }
}
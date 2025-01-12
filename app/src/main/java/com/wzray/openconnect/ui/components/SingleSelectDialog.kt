package com.wzray.openconnect.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun <T> SingleSelectDialog(
    title: String,
    submitButtonText: String,
    dismissButtonText: String,
    options: Map<T, String>,
    defaultSelected: T,
    onSubmitButtonClick: (T) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedOption by remember { mutableStateOf(defaultSelected) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.requiredWidth(300.dp), shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 12.dp)
                )
                LazyColumn {
                    options.forEach {
                        item {
                            RadioButtonText(
                                text = it.value, isSelected = (it.key == selectedOption)
                            ) {
                                selectedOption = it.key
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Absolute.Right,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                ) {
                    SimpleTextButton(onDismissRequest, dismissButtonText)
                    SimpleTextButton({
                        onSubmitButtonClick.invoke(selectedOption)
                        onDismissRequest.invoke()
                    }, submitButtonText)
                }
            }
        }
    }
}
package io.github.rafetefe

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Dialog allowing the user to customize the file name before saving the form document.
 *
 * @param onDismiss Request to close the dialog without saving.
 * @param onConfirm Callback when a file name is submitted.
 */
@Composable
fun SaveFileDialog(
    onDismiss: () -> Unit,
    onConfirm: (fileName: String) -> Unit,
) {
    val defaultFileName = remember {
        SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
    }
    var fileName by remember { mutableStateOf(defaultFileName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Dosya Adını Kaydet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            OutlinedTextField(
                value = fileName,
                onValueChange = { fileName = it },
                label = { Text("Dosya Adı") },
                placeholder = { Text(defaultFileName) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(fileName)
                    onDismiss()
                },
            ) {
                Text("Kaydet", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat")
            }
        },
    )
}

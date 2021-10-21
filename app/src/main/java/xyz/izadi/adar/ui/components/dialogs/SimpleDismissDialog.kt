package xyz.izadi.adar.ui.components.dialogs

import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import xyz.izadi.adar.R

@Composable
fun SimpleDismissDialog(text: String, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = { Text(text = text, style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface)) },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest,
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.secondary
                )
            ) {
                Text(text = stringResource(R.string.dialog_generic_diminish).uppercase())
            }
        }
    )
}
package xyz.izadi.adar.ui.components.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Overline(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.overline.copy(
            color = color,
            fontWeight = fontWeight
        ),
        modifier = modifier
    )
}

@Composable
fun BoldOverline(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
) {
    Overline(
        text = text,
        modifier = modifier,
        color = color,
        fontWeight = FontWeight.Bold
    )
}
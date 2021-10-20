package xyz.izadi.adar.ui.components.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun HandleRow(visible: Boolean, handleColor: Color = MaterialTheme.colors.onSurface) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandIn(expandFrom = Alignment.CenterStart),
        exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.CenterStart)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Handle(color = handleColor)
        }
    }
}

@Composable
fun Handle(color: Color = MaterialTheme.colors.onSurface) {
    Spacer(
        modifier = Modifier
            .width(32.dp)
            .height(6.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color)
    )
}
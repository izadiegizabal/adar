package xyz.izadi.adar.ui.components.lists

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@Composable
fun SwipeToDelete(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            it != DismissValue.DismissedToEnd
        }
    )
    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        onDismiss()
    }
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = { SwipeToDeleteBackground(dismissState) }
    ) {
        content()
    }
}

@ExperimentalMaterialApi
@Composable
fun SwipeToDeleteBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> MaterialTheme.colors.secondary.copy(alpha = 0.75f)
            else -> MaterialTheme.colors.secondary
        }
    )
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.TwoTone.Delete,
            tint = MaterialTheme.colors.onSecondary,
            contentDescription = null,
            modifier = Modifier.scale(scale)
        )
    }
}
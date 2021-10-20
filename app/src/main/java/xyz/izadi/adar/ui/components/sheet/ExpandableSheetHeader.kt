package xyz.izadi.adar.ui.components.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import xyz.izadi.adar.utils.isExpandingOrExpanded
import xyz.izadi.adar.utils.toDp

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ExpandableSheetHeader(
    sheetState: ModalBottomSheetState,
    onHide: () -> Unit,
    bgColor: Color = Color.Transparent,
    handleColor: Color = MaterialTheme.colors.onSurface,
    title: @Composable () -> Unit
) {
    val topPadding = animateDpAsState(if (sheetState.isExpandingOrExpanded()) LocalWindowInsets.current.systemBars.top.toDp() else 0.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = bgColor)
            .padding(top = topPadding.value)
    ) {
        HandleRow(visible = !sheetState.isExpandingOrExpanded(), handleColor = handleColor)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp)
            ) {
                title()
            }
            Spacer(modifier = Modifier.weight(1.0f))
            AnimatedVisibility(visible = sheetState.isExpandingOrExpanded()) {
                IconButton(onClick = { onHide() }) {
                    Icon(Icons.TwoTone.ExpandMore, null)
                }
            }
        }
    }
}
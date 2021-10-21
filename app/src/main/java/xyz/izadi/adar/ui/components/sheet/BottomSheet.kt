package xyz.izadi.adar.ui.components.sheet

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun BottomSheet(
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    onHide: () -> Unit = {},
    id: Any,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit
) {
    if (!sheetState.isVisible) {
        LaunchedEffect(id) {
            onHide()
        }
    } else {
        BackHandler {
            scope.launch {
                sheetState.hide()
            }
        }
    }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = sheetContent
    ) {
        content()
    }
}
package xyz.izadi.adar.screens.dashboard.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.izadi.adar.domain.entity.AccountWithTransactions
import xyz.izadi.adar.domain.entity.Result
import xyz.izadi.adar.screens.account.AccountSheetContent

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun AccountSheet(
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    selectedAccountTransactions: Result<AccountWithTransactions>,
    onHide: () -> Unit,
    content: @Composable () -> Unit
) {
    if (!sheetState.isVisible) {
        LaunchedEffect(selectedAccountTransactions) {
            onHide()
        }
    }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            AccountSheetContent(
                accountWithTransactions = selectedAccountTransactions,
                sheetState = sheetState,
                onExpandLess = {
                    scope.launch {
                        sheetState.hide()
                    }
                }
            )
        }

    ) {
        content()
    }
}
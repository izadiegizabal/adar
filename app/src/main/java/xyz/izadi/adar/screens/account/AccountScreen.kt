package xyz.izadi.adar.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xyz.izadi.adar.utils.goToDashboard

@Composable
fun AccountScreen(vm: AccountViewModel = hiltViewModel(), navController: NavController, accountId: Int?) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column {
                Text(text = "This is the account $accountId screen!")
                Button(onClick = { navController.goToDashboard() }) {
                    Text(text = "Go To Dashboard")
                }
            }
        }
    }
}
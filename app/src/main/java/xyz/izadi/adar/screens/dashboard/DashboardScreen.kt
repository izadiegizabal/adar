package xyz.izadi.adar.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xyz.izadi.adar.utils.goToAccount

@Composable
fun DashboardScreen(vm: DashboardViewModel = hiltViewModel(), navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column {
                Text(text = "This is the dashboard screen!")
                Button(onClick = { navController.goToAccount() }) {
                    Text(text = "Go To Account Screen")
                }
            }
        }
    }
}
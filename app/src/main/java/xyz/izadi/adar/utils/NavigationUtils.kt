package xyz.izadi.adar.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import xyz.izadi.adar.domain.constants.Routes

fun NavController.goToAccount() {
    navigate(Routes.Account) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        restoreState = true
    }
}

fun NavController.goToDashboard() {
    navigate(Routes.Dashboard) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        restoreState = true
    }
}
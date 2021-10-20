package xyz.izadi.adar.utils

import androidx.navigation.NavController
import xyz.izadi.adar.domain.entity.Routes

fun NavController.goToAccount(id: Int, name: String) {
    navigate(Routes.Account(id, name).getFullPath())
}

fun NavController.goToDashboard() {
    navigate(Routes.Dashboard.getFullPath())
}
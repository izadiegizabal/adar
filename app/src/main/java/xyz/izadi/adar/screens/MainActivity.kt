package xyz.izadi.adar.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.izadi.adar.domain.entity.Routes
import xyz.izadi.adar.screens.account.AccountScreen
import xyz.izadi.adar.screens.dashboard.DashboardScreen

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MainScreen() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.Dashboard.getFullPath()
    ) {
        composable(Routes.Dashboard.getFullPath()) { DashboardScreen(navController = navController) }
        composable(
            Routes.Account().getFullPath(),
            arguments = Routes.Account().getArguments()
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(Routes.Account.ARGUMENT_ID)
            AccountScreen(navController = navController, accountId = id)
        }
    }
}
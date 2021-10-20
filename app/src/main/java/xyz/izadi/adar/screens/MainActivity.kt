package xyz.izadi.adar.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import xyz.izadi.adar.domain.entity.Routes
import xyz.izadi.adar.screens.account.AccountScreen
import xyz.izadi.adar.screens.dashboard.DashboardScreen
import xyz.izadi.adar.ui.components.Base
import xyz.izadi.adar.ui.theme.AdarTheme

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdarTheme {
                ProvideWindowInsets {
                    Base {
                        MainScreen()
                    }
                }
            }
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
        startDestination = Routes.Dashboard.getFullPath(),
        enterTransition = { _, _ -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(400)) },
        exitTransition = { _, _ -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(400)) + fadeOut(animationSpec = tween(durationMillis = 50, delayMillis = 370)) }
    ) {
        composable(Routes.Dashboard.getFullPath()) { DashboardScreen(navController = navController) }
        composable(
            Routes.Account().getFullPath(),
            arguments = Routes.Account().getArguments()
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(Routes.Account.ARGUMENT_ID)
            val name = backStackEntry.arguments?.getString(Routes.Account.ARGUMENT_NAME)
            AccountScreen(navController = navController, accountId = id, accountName = name)
        }
    }
}
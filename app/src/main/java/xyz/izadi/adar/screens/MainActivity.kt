package xyz.izadi.adar.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
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
                        DashboardScreen()
                    }
                }
            }
        }
    }
}
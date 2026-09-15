package com.example.a4469_inova.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.core_ui.navigation.onCoreUiNavGraph
import com.example.a4469_inova.common.HideSystemButtons
import com.example.navigation.Routes
import com.example.onboarding.navigation.onboardingNavGraph

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavHost(
    startDestination: String,
) {

    HideSystemButtons(hide = true)

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        onboardingNavGraph(
            onFinished = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ONBOARDING) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )

        onCoreUiNavGraph()

    }
}
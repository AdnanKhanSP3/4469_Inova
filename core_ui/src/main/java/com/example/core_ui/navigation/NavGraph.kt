package com.example.core_ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.core_ui.main.presentation.MainScreen
import com.example.navigation.Routes

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.onCoreUiNavGraph(
//    navController: NavHostController,
//    onFinished: () -> Unit,
) {
    navigation(
        route = Routes.HOME,
        startDestination = Routes.HOME_SCREEN
    ){

        composable(route = Routes.HOME_SCREEN) {
            MainScreen()
        }
    }
}
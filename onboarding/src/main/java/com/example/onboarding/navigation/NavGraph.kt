package com.example.onboarding.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.navigation.Routes
import com.example.onboarding.OnboardingScreen


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.onboardingNavGraph(
    onFinished: () -> Unit,
) {
    navigation(
        route = Routes.ONBOARDING,
        startDestination = Routes.ONBOARDING_SCREEN
    ){

        composable(route = Routes.ONBOARDING_SCREEN) {
            OnboardingScreen(onFinished = onFinished)
        }
    }
}
package com.example.onboarding.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun WifiAnimation() {


    var targetColor by remember { mutableStateOf(Color.LightGray) }

    // Animate color changes
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    //     Change the color in a loop
    LaunchedEffect(Unit) {
        while (true) {
            targetColor = if (targetColor == Color.LightGray) {
                Color.Yellow
            } else {
                Color.LightGray
            }
            delay(500)
        }
    }

    Column(
        modifier = Modifier
            .testTag("wifiAnimation")
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Image(
            painter = painterResource(com.example.commonresources.R.drawable.wifi),
            contentDescription = "Animated Vector",
            modifier = Modifier
                .size(120.dp),
            colorFilter = ColorFilter.tint(animatedColor)
        )
    }
}

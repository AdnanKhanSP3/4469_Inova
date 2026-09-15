package com.example.onboarding.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun DatabaseAnimation() {


    var targetColor by remember { mutableStateOf(Color.LightGray) }


    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )


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
            .testTag("DatabaseAnimation")
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Image(
            painter = painterResource(com.example.commonresources.R.drawable.database),
            contentDescription = "Animated Vector",
            modifier = Modifier
                .size(120.dp),
            colorFilter = ColorFilter.tint(animatedColor)
        )
    }

}
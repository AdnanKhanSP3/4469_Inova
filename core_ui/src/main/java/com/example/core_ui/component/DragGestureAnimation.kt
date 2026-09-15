package com.example.core_ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
import com.example.commonresources.R


@Composable
fun DragGestureAnimation(interFontFamily: androidx.compose.ui.text.font.FontFamily?) {
    val infiniteTransition = rememberInfiniteTransition(label = "dragGuide")

    // 1. Animate the horizontal drag offset (0px to 120px and back)
    val dragOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 120f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2500
                0f at 0           // Start at zero
                0f at 600         // Hold position to simulate "Long Press"
                120f at 1800      // Smooth drag to the right
                120f at 2200      // Pause at destination
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "dragOffset"
    )

    // 2. Animate the pointer scale to simulate "pressing down"
    val fingerScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2500
                1.0f at 0
                0.8f at 400       // Scale down (Press)
                0.8f at 1800      // Keep holding during drag
                1.0f at 2000      // Scale up (Release)
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "fingerScale"
    )

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.button_controls_title),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFontFamily
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.button_controls_description),
            color = Color.LightGray,
            fontSize = 14.sp,
            fontFamily = interFontFamily
        )

        /*
        // Animated track visualizer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.move_to_reorder)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .align(Alignment.CenterStart)
            )

            // Simulated Target Box Outline
//            Box(
//                modifier = Modifier
//                    .size(60.dp)
//                    .align(Alignment.CenterStart)
//                    .graphicsLayer {
//                        // The button slightly shifts to show it's being dragged
//                        translationX = dragOffset * 0.3f
//                    }
//                    .border(
//                        1.dp,
//                        Color.Yellow.copy(alpha = 0.6f),
//                        androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
//                    )
//            )

            // The Hand/Finger Indicator
//            Icon(
//                imageVector = Icons.Sharp.,   // TouchApp icon name was
//                contentDescription = null,
//                tint = Color.Yellow,
//                modifier = Modifier
//                    .size(40.dp)
//                    .align(Alignment.CenterStart)
//                    .graphicsLayer {
//                        // Match the exact dragging track translation
//                        translationX = dragOffset
//                        scaleX = fingerScale
//                        scaleY = fingerScale
//                    }
//            )


        }

         */
    }
}
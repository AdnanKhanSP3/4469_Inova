package com.example.onboarding.firstpage

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onboarding.component.TypewriteText
import com.example.commonresources.ui.theme.interFontFamily
import com.example.commonresources.R
import kotlinx.coroutines.launch

@Composable
fun OnboardingFirstScreen(
    onNext: () -> Unit
) {

    val offsetY = remember { Animatable(300f) }
    val alpha = remember { Animatable(0f) }


    LaunchedEffect(Unit) {
        launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("firstScreen")
            .padding(16.dp)
    ){

        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .offset(y = offsetY.value.dp)
                        .alpha(alpha.value)
                        .clip(RoundedCornerShape(12.dp)),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = ""
                )
                
                Spacer(modifier = Modifier.height(44.dp))

                TypewriteText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 46.dp),
                    text = "Tur Demonstrator",
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = Color.Yellow,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.explation),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                    )
                )

                Spacer(modifier = Modifier.height(44.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("NextButton")
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black,
                                    Color.Gray
                                )
                            )
                        )
                        .padding(16.dp)
                        .clickable {
                            onNext()
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(id = R.string.EXPERIENCE),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.White,
                        )
                    )
                }
            }
        }
    }
}

package com.example.core_ui.main.component

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily
import kotlinx.coroutines.delay

@Composable
fun NotificationView(
    text:String,
    status:Boolean,
    timeOut: () -> Unit
) {

    val isVisible = remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Load the Lottie animation composition from the assets folder
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("connecting_v.json"))

    // Control the animation progress
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Loop the animation forever
    )

    LaunchedEffect(Unit) {

        delay(200)

        isVisible.value = true

        delay(10000)

        isVisible.value = false

        delay(1000)

        if (status){
            //show connected toast
            Toast.makeText(context,"Connected",Toast.LENGTH_LONG).show()
            timeOut()
        }else
        {
            Toast.makeText(context,"Error in connecting to model.",Toast.LENGTH_LONG).show()
            timeOut()
        }
    }

        AnimatedVisibility(
            visible = isVisible.value,
            enter = slideInVertically(
                animationSpec = tween(
                    durationMillis = 1500,
                    delayMillis = 0,
                    easing = FastOutSlowInEasing
                ),
                initialOffsetY = {
                    fullHeight -> -fullHeight
                }
            )
//                    + expandVertically(
//                animationSpec = tween(durationMillis = 1000),
//                expandFrom = Alignment.Top
//            )
            ,
            exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = 1500,
                    delayMillis = 0,
                    easing = FastOutSlowInEasing
                ),
                targetOffsetY = {  -it * 5 }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Card(
                    modifier = Modifier
                        .width(450.dp)
                        .height(190.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .padding(horizontal = 16.dp)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .fillMaxHeight()
                                .width(400.dp)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier.size(118.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = text,
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                }
            }
        }
}
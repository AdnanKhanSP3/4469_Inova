package com.example.core_ui.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily

@Composable
fun ContactDialog (
    onCancel: () -> Unit,
){

    // Choose which animation to show
    val activeComposition by rememberLottieComposition(
        LottieCompositionSpec.Asset("contact_us.json")
    )

    // Restart animation when the state changes
    val progress by animateLottieCompositionAsState(
        composition = activeComposition,
        iterations = 10,
        restartOnPlay = false
    )

    // Choose which animation to show
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("cancel.json")
    )

    Dialog(
        onDismissRequest = {
//            onCancel()
        }) {

        Card(
            modifier = Modifier
                .width(400.dp)
                .height(610.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white)
            )
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ){
                    LottieAnimation(
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                onCancel()
                            },
                        composition = composition,

                        // MOST IMPORTANT → stop at frame 43
                        clipSpec = LottieClipSpec.Frame(0, 43),

                        // Prevent clipping if last frame is outside bounds
                        clipToCompositionBounds = false
                    )
                }
                LottieAnimation(
                    modifier = Modifier
                        .size(250.dp),
                    composition = activeComposition,
                    progress = { progress },
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.SP3_GmbH),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontSize = 14.sp,
                )
                Text(
                    text = stringResource(id = R.string.address),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    fontSize = 12.sp,
                )
                Text(
                    text = stringResource(id = R.string.phone),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    fontSize = 12.sp,
                )
                Text(
                    text = stringResource(id = R.string.email),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    fontSize = 12.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.person),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 14.sp,
                )
                Text(
                    text = stringResource(id = R.string.name),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    fontSize = 12.sp,
                )
                Text(
                    text = stringResource(id = R.string.person_email),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    fontSize = 12.sp,
                )
                Text(
                    text = stringResource(id = R.string.person_phone),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.version),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.Black,
                    fontSize = 12.sp,
                )
            }
        }
    }
}
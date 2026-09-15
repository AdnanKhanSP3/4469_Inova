package com.example.core_ui.component

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily
import com.example.core_ui.dynamicscreen.presentation.DynamicScreenViewModel


@Composable
fun SliderValueDialog(
    dynamicScreenViewModel: DynamicScreenViewModel = hiltViewModel(),
    onUpdate: ()-> Unit
) {

    val context = LocalContext.current

    // Choose which animation to show
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("cancel.json")
    )

    Dialog(
        onDismissRequest = {
            dynamicScreenViewModel.sliderBtnValueDialogState(false)
        }
    ){

        Card(
            modifier = Modifier
                .width(400.dp)
                .height(300.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white)
            )
        ){

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ){
                LottieAnimation(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            dynamicScreenViewModel.sliderBtnValueDialogState(false)
                        },
                    composition = composition,

                    // MOST IMPORTANT → stop at frame 43
                    clipSpec = LottieClipSpec.Frame(0, 43),

                    // Prevent clipping if last frame is outside bounds
                    clipToCompositionBounds = false
                )
            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ){

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.slider_value),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontSize = 12.sp
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                    ),
                    value = dynamicScreenViewModel.updatedSliderBtnValue.toString(),
                    onValueChange ={
                        var value = it.toIntOrNull()

                        if (value == null){
                            value = 0
                        }

                        if ( value < -1 || value >100){

                            Toast.makeText(context,"Please enter correct slider values",Toast.LENGTH_SHORT)
                                .show()

                            return@OutlinedTextField
                        }
                         dynamicScreenViewModel.updatedSliderBtnValue = it.toIntOrNull() ?: 0

                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )

//                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom

                ){

                    OutlinedButton(
                        modifier = Modifier.width(150.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(4.dp),

                        onClick = {
                            dynamicScreenViewModel.sliderBtnValueDialogState(false)
                        }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = interFontFamily,
                            ),
                            fontSize = 12.sp
                        )
                    }
                    OutlinedButton(
                        modifier = Modifier.width(150.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  Color.Yellow,
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color.Yellow),
                        shape = RoundedCornerShape(4.dp),
                        onClick = {

                            dynamicScreenViewModel.sliderBtnValueDialogState(false)
                            onUpdate()

                        }) {
                        Text(
                            text = stringResource(id = R.string.update),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = interFontFamily,
                            ),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
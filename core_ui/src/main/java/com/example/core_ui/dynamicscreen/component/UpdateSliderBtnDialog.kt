package com.example.core_ui.dynamicscreen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.example.database.data.model.SliderButton

@Composable
fun UpdateSliderBtnDialog(
    sliderButton: SliderButton,
    dynamicScreenViewModel: DynamicScreenViewModel = hiltViewModel()
) {

    // Choose which animation to show
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("cancel.json")
    )


    Dialog(
        onDismissRequest = {
            dynamicScreenViewModel.UpdateSliderBtnState(false)
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

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ){

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ){
                    LottieAnimation(
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                dynamicScreenViewModel.UpdateSliderBtnState(false)
                            },
                        composition = composition,

                        // MOST IMPORTANT → stop at frame 43
                        clipSpec = LottieClipSpec.Frame(0, 43),

                        // Prevent clipping if last frame is outside bounds
                        clipToCompositionBounds = false
                    )
                }

                Text(
                    text = stringResource(id = R.string.enter_button_name),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontSize = 14.sp
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                    ),
                    value = dynamicScreenViewModel.updatedSliderBtnText,
                    onValueChange ={
                        dynamicScreenViewModel.updatedSliderBtnText =  it
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

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
                            dynamicScreenViewModel.UpdateSliderBtnState(false)
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
//                            onSaveButton()
//                            dynamicScreenViewModel.updateSliderBtnText(sliderButton)
//                            dynamicScreenViewModel.UpdateSliderBtnState(false)

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
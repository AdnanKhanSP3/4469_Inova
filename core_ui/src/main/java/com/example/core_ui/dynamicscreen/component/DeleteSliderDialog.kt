package com.example.core_ui.dynamicscreen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core_ui.dynamicscreen.presentation.DynamicScreenViewModel
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily

@Composable
fun DeleteSliderDialog(
    onDeleteButton : () -> Unit,
    dynamicScreenViewModel: DynamicScreenViewModel = hiltViewModel()
) {

    Dialog(onDismissRequest = {
        dynamicScreenViewModel.deleteSliderDialogState(false)
    }) {
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
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp),
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "")


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.delete_slider),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(36.dp))

                Row (modifier = Modifier.fillMaxWidth(),
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
                        dynamicScreenViewModel.deleteSliderDialogState(false)
                    }) {

                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.Black
                        )
                    }

                    OutlinedButton(
                        modifier = Modifier.width(150.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  colorResource(id = R.color.positive_color),
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, colorResource(id = R.color.positive_color)),
                        shape = RoundedCornerShape(4.dp),
                        onClick = {
                        onDeleteButton()
                    }) {
                        Text(
                            text = stringResource(id = R.string.delete),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.White)
                    }
                }
            }
        }
    }
}
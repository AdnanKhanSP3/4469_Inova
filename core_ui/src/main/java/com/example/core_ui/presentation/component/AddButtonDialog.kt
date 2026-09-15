package com.example.core_ui.admin.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily
import com.example.core_ui.admin.presentation.dynamicScreen.AdminDynamicScreenViewModel


@Composable
fun AddButtonDialog(
    onSaveButton : () -> Unit,
    adminDynamicScreenViewModel: AdminDynamicScreenViewModel = hiltViewModel()
) {

    Dialog(
        onDismissRequest = {
            adminDynamicScreenViewModel.dialogState(false)
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
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {


                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp),
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.enter_button_name),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 14.sp
                    )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                    ),
                    value = adminDynamicScreenViewModel.buttonText.value,
                    onValueChange ={
                        adminDynamicScreenViewModel.buttonText.value = it
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
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
                            adminDynamicScreenViewModel.run { dialogState(false) }
                        }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
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
                            onSaveButton()
                            adminDynamicScreenViewModel.run { dialogState(false) }
                        }) {
                        Text(
                            text = stringResource(id = R.string.add),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
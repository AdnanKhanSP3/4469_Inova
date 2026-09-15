package com.example.core_ui.admin.presentation.component

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily


@Composable
fun  IpAddressDialog(
    onDismiss: () -> Unit,
    onAppNameChange:(String) -> Unit,
    appName : String,
    onIpAddressChange:(String) -> Unit,
    ipAddress: String,
    onPortChange:(String) -> Unit,
    port: String
) {

    var _appName by remember { mutableStateOf(appName) }
    var _ipAddress by remember { mutableStateOf(ipAddress) }
    var _port by remember { mutableStateOf(port) }

    Log.d("ipAddr","$_ipAddress")

    Dialog(
        onDismissRequest = {
        onDismiss()
    }
    ){
        Card(
            modifier = Modifier
                .width(450.dp)
                .height(430.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white)
            )
        ){
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.settings),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "IP Address",
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.Black
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                    ),
                    value = _ipAddress,
                    onValueChange ={
//                        onIpAddressChange(it)
                        _ipAddress = it
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(id = R.string.port_number),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.Black
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                    ),
                    value =  _port,
                    onValueChange ={
//                        onPortChange(it)
                        _port = it                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(id = R.string.app_title),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.Black
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                    ),
                    value = _appName,
                    onValueChange = { name ->
//                        onAppNameChange(name)
                        _appName = name
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
                            onDismiss()
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
                            containerColor =  Color.Yellow,
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color.Yellow),
                        shape = RoundedCornerShape(4.dp),
                        onClick = {
                            onAppNameChange(_appName)
                            onIpAddressChange(_ipAddress)
                            onPortChange(_port)
                            onDismiss()
                            utils.address = _ipAddress
                            utils.port = _port
                            
                        })
                    {
                        Text(text = stringResource(id = R.string.confirm),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
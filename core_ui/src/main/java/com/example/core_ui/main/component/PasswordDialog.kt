package com.example.core_ui.main.component

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily
import com.example.core_ui.main.presentation.MainViewModel

@Composable
fun PasswordDialog(
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val context = LocalContext.current


    // Lottie animation setup
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("password.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = Int.MAX_VALUE)

    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    Dialog(
        onDismissRequest = {
            mainViewModel.dialogState(false)
        }
    ) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .height(330.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white)
            )
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Lottie animation in place of CircularProgressIndicator
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(id = R.string.enter_password),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =mainViewModel.enterPassword,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                    ),
                    onValueChange = { password ->
                       mainViewModel.updatePassword(password)
                    },
                    visualTransformation =  if (passwordVisible) VisualTransformation.None
                                        else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            R.drawable.visibility
                        else  R.drawable.visibility_off

                        // Please provide localized description for accessibility services
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(painter  = painterResource(image), description)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row (
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
                            mainViewModel.dialogState(false)
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
                            //Now compare these two passwords
                            if(mainViewModel.enterPassword.equals("sp312345")){
//                                scope.launch {
                                    //show progress dialog  ad delay for 2 seconds
                                    mainViewModel.dialogState(false)
//
//                                    mainViewModel.progressDialogState(true)
//
//                                    mainViewModel.progressDialogState(false)

//                                }
                            }
                            else{
                                mainViewModel.dialogState(false)
                                Toast.makeText(context,
                                    context.getString(R.string.wrong_password),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }) {
                        Text(
                            text = stringResource(id = R.string.confirm),
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

@Composable
fun ProgressbarDialog(
   mainViewModel: MainViewModel = hiltViewModel()
){
    val currentMode = mainViewModel.currentMode.collectAsState()

    // Lottie animation setup
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("loading.json"))
    val progress by animateLottieCompositionAsState(composition, iterations = Int.MAX_VALUE)


    val title = if(currentMode.value == AppMode.USER) "Admin Mode" else "User Mode"
    Dialog(
        onDismissRequest = {}
    ) {

        Card(
            modifier = Modifier
                .width(400.dp)
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white)
            )
        ){

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Text(text = "Loading : " + title,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Lottie animation in place of CircularProgressIndicator
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }

}

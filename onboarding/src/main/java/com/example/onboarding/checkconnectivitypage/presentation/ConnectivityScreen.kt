package com.example.onboarding.checkconnectivitypage.presentation

import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commonresources.R
import com.example.onboarding.checkconnectivitypage.component.ConnectModelWifiDialog
import com.example.onboarding.checkconnectivitypage.component.EnableWifiDialog
import com.example.onboarding.checkconnectivitypage.component.MQTTDialog
import com.example.onboarding.checkconnectivitypage.component.PermanentSnackbar
import com.example.onboarding.component.DatabaseAnimation
import com.example.onboarding.component.TaskState
import com.example.onboarding.component.DoneTask
import com.example.onboarding.component.MQTTConnectionAnimation
import com.example.onboarding.component.TodoItem
import com.example.onboarding.component.WifiAnimation
import com.example.commonresources.ui.theme.interFontFamily
import com.example.onboarding.checkconnectivitypage.ConnectivityViewModel
import com.example.wifi.presentation.LocationPermissionScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ConnectivityScreenWrapper(
    viewModel: ConnectivityViewModel = hiltViewModel(),
    onFinished: () -> Unit,
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        val context = LocalContext.current

        val task1Checked by viewModel.task1Checked
        val showEnableWifiDialog by viewModel.showEnableWifiDialog.collectAsState()

        val showModelWifiDialog by viewModel.showModelWifiDialog

        val task2Checked by viewModel.task2Checked.collectAsState()
        val task3Checked by viewModel.task3Checked.collectAsState()
        val snackbarMessage by viewModel.snackbarMessage.collectAsState(initial = "")
        val showPermanentSnackbar by viewModel.showPermanentSnackbar.collectAsState()

        val showMQTTDialog by viewModel.showMQTTDialog


        //first call wifi module here
        LocationPermissionScreen(
            modifier = Modifier,
            wifiStatus = { status ->

                if (status.isWifiConnected == false) {

                    viewModel.updateTask1Checked(false)
                    viewModel.showEnableWifiDialogState(true)

                }
                else if (!status.isTargetSsid) {

//                        Log.d("TAG","${status.ssid} ${status.isTargetSsid}")
                        viewModel.showModelWifiDialogState(true)
                        viewModel.updateTask1Checked(false)

                    }
//                else if (status.ssid != "4412 CLA Grill #2") {
                else if (status.ssid != "SP3") {

                    viewModel.showModelWifiDialogState(true)

                } else {

                        viewModel.showEnableWifiDialogState(false)

                        viewModel.showModelWifiDialogState(false)

                        viewModel.updateTask1Checked(true)

                        //now move to mqtt
                        viewModel.checkMQTT()

                        viewModel.hideSnackbar()
                    }
                }
            )

        // Initialize the launcher
        val wifiSettingsLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {result ->

            if (result.resultCode == RESULT_CANCELED) {

            }else
            {
                viewModel.showEnableWifiDialogState(false)
            }
        }

        val specificNetworkLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){result ->
            if (result.resultCode== RESULT_CANCELED) {

            }else {
                viewModel.showModelWifiDialogState(false)
            }
        }


        ConnectivityScreen(
            task1Checked = task1Checked,
            task2Checked = task2Checked,
            task3Checked = task3Checked,
            snackbarMessage = snackbarMessage,
            showPermanentSnackbar = showPermanentSnackbar,
            showEnableWifiDialog = showEnableWifiDialog,
            showModelWifiDialog = showModelWifiDialog,
            showMQTTDialog = showMQTTDialog,
            onSkip = {
                onFinished()
            },
            onEnableWifi = {

                viewModel.showEnableWifiDialogState(false)
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                wifiSettingsLauncher.launch(intent)
            },
            onModelWifi = {
            viewModel.showModelWifiDialogState(false)
            val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
            specificNetworkLauncher.launch(intent)
            },
            onMQTTCheck = {
            viewModel.checkMQTT()
            },
            onSnackbarRetryClick = {
                if (it.contains("model Network")){
                    viewModel.showModelWifiDialogState(true)
                    Log.d("Wifi","model Network")
                }
                else if (it.contains("WiFi") || it.contains("WLAN")
                    || it.contains("Wifi") || it.contains("wlan")
                    ){
                    viewModel.showEnableWifiDialogState(true)
                    Log.d("Wifi","Wifi WifiWifi")
                }
            },
            onDismissEnableWifiDialog = {

                viewModel.showEnableWifiDialogState(false)
                //now show snackbar here permanent
                viewModel.showSnackbarMessage(
                    context.getString(R.string.wifi_not_enabled)
                )
            },
            onDismissModelWifiDialog = {
                viewModel.showModelWifiDialogState(false)
                viewModel.showSnackbarMessage(context.getString(R.string.wifi_not_connected_router))
            },
            onDismissMQTTDialog = {
                viewModel.showMQTTDialog.value = false
                viewModel.showSnackbarMessage(context.getString(R.string.mqtt_message))
            },
            onPermissionUpdate = {

            }
        )
    }
}

@Composable
fun ConnectivityScreen(
    task1Checked: Boolean,
    task2Checked: Boolean,
    task3Checked: Boolean,
    showEnableWifiDialog: Boolean = false,
    showModelWifiDialog: Boolean = false,
    showMQTTDialog: Boolean = false,
    snackbarMessage: String,
    showPermanentSnackbar: Boolean,
    onSkip: () -> Unit,
    onEnableWifi: () -> Unit,
    onModelWifi: () -> Unit,
    onMQTTCheck: () -> Unit,
    onSnackbarRetryClick: (String) -> Unit,
    onDismissEnableWifiDialog: () -> Unit,
    onDismissModelWifiDialog: () -> Unit,
    onDismissMQTTDialog: () -> Unit,
    onPermissionUpdate: (Boolean) -> Unit)
{
        val taskState = when {
            !task1Checked && !task2Checked -> TaskState.WIFI
            task1Checked && !task2Checked -> TaskState.MQTT
            !task3Checked -> TaskState.DATABASE
            task1Checked && task2Checked && task3Checked -> TaskState.DONE
            else -> TaskState.NONE
        }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("ConnectivityScreen")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.black))
                        .padding(16.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .testTag("SkipButton")
                                .height(36.dp)
                                .width(145.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color.White),
                            onClick = onSkip
                        ) {
                            Text(
                                modifier = Modifier,
                                text = stringResource(id = R.string.skip),
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light,
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.padding(50.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    text = stringResource(id = R.string.checking) +"\n" + stringResource(
                                        id = R.string.requirements
                                    ),
                                    fontFamily = interFontFamily,
                                    fontWeight = FontWeight.Light,
                                    style = TextStyle(
                                        fontSize = 32.sp,
                                        color = Color.Yellow
                                    )
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                // Task 1
                                TodoItem(
                                    text = stringResource(id = R.string.todo1),
                                    checked = task1Checked,
                                    modifier = Modifier.testTag("wifi")
                                )

                                // Task 2
                                TodoItem(
                                    text = stringResource(id = R.string.todo2),
                                    checked = task2Checked,
                                )

                                // Task 3
                                TodoItem(
                                    text = stringResource(id = R.string.todo3),
                                    checked = task3Checked,
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AnimatedContent(
                                targetState = taskState,
                                transitionSpec = {
                                    (slideInHorizontally(
                                        initialOffsetX = { fullWidth -> fullWidth },
                                        animationSpec = tween(durationMillis = 1000)
                                    ) + fadeIn(animationSpec = tween(durationMillis = 800))).togetherWith(
                                        slideOutHorizontally(
                                            targetOffsetX = { fullWidth -> -fullWidth },
                                            animationSpec = tween(durationMillis = 1000)
                                        ) + fadeOut(animationSpec = tween(durationMillis = 800))
                                    )
                                }, label = ""
                            ) { state ->
                                when (state) {
                                    TaskState.WIFI -> WifiAnimation()
                                    TaskState.MQTT -> MQTTConnectionAnimation()
                                    TaskState.DONE -> DoneTask {
                                        onSkip()
                                    }
                                    TaskState.DATABASE -> DatabaseAnimation()
                                    TaskState.NONE -> Box(modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                    }
                }
                if (showPermanentSnackbar) {
                    PermanentSnackbar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .zIndex(1f),
                        message = snackbarMessage,
                        onRetryClick = { onSnackbarRetryClick(snackbarMessage) }
                    )
                }
            }

    if (showEnableWifiDialog) {
        EnableWifiDialog(
            onConfirm = onEnableWifi,
            onDismiss = onDismissEnableWifiDialog
        )
    }

    if (showModelWifiDialog) {
        ConnectModelWifiDialog(
            onConfirm = onModelWifi,
            onDismiss = onDismissModelWifiDialog
        )
    }

    if (showMQTTDialog) {
        MQTTDialog(
            onDismiss = onDismissMQTTDialog,
            onConfirm = onMQTTCheck
        )
    }
}
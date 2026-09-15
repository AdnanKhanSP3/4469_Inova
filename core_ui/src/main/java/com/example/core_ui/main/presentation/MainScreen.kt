package com.example.core_ui.main.presentation

import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.canopas.lib.showcase.component.rememberIntroShowcaseState
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily
import com.example.core_ui.animation.AnimationScreen
import com.example.core_ui.component.EnableWifiDialog
import com.example.core_ui.main.component.AppMode
import com.example.core_ui.main.component.AppNavigationRail
import com.example.core_ui.main.component.ContactDialog
import com.example.core_ui.main.component.ImageBox
import com.example.database.data.model.NavigationItem
import com.example.wifi.presentation.LocationPermissionScreen
import com.example.core_ui.component.ConnectModelWifiDialog
import com.example.core_ui.component.KeepScreenOn
import com.example.core_ui.component.MQTTDialog
import com.example.core_ui.main.component.NotificationView
import com.example.core_ui.main.component.PermanentSnackbar
import com.example.core_ui.setting.SettingScreen
import kotlinx.coroutines.selects.select

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
) {

    val testList = mainViewModel.testItemList.collectAsState()

    val context = LocalContext.current
    val currentMode by mainViewModel.currentMode.collectAsState()

    val showCaseState by mainViewModel.showCaseState.collectAsState()

    val introShowcaseState = rememberIntroShowcaseState()

    val snackbarMessage by mainViewModel.snackbarMessage.collectAsState(initial = "")

    val showPermanentSnackbar by mainViewModel.showPermanentSnackbar.collectAsState()

    var selectedItem by remember {
        mutableStateOf(
            testList.value.navigationItems.firstOrNull()
        )
    }

    val showMQTTDialog by mainViewModel.showMQTTDialog

    var selectedItemToDelete by remember {
        mutableStateOf<NavigationItem?>(null)
    }

    // Initialize the launcher
    val wifiSettingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_CANCELED) {

        }else
        {
//            mainViewModel.showEnableWifiDialogState(false)
        }
    }

    val specificNetworkLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode== RESULT_CANCELED) {

        }else {
//            mainViewModel.showModelWifiDialogState(false)
        }
    }

    LaunchedEffect(currentMode)
    {
        if (currentMode == AppMode.USER) {
            mainViewModel.getNavigationItemsWithActionButtons()
            selectedItem = testList.value.navigationItems.first()
            selectedItemToDelete = null
        }

        mainViewModel.subscribeTopic(
            topic = "SP3/4469/Kamera",
            ){ value ->
            Log.d("kamera","$value")
            if(value == "1"){
                //turn on switch
                mainViewModel.demoSwitch = true

            }else if (value == "0"){
                //turn off switch
                mainViewModel.demoSwitch = false
            }
        }

        mainViewModel.subscribeTopic(
            topic = "SP3/4469/warm_white",
        ){value ->
            if(value == "1"){
                //turn on switch
                mainViewModel.demoAction = true

            }else if (value == "0"){
                //turn off switch

                mainViewModel.demoAction = false
            }
        }
    }

    ImageBox()

    KeepScreenOn(true)

    //subscribe Demo button

    if (currentMode == AppMode.USER){
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(White.copy(.3f))
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 16.dp, start = 16.dp, end = 8.dp)

            ) {

                Spacer(modifier = Modifier.height(26.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ){

                    Image(
                        modifier = Modifier
                            .height(125.dp)
                            .width(200.dp)
                            .padding(bottom = 45.dp),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription ="company logo",
                        contentScale = ContentScale.FillBounds
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                }

                Spacer(modifier = Modifier.height(16.dp))

                if(!testList.value.isLoading){

                    AppNavigationRail(
                        modifier = Modifier
                            .weight(1.0f),
                        onModeSwitch = {

                            if(currentMode == AppMode.USER){
                                mainViewModel.dialogState(true)
                            }else
                            {
                                mainViewModel.switchMode(AppMode.USER)
                            }
                        },
                        navigationItems = testList.value.navigationItems,
                        selectItemClick = {
                            selectedItem  = it
                        },
                        selectedItem = selectedItem,

                        onLongPress = {
                            if(it.createdByAdmin){
                                Toast.makeText(context,
                                    context.getString(R.string.cannot_delete_page),
                                    Toast.LENGTH_SHORT).show()
                            }else
                            {
                                selectedItemToDelete = it
                                mainViewModel.deleteDialogItemState(true)
                            }
                        },
                        onMove = { from, to ->
                            mainViewModel.moveNavigationItem(
                                from,
                                to
                            )
                        },
                        onDragEnd = {
                            mainViewModel.saveNavigationOrder()
                        },
                    )
                }

                IntroShowcase(
                    showIntroShowCase = showCaseState,
                    dismissOnClickOutside = false,
                    onShowCaseCompleted = {
                        mainViewModel.updateShowCaseState(false)
                    },
                    state = introShowcaseState,
                ){
                    Column(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top

                        ) {

                            Text(
                                modifier = Modifier,
                                text = stringResource(id = R.string.camera),
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    color = White
                                )
                            )

                            Switch(
                                modifier = Modifier
                                    .scale(0.8f)
                                    .offset(y = (-14).dp),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = colorResource(id = R.color.surface),
                                    checkedTrackColor = colorResource(id = R.color.white),
                                    uncheckedThumbColor = colorResource(id = R.color.surface),
                                    uncheckedTrackColor = colorResource(id = R.color.white)
                                ),
                                checked = mainViewModel.demoSwitch,
                                onCheckedChange = {
                                    mainViewModel.demoSwitch = it

                                    if (mainViewModel.demoSwitch){
                                        mainViewModel.publishMessage(
                                            "SP3/4469/Kamera",
                                            "1"
                                        )
                                    }else{
                                        mainViewModel.publishMessage(
                                            "SP3/4469/Kamera",
                                            "0"
                                        )
                                    }
                                }
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top

                        ) {

                            Text(
                                modifier = Modifier,
                                text = stringResource(id = R.string.action),
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    color = White
                                )
                            )

                            Switch(
                                modifier = Modifier
                                    .scale(0.8f)
                                    .offset(y = (-14).dp),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = colorResource(id = R.color.surface),
                                    checkedTrackColor = colorResource(id = R.color.white),
                                    uncheckedThumbColor = colorResource(id = R.color.surface),
                                    uncheckedTrackColor = colorResource(id = R.color.white)
                                ),
                                checked = mainViewModel.demoAction,
                                onCheckedChange = {
                                    mainViewModel.demoAction = it

                                    if (mainViewModel.demoAction){
                                        mainViewModel.publishMessage(
                                            "SP3/4469/warm_white",
                                            "1"
                                        )
                                    }else{
                                        mainViewModel.publishMessage(
                                            "SP3/4469/warm_white",
                                            "0"
                                        )
                                    }
                                }
                            )
                        }

                        Text(
                            modifier = Modifier
                                .width(300.dp)
                                .clickable {
                                    mainViewModel.updateShowCaseState(true)
                                    mainViewModel.getshowCaseState()
                                    introShowcaseState.reset()
                                },
                            text = stringResource(id = R.string.restart),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = White
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            modifier = Modifier
                                .height(30.dp)
                                .width(250.dp)
                                .clickable {
                                    mainViewModel.contactDialogState(true)
                                }
                                .introShowCaseTarget(
                                    index = 0,
                                    style = ShowcaseStyle.Default.copy(
                                        backgroundColor = Color(0xFF1C0A00),
                                        backgroundAlpha = 0.98f,
                                        targetCircleColor = White
                                    ),
                                    content = {

                                        Column {
                                            Text(
                                                text = stringResource(id = R.string.idea),
                                                color = White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = stringResource(id = R.string.idea_s),
                                                color = White,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                ),
                            text = stringResource(id = R.string.contact_us),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = White
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .width(300.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = stringResource(id = R.string.sp3),
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light,
                                fontSize = 13.sp,
                                color = White.copy(.5f)
                            )
                        }
                    }
                }
            }
            // Dynamic Screen content on the right
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = colorResource(id = R.color.background_color)
                    )
            ) {
                if (selectedItem?.navigationItem?.navigationItemId == 1L){

                    selectedItem?.let { item ->
                        AnimationScreen( item,
                            introShowcaseState,
                            showCaseState,
                            {
                                mainViewModel.updateShowCaseState(it)
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                        ?: run {
                            // Show a default screen when no item is selected
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center) {
                                Text(
                                    stringResource(id = R.string.select_item),
                                    fontFamily = interFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    style = TextStyle(
                                        color = White
                                    )
                                )
                            }
                        }
                }
                if (selectedItem?.navigationItem?.navigationItemId == 2L){

                    selectedItem?.let { item ->
                        SettingScreen()
                    }
                        ?: run {
                            // Show a default screen when no item is selected
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center) {
                                Text(
                                    stringResource(id = R.string.select_item),
                                    fontFamily = interFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    style = TextStyle(
                                        color = White
                                    )
                                )
                            }
                        }
                }
                /*
                selectedItem?.let { item ->
                    AnimationScreen( item,
                        introShowcaseState,
                        showCaseState,
                        {
                            mainViewModel.updateShowCaseState(it)
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                    ?: run {
                    // Show a default screen when no item is selected
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(id = R.string.select_item),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Normal,
                            style = TextStyle(
                                color = White
                            )
                        )
                    }
                }

                 */
            }
        }
    }

    //first call wifi module here
    LocationPermissionScreen(
        modifier = Modifier,
        wifiStatus = { status ->
            if (status.isWifiConnected == false) {

                mainViewModel.showEnableWifiDialogState(true)

            } else if (!status.isTargetSsid) {

                mainViewModel.showModelWifiDialogState(true)

//            } else if (status.ssid != "4412 CLA Grill #2") {
            } else if (status.ssid != "SP3") {

                mainViewModel.showModelWifiDialogState(true)

            } else {
                mainViewModel.showEnableWifiDialogState(false)

                mainViewModel.showModelWifiDialogState(false)

                mainViewModel.hideSnackbar()

                // now move to mqtt
                mainViewModel.checkMQTT()
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        if (showPermanentSnackbar) {
            PermanentSnackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f),
                message = snackbarMessage,
                onRetryClick = {
                    if (snackbarMessage.contains("MQTT")){
                        //notification view
                        mainViewModel.notificationDialog = true
                        mainViewModel.checkMQTT()
                    } else if (snackbarMessage.contains("model Network")){
                    mainViewModel.showModelWifiDialogState(true)
                }
                else if (
                        snackbarMessage.contains("WiFi") || snackbarMessage.contains("WLAN")
                        || snackbarMessage.contains("Wifi") || snackbarMessage.contains("wlan")
                    ){
                    mainViewModel.showEnableWifiDialogState(true)
                }
                }
            )
        }
    }

    if (mainViewModel.showEnableWifiDialog.value){
        EnableWifiDialog(
            onConfirm = {
                mainViewModel.showEnableWifiDialogState(false)
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                wifiSettingsLauncher.launch(intent)
            }) {
            mainViewModel.showEnableWifiDialogState(false)
            //now show snackbar here permanent
            mainViewModel.showSnackbarMessage(context.getString(R.string.wifi_not_enabled))
        }
    }

    if (mainViewModel.showModelWifiDialog.value ){
        ConnectModelWifiDialog(
            onConfirm = {
                mainViewModel.showModelWifiDialogState(false)
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                specificNetworkLauncher.launch(intent)
            }
        ) {
            mainViewModel.showModelWifiDialogState(false)
            mainViewModel.showSnackbarMessage(context.getString(R.string.wifi_not_connected_router))
        }
    }

    if (mainViewModel.notificationDialog) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                }
                .zIndex(100f)
        ){

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            ) {
                NotificationView(
                    text = "Connecting to the Model",
                    status =  mainViewModel.notificationDialogVisible
                ) {
                    mainViewModel.notificationDialog = false
                }
            }
        }
    }

    if (showMQTTDialog && !showPermanentSnackbar){
        MQTTDialog(
            onDismiss = {
                mainViewModel.showMQTTDialog.value = false
                mainViewModel.showSnackbarMessage(context.getString(R.string.mqtt_message))
            },
            onConfirm = {
                mainViewModel.showMQTTDialog.value = false
                mainViewModel.checkMQTT()
            }
        )
    }

    if (mainViewModel.contactDialog.value){
        ContactDialog {
            mainViewModel.contactDialogState(false)
        }
    }
}

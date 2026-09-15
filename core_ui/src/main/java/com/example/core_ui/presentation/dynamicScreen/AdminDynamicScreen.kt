package com.example.core_ui.admin.presentation.dynamicScreen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily
import com.example.core_ui.admin.presentation.AdminMainScreenViewModel
import com.example.core_ui.admin.presentation.component.AddButtonDialog
import com.example.core_ui.admin.presentation.component.DeleteDialog
import com.example.core_ui.admin.presentation.component.DeleteSliderButtonDialog
import com.example.core_ui.admin.presentation.component.SliderButtonDialog
import com.example.core_ui.admin.presentation.component.SliderDialog
import com.example.core_ui.dynamicscreen.component.LanguageChangeHelper
import com.example.core_ui.dynamicscreen.presentation.Language
import com.example.core_ui.dynamicscreen.presentation.LanguagesDropdown
import com.example.core_ui.main.component.ColorSlider
import com.example.database.data.model.ActionButtons
import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.data.model.SliderButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.runtime.LaunchedEffect
import com.example.core_ui.component.ShutDownDialog
import com.example.core_ui.main.presentation.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDynamicScreen(
    navigationWithButtons: NavigationItemWithActionButtons,
    modifier: Modifier,
    adminDynamicScreenViewModel: AdminDynamicScreenViewModel = hiltViewModel(),
    adminMainScreenViewModel: AdminMainScreenViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
) {

    adminDynamicScreenViewModel.setNavigationItem(navigationWithButtons)

    //load slider
    adminDynamicScreenViewModel.getSlider(navigationWithButtons.navigationItem!!.navigationItemId)

    //load Slider Buttons
    adminDynamicScreenViewModel.getSliderButton(navigationWithButtons.navigationItem!!.navigationItemId)

    val navigationItemWithButtonsState = adminDynamicScreenViewModel.navigationItemWithButtons.collectAsState()

    val appName = adminMainScreenViewModel.appName.value

    val buttonStates by adminDynamicScreenViewModel.buttonStates.collectAsState()


    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var selectedButton by remember { mutableStateOf<ActionButtons?>(null) }

    var selectedSliderButton by remember { mutableStateOf<SliderButton?>(null) }

    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }

    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    val animatedBrightness by animateFloatAsState(
        targetValue = adminDynamicScreenViewModel.brightness,
        animationSpec = tween(
            durationMillis = 500,
            easing = EaseOut
        ),
        label = "animatedBrightnessRed"
    )

    BackHandler(modalSheetState.isVisible) {
        scope.launch { modalSheetState.hide() }
    }


    // Declare borderColor here within the @Composable function
    var borderColor by remember { mutableStateOf(White) }

    var borderColorAllOff by remember { mutableStateOf(White) }

    val borderColorLanugage by remember { mutableStateOf(White) }

    val languageChangeHelper by lazy {
        LanguageChangeHelper()
    }

    val listOfLanguages = listOf(
        Language("de", "Deutsch", R.drawable.germany),
        Language("en", "English", R.drawable.usa)
    )

    val currentLanguageCode: String = languageChangeHelper.getLanguageCode(context)

    var currentLanguage by remember { mutableStateOf(currentLanguageCode) }

    val onCurrentLanguageChange: (String) -> Unit = { newLanguage ->
        currentLanguage = newLanguage
        languageChangeHelper.changeLanguage(context, newLanguage)
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ){
        Image(
            modifier = Modifier
                .height(300.dp)
                .width(200.dp)
                .padding(bottom = 130.dp),
            painter = painterResource(id = R.drawable.round_ball),
            contentDescription ="" )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = appName,
                fontFamily =  interFontFamily,
                fontWeight = FontWeight.Medium,
                style = TextStyle(
                    fontSize = 40.sp,
                    color = Color.Yellow
                )
            )

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {
                LanguagesDropdown(
                    modifier = Modifier
                        .width(130.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, borderColorLanugage),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color.Black),
                    languagesList = listOfLanguages,
                    currentLanguage,
                    onCurrentLanguageChange
                )

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, borderColorAllOff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color.Black)

                        .pointerInput(Unit) {

                            detectTapGestures(
                                onTap = {
                                },
                                onPress = {
                                    borderColorAllOff = Color.Yellow
                                    try {

                                        adminDynamicScreenViewModel.resetAllButtonStates()
                                        //send mqtt msg
                                        mainViewModel.publishMessage(
                                            "allOff",
                                            "1"
                                        )

                                        tryAwaitRelease()
                                        // Change borderColor to Yellow on press
                                        borderColorAllOff = White

                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    context.getString(R.string.exception_caught)
                                                            + "${e.message}",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                },
                                onLongPress = {

                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        modifier = Modifier
                            .padding(6.dp),
                        text = stringResource(id = R.string.all_off),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            color = White,
                            fontSize = 12.sp,
                        )
                    )
                }

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, borderColor),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color.Black)
                        .pointerInput(Unit) {

                            detectTapGestures(
                                onTap = {
                                },
                                onPress = {
                                    borderColor = Color.Yellow
                                    try {

                                        //shut down  PC mqtt msg
                                      adminDynamicScreenViewModel.showShutDowndialog = true

                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    context.getString(R.string.exception_caught)
                                                            + "${e.message}",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                    try {
                                        adminMainScreenViewModel.publishMessage("shutdown")
                                        tryAwaitRelease()
                                        borderColor = White
                                    } catch (e: Exception) {

                                        withContext(Dispatchers.Main) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    context.getString(R.string.exception_caught)
                                                            + "${e.message}",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                },
                                onLongPress = {
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        modifier = Modifier
                            .padding(6.dp),
                        text = stringResource(id = R.string.shut_down),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            color = White,
                            fontSize = 12.sp,
                        )
                    )
                }
            }
        }

        LazyVerticalGrid (
            modifier
            = modifier
                .fillMaxSize()
                .weight(2f)
                .padding(horizontal = 100.dp),
            columns =  GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(60.dp),
        ){
            items(navigationItemWithButtonsState
                .value.navigationItemActionButtons,
                key = { button -> button.actionButtonId }
                ) { buttons ->

                // Create a separate MutableInteractionSource for each button
                val interactionSource = remember { MutableInteractionSource() }

                val action = buttons.buttonlabel
                val isClicked = buttonStates[action] ?: false

                LaunchedEffect(key1 = action) {

                    mainViewModel.subscribeTopic(
                        "${navigationWithButtons.navigationItem!!.label}/${action}"
                    ){
                        if (it.equals("1")){
                            adminDynamicScreenViewModel.toggleButtonState(action , true)
                        }else {
                            adminDynamicScreenViewModel.toggleButtonState(action, false)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(80.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, if (isClicked) Color.Yellow else White),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(if (isClicked) Color.Yellow else Color.Black)
                        .indication(
                            interactionSource,
                            rememberRipple(bounded = true, color = White)
                        )
                        .pointerInput(Unit) {
                            scope.launch(Dispatchers.IO) {
                                detectTapGestures(
                                    onTap = {
                                    },
                                    onPress = { offset: Offset ->
                                        val press = PressInteraction.Press(offset)
                                        interactionSource.emit(press)
                                        try {
                                            interactionSource.emit(press)
                                            if (buttonStates[action] == true) {

                                                mainViewModel.publishMessage(
                                                    "${navigationWithButtons.navigationItem!!.label}/${buttons.buttonlabel}",
                                                    "0"
                                                )
                                            } else {

                                                mainViewModel.publishMessage(
                                                    "${navigationWithButtons.navigationItem!!.label}/${buttons.buttonlabel}",
                                                    "1"
                                                )
                                            }
                                            interactionSource.emit(
                                                PressInteraction.Release(
                                                    press
                                                )
                                            )
                                        }
                                        catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        context.getString(R.string.exception_caught) + "${e.message}",
                                                        Toast.LENGTH_LONG
                                                    )
                                                    .show()
                                            }
                                        }
                                    },
                                    onLongPress = {
                                        selectedButton = buttons
                                        adminDynamicScreenViewModel.deleteDialogState(true)
                                    }
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ){

                    Text(
                        modifier = Modifier
                            .padding(4.dp),
                        text = buttons.buttonlabel,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color =  if (isClicked) Color.Black else White
                        )
                    )
                }
            }

            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Spacer(modifier = Modifier.height(1.dp))
            }


            items(
                items = navigationItemWithButtonsState
                    .value.navigationItemSliderButton,
                    key = {sliderButton -> sliderButton.action }
            ){ sliderButton ->

                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Each slider will use its own value from the data class
                    var sliderValue by remember { mutableFloatStateOf(100f) }

                    val topic = sliderButton.buttonlabel ?: return@items
                    val isClicked = buttonStates[topic] ?: false

                    LaunchedEffect(key1 = topic) {

                        //subscribe button
                        mainViewModel.subscribeTopic(
                            "${navigationWithButtons.navigationItem!!.label}/${topic}"
                        ){
                            if (it.equals("1")){
                                adminDynamicScreenViewModel.toggleButtonState(topic , true)
                            }else {
                                adminDynamicScreenViewModel.toggleButtonState(topic, false)
                            }
                        }
                        //subscribe slider
                        mainViewModel.subscribeMQTTSlider(
                            "${navigationWithButtons.navigationItem!!.label}/${topic}/opacity"
                        ){

                            sliderValue = it.toFloat()

                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Create a separate MutableInteractionSource for each button
                    val interactionSource = remember { MutableInteractionSource() }

                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(80.dp)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                BorderStroke(1.dp, if (isClicked) Color.Yellow else White),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(if (isClicked) Color.Yellow else Color.Black)
                            .indication(
                                interactionSource,
                                rememberRipple(bounded = false, color = White)
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        scope.launch(Dispatchers.IO) {
                                            try {

                                                if (buttonStates[topic] == true) {
                                                    mainViewModel.publishMessage(
                                                        "${navigationWithButtons.navigationItem!!.label}/${topic}",
                                                        "0"
                                                    )
                                                } else {
                                                    mainViewModel.publishMessage(
                                                        "${navigationWithButtons.navigationItem!!.label}/${topic}",
                                                        "1"
                                                    )
                                                }
                                            }
                                            catch (e: Exception) {
                                                withContext(adminDynamicScreenViewModel.mainDispatcher) {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            context.getString(R.string.exception_caught)
                                                                    + "${e.message}",
                                                            Toast.LENGTH_LONG
                                                        )
                                                        .show()
                                                }
                                            }
                                        }
                                    },
                                    onLongPress = {
                                        scope.launch {
                                            selectedSliderButton = sliderButton
                                            delay(800)
                                            adminDynamicScreenViewModel.sliderButtonDeleteDialog(true)
                                        }
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ){

                        Text(
                            modifier = Modifier,
                            text = sliderButton.buttonlabel,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color =  if (isClicked) Color.Black else White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ){

                            Text(
                                text =  "${sliderValue.toInt()}",
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light,
                                color =  White,
                                fontSize = 12.sp,
                            )
                            ColorSlider(
                                modifier = Modifier
                                    .width(250.dp),
                                value = sliderValue,
                                onValueChange = {

                                    sliderValue = it

                                    try {
                                        mainViewModel.publishMessage(
//                                            "$topic/opacity",
                                            "${navigationWithButtons.navigationItem!!.label}/${topic}/opacity",
                                            sliderValue.toInt().toString()
                                        )

                                    }catch (e:Exception){
                                        Log.d("TAG" , "exception ${e.message}")
                                    }
                                },
                                onValueFinished = {},
                                valueRange = 0.0f..100f,
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color.Black, White)
                                )
                            )
                        }
                    }
                }
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){

            if (navigationItemWithButtonsState.value.navigationItemSlider == null){
            }else
            {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(.3f)
                    .padding(8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {},
                            onLongPress = {
                                adminDynamicScreenViewModel.onEvent(
                                    ScreenEvent.onDeleteSliderEvent(
                                        navigationItemWithButtonsState.value.navigationItemSlider!!
                                    )
                                )
                            }
                        )
                    },
                    contentAlignment = Alignment.Center
                ){
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = navigationItemWithButtonsState.value.navigationItemSlider!!.sliderLabel,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = White,
                            fontSize = 12.sp,
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp)
                                .padding(4.dp),
                            value = animatedBrightness,
                            onValueChange = {
                                adminDynamicScreenViewModel.updateBrightness(it)

                                mainViewModel.publishMessage(
                                    "master/opacity",
                                    adminDynamicScreenViewModel.brightness.toInt().toString()
                                )
                                //send values from here
                                val alpha = it / 100f
                            },
                            onValueFinished = {

                            },
                            valueRange = 0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Black, White)
                            )
                        )

                        Text(
                            text =  "${adminDynamicScreenViewModel.brightness.toInt()}",
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            color = White,
                            fontSize = 12.sp,

                        )
                    }
                }
            }

            OutlinedButton(
                shape = RoundedCornerShape(29.dp),
                modifier = Modifier
                    .width(70.dp)
                    .padding(bottom = 28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = White
                ),
                border = BorderStroke(1.dp, White),
                onClick = {
                    openBottomSheet = !openBottomSheet
                }) {

                Icon(
                    modifier = Modifier.fillMaxWidth(),
                    imageVector = Icons.Filled.Add,
                    tint = White,
                    contentDescription = ""
                )
            }
        }
    }

    if (openBottomSheet){
        ModalBottomSheet(
            modifier = Modifier
                .height(340.dp)
                .width(400.dp),
            containerColor = Color.LightGray.copy(alpha = .5f),
            sheetState = modalSheetState,
            onDismissRequest = {
                openBottomSheet = false
            }) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text =  stringResource(id = R.string.please_select),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = White,
                    fontSize = 14.sp,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .width(400.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .width(150.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = White
                        ),
                        border = BorderStroke(1.dp, White),
                        onClick = {
                            scope
                                .launch {
                                    modalSheetState.hide()
                                }
                                .invokeOnCompletion {
                                    if (!modalSheetState.isVisible) {
                                        openBottomSheet = false
                                    }
                                    if(
                                        navigationItemWithButtonsState.value.navigationItemSlider == null
                                        ){
                                        adminDynamicScreenViewModel.sliderDialog(true)
                                    }else
                                    {
                                        Toast.makeText(context, context.getString(R.string.one_slider_one_page),
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }) {
                        Text(
                            text = stringResource(id = R.string.add_slider),
                            fontFamily = interFontFamily,
                            color = White,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    OutlinedButton(
                        modifier = Modifier
                            .width(150.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = White
                        ),
                        border = BorderStroke(1.dp, White),
                        onClick = {
                            scope
                                .launch {
                                    modalSheetState.hide()
                                }
                                .invokeOnCompletion {
                                    if (!modalSheetState.isVisible) {
                                        openBottomSheet = false
                                    }
                                    adminDynamicScreenViewModel.dialogState(true)
                                }
                        }) {
                        Text(text = stringResource(id = R.string.add_button),
                            fontFamily = interFontFamily,
                            color = White,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))


                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, White),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = White
                    ),
                    onClick = {
                        scope.launch {
                            modalSheetState.hide()
                        }.invokeOnCompletion {
                            if (!modalSheetState.isVisible) {
                                openBottomSheet = false
                            }
                            adminDynamicScreenViewModel.sliderButtonDialog(true)
                        }
                    })
                {
                    Text(
                        text = stringResource(id = R.string.add_slider_button),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        scope.launch {
                            modalSheetState.hide()
                        }.invokeOnCompletion {
                            if (!modalSheetState.isVisible) {
                                openBottomSheet = false
                            }
                        }
                    })
                {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,

                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }


    if (adminDynamicScreenViewModel.showShutDowndialog){
        ShutDownDialog(
            onShutDown = {

                //send shutDown message
                mainViewModel.publishMessage(
                    "PC/shutdown",
                    "1"
                )
                adminDynamicScreenViewModel.resetAllButtonStates()
                adminDynamicScreenViewModel.showShutDowndialog = false
            },
            onDismiss = {
                adminDynamicScreenViewModel.showShutDowndialog = false
            }
        )
    }

    if (adminDynamicScreenViewModel.deleteSliderButtonDialog.value){
        DeleteSliderButtonDialog({
            adminDynamicScreenViewModel.sliderButtonDeleteDialog(false)
            adminDynamicScreenViewModel.onEvent(
                ScreenEvent.onDeleteSliderButtonEvent(
                    selectedSliderButton!!
                )
            )
        })
    }

    if (adminDynamicScreenViewModel.showSliderButtonDialog.value){
        SliderButtonDialog(
            onSaveButton = {
                adminDynamicScreenViewModel.sliderButtonDialog(false)
                adminDynamicScreenViewModel.onEvent(
                    ScreenEvent.onAddSliderButtonEvent(
                    navigationWithButtons.navigationItem!!.navigationItemId
                ))
            }
        )
    }

    if (adminDynamicScreenViewModel.showDeleteDialog.value){
        DeleteDialog({
            Toast
                .makeText(
                    context,
                    selectedButton!!.buttonlabel,
                    Toast.LENGTH_LONG
                )
                .show()
            adminDynamicScreenViewModel.onEvent(ScreenEvent.onDeleteEvent(selectedButton!!))
            adminDynamicScreenViewModel.deleteDialogState(false)
        })
    }

    if (adminDynamicScreenViewModel.showDialog.value){
        AddButtonDialog({
            if(adminDynamicScreenViewModel.buttonText.value.isEmpty()){
                Toast.makeText(context, context.getString(R.string.correct_button_name),Toast.LENGTH_LONG).show()

            }else{

                adminDynamicScreenViewModel.onEvent(
                    ScreenEvent.onAddEvent(
                        navigationWithButtons.navigationItem!!.navigationItemId
                    ))
            }
            adminDynamicScreenViewModel.deleteDialogState(false)
        })
    }

    //slider dialog
    if (adminDynamicScreenViewModel.showSliderDialog.value){
        SliderDialog(
            onSaveButton = {
                if(adminDynamicScreenViewModel.sliderText.value.isEmpty()){
                    Toast.makeText(context,
                        context.getString(R.string.correct_slider_name),
                        Toast.LENGTH_LONG)
                        .show()
                }
                else{
                    adminDynamicScreenViewModel.onEvent(
                        ScreenEvent.onAddSliderEvent(navigationWithButtons.navigationItem!!.navigationItemId)
                    )
                    Toast.makeText(context,
                        adminDynamicScreenViewModel.sliderText.value,
                        Toast.LENGTH_SHORT)
                        .show()
                    adminDynamicScreenViewModel.sliderText.value = ""
                }
            }
        )
    }
}
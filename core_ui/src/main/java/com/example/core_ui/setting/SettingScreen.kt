package com.example.core_ui.setting

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
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
import com.example.core_ui.component.ShutDownDialog
import com.example.core_ui.component.SliderType
import com.example.core_ui.dynamicscreen.component.LanguageChangeHelper
import com.example.core_ui.dynamicscreen.component.SliderBtnValueDialog
import com.example.core_ui.main.component.ColorSlider
import com.example.core_ui.main.presentation.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun SettingScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    settingViewModel: SettingViewModel = hiltViewModel(),

) {

    val context = LocalContext.current

    val languageChangeHelper by lazy {
        LanguageChangeHelper()
    }

    val listOfLanguages = listOf(
        com.example.core_ui.Language("de", "Deutsch", R.drawable.germany),
        com.example.core_ui.Language("en", "English", R.drawable.usa)
    )

    val currentLanguageCode: String = languageChangeHelper.getLanguageCode(context)

    var currentLanguage by remember { mutableStateOf(currentLanguageCode) }

    val onCurrentLanguageChange: (String) -> Unit = { newLanguage ->
        currentLanguage = newLanguage
        languageChangeHelper.changeLanguage(context, newLanguage)
    }

    val borderColorLanugage by remember { mutableStateOf(White) }

    var borderColor by remember { mutableStateOf(White) }
    var borderColorAllOff by remember { mutableStateOf(White) }

    //subscribe all slider values
    LaunchedEffect(Unit) {

        mainViewModel.subscribeTopic(
            "SP3/4469/Settings/Turgriff"
        ){
            settingViewModel.turgriffSlider = it.toFloat()
        }

        mainViewModel.subscribeTopic(
            "SP3/4469/Settings/Blinker"
        ){
            settingViewModel.blinkerSlider = it.toFloat()
        }

        mainViewModel.subscribeTopic(
            "SP3/4469/Settings/Lichtleiste"
        ){
            settingViewModel.lichtleisteSlider = it.toFloat()
        }

        mainViewModel.subscribeTopic(
            "SP3/4469/Settings/Panel"
        ){
            settingViewModel.panelSlider = it.toFloat()
        }

        mainViewModel.subscribeTopic(
            "SP3/4469/Settings/Ambiente"
        ){
            settingViewModel.ambientSlider = it.toFloat()
        }

        mainViewModel.subscribeTopic(
            "SP3/4469/Settings/Kartentasche"
        ){
            settingViewModel.kartentascheSlider = it.toFloat()
        }

        mainViewModel.subscribeTopic(
            "SP3/4469/Settings/Lightbar"
        ){
            settingViewModel.lightbarSlider = it.toFloat()
        }
        mainViewModel.subscribeTopic(
            "SP3/4469/Settings/StopLight"
        ){
            settingViewModel.stoplightSlider = it.toFloat()
        }

        mainViewModel.subscribeTopic(
            "Sp3/4469/Settings/TuroffnerOut"
        )
        {
            settingViewModel.turoffnerOutSlider = it.toFloat()
        }

        mainViewModel.subscribeTopic(
            "Sp3/4469/Settings/TuroffnerIn")
        {
            settingViewModel.turoffnerInSlider = it.toFloat()
        }
        mainViewModel.subscribeTopic(
            "SP3/4469/Settings/Master"
        ){
            settingViewModel.masterSlider = it.toFloat()
        }
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
            contentDescription =""
        )
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
                .weight(.1f),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Tür Demonstrator",
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Medium,
                style = TextStyle(
                    fontSize = 35.sp,
                    color = Color.Yellow
                )
            )

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {
                com.example.core_ui.LanguagesDropdown(
                    modifier = Modifier
                        .width(130.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, borderColorLanugage),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Black),
                    languagesList = listOfLanguages,
                    currentLanguage,
                    onCurrentLanguageChange
                )

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, borderColorAllOff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Black)
                        .pointerInput(Unit) {

                            detectTapGestures(
                                onTap = {
                                },
                                onPress = {
                                    borderColorAllOff = Color.Yellow

//                                    dynamicScreenViewModel.resetAllButtonStates()

                                    try {

//                                        dynamicScreenViewModel.resetAllButtonStates()

                                        //send mqtt msg
                                        mainViewModel.publishMessage(
                                            "allOff",
                                            "1"
                                        )

                                        tryAwaitRelease()
                                        borderColorAllOff = White

                                    } catch (e: Exception) {

                                        withContext(settingViewModel.mainDispatcher) {
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
                            fontSize = 12.sp,
                            color = White
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
                        .background(Black)
                        .pointerInput(Unit) {

                            detectTapGestures(
                                onTap = {
                                },
                                onPress = {
                                    borderColor = Color.Yellow
                                    try {
                                        //now show  dialogue box
                                        settingViewModel.showShutDowndialog = true

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
                            .padding(6.dp)
                            .align(Alignment.Center),
                        text = stringResource(id = R.string.shut_down),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = White
                        )
                    )
                }
            }
        }

        //sliders
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .horizontalScroll(rememberScrollState())
                .weight(.9f)
                .padding(top = 64.dp , end = 64.dp , start = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            //slider 1st row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //	Türgriff slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Türgriff",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.Yellow
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .clickable {
                                    settingViewModel.selectSlider(SliderType.TURGRIFF)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.turgriffSlider.toInt().toString(),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Yellow
                            )
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp),
                            value = settingViewModel.turgriffSlider,
                            onValueChange = {
                                settingViewModel.turgriffSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/Turgriff",
                                    settingViewModel.turgriffSlider.toString()
                                )
                            },
                            onValueFinished = {
                                //send mqtt msg

                            },
                            valueRange = 0.0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Black, White)
                            )
                        )
                    }
                }
                //	Blinker slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Blinker",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.Yellow
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .clickable {
                                    settingViewModel.selectSlider(SliderType.BLINKER)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.blinkerSlider.toInt().toString(),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Yellow
                            )
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp),
                            value = settingViewModel.blinkerSlider,
                            onValueChange = {
                                settingViewModel.blinkerSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/Blinker",
                                    settingViewModel.blinkerSlider.toString()
                                )
                            },
                            onValueFinished = {},
                            valueRange = 0.0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Black, White)
                            )
                        )
                    }
                }
            }

            //slider 2nd row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //	Lichtleiste slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Lichtleiste",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.Yellow
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .clickable {
                                    settingViewModel.selectSlider(SliderType.LICHTLEISTE)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.lichtleisteSlider.toInt().toString(),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Yellow
                            )
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp),
                            value = settingViewModel.lichtleisteSlider,
                            onValueChange = {
                                settingViewModel.lichtleisteSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/Lichtleiste",
                                    settingViewModel.lichtleisteSlider.toString()
                                )
                            },
                            onValueFinished = {},
                            valueRange = 0.0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Black, White)
                            )
                        )
                    }
                }
                //		Panel slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Panel",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.Yellow
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .clickable {
                                    settingViewModel.selectSlider(SliderType.PANEL)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.panelSlider.toInt().toString(),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Yellow
                            )
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp),
                            value = settingViewModel.panelSlider,
                            onValueChange = {
                                settingViewModel.panelSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/Panel",
                                    settingViewModel.panelSlider.toString()
                                )
                            },
                            onValueFinished = {},
                            valueRange = 0.0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Black, White)
                            )
                        )
                    }
                }
            }

            //slider 3rd row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //		Ambiente Türspiegel slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ambiente Türspiegel",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.Yellow
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .clickable {
                                    settingViewModel.selectSlider(SliderType.AMBIENT)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.ambientSlider.toInt().toString(),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Yellow
                            )
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp),
                            value =  settingViewModel.ambientSlider,
                            onValueChange = {

                                settingViewModel.ambientSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/Ambiente",
                                    settingViewModel.ambientSlider.toString()
                                )
                            },
                            onValueFinished = {},
                            valueRange = 0.0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Black, White)
                            )
                        )
                    }
                }
                //Kartentasche slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Kartentasche",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.Yellow
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .clickable {
                                    settingViewModel.selectSlider(SliderType.KARTENTASCHE)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.kartentascheSlider.toInt().toString(),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Yellow
                            )
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp),
                            value = settingViewModel.kartentascheSlider,
                            onValueChange = {

                                settingViewModel.kartentascheSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/Kartentasche",
                                    settingViewModel.kartentascheSlider.toString()
                                )
                            },
                            onValueFinished = {},
                            valueRange = 0.0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Black, White)
                            )
                        )
                    }
                }
            }

            //slider 4th row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),

            ) {
                //	Lightbar Armlehne slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Lightbar Armlehne",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.Yellow
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .clickable {
                                    settingViewModel.selectSlider(SliderType.LIGHTBAR)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.lightbarSlider.toInt().toString(),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Yellow
                            )
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp),
                            value = settingViewModel.lightbarSlider,
                            onValueChange = {
                                settingViewModel.lightbarSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/Lightbar",
                                    settingViewModel.lightbarSlider.toString()
                                )
                            },
                            onValueFinished = {},
                            valueRange = 0.0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Black, White)
                            )
                        )
                    }
                }
                //		StopLight slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "StopLight",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.Yellow
                        )

                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .clickable {
                                    settingViewModel.selectSlider(SliderType.STOPLIGHT)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.stoplightSlider.toInt().toString(),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Yellow
                            )
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp),
                            value = settingViewModel.stoplightSlider,
                            onValueChange = {

                                settingViewModel.stoplightSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/StopLight",
                                    settingViewModel.stoplightSlider.toString()
                                )
                            },
                            onValueFinished = {},
                            valueRange = 0.0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Black, White)
                            )
                        )
                    }
                }
            }

            //slider 5th row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                ) {
                //		MASTER Slider slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Master",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.Yellow
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier
                                .clickable {
                                    settingViewModel.selectSlider(SliderType.MASTER)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.masterSlider.toInt().toString(),
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Light,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Yellow
                            )
                        )

                        ColorSlider(
                            modifier = Modifier
                                .width(250.dp),
                            value = settingViewModel.masterSlider,
                            onValueChange = {

                                settingViewModel.masterSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/Master",
                                    settingViewModel.masterSlider.toString()
                                )
                            },
                            onValueFinished = {},
                            valueRange = 0.0f..100f,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Black, White)
                            )
                        )
                    }
                }
            }
        }
    }

    if (settingViewModel.showSliderBtnValueDialog.value){

        SliderBtnValueDialog(
            onUpdate = {
                settingViewModel.setSelectedSliderValue(settingViewModel.updatedSliderBtnValue.toFloat())

                when(settingViewModel.selectedSlider){
                    SliderType.TURGRIFF ->{
                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/Turgriff",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }

                    SliderType.BLINKER -> {

                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/Blinker",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }
                    SliderType.LICHTLEISTE -> {

                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/Lichtleiste",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }
                    SliderType.PANEL -> {
                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/Panel",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }
                    SliderType.AMBIENT -> {
                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/Ambiente",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }
                    SliderType.KARTENTASCHE -> {

                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/Kartentasche",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }
                    SliderType.LIGHTBAR -> {

                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/Lightbar",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }
                    SliderType.STOPLIGHT -> {

                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/StopLight",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }
                    SliderType.MASTER -> {
                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/Master",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }
                    null -> Unit
                }
            }
        )
    }

    if (settingViewModel.showShutDowndialog){
        ShutDownDialog(
            onShutDown = {

                //send shutDown message
                mainViewModel.publishMessage(
                    "PC/shutdown",
                    "1"
                )

//                settingViewModel.resetAllButtonStates()


                settingViewModel.showShutDowndialog = false
            },
            onDismiss = {
                settingViewModel.showShutDowndialog = false
            }
        )
    }
}
package com.example.core_ui

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily
import com.example.core_ui.component.DragGestureAnimation
import com.example.core_ui.component.ShutDownDialog
import com.example.core_ui.dynamicscreen.component.CalloutMenu
import com.example.core_ui.dynamicscreen.component.CustomReorderableItem
import com.example.core_ui.dynamicscreen.component.DeleteDialog
import com.example.core_ui.dynamicscreen.component.LanguageChangeHelper
import com.example.core_ui.dynamicscreen.component.SliderBtnValueDialog

import com.example.core_ui.main.component.ColorSlider
import com.example.core_ui.main.presentation.MainViewModel
import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.data.model.SliderButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.derivedStateOf
import com.example.core_ui.component.SliderType
import com.example.core_ui.setting.SettingViewModel
import com.example.database.data.model.ActionButtons


/*
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DynamicScreenTest(
    navigationWithButtons: NavigationItemWithActionButtons,
    introShowcaseState: IntroShowcaseState,
    showCaseState: Boolean,
    onComplete:(Boolean) -> Unit,
    modifier: Modifier,
    dynamicScreenViewModel: DynamicScreenViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
){

    var showAppIntro by remember {
        mutableStateOf(showCaseState)
    }



    val navigationItemWithButtonsState = dynamicScreenViewModel.navigationItemWithButtons.collectAsState()

//    val appName = dynamicScreenViewModel.appName.value

    val context = LocalContext.current

    var selectedSliderButton by remember { mutableStateOf<SliderButton?>(null) }

    // Declare borderColor here within the @Composable function
    var borderColor by remember { mutableStateOf(White) }
    var borderColorAllOff by remember { mutableStateOf(White) }

    val borderColorLanugage by remember { mutableStateOf(White) }

    val scope = rememberCoroutineScope()

    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }

    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    val animatedBrightness by animateFloatAsState(
        targetValue = dynamicScreenViewModel.brightness,
        animationSpec = tween(
            durationMillis = 500,
            easing = EaseOut
        ),
        label = "animatedBrightnessRed"
    )

    val sliderValues = remember {
        mutableStateMapOf<String, Float>()
    }

    BackHandler(modalSheetState.isVisible) {
        scope.launch { modalSheetState.hide() }
    }

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

    val buttonStates by dynamicScreenViewModel.buttonStates.collectAsState()

    val gridState = rememberLazyGridState()

    var selectedButton by remember { mutableStateOf<ActionButtons?>(null) }


    val scale = remember {
        Animatable(1f)
    }

    val reorderState = rememberCustomReorderableGridState (
        key1 = navigationWithButtons.navigationItem!!.navigationItemId
    )
    { from, to ->

        dynamicScreenViewModel.moveSliderButton(
            from,
            to
        )
    }

    LaunchedEffect(navigationWithButtons.navigationItem!!.navigationItemId) {
        dynamicScreenViewModel.setNavigationItem(navigationWithButtons)


        reorderState.itemPositions.clear()
        reorderState.endDrag()
    }

    LaunchedEffect(navigationItemWithButtonsState.value.navigationItemSliderButton) {
        reorderState.setOrderedKeys(
            navigationItemWithButtonsState.value.navigationItemSliderButton.map { it.action }
        )
    }

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    val density = LocalDensity.current

    IntroShowcase(
        showIntroShowCase = showAppIntro,
        dismissOnClickOutside = false,
        onShowCaseCompleted = {
            showAppIntro = false
            onComplete(false)
        },
        state = introShowcaseState,
    ){

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
                    .weight(.5f),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Inova",
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
                    LanguagesDropdown(
                        modifier = Modifier
                            .width(130.dp)
                            .height(36.dp)
                            .introShowCaseTarget(
                                index = 1,
                                style = ShowcaseStyle.Default.copy(
                                    backgroundColor = Color(0xFF1C0A00),
                                    backgroundAlpha = 0.98f,
                                    targetCircleColor = White
                                ),
                                content = {
                                    Column {

                                        Text(
                                            text = stringResource(id = R.string.language),
                                            color = White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = interFontFamily
                                        )
                                        Text(
                                            text = stringResource(id = R.string.language_text),
                                            color = White,
                                            fontSize = 14.sp,
                                            fontFamily = interFontFamily
                                        )
                                    }
                                }
                            )
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
                            .introShowCaseTarget(
                                index = 2,
                                style = ShowcaseStyle.Default.copy(
                                    backgroundColor = Color(0xFF1C0A00),
                                    backgroundAlpha = 0.98f,
                                    targetCircleColor = White
                                ),
                                content = {
                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.turn_off),
                                            color = White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = interFontFamily
                                        )
                                        Text(
                                            text = stringResource(id = R.string.tap_to_disable),
                                            color = White,
                                            fontSize = 14.sp,
                                            fontFamily = interFontFamily
                                        )
                                    }
                                }
                            )
                            .pointerInput(Unit) {

                                detectTapGestures(
                                    onTap = {
                                    },
                                    onPress = {
                                        borderColorAllOff = Color.Yellow

                                        dynamicScreenViewModel.resetAllButtonStates()

                                        try {

                                            dynamicScreenViewModel.resetAllButtonStates()

                                            //send mqtt msg
                                            mainViewModel.publishMessage(
                                                "allOff",
                                                "1"
                                            )

                                            tryAwaitRelease()
                                            borderColorAllOff = White

                                        } catch (e: Exception) {

                                            withContext(dynamicScreenViewModel.mainDispatcher) {
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
                            .introShowCaseTarget(
                                index = 3,
                                style = ShowcaseStyle.Default.copy(
                                    backgroundColor = Color(0xFF1C0A00),
                                    backgroundAlpha = 0.98f,
                                    targetCircleColor = White
                                ),
                                content = {

                                    Column {

                                        Text(
                                            text = stringResource(id = R.string.Model_shut_down),
                                            color = White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = interFontFamily
                                        )
                                        Text(
                                            text = stringResource(id = R.string.model_shut_down_msg),
                                            color = White,
                                            fontSize = 14.sp,
                                            fontFamily = interFontFamily
                                        )
                                    }
                                }
                            )
                            .pointerInput(Unit) {

                                detectTapGestures(
                                    onTap = {
                                    },
                                    onPress = {
                                        borderColor = Color.Yellow
                                        try {
                                            //now show  dialogue box
                                            dynamicScreenViewModel.showShutDowndialog = true

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

            if(navigationWithButtons.navigationItem!!.navigationItemId.toInt() !== 2){

                LazyVerticalGrid (
                    state = gridState,
                    modifier = modifier
                        .fillMaxSize()
                        .weight(2f)
                        .padding(horizontal = 100.dp),
                    columns =  GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(60.dp),
                ) {

                    items(
                        items = navigationItemWithButtonsState
                            .value.navigationItemActionButtons,
                        key = { button -> button.buttonlabel }
                    ){ button ->

                        CustomReorderableItem(
                            modifier = Modifier
                                .width(200.dp)
                                .height(150.dp),
                            state = reorderState,
                            key = button.buttonlabel
                        ) { isDragging ->

                            // Create a separate MutableInteractionSource for each button
                            val interactionSource = remember { MutableInteractionSource() }

                            val elevation by animateDpAsState(
                                targetValue = if (isDragging) 12.dp else 0.dp,
                                label = "elevation"
                            )

                            val backgroundColor by animateColorAsState(
                                targetValue =
                                if (isDragging) Color.DarkGray
//                            else if (isClicked) Color.Yellow
                                else Black,
                                label = "background"
                            )

                            val action = button.buttonlabel
                            val isClicked = buttonStates[action] ?: false

                            LaunchedEffect(key1 = action) {

                                mainViewModel.subscribeTopic(
                                    "${navigationWithButtons.navigationItem!!.label}/${action}"
                                ){

                                    if (it.equals("1")){
                                        dynamicScreenViewModel.toggleButtonState(action , true)
                                    }else {
                                        dynamicScreenViewModel.toggleButtonState(action, false)
                                    }
                                }
                            }

                            Surface(
                                modifier = Modifier
                                    .height(150.dp)
                                    .animateContentSize(
                                        animationSpec = tween(durationMillis = 200)
                                    )
                                    .animateItemPlacement()
                                    .introShowCaseTarget(
                                        index = 4,
                                        style = ShowcaseStyle(
                                            backgroundColor = Color(0xFF1C0A00),
                                            backgroundAlpha = 0.98f,
                                            targetCircleColor = White
                                        ),
                                        content = {
                                            // Renders the live animation track directly inside the showcase window
                                            DragGestureAnimation(interFontFamily = interFontFamily)
                                        }
                                    ),
                                shadowElevation = elevation,
                                shape = RoundedCornerShape(24.dp),
                                color = Color.Transparent
                            ){

                                Column(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(150.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    // Create a separate MutableInteractionSource for each button
                                    val interactionSource = remember { MutableInteractionSource() }

                                    var expanded by remember { mutableStateOf(false) }

                                    var anchorBounds by remember { mutableStateOf(Rect.Zero) }

                                    Box{

                                        Column{
                                            // Replaces the old DropdownMenu
                                            if(expanded){

                                                CalloutMenu(
                                                    text = button.buttonlabel,
                                                    expanded = expanded,
                                                    onDismissRequest = { expanded = false },
                                                    items = listOf("Edit Name", "Delete"),
                                                    onItemClick = {selected ->
                                                        when (selected){
                                                            "Edit Name" -> {
                                                                selectedButton = button
//                                                            dynamicScreenViewModel.UpdateSliderBtnState(true)
                                                            }
                                                            "Delete" -> {
                                                                selectedButton = button
                                                                dynamicScreenViewModel.deleteDialogState(true)
                                                            }
                                                        }
                                                    }
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(34.dp))

                                            Box(
                                                modifier = Modifier
                                                    .width(200.dp)
                                                    .height(80.dp)
                                                    .padding(16.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .clickable {
                                                        //Start
                                                        scope.launch(dynamicScreenViewModel.ioDispatcher)
                                                        {
                                                            try {

//                                                                Log.d(
//                                                                    "TAG",
//                                                                    "buttons clicked  =  ${button.buttonlabel}"
//                                                                )

//                                                                val v =
//                                                                    sliderValues[button.action]
//                                                                Log.d("TAG", " slider = $v")

                                                                //mqtt msg logic came here

                                                            }
                                                            catch (e: Exception) {
                                                                withContext(dynamicScreenViewModel.mainDispatcher) {
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
                                                        //End
                                                    }
                                                    .border(
                                                        BorderStroke(
                                                            1.dp,
                                                            if (isClicked) Color.Yellow else White
                                                        ),
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    .onGloballyPositioned { coordinates ->
                                                        anchorBounds = coordinates.boundsInWindow()
                                                    }
                                                    .onSizeChanged {
                                                        itemHeight =
                                                            with(density) { it.height.toDp() }
                                                    }
                                                    .background(backgroundColor)
                                                    .indication(
                                                        interactionSource,
                                                        rememberRipple(
                                                            bounded = false,
                                                            color = White
                                                        )
                                                    )
                                                    .pointerInput(button) {
                                                        var moved = false
                                                        detectDragGesturesAfterLongPress(
                                                            onDragStart = {
                                                                moved = false
//                                                                Log.d("TAG", "onDragStart")
                                                                reorderState.startDrag(button.action)
                                                            },
                                                            onDrag = { change, dragAmount ->
                                                                change.consume()
                                                                if (dragAmount.getDistance() > 5f) moved =
                                                                    true
                                                                reorderState.onDrag(dragAmount)
//                                                                Log.d("TAG", "onDrag")
                                                            },
                                                            onDragEnd = {
                                                                reorderState.endDrag()

                                                                if (!moved) {
                                                                    expanded = true
                                                                } else {
//                                                                dynamicScreenViewModel.saveSliderOrder()
                                                                }
                                                            },
                                                            onDragCancel = {
                                                                reorderState.endDrag()
                                                            }
                                                        )
                                                    },
                                                contentAlignment = Alignment.Center
                                            ){

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(backgroundColor),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceAround
                                                ) {

                                                    if (button.isFavorite)
                                                    {
                                                        Icon(
                                                            modifier = Modifier
                                                                .size(25.dp)
                                                                .graphicsLayer {
                                                                    scaleX = scale.value
                                                                    scaleY = scale.value
                                                                }
                                                                .clickable {

                                                                    //remove from favourite
//                                                                dynamicScreenViewModel.deleteFavouriteSliderBtn(
//                                                                    button
//                                                                )
                                                                },
                                                            imageVector = Icons.Filled.Favorite,
                                                            contentDescription = "",
                                                            tint = Color.Red
                                                        )
                                                    }
                                                    else
                                                    {
                                                        Icon(
                                                            modifier = Modifier
                                                                .size(25.dp)
                                                                .graphicsLayer {
                                                                    scaleX = scale.value
                                                                    scaleY = scale.value
                                                                }
                                                                .clickable {

//                                                                dynamicScreenViewModel.addFavouriteSliderBtn(
//                                                                    button
//                                                                )
                                                                },
                                                            imageVector = Icons.Outlined.Favorite,
                                                            contentDescription = "",
                                                            tint = White
                                                        )
                                                    }

                                                    Text(
                                                        modifier = Modifier,
                                                        text = button.buttonlabel,
                                                        fontFamily = interFontFamily,
                                                        fontWeight = FontWeight.Light,
                                                        style = TextStyle(
                                                            fontSize = 12.sp,
                                                            color =  if (isClicked) Black else White
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }

                        /*
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
                                .introShowCaseTarget(
                                    index = 1,
                                    style = ShowcaseStyle.Default.copy(
                                        backgroundColor = Color(0xFF1C0A00),
                                        backgroundAlpha = 0.98f,
                                        targetCircleColor = White
                                    ),
                                    content = {
                                        Column {

                                            Text(
                                                text = stringResource(id = R.string.onclick_onlong),
                                                color = White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = interFontFamily
                                            )

                                            Text(
                                                text = stringResource(id = R.string.OSC_message),
                                                fontFamily = interFontFamily,
                                                fontWeight = FontWeight.Light,
                                                color = White,
                                                fontSize = 18.sp,
                                            )

                                            Text(
                                                text = stringResource(id = R.string.long_press),
                                                fontFamily = interFontFamily,
                                                fontWeight = FontWeight.Light
                                            )
                                        }
                                    }
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {

                                        },
                                        onPress = { offset: Offset ->

                                            val press = PressInteraction.Press(offset)

                                            scope.launch(dynamicScreenViewModel.ioDispatcher) {
                                                try {
                                                    interactionSource.emit(press)
                                                    if (buttonStates[action] == true) {

                                                        mainViewModel.publishMessage(
                                                            "${navigationWithButtons.navigationItem!!.label}/${button.buttonlabel}",
                                                            "0"
                                                        )

                                                    } else {

                                                        mainViewModel.publishMessage(
                                                            "${navigationWithButtons.navigationItem!!.label}/${button.buttonlabel}",
                                                            "1"
                                                        )
                                                        Log.d("TAG"," btn = ${navigationWithButtons.navigationItem!!.label}/${button.buttonlabel}")

                                                    }

                                                    interactionSource.emit(
                                                        PressInteraction.Release(
                                                            press
                                                        )
                                                    )
                                                } catch (e: Exception) {
                                                    withContext(dynamicScreenViewModel.mainDispatcher) {
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
                                            selectedButton = button
                                            if (button.isAdmin) {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        context.getString(R.string.cannot_delete),
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                                selectedButton = null
                                            } else {
                                                scope.launch {
                                                    delay(800)
                                                    dynamicScreenViewModel.deleteDialogState(true)
                                                }
                                            }
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                modifier = Modifier
                                    .padding(2.dp),
                                textAlign = TextAlign.Center,
                                text = button.buttonlabel,
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color =  if (isClicked) Color.Black else White
                                )
                            )
                        }

                         */
                    }


                    //slider button and slider
                    items(
                        items = navigationItemWithButtonsState
                            .value
                            .navigationItemSliderButton,
                        key = { sliderButton -> sliderButton.action }
                    ){ sliderButton ->
                        CustomReorderableItem(
                            modifier = Modifier
                                .width(200.dp)
                                .height(150.dp),
                            state = reorderState,
                            key = sliderButton.action
                        ) { isDragging ->

                            val topic = sliderButton.buttonlabel

                            // Build MQTT topic
                            val mqttBaseTopic =
                                if (navigationWithButtons.navigationItem!!.label == "Favourite") {
//                                "SP3/4412/${sliderButton.action}"
                                    sliderButton.action
                                }
                                else {
                                    sliderButton.action
//                                "SP3/4412/${navigationWithButtons.navigationItem!!.label}/${topic}"
//                                "SP3/4412/${navigationWithButtons.navigationItem!!.label}"
                                }

                            val isClicked = buttonStates[topic] ?: false

                            val elevation by animateDpAsState(
                                targetValue = if (isDragging) 12.dp else 0.dp,
                                label = "elevation"
                            )

                            val backgroundColor by animateColorAsState(
                                targetValue =
                                if (isDragging) Color.DarkGray
                                else if (isClicked) Color.Yellow
                                else Black,
                                label = "background"
                            )

                            val sliderValue =
                                sliderValues[sliderButton.action] ?: 100f

                            val animatedSlider by animateFloatAsState(
                                targetValue = sliderValue,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = EaseOut
                                ),
                                label = "animatedSlider"
                            )

                            LaunchedEffect(key1 = topic) {

                                //subscribe to warm white BFS topics
                                mainViewModel.subscribeTopic(
                                    "SP3/4412/Maintenance/BFS_4000"
                                ){
                                    if (it.equals("1")){
                                        dynamicScreenViewModel.toggleButtonState("BFS" ,true)
                                    }
                                    if(it.equals("0")){
                                        dynamicScreenViewModel.toggleButtonState("BFS"  ,  false)
                                    }
                                }

                                mainViewModel.subscribeMQTTSlider(
                                    "SP3/4412/Maintenance/BFS_4000/opacity",
                                ){
//                                    Log.d("BFS" , "$it")
                                }

                                //subscribe to warm white FS topics
                                mainViewModel.subscribeTopic(
                                    "SP3/4412/Maintenance/FS_4000"
                                ){
                                    if (it.equals("1")){
                                        dynamicScreenViewModel.toggleButtonState("FS" ,true)
                                    }
                                    if(it.equals("0")){
                                        dynamicScreenViewModel.toggleButtonState("FS"  ,  false)
                                    }
                                }

                                //subscribe to warm white Logo topics
                                mainViewModel.subscribeTopic(
                                    "SP3/4412/Maintenance/Logo_4000"
                                ){
                                    if (it.equals("1")){
                                        dynamicScreenViewModel.toggleButtonState("Logo" ,true)
                                    }
                                    if(it.equals("0")){
                                        dynamicScreenViewModel.toggleButtonState("Logo"  ,  false)
                                    }
                                }

                                //subscribe button
                                mainViewModel.subscribeTopic(
//                                "SP3/4412/${navigationWithButtons.navigationItem!!.label}/${topic}"
                                    mqttBaseTopic
                                ){

                                    if (it.equals("1")){
                                        dynamicScreenViewModel.toggleButtonState(topic ,true)
                                    }
                                    if(it.equals("0")){
                                        dynamicScreenViewModel.toggleButtonState(topic  ,  false)
                                    }
                                }

                                //subscribe slider
                                mainViewModel.subscribeMQTTSlider(
//                                "SP3/4412/${navigationWithButtons.navigationItem!!.label}/${topic}/opacity"
                                    mqttBaseTopic+"/opacity"
                                ){
                                    Log.d("TAG", "topic  =  $ mqttBaseTopic and  value received = $it" )

                                    sliderValues[sliderButton.action] = it.toFloat()
                                }
                            }

                            Surface(
                                modifier = Modifier
                                    .height(150.dp)
                                    .animateContentSize(
                                        animationSpec = tween(durationMillis = 200)
                                    )
                                    .animateItemPlacement()
                                    .introShowCaseTarget(
                                        index = 4,
                                        style = ShowcaseStyle(
                                            backgroundColor = Color(0xFF1C0A00),
                                            backgroundAlpha = 0.98f,
                                            targetCircleColor = White
                                        ),
                                        content = {
                                            // Renders the live animation track directly inside the showcase window
                                            DragGestureAnimation(interFontFamily = interFontFamily)
                                        }
                                    ),
                                shadowElevation = elevation,
                                shape = RoundedCornerShape(24.dp),
                                color = Color.Transparent
                            )
                            {
                                Column(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(150.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    // Create a separate MutableInteractionSource for each button
                                    val interactionSource = remember { MutableInteractionSource() }

                                    var expanded by remember { mutableStateOf(false) }

                                    var anchorBounds by remember { mutableStateOf(Rect.Zero) }

                                    Box{

                                        Column{
                                            // Replaces the old DropdownMenu
                                            if(expanded){

                                                CalloutMenu(
                                                    text = sliderButton.buttonlabel,
                                                    expanded = expanded,
                                                    onDismissRequest = { expanded = false },
                                                    items = listOf("Edit Name", "Delete"),
                                                    onItemClick = {selected ->
                                                        when (selected){
                                                            "Edit Name" -> {
                                                                selectedSliderButton = sliderButton
                                                                dynamicScreenViewModel.UpdateSliderBtnState(true)
                                                            }
                                                            "Delete" -> {
                                                                selectedSliderButton = sliderButton
                                                                dynamicScreenViewModel.deleteDialogState(true)
                                                            }
                                                        }
                                                    }
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(34.dp))

                                            Box(
                                                modifier = Modifier
                                                    .width(200.dp)
                                                    .height(80.dp)
                                                    .padding(16.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .clickable {
                                                        //Start
                                                        scope.launch(dynamicScreenViewModel.ioDispatcher)
                                                        {
                                                            try {

                                                                Log.d(
                                                                    "TAG",
                                                                    "buttons clicked  =  ${sliderButton.buttonlabel}"
                                                                )

                                                                val v =
                                                                    sliderValues[sliderButton.action]
                                                                Log.d("TAG", " slider = $v")

                                                                if (buttonStates[topic] == true) {

                                                                    if (navigationWithButtons.navigationItem!!.navigationItemId.toInt() !== 2) {
                                                                        mainViewModel.publishMessage(
//                                                                    "SP3/4412/${navigationWithButtons.navigationItem!!.label}/${topic}",
                                                                            mqttBaseTopic,
                                                                            "0"
                                                                        )
                                                                    }

                                                                    if (dynamicScreenViewModel.warmWhiteBtn) {

                                                                        //turn off warm color Logo
                                                                        if (sliderButton.action.contains(
                                                                                "Logo"
                                                                            )
                                                                        ) {
                                                                            mainViewModel.publishMessage(
                                                                                "SP3/4412/Maintenance/Logo_4000",
                                                                                "0"
                                                                            )
                                                                        }

                                                                        if (sliderButton.buttonlabel.equals(
                                                                                "BFS"
                                                                            )
                                                                        ) {

                                                                            mainViewModel.publishMessage(
                                                                                "SP3/4412/Maintenance/BFS_4000",
                                                                                "0"
                                                                            )
                                                                        }

                                                                        if (sliderButton.buttonlabel.equals(
                                                                                "FS"
                                                                            )
                                                                        ) {

                                                                            Log.d("", "")
                                                                            mainViewModel.publishMessage(
                                                                                "SP3/4412/Maintenance/FS_4000",
                                                                                "0"
                                                                            )
                                                                        }
                                                                    } else
                                                                    {
                                                                        mainViewModel.publishMessage(
                                                                            mqttBaseTopic,
                                                                            "0"
                                                                        )
                                                                    }

                                                                }
                                                                else {

                                                                    //check  maintenance  is selected
                                                                    if (navigationWithButtons.navigationItem!!.navigationItemId.toInt() == 2) {

                                                                        //check warm white is selected
                                                                        if (dynamicScreenViewModel.warmWhiteBtn) {
                                                                            if (sliderButton.buttonlabel.equals(
                                                                                    "Logo"
                                                                                )
                                                                            ) {

                                                                                //turn off cold white color of Logo
                                                                                mainViewModel.publishMessage(
                                                                                    "SP3/4412/Maintenance/Logo",
                                                                                    "0"
                                                                                )

                                                                                //Now publish warm white mqtt msg from here.
                                                                                mainViewModel.publishMessage(
                                                                                    "SP3/4412/Maintenance/Logo_4000",
                                                                                    "1"
                                                                                )

                                                                                dynamicScreenViewModel.toggleButtonState(
                                                                                    sliderButton.buttonlabel,
                                                                                    true
                                                                                )
                                                                            } else
                                                                                if (sliderButton.buttonlabel.equals(
                                                                                        "BFS"
                                                                                    )
                                                                                ) {

                                                                                    //turn off cold white color of BFS
                                                                                    mainViewModel.publishMessage(
                                                                                        "SP3/4412/Maintenance/BFS",
                                                                                        "0"
                                                                                    )

                                                                                    //Now publish warm white mqtt msg from here.
                                                                                    mainViewModel.publishMessage(
                                                                                        "SP3/4412/Maintenance/BFS_4000",
                                                                                        "1"
                                                                                    )

                                                                                    dynamicScreenViewModel.toggleButtonState(
                                                                                        "BFS",
                                                                                        true
                                                                                    )

                                                                                } else if (sliderButton.buttonlabel.equals(
                                                                                        "FS"
                                                                                    )
                                                                                ) {

                                                                                    mainViewModel.publishMessage(
                                                                                        "SP3/4412/Maintenance/FS",
                                                                                        "0"
                                                                                    )

                                                                                    //Now publish warm white mqtt msg from here.
                                                                                    mainViewModel.publishMessage(
                                                                                        "SP3/4412/Maintenance/FS_4000",
                                                                                        "1"
                                                                                    )
                                                                                    dynamicScreenViewModel.toggleButtonState(
                                                                                        topic,
                                                                                        true
                                                                                    )

                                                                                }
                                                                        }
                                                                        //check cold white is selected
                                                                        if (dynamicScreenViewModel.coldWhiteBtn) {

                                                                            mainViewModel.publishMessage(
                                                                                mqttBaseTopic,
                                                                                "1"
                                                                            )
                                                                        }
                                                                    } else {
                                                                        mainViewModel.publishMessage(
                                                                            mqttBaseTopic,
                                                                            "1"
                                                                        )

                                                                        //check it slider values is null or zero
                                                                        val v =
                                                                            sliderValues[sliderButton.action]
                                                                        if (v == null || v <= 0) {

                                                                            //publish now slider value from here

                                                                            mainViewModel.publishMessage(
                                                                                mqttBaseTopic + "/opacity",
                                                                                100f.toString()
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            catch (e: Exception) {
                                                                withContext(dynamicScreenViewModel.mainDispatcher) {
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
                                                        //End
                                                    }
                                                    .border(
                                                        BorderStroke(
                                                            1.dp,
                                                            if (isClicked) Color.Yellow else White
                                                        ),
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    .onGloballyPositioned { coordinates ->
                                                        anchorBounds = coordinates.boundsInWindow()
                                                    }
                                                    .onSizeChanged {
                                                        itemHeight =
                                                            with(density) { it.height.toDp() }
                                                    }
                                                    .background(backgroundColor)
                                                    .indication(
                                                        interactionSource,
                                                        rememberRipple(
                                                            bounded = false,
                                                            color = White
                                                        )
                                                    )
                                                    .pointerInput(sliderButton) {
                                                        var moved = false
                                                        detectDragGesturesAfterLongPress(
                                                            onDragStart = {
                                                                moved = false
//                                                                Log.d("TAG", "onDragStart")
                                                                reorderState.startDrag(sliderButton.action)
                                                            },
                                                            onDrag = { change, dragAmount ->
                                                                change.consume()
                                                                if (dragAmount.getDistance() > 5f) moved =
                                                                    true
                                                                reorderState.onDrag(dragAmount)
//                                                                Log.d("TAG", "onDrag")
                                                            },
                                                            onDragEnd = {
                                                                reorderState.endDrag()

                                                                if (!moved) {
                                                                    expanded = true
                                                                } else {
//                                                                    dynamicScreenViewModel.saveSliderOrder()
                                                                }
                                                            },
                                                            onDragCancel = {
                                                                reorderState.endDrag()
                                                            }
                                                        )
                                                    },
                                                contentAlignment = Alignment.Center
                                            ){

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(backgroundColor),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceAround
                                                ) {

                                                    if (sliderButton.isFavorite)
                                                    {
                                                        Icon(
                                                            modifier = Modifier
                                                                .size(25.dp)
                                                                .graphicsLayer {
                                                                    scaleX = scale.value
                                                                    scaleY = scale.value
                                                                }
                                                                .clickable {

                                                                    //remove from favourite
//                                                                    dynamicScreenViewModel.deleteFavouriteSliderBtn(
//                                                                        sliderButton
//                                                                    )
                                                                },
                                                            imageVector = Icons.Filled.Favorite,
                                                            contentDescription = "",
                                                            tint = Color.Red
                                                        )
                                                    }
                                                    else
                                                    {
                                                        Icon(
                                                            modifier = Modifier
                                                                .size(25.dp)
                                                                .graphicsLayer {
                                                                    scaleX = scale.value
                                                                    scaleY = scale.value
                                                                }
                                                                .clickable {

//                                                                    dynamicScreenViewModel.addFavouriteSliderBtn(
//                                                                        sliderButton
//                                                                    )
                                                                },
                                                            imageVector = Icons.Outlined.Favorite,
                                                            contentDescription = "",
                                                            tint = White
                                                        )
                                                    }

                                                    Text(
                                                        modifier = Modifier,
                                                        text = sliderButton.buttonlabel,
                                                        fontFamily = interFontFamily,
                                                        fontWeight = FontWeight.Light,
                                                        style = TextStyle(
                                                            fontSize = 12.sp,
                                                            color =  if (isClicked) Black else White
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    //slider and slider text
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Row (
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ){
                                            Text(
                                                modifier = Modifier
                                                    .padding(4.dp)
                                                    .clickable {
                                                        //open input dialog for slider value.
                                                        selectedSliderButton = sliderButton

                                                        dynamicScreenViewModel.sliderBtnValueDialogState(
                                                            true
                                                        )
                                                    },
                                                text =  "${sliderValue.toInt()}",
                                                fontFamily = interFontFamily,
                                                fontWeight = FontWeight.Light,
                                                color =  White,
                                                style = TextStyle(
                                                    fontSize = 12.sp,
                                                )
                                            )
                                            ColorSlider(
                                                modifier = Modifier
                                                    .width(250.dp),
                                                value = animatedSlider,
                                                onValueChange = {

                                                    scope.launch {

                                                        try {

                                                            //now check maintenance page is selected

                                                            if (navigationWithButtons.navigationItem!!.navigationItemId.toInt() == 2)
                                                            {
                                                                if (dynamicScreenViewModel.warmWhiteBtn == true){

                                                                    //now check which button is selected
                                                                    if (sliderButton.buttonlabel.equals("BFS"))
                                                                    {
                                                                        //Now publish warm white mqtt msg from here.
                                                                        mainViewModel.publishMessage(
                                                                            "SP3/4412/Maintenance/BFS_4000/opacity",
                                                                            it.toInt().toString()
                                                                        )
                                                                    }
                                                                    if (sliderButton.buttonlabel.equals("FS")){
                                                                        mainViewModel.publishMessage(
                                                                            "SP3/4412/Maintenance/FS_4000/opacity",
                                                                            it.toInt().toString()
                                                                        )
                                                                    }
                                                                    if (sliderButton.buttonlabel.equals("Logo")){
                                                                        Log.d("topic","logo 400 opacity")
                                                                        mainViewModel.publishMessage(
                                                                            "SP3/4412/Maintenance/Logo_4000/opacity",
                                                                            it.toInt().toString()
                                                                        )
                                                                    }
                                                                }
                                                            }


                                                            mainViewModel.publishMessage(
                                                                mqttBaseTopic+"/opacity",
                                                                it.toInt().toString()
                                                            )
                                                            //send mqtt msg here
                                                        }catch (e:Exception){
                                                            Log.d("TAG" , "exception ${e.message}")
                                                        }
                                                    }
                                                },
                                                onValueFinished = {
                                                },
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
                }
                else
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(.3f)
                            .padding(8.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {},
                                    onLongPress = {
                                        if (navigationItemWithButtonsState.value.navigationItemSlider!!.isAdmin) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    context.getString(R.string.msg),
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        } else {
                                            //show delete slider dialog
                                            dynamicScreenViewModel.deleteSliderDialogState(true)
                                        }
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
                                text = navigationItemWithButtonsState
                                    .value.navigationItemSlider!!.sliderLabel,
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light,
                                color = White,
                                fontSize = 5.sp,
                            )

                            ColorSlider(
                                modifier = Modifier
                                    .width(250.dp)
                                    .padding(4.dp),
                                value = animatedBrightness,
                                onValueChange = {
                                    dynamicScreenViewModel.updateBrightness(it)
                                    mainViewModel.publishMessage(
                                        "master/opacity",
                                        dynamicScreenViewModel.brightness.toInt().toString()
                                    )
                                },
                                onValueFinished = {
                                },
                                valueRange = 0f..100f,
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Black, White)
                                )
                            )

                            Text(
                                text = "${dynamicScreenViewModel.brightness.toInt()}",
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light,
                                color = White,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
        }
    }

    if (dynamicScreenViewModel.showDeleteDialog.value)
    {
        DeleteDialog(
            onDeleteButton = {
                dynamicScreenViewModel.deleteDialogState(false)
//                dynamicScreenViewModel.deleteSliderButton(selectedSliderButton!!)
            }
        )
    }
    if (dynamicScreenViewModel.showShutDowndialog){
        ShutDownDialog(
            onShutDown = {

                //send shutDown message
                mainViewModel.publishMessage(
                    "PC/shutdown",
                    "1"
                )

                dynamicScreenViewModel.resetAllButtonStates()

                dynamicScreenViewModel.showShutDowndialog = false
            },
            onDismiss = {
                dynamicScreenViewModel.showShutDowndialog = false
            }
        )
    }

    //update slider btn name dialog
    if (dynamicScreenViewModel.showUpdateSliderBtnDialog.value){
        selectedSliderButton?.let {
            UpdateSliderBtnDialog(
                it
            )
        }
    }

    if (dynamicScreenViewModel.showSliderBtnValueDialog.value){

        SliderBtnValueDialog(
            onUpdate = {
                selectedSliderButton?.let { button ->

                    val value = dynamicScreenViewModel.updatedSliderBtnValue.toFloat()

                    sliderValues[button.action] = value

                    scope.launch {
                        try {
                            mainViewModel.publishMessage(
                                "SP3/4412/${button.action}/opacity",
                                value.toInt().toString()
                            )
                        } catch (e: Exception) {
                            Log.e("TAG", "MQTT publish failed", e)
                        }
                    }
                }
            }
        )
    }
}

 */

@Composable
fun SettingScreen1(
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
            "Sp3/4469/Settings/Projektor")
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
                .height(100.dp),
//                .weight(.1f),
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
                .height(470.dp)
                .verticalScroll(rememberScrollState())
                .padding(top = 64.dp, end = 64.dp, start = 32.dp),
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
                            color = Color.White
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
                            color = Color.White
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
            Spacer(modifier = Modifier.height(24.dp))
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
                            color = Color.White
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
                            color = Color.White
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

            Spacer(modifier = Modifier.height(36.dp))

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
                            color = Color.White
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
                            color = Color.White
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

            Spacer(modifier = Modifier.height(36.dp))

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
                            color = Color.White
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
                            color = Color.White
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

            Spacer(modifier = Modifier.height(36.dp))

            //slider 5th row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                ) {
                //	TurOffnerIn slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TurOffnerIn",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.White
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
                                    settingViewModel.selectSlider(SliderType.TurOffnerIn)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.turoffnerInSlider.toInt().toString(),
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
                            value = settingViewModel.turoffnerInSlider,
                            onValueChange = {
                                settingViewModel.turoffnerInSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/TuroffnerIn",
                                    settingViewModel.turoffnerInSlider.toString()
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
                //		TurOffnerOut slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TurOffnerOut",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color.White
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
                                    settingViewModel.selectSlider(SliderType.TurOffnerOut)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.turoffnerOutSlider.toInt().toString(),
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
                            value = settingViewModel.turoffnerOutSlider,
                            onValueChange = {

                                settingViewModel.turoffnerOutSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/TuroffnerOut",
                                    settingViewModel.turoffnerOutSlider.toString()
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

            Spacer(modifier = Modifier.height(36.dp))

            //slider 6th row master slider
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                
            ) {
                //		Projector Slider slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Projektor",
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = White
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
                                    settingViewModel.selectSlider(SliderType.Projektor)
                                    settingViewModel.sliderBtnValueDialogState(true)
                                },
                            text = settingViewModel.projectSlider.toInt().toString(),
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
                            value = settingViewModel.projectSlider,
                            onValueChange = {

                                settingViewModel.projectSlider = it

                                //send mqt msg
                                mainViewModel.publishMessage(
                                    "SP3/4469/Settings/Projektor",
                                    settingViewModel.projectSlider.toString()
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
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                ) {

                }

            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        //slider 8th row master slider
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            // MASTER Slider slider
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Master",
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = Color.White
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

                    SliderType.TurOffnerIn -> {

                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/TuroffnerIn",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }

                    SliderType.TurOffnerOut -> {

                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/TuroffnerOut",
                            settingViewModel.updatedSliderBtnValue.toString()
                        )
                    }
                    SliderType.Projektor -> {

                        //send mqt msg
                        mainViewModel.publishMessage(
                            "SP3/4469/Settings/Projektor",
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

@Composable
fun LanguagesDropdown(
    modifier: Modifier = Modifier,
    languagesList: List<Language>,
    currentLanguage: String,
    onCurrentLanguageChange: (String) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(languagesList.first { it.code == currentLanguage }) }

    Box(
        modifier = modifier
            .height(22.dp)
            .width(100.dp)
            .background(color = Black)
            .padding(end = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .height(22.dp)
                .width(100.dp)
                .clickable {
                    expanded = !expanded
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageListItem(selectedItem)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(color = colorResource(id = R.color.black))
        ) {
            repeat(languagesList.size) {
                val item = languagesList[it]
                DropdownMenuItem(
                    text = {
                        LanguageListItem(selectedItem = item)
                    }, onClick = {
                        selectedItem = item
                        expanded = !expanded
                        onCurrentLanguageChange(selectedItem.code)
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageListItem(selectedItem: Language) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape),
            painter = painterResource(selectedItem.flag),
            contentScale = ContentScale.Crop,
            contentDescription = selectedItem.code
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = selectedItem.name,
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            style = TextStyle(
                color = White
            )
        )
    }
}

data class Language(
    val code: String,
    val name: String,
    @DrawableRes val flag: Int
)


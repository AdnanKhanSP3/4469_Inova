package com.example.core_ui.animation

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.IntroShowcaseState
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily
import com.example.core_ui.component.DragGestureAnimation
import com.example.core_ui.component.ShutDownDialog
import com.example.core_ui.dynamicscreen.component.CalloutMenu
import com.example.core_ui.dynamicscreen.component.CustomReorderableItem
import com.example.core_ui.dynamicscreen.component.LanguageChangeHelper
import com.example.core_ui.dynamicscreen.component.SliderBtnValueDialog
import com.example.core_ui.dynamicscreen.component.rememberCustomReorderableGridState
import com.example.core_ui.dynamicscreen.presentation.DynamicScreenViewModel
import com.example.core_ui.main.presentation.MainViewModel
import com.example.database.data.model.ActionButtons
import com.example.database.data.model.NavigationItemWithActionButtons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AnimationScreen(
    navigationWithButtons: NavigationItemWithActionButtons,
    introShowcaseState: IntroShowcaseState,
    showCaseState: Boolean,
    onComplete:(Boolean) -> Unit,
    modifier: Modifier,
    dynamicScreenViewModel: DynamicScreenViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    animationViewModel: AnimationViewModel = hiltViewModel()
) {


    var showAppIntro by remember {
        mutableStateOf(showCaseState)
    }

    val navigationItemWithButtonsState = animationViewModel.navigationItemWithButtons.collectAsState()
//    val buttonStates by dynamicScreenViewModel.buttonStates.collectAsState()

    val gridState = rememberLazyGridState()

    var selectedButton by remember { mutableStateOf<ActionButtons?>(null) }

    val buttonStates by animationViewModel.buttonStates.collectAsState()

    val context = LocalContext.current

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

    BackHandler(modalSheetState.isVisible) {
        scope.launch { modalSheetState.hide() }
    }

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

    val scale = remember {
        Animatable(1f)
    }

    var selectedSliderButton by remember { mutableStateOf<ActionButtons?>(null) }


    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    val density = LocalDensity.current

    val reorderState = rememberCustomReorderableGridState (
        key1 = navigationWithButtons.navigationItem!!.navigationItemId
    )
    { from, to ->

        Log.d("TAG", "move callback: from=$from, to=$to")

        animationViewModel.moveActionButton(
            from,
            to
        )
    }

    LaunchedEffect( navigationItemWithButtonsState.value.navigationItemActionButtons) {
        reorderState.setOrderedKeys(
            navigationItemWithButtonsState.value.navigationItemActionButtons.map { it.buttonlabel }
        )
    }

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
        ){

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.5f),
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
                ){
                    com.example.core_ui.LanguagesDropdown(
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
                                            animationViewModel.showShutDowndialog = true

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


            LazyVerticalGrid (
                state = gridState,
                modifier = modifier
                    .fillMaxSize()
                    .weight(2f)
                    .padding(horizontal = 100.dp),
                columns =  GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(60.dp),
            ){

                items(
                    items = navigationItemWithButtonsState
                        .value.navigationItemActionButtons,
                    key = { button -> button.buttonlabel }
                ){ button ->

                    CustomReorderableItem(
                        modifier = Modifier
                            .width(150.dp)
                            .height(120.dp),
                        state = reorderState,
                        key = button.buttonlabel
                    ) { isDragging ->

                        // Create a separate MutableInteractionSource for each button
                        val interactionSource = remember { MutableInteractionSource() }

                        val topic = button.buttonlabel
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

                        val action = button.buttonlabel

                        LaunchedEffect(key1 = action) {

                            mainViewModel.subscribeTopic(
                                "SP3/4469/${navigationWithButtons.navigationItem!!.label}/${action}"
                            ){
                                Log.d("MQTT","$topic = ${"SP3/4469/${navigationWithButtons.navigationItem!!.label}/${action}"}")
                                if (it.equals("1")){
                                    animationViewModel.toggleButtonState(action , true)
                                }else {
                                    animationViewModel.toggleButtonState(action, false)
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
                                        }

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

                                                            if (buttonStates[action] == true) {

                                                                mainViewModel.publishMessage(
                                                                    "SP3/4469/${navigationWithButtons.navigationItem!!.label}/${topic}",
                                                                        "0"
                                                                )

                                                            }

                                                            else {
                                                                mainViewModel.publishMessage(
                                                                    "SP3/4469/${navigationWithButtons.navigationItem!!.label}/${topic}",
                                                                    "1"
                                                                )

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
                                                .pointerInput(button) {
                                                    var moved = false
                                                    detectDragGesturesAfterLongPress(
                                                        onDragStart = {
                                                            moved = false
                                                            reorderState.startDrag(button.buttonlabel)
                                                        },
                                                        onDrag = { change, dragAmount ->
                                                            change.consume()
                                                            if (dragAmount.getDistance() > 5f) moved =
                                                                true
                                                            reorderState.onDrag(dragAmount)
                                                        },
                                                        onDragEnd = {
                                                            reorderState.endDrag()

                                                            if (!moved) {
                                                                expanded = true
                                                            } else {
                                                                animationViewModel.saveActionButtonOrder()
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

                                                /*
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
                                                                animationViewModel.deleteFavouriteActionBtn(
                                                                    button
                                                                )
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

                                                                animationViewModel.addFavouriteActionBtn(
                                                                    button
                                                                )
                                                            },
                                                        imageVector = Icons.Outlined.Favorite,
                                                        contentDescription = "",
                                                        tint = White
                                                    )
                                                }

                                                 */

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
                            }
                        }
                    }
                }
            }
        }
    }

    //shut down dialog
    if (animationViewModel.showShutDowndialog){
        ShutDownDialog(
            onShutDown = {

                //send shutDown message
                mainViewModel.publishMessage(
                    "PC/shutdown",
                    "1"
                )

                animationViewModel.resetAllButtonStates()

                animationViewModel.showShutDowndialog = false
            },
            onDismiss = {
                animationViewModel.showShutDowndialog = false
            }
        )
    }
}
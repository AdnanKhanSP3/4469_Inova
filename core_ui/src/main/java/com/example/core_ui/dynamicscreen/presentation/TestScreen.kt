package com.example.core_ui.dynamicscreen.presentation

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.IntroShowcaseState
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily
import com.example.core_ui.component.ShutDownDialog
import com.example.core_ui.dynamicscreen.component.CustomReorderableItem
import com.example.core_ui.dynamicscreen.component.LanguageChangeHelper
import com.example.core_ui.dynamicscreen.component.SliderBtnValueDialog
import com.example.core_ui.dynamicscreen.component.UpdateSliderBtnDialog
import com.example.core_ui.dynamicscreen.component.rememberCustomReorderableGridState
import com.example.core_ui.main.component.ColorSlider
import com.example.core_ui.main.presentation.MainViewModel
import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.data.model.SliderButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState


@Composable
fun CustomReorderableGridScreen(
    navigationWithButtons: NavigationItemWithActionButtons,
    introShowcaseState: IntroShowcaseState,
    showCaseState: Boolean,
    onComplete:(Boolean) -> Unit,
    modifier: Modifier,
    dynamicScreenViewModel: DynamicScreenViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    var items by remember { mutableStateOf(listOf("text", "AK", "ZK", "DB" , "Testing", "OOP", "Java" , "C++", " Kotlin")) }

    val reorderState = rememberCustomReorderableGridState(
        null
    ) { from, to ->
        items = items.toMutableList().apply {
            add(to, removeAt(from))
        }
    }

    LaunchedEffect(items) {
        reorderState.setOrderedKeys(items)
    }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
    ) {
        items(items, key = { it }) { label ->

            var backgroundColor by remember { mutableStateOf(Color.Transparent) }
            var showDropdown by remember { mutableStateOf(false) }

            CustomReorderableItem(
                state = reorderState,
                key = label,
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp)
                    .padding(16.dp)
            ) { isDragging ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (isDragging) Color.Cyan else backgroundColor)
                        .pointerInput(Unit){
                            detectTapGestures (
                                onTap = {
                                    Toast.makeText(context, "Tapped $label", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                            .pointerInput(label) {
                            var moved = false
                            detectDragGesturesAfterLongPress(
                                onDragStart = {
                                    moved = false
                                    reorderState.startDrag(label)
                                    backgroundColor = Color.Cyan
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    if (dragAmount.getDistance() > 5f) moved = true
                                    reorderState.onDrag(dragAmount)
                                },
                                onDragEnd = {
                                    reorderState.endDrag()
                                    backgroundColor = Color.Transparent
                                    if (!moved) {
                                        showDropdown = true
                                    }
                                },
                                onDragCancel = {
                                    reorderState.endDrag()
                                    backgroundColor = Color.Transparent
                                }
                            )
                        }
                ) {
                    Text(
                        text = label,
                        color = Color.Yellow,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )


                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        DropdownMenuItem(text = { Text("Option 1") }, onClick = { showDropdown = false })
                        DropdownMenuItem(text = { Text("Option 2") }, onClick = { showDropdown = false })
                    }
                }
            }
        }
    }
}

/*
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DynamicScreen1(
    navigationWithButtons: NavigationItemWithActionButtons,
    introShowcaseState: IntroShowcaseState,
    showCaseState: Boolean,
    onComplete:(Boolean) -> Unit,
    modifier: Modifier,
    dynamicScreenViewModel: DynamicScreenViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
){

    var backgroundColor by remember { mutableStateOf(Color.Transparent) }

    var showDropdown by remember { mutableStateOf(false) }
    var dragStarted by remember { mutableStateOf(false) }


    val context = LocalContext.current

    val gridState = rememberLazyGridState()

    val scope = rememberCoroutineScope()

    var longPressJob by remember { mutableStateOf<Job?>(null) }


    val reorderableGridState =
        rememberReorderableLazyGridState(
            lazyGridState = gridState
        ) { from, to ->
            Log.d(
                "TAG",
                "from=${from.index} to=${to.index}"
            )
        }
    LazyVerticalGrid(
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        columns =  GridCells.Fixed(2),
    ){
        items(
            items = listOf(
                "text",
                "AK",
                "ZK",
                "DB",
            ),
            key = { it }
        ){label ->

            ReorderableItem(
                state = reorderableGridState,
                key = label
            ){


                Box(
                    modifier =  Modifier
                        .width(200.dp)
                        .height(80.dp)
                        .padding(16.dp)
                        .background(backgroundColor)
                        .longPressDraggableHandle (
                            onDragStarted = {
                                dragStarted = true
//                                showDropdown = false
                            },
                            onDragStopped = {
                                dragStarted = false
                            }
                        )
                        .pointerInput(Unit) {

                            detectTapGestures (
                                onLongPress = {
                                    // small delay lets longPressDraggableHandle's own
                                    // recognition win first if a drag is actually happening
                                    if (!dragStarted) {
                                        showDropdown = true
                                    }
                                }
                            )
                        }
                ){

                    Text(
                        text = label,
                        color = Color.Yellow
                    )

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Option 1") },
                            onClick = { showDropdown = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Option 2") },
                            onClick = { showDropdown = false }
                        )
                    }
                }

                /*
                That is first solution
                Box(
                    modifier =  Modifier
                        .width(200.dp)
                        .height(80.dp)
                        .padding(16.dp)
                        .background(backgroundColor)
                        .longPressDraggableHandle(
                             onDragStarted = {
                                 longPressJob?.cancel()
                                 longPressJob = null
                                 showDropdown = false
                             },
                            onDragStopped = {
                                backgroundColor = Color.Transparent
                            }
                        )
                        .pointerInput(Unit){

                            awaitEachGesture{
                                 awaitFirstDown(requireUnconsumed = false)

                                 longPressJob = scope.launch {
                                    delay(500L)
                                        showDropdown = true
                                 }

                                waitForUpOrCancellation()
                                longPressJob?.cancel()
                            }
                        }
                ){
                    Text(
                        text = label,
                        color = Color.Yellow
                    )

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Option 1") },
                            onClick = { showDropdown = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Option 2") },
                            onClick = { showDropdown = false }
                        )
                    }
                }

                 */
            }
        }
    }
}

 */
package com.example.core_ui.main.component


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.database.data.model.NavigationItem
import com.example.database.data.model.NavigationItemWithActionButtons
import sh.calvin.reorderable.rememberReorderableLazyListState
import sh.calvin.reorderable.ReorderableItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppNavigationRail(
    modifier: Modifier,
    onModeSwitch: () -> Unit,
    navigationItems: List<NavigationItemWithActionButtons>,
    selectItemClick: (NavigationItemWithActionButtons) -> Unit,
    selectedItem: NavigationItemWithActionButtons?,
    onLongPress: (NavigationItem) -> Unit,
//    onPageButtonClick: () -> Unit,
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit
) {

    val hapticFeedback = LocalHapticFeedback.current

    val listState = rememberLazyListState()

    val reorderableLazyListState = rememberReorderableLazyListState(
        lazyListState = listState,
        onMove = { from, to ->
            onMove(from.index, to.index)
        }
    )

    var selectedButton by remember { mutableStateOf<NavigationItem?>(null) }

    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp),
        containerColor = Color.Transparent
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ){
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
            ){

                //new code

                items(
                    items = navigationItems,
                    key = {
                        it.navigationItem!!.navigationItemId
                    }
                ) { item ->

                    ReorderableItem(
                        state = reorderableLazyListState,
                        key = item.navigationItem!!.navigationItemId
                    ) { isDragging ->

                        Box {

                            NavigationRailItem(
                                modifier = Modifier
                                    .draggableHandle(
                                    onDragStarted = {
                                        Log.d("TAG", "AppNavigationRail: drag started")
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                    },
                                    onDragStopped = {
                                        Log.d("TAG", "AppNavigationRail: drag stopped")
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )

                                        onDragEnd()
                                    }
                                )
                                ,
                                icon = Icons.Filled.Home,
                                text = item.navigationItem!!.label,
                                onClick = {
                                    selectItemClick(item)
                                },
                                navigationItem = item.navigationItem!!,
                                isSelected = item == selectedItem,
                                longPress = {
//                                    onLongPress(item.navigationItem!!)
//                                    selectedButton = item.navigationItem
                                }
                            )
                        }
                    }
                }
                //ends

                /*
                old code starts
                items(
                    items= navigationItems,
                    key = { item ->
                        item.navigationItem!!
                    }
                )
                { item ->
                    Box(modifier = Modifier
                    ){
                        NavigationRailItem(
                            modifier = Modifier,
                            icon = Icons.Filled.Home,
                            text = item.navigationItem!!.label,
                            onClick = {
                                selectItemClick(item)
                            },
                            navigationItem = item.navigationItem!!,
                            isSelected = item == selectedItem,
                            longPress = {
                                onLongPress(item.navigationItem!!)
                                selectedButton = item.navigationItem

                            }
                        )
                    }
                }

                 */
            }
        }
    }
}
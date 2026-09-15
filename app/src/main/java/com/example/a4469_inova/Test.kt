package com.example.a4469_inova

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

// ---------- State holder ----------

class CustomReorderableGridState(
    private val onMove: (from: Int, to: Int) -> Unit
) {
    val itemPositions = mutableStateMapOf<Any, IntRect>()

    var draggingKey by mutableStateOf<Any?>(null)
        private set

    var dragOffset by mutableStateOf(Offset.Zero)
        private set

    private var draggingStartBounds: IntRect? = null

    private var orderedKeys: List<Any> = emptyList()

    fun setOrderedKeys(keys: List<Any>) {
        orderedKeys = keys
    }

    fun onItemPositioned(key: Any, bounds: IntRect) {
        itemPositions[key] = bounds
    }

    fun startDrag(key: Any) {
        draggingKey = key
        dragOffset = Offset.Zero
        draggingStartBounds = itemPositions[key]
    }

    private fun IntRect.center() = Offset(
        x = left + width / 2f,
        y = top + height / 2f
    )

    fun onDrag(delta: Offset) {
        dragOffset += delta
        val key = draggingKey ?: return
        val startBounds = draggingStartBounds ?: return

        val draggedCenter = startBounds.center() + dragOffset

        val fromIndex = orderedKeys.indexOf(key)
        if (fromIndex == -1) return

        // find the closest *other* item to the dragged item's current center
        var bestKey: Any? = null
        var bestDistance = Float.MAX_VALUE

        for ((otherKey, bounds) in itemPositions) {
            if (otherKey == key) continue
            val d = (bounds.center() - draggedCenter).getDistanceSquared()
            if (d < bestDistance) {
                bestDistance = d
                bestKey = otherKey
            }
        }

        val targetKey = bestKey ?: return
        val targetBounds = itemPositions[targetKey] ?: return
        val targetIndex = orderedKeys.indexOf(targetKey)
        if (targetIndex == -1 || targetIndex == fromIndex) return

        // only swap once the dragged center has actually crossed into the
        // target's cell — avoids jitter/flicker right at cell boundaries
        val halfWidth = targetBounds.width / 2f
        val halfHeight = targetBounds.height / 2f
        val withinTargetCell =
            kotlin.math.abs(draggedCenter.x - targetBounds.center().x) < halfWidth &&
                    kotlin.math.abs(draggedCenter.y - targetBounds.center().y) < halfHeight

        if (withinTargetCell) {
            onMove(fromIndex, targetIndex)

            // re-anchor so the dragged item's visual position doesn't jump
            // (bounds for `key` won't reflect the new order until next layout pass,
            // so estimate using the target's old bounds as the new "start")
            draggingStartBounds = targetBounds
            dragOffset = draggedCenter - targetBounds.center()
        }
    }

    fun endDrag() {
        draggingKey = null
        dragOffset = Offset.Zero
        draggingStartBounds = null
    }
}

@Composable
fun rememberCustomReorderableGridState(
    onMove: (from: Int, to: Int) -> Unit
): CustomReorderableGridState {
    return remember { CustomReorderableGridState(onMove) }
}

@Composable
fun CustomReorderableItem(
    state: CustomReorderableGridState,
    key: Any,
    modifier: Modifier = Modifier,
    content: @Composable (isDragging: Boolean) -> Unit
) {
    val isDragging = state.draggingKey == key
    val offset = if (isDragging) state.dragOffset else Offset.Zero

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                val pos = coordinates.positionInParent()
                val size = coordinates.size
                state.onItemPositioned(
                    key,
                    IntRect(
                        left = pos.x.toInt(),
                        top = pos.y.toInt(),
                        right = pos.x.toInt() + size.width,
                        bottom = pos.y.toInt() + size.height
                    )
                )
            }
            .graphicsLayer {
                translationX = offset.x
                translationY = offset.y
            }
            .zIndex(if (isDragging) 1f else 0f)
    ) {
        content(isDragging)
    }
}

@Composable
fun CustomReorderableGridScreen() {

    val context = LocalContext.current

    var items by remember { mutableStateOf(listOf("text", "AK", "ZK", "DB" , "Testing", "OOP", "Java" , "C++", " Kotlin")) }

    val reorderState = rememberCustomReorderableGridState { from, to ->
        items = items.toMutableList().apply {
            add(to, removeAt(from))
        }
//        Log.d("TAG", "from=$from to=$to")
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


//                    CalloutMenu(
//                        expanded = showDropdown,
//                        onDismissRequest = { showDropdown = false },
//                        items = listOf("Option 1", "Option 2"),
//                        onItemClick = { selected ->
//                            Toast.makeText(context, "Selected $selected", Toast.LENGTH_SHORT).show()
//                        }
//                    )


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
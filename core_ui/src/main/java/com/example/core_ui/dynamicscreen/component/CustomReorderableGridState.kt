    package com.example.core_ui.dynamicscreen.component

    import android.util.Log
    import androidx.compose.foundation.layout.Box
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateMapOf
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.geometry.Offset
    import androidx.compose.ui.graphics.graphicsLayer
    import androidx.compose.ui.layout.onGloballyPositioned
    import androidx.compose.ui.layout.positionInParent
    import androidx.compose.ui.layout.positionInRoot
    import androidx.compose.ui.unit.IntRect
    import androidx.compose.ui.zIndex

    // ---------- State holder ----------

    class CustomReorderableGridState(
        private val onMove: (from: Int, to: Int) -> Unit
    )
    {
        val itemPositions = mutableStateMapOf<Any, IntRect>()

        var draggingKey by mutableStateOf<Any?>(null)
            private set

        var dragOffset by mutableStateOf(Offset.Zero)
            private set

        private var draggingStartBounds: IntRect? = null

        private var orderedKeys: List<Any> = emptyList()

        fun setOrderedKeys(keys: List<Any>) {
            if (draggingKey == null) {
                orderedKeys = keys.toList()
            }
//            orderedKeys = keys
        }

        fun onItemPositioned(key: Any, bounds: IntRect) {
            itemPositions[key] = bounds
        }

        fun startDrag(key: Any) {
            Log.d("key" , "$key")
            draggingKey = key
            dragOffset = Offset.Zero
            draggingStartBounds = itemPositions[key]
        }

        private fun IntRect.center() = Offset(
            x = left + width / 2f,
            y = top + height / 2f
        )

        fun onDrag(delta: Offset) {

            val key = draggingKey ?: return
            val startBounds = draggingStartBounds ?: return

            val draggedCenter = startBounds.center() + dragOffset

            val fromIndex = orderedKeys.indexOf(key)

            if (fromIndex == -1) {
                Log.w("key", "key=$key not found in orderedKeys=$orderedKeys — drag will not swap")
                return
            }
            // find the closest *other* item to the dragged item's current center
            var bestKey: Any? = null
            var bestDistance = Float.MAX_VALUE

            Log.d("key","bestDistance = $bestDistance")

            for ((otherKey, bounds) in itemPositions) {
                if (otherKey == key) continue
                val d = (bounds.center() - draggedCenter).getDistanceSquared()
//                Log.d("key","d= $d")
                if (d < bestDistance) {
                    Log.d("key","d < bestDistance   $d")
                    bestDistance = d
                    bestKey = otherKey
                    Log.d("key","bestKey = $bestKey")
                }
            }
            dragOffset += delta

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

//            if (withinTargetCell) {
//                onMove(fromIndex, targetIndex)

                // re-anchor so the dragged item's visual position doesn't jump
                // (bounds for `key` won't reflect the new order until next layout pass,
                // so estimate using the target's old bounds as the new "start")
//                draggingStartBounds = targetBounds
//                dragOffset = draggedCenter - targetBounds.center()
//            }
            if (withinTargetCell) {
                Log.d("key","withIntargetCell = $withinTargetCell")
                // Keep our local ordering in sync with the mutation we're about to trigger.
                // Without this, orderedKeys goes stale after the first swap and every
                // subsequent swap in the same gesture computes wrong from/to indices.
                val updatedKeys = orderedKeys.toMutableList()
                val movedKey = updatedKeys.removeAt(fromIndex)
                updatedKeys.add(targetIndex, movedKey)
                orderedKeys = updatedKeys

                onMove(fromIndex, targetIndex)

                // re-anchor so the dragged item's visual position doesn't jump
                draggingStartBounds = targetBounds
                dragOffset = draggedCenter - targetBounds.center()
            }
        }


        /*
        fun onDrag(delta: Offset) {
            dragOffset += delta

            val key = draggingKey ?: return
            val startBounds = draggingStartBounds ?: return

            val draggedCenter = startBounds.center() + dragOffset

            val fromIndex = orderedKeys.indexOf(key)
            if (fromIndex == -1) {
                Log.d("REORDER", "Dragged key not found: $key")
                return
            }

            var bestKey: Any? = null
            var bestDistance = Float.MAX_VALUE

            for ((otherKey, bounds) in itemPositions) {
                if (otherKey == key) continue

                val distance =
                    (bounds.center() - draggedCenter).getDistanceSquared()

                if (distance < bestDistance) {
                    bestDistance = distance
                    bestKey = otherKey
                }
            }

            val targetKey = bestKey ?: return
            val targetBounds = itemPositions[targetKey] ?: return

            val targetIndex = orderedKeys.indexOf(targetKey)

            if (targetIndex == -1 || targetIndex == fromIndex) {
                return
            }

            val targetCenter = targetBounds.center()

            val withinTargetCell =
                kotlin.math.abs(draggedCenter.x - targetCenter.x) <
                        targetBounds.width / 2f &&
                        kotlin.math.abs(draggedCenter.y - targetCenter.y) <
                        targetBounds.height / 2f

            Log.d(
                "REORDER",
                "onDrag key=$key from=$fromIndex " +
                        "target=$targetKey targetIndex=$targetIndex " +
                        "withinTarget=$withinTargetCell"
            )

            if (withinTargetCell) {
                val updatedKeys = orderedKeys.toMutableList()

                val movedKey = updatedKeys.removeAt(fromIndex)
                updatedKeys.add(targetIndex, movedKey)

                orderedKeys = updatedKeys

                onMove(fromIndex, targetIndex)

                draggingStartBounds = targetBounds
                dragOffset = draggedCenter - targetCenter

                Log.d(
                    "REORDER",
                    "MOVE key=$key from=$fromIndex to=$targetIndex " +
                            "keys=$orderedKeys"
                )
            }
        }
         */

        fun endDrag() {
            draggingKey = null
            dragOffset = Offset.Zero
            draggingStartBounds = null
        }
    }

    @Composable
    fun rememberCustomReorderableGridState(
        key1: Any?, // 👈 Add this key parameter
        onMove: (from: Int, to: Int) -> Unit
    ): CustomReorderableGridState {
    //    return remember { CustomReorderableGridState(onMove) }
        // Whenever key1 changes, remember will discard the old state object and build a fresh one
        return remember(key1) { CustomReorderableGridState(onMove) }
    }

    @Composable
    fun CustomReorderableItem(
        state: CustomReorderableGridState,
        key: Any,
        modifier: Modifier = Modifier,
        content: @Composable (isDragging: Boolean) -> Unit
    ) {

//        Log.d(
//            "RECOMPOSE",
//            "Item recomposed: $key"
//        )

        val isDragging = state.draggingKey == key
        val offset = if (isDragging) state.dragOffset else Offset.Zero

        Box(
            modifier = modifier
                .onGloballyPositioned { coordinates ->
    //                val pos = coordinates.positionInParent()
                    val pos = coordinates.positionInRoot() // ← positionInRoot, not positionInParent
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

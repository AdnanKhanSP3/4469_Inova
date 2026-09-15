package com.example.core_ui.main.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSlider(
    modifier: Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueFinished: () -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    brush: Brush
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(26.dp)
            .background(Color.Transparent)
            .testTag("AlphaSlider"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(10.dp)
                .padding(start = 18.dp, end = 18.dp)
                .align(Alignment.Center),
            onDraw = {
                drawRoundRect(
                    brush = brush,
                    size = size,
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
            }
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .align(Alignment.Center),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            onValueChangeFinished = {
                onValueFinished()
            },
            thumb = {
                CustomThumb()
            }
        )
    }
}

@Composable
fun CustomThumb() {
    Canvas(
        modifier = Modifier
            .size(60.dp)
            .padding(bottom = 1.7.dp),
        onDraw = {
            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2.5f,
                center = Offset(size.width / 2f, size.height / 2f),
                style = Stroke(
                    width = 4f,
                )
            )
        }
    )
}

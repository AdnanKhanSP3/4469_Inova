package com.example.onboarding.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.commonresources.ui.theme.interFontFamily

@Composable
fun TodoItem(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = checked,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Yellow,
                unselectedColor = Color.White
            ),
            onClick = {

            })

        Spacer(modifier = Modifier.width(8.dp))
        AnimatedStrikethroughText(
            text = text,
            isVisible = checked,
            strikethroughStyle = SpanStyle(
                color = Color.LightGray.copy(0.32f)
            )
        )
    }
}

@Composable
fun AnimatedStrikethroughText(
    text: String,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    animateOnHide: Boolean = true,
    spec: AnimationSpec<Int> = tween(text.length * 30, easing = FastOutLinearInEasing),
    strikethroughStyle: SpanStyle = SpanStyle(),
    textStyle: TextStyle = LocalTextStyle.current
){

    var textToDisplay by remember { mutableStateOf(AnnotatedString("")) }

    val length = remember { Animatable(initialValue = 0, typeConverter = Int.VectorConverter) }

    LaunchedEffect(length.value) {
        textToDisplay = text.buildStrikethrough(length.value, strikethroughStyle)
    }

    LaunchedEffect(isVisible) {
        when {
            isVisible -> length.animateTo(text.length, spec)
            !isVisible && animateOnHide -> length.animateTo(0, spec)
            else -> length.snapTo(0)
        }
    }

    LaunchedEffect(text) {
        when {
            isVisible && text.length == length.value -> {
                textToDisplay = text.buildStrikethrough(length.value, strikethroughStyle)
            }
            isVisible && text.length != length.value -> {
                length.snapTo(text.length)
            }
            else -> textToDisplay = AnnotatedString(text)
        }
    }

    Text(
        text = textToDisplay,
        modifier = modifier,
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Light,
        style = TextStyle(
            color = Color.White,
//            fontSize = textStyle.fontSize
            fontSize = 14.sp
        )
    )
}


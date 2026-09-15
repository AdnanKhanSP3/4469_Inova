package com.example.onboarding.component

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration


// Define an enum or other state type for clarity
enum class TaskState { NONE, WIFI, MQTT, DATABASE,DONE }


fun String.buildStrikethrough(length: Int, style: SpanStyle) = buildAnnotatedString {
    append(this@buildStrikethrough)
    val strikethroughStyle = style.copy(textDecoration = TextDecoration.LineThrough)
    addStyle(strikethroughStyle, 0, length)
}
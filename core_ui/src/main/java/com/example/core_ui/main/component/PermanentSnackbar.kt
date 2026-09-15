package com.example.core_ui.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.commonresources.R
import com.example.commonresources.ui.theme.interFontFamily


@Composable
fun PermanentSnackbar(
    modifier: Modifier,
    message: String,
    onRetryClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.snakbar_color))
            .height(60.dp)
            .padding(16.dp)
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Light,
                style = TextStyle(
                    color = colorResource(id = R.color.snakbar_msg_color),
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .weight(1f)
                    .zIndex(1f),
                )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = stringResource(id = R.string.retry),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .clickable { onRetryClick() }
                    .zIndex(1f),
            )
        }
    }
}
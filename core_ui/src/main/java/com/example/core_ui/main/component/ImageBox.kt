package com.example.core_ui.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.commonresources.R


@Composable
fun ImageBox() {

    Box(modifier = Modifier
        .fillMaxHeight()
        .width(300.dp)
        .background(Color.Black.copy(alpha = .9f)),
    ){

        Spacer(modifier = Modifier.height(100.dp))

        Image(
            modifier = Modifier
                .width(140.dp)
                .padding(top = 100.dp, start = 20.dp, end = 20.dp)
                .align(Alignment.TopEnd),
            painter = painterResource(id = R.drawable.round_ball),
            contentDescription = "")

        Image(
            modifier = Modifier
                .width(100.dp)
                .padding(bottom = 150.dp)
                .align(Alignment.BottomStart),
            painter = painterResource(id = R.drawable.round_ball),
            contentDescription = "")
    }

}
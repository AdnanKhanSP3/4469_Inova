package com.example.core_ui.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.commonresources.ui.theme.interFontFamily
import com.example.database.data.model.NavigationItem

@Composable
fun NavigationRailItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    navigationItem: NavigationItem,
    isSelected: Boolean,
    longPress : () -> Unit

) {

    val backgroundColor = if (isSelected) Color.Yellow else Color.Transparent
    val textColor = if (isSelected) Color.Black else Color.White

    Spacer(modifier = Modifier.height(2.dp))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable {
                onClick()
            }
    ) {
        Row(modifier = Modifier
            .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text ,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Light,
                style = TextStyle(
                    fontSize = 13.sp,
                    color = textColor
                )
            )
        }
    }
}

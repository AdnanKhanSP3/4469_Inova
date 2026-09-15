package com.example.core_ui.dynamicscreen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.commonresources.ui.theme.interFontFamily


@Composable
fun CalloutMenu(
    text : String?,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<String>,
    onItemClick: (String) -> Unit
) {
    if (!expanded) return

    Popup(
        offset = IntOffset(x = 24.dp.value.toInt(), y = -90.dp.value.toInt()),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier,
//                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF2B2B2B),
                border = BorderStroke(0.5.dp, Color(0xFF4A4A4A)),
                shadowElevation = 14.dp
            ) {
                Column(
                    modifier = Modifier
                        .width(200.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.Black),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = text!!,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    HorizontalDivider(
                        color = Color(0xFF4A4A4A),
                    )

                    //Row for all
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        //column for edit name
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .clickable {
                                    onItemClick("Edit Name")
                                    onDismissRequest()
                                },
                            verticalArrangement = Arrangement.spacedBy((-5).dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit",
                                tint = Color.White
                            )

                            Text(
                                text = "Edit",
                                fontSize = 8.sp,
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                                )
                        }

                        //column for delete
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .clickable {
                                    onItemClick("Delete")
                                    onDismissRequest()
                                },
                            verticalArrangement = Arrangement.spacedBy((-5).dp),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )

                            Text(
                                text = "Delete",
                                fontSize = 8.sp,
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                                )

                        }
                    }
                    /*
                    items.forEachIndexed { index, label ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onItemClick(label)
                                    onDismissRequest()
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = when (label) {
                                    "Edit Name" -> Icons.Outlined.Edit
                                    "Delete" -> Icons.Outlined.Delete
                                    else -> Icons.Outlined.Delete
                                },
                                contentDescription = label,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = label,
                                color = Color.White,
                                fontSize = 8.sp,
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Light
                            )

                        }
//                        Text(
//                            text = label,
//                            color = Color.White,
//                            fontSize = 12.sp,
//                            fontFamily = interFontFamily,
//                            fontWeight = FontWeight.Light,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    onItemClick(label)
//                                    onDismissRequest()
//                                }
//                                .padding(horizontal = 12.dp, vertical = 8.dp)
//                        )
//                        if (index != items.lastIndex) {
//                            Divider(color = Color(0xFF4A4A4A), thickness = 0.5.dp)
//                        }
                    }

                     */
                }
            }

            Canvas(
                modifier = Modifier
                    .size(
                        width = 16.dp,
                        height = 12.dp
                    )
            ) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    quadraticBezierTo(
                        size.width / 2f,
                        size.height * 1.15f,
                            size.width, 0f
                    )
                    close()
                }
                drawPath(path, color = Color.Black)
//                drawPath(path, color = Color(0xFF2B2B2B))
            }
        }
    }
}

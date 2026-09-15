package com.example.core_ui.admin.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.commonresources.ui.theme.interFontFamily
import com.example.commonresources.R
import com.example.core_ui.admin.presentation.component.AddItemDialog
import com.example.core_ui.admin.presentation.component.DeleteDialog
import com.example.core_ui.admin.presentation.component.DeleteItemDialog
import com.example.core_ui.admin.presentation.component.IpAddressDialog
import com.example.core_ui.admin.presentation.dynamicScreen.AdminDynamicScreen
import com.example.core_ui.main.component.AppNavigationRail
import com.example.core_ui.main.component.ContactDialog
import com.example.database.data.model.NavigationItem


@Composable
fun AdminMainScreen(
    viewModel: AdminMainScreenViewModel = hiltViewModel(),
    onModeSwitch: () -> Unit
) {

    val state = viewModel.navigationItemList.collectAsState()

    var selectedItemToDelete by remember {
        mutableStateOf<NavigationItem?>(null)
    }

//     Ensure selectedItem is set after the data is loaded
    LaunchedEffect(state.value) {
        viewModel.getNavigationItemsWithActionButtons()
            if (state.value.navigationItems.isNotEmpty() && viewModel.selectedItem == null) {
            viewModel.selectedItem.value = state.value.navigationItems.first()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(.3f))
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(8.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,

            ){

                Image(
                    modifier = Modifier
                        .size(80.dp),
                    painter = painterResource(id = R.drawable.bmw_logo_white),
                    contentDescription ="BMW logo"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "BMW",
                    style = TextStyle(
                        fontSize = 50.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = interFontFamily,
                        color = White
                    )
                )

                Spacer(modifier = Modifier.width(10.dp))
            }
//
//            AppNavigationRail(
//                modifier = Modifier.weight(1.2f),
//                onModeSwitch = {
//                    onModeSwitch()
//                },
//                navigationItems = state.value.navigationItems,
//                selectItemClick = {
//                    viewModel.selectedItem.value = it
////                    selectedItem  = it
//                },
//                selectedItem = viewModel.selectedItem.value,
//                onPageButtonClick = {
//                    //Now admin will create new page from here
//                    viewModel.dialogState(true)
//                },
//                onLongPress = {
//                    selectedItemToDelete = it
//                    viewModel.deleteDialogItemState(true)
//                }
//            )

            Column(
                modifier = Modifier
                    .padding(bottom = 24.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .width(130.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White),
                    onClick = {
                    viewModel.dialogState(true)
                }) {
                    Text(
                        text = stringResource(id = R.string.add_page),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(300.dp)
                        .clickable {
                            onModeSwitch()
                        },
                    text = stringResource(id = R.string.settings),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .width(300.dp)
                        .clickable {
                            viewModel.contactDialogState(true)
                        },
                    text = stringResource(id = R.string.contact_us),
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Light,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (viewModel.showDialog.value){
            AddItemDialog(
                onSaveButton = {
                  viewModel.addNavigationItem(viewModel.itemText.value)
                })
        }
        if (viewModel.showDeleteDialog.value){
            DeleteDialog(
                onDeleteButton = {
                    viewModel.deleteDialogState(false)
                }
            )
        }
        if (viewModel.showDeleteItemDialog.value){
            DeleteItemDialog(
                onDelete = {
                    viewModel.deleteDialogItemState(false)
                    viewModel.deleteNavigationItem(selectedItemToDelete!!)
                }
            )
        }
        if (viewModel.contactDialog.value){
            ContactDialog {
                viewModel.contactDialogState(value = false)
            }
        }
        if (viewModel.settingDialog.value){
            IpAddressDialog(
                onDismiss = {
                    viewModel.settingDialogState(value = false)
                },
                onPortChange = {newPort ->
                    viewModel.updatePortNumber(newPort)
                },
                onIpAddressChange = {newIpAddress ->
                    viewModel.updateIpAddress(newIpAddress)
                },
                onAppNameChange = {name ->
                    viewModel.updateAppName(name)
                },
                appName = viewModel.appName.value,
                ipAddress = viewModel.ipAddress.value,
                port = viewModel.port.value
            )
        }
        // Dynamic Screen content on the right
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(color = colorResource(id = R.color.background_color))
        ) {
            viewModel.selectedItem.value?.let { item ->
                AdminDynamicScreen( item, modifier = Modifier.fillMaxSize())
            } ?: run {
                // Show a default screen when no item is selected
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center)
                {
                    Text(
                        stringResource(id = R.string.display_content),
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Medium,
                        style = TextStyle(
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}
package com.example.wifi.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.wifi.domain.model.WifiStatus
import com.example.wifi.presentation.component.PermissionUiEvent

private fun isGranted(context: Context, permission: String): Boolean =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

private fun shouldShowRationale(activity: Activity, permission: String): Boolean =
    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

private fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}

@Composable
private fun OnResume(action: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) action()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun LocationPermissionScreen(
    modifier: Modifier = Modifier,
    vm: PermissionViewModel = hiltViewModel(),
    wifiViewModel: WifiStatusViewModel = hiltViewModel(),
    wifiStatus: (WifiStatus) -> Unit = {}
){

    val context = LocalContext.current
    val activity = context as Activity

    val state by vm.state.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        vm.onPermissionResult(
            granted = granted,
            shouldShowRationale = shouldShowRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        )
    }

    // Refresh permission whenever app returns to foreground (e.g., after Settings)
    OnResume {
        val grantedNow = isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)
        vm.onScreenShown(currentlyGranted = grantedNow)
//        vm.dismissSettingsDialog() // optional: close settings dialog when returning
    }

    // Init: tell VM current permission state
    LaunchedEffect(Unit) {
        vm.onScreenShown(
            currentlyGranted = isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)
        )
    }

    // One-shot events from VM
    LaunchedEffect(Unit) {
        vm.events.collect { event ->
            when (event) {
                PermissionUiEvent.RequestPermission ->
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                PermissionUiEvent.OpenAppSettings -> {
                    vm._showSettingsDialog = true
                }
            }
        }
    }

    // UI
    if (state.showRationaleDialog) {
        AlertDialog(
            onDismissRequest = vm::onRationaleDismiss,
            title = { Text("Permission needed") },
            text = { Text("Location permission is required to scan Wi-Fi / discover devices.") },
            confirmButton = {
                TextButton(onClick = vm::onRationaleConfirmRequestAgain) { Text("Allow") }
            },
            dismissButton = {
                TextButton(onClick = vm::onRationaleDismiss) { Text("Cancel") }
            }
        )
    }

    //show settings dialog
    if (vm._showSettingsDialog){

        AlertDialog(
            onDismissRequest = {

            },
            title = { Text("Enable permission in Settings") },
            text = { Text("Please enable Location permission from Settings.") },

            confirmButton = {
                TextButton(
                    onClick = {
                        openAppSettings(context)
                    }
                ) {
                    Text("Settings")
                }
            },
            dismissButton = {
                TextButton(
                    onClick ={
                        vm._showSettingsDialog = false
                    }

                ) {
                    Text("Cancel") }
            }
        )
    }

    if (state.isGranted){

        val status by wifiViewModel.wifiStatus.collectAsState()

        // invoke callback only when status changes
        LaunchedEffect(status) {
            wifiStatus(status)
        }
    }
}

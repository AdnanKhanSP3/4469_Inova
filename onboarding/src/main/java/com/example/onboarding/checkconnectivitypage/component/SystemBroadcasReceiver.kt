package com.example.onboarding.checkconnectivitypage.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext


@Composable
fun SystemBroadcasReceiver(
    systemAction:String,
    onSystemEvent: (intent:Intent?) -> Unit
) {

    //Grab current context  in this part of UI tree

    val context = LocalContext.current

    //safely use the latest onSystemEvent lambda passed to function

    val currentOnSystemEvent by rememberUpdatedState(newValue = onSystemEvent)

    //if context or systemAction changes , unregister
    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
             currentOnSystemEvent(intent)
            }
        }
        //regiser broadcast receiver for system action
        context.registerReceiver(broadcastReceiver, intentFilter , Context.RECEIVER_NOT_EXPORTED)

        //unregiser broadcast receiver for system action
        onDispose {
            context.unregisterReceiver(broadcastReceiver)
        }
    }
}
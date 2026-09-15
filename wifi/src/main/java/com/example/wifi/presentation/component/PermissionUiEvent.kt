package com.example.wifi.presentation.component


sealed interface PermissionUiEvent {
    data object RequestPermission : PermissionUiEvent
    data object OpenAppSettings : PermissionUiEvent
}
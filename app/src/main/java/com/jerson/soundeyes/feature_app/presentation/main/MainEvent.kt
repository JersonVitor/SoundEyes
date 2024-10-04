package com.jerson.soundeyes.feature_app.presentation.main

sealed class MainEvent{
    object NavigateToYolo: MainEvent()
    object NavigateToMobile: MainEvent()
    object PermissionCamera: MainEvent()
    object PermissionGalery: MainEvent()
}
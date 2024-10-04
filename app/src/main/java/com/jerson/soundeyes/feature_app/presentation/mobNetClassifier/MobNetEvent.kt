package com.jerson.soundeyes.feature_app.presentation.mobNetClassifier

import android.graphics.Bitmap

sealed class MobNetEvent {
    data class ClassifyImage(val bitmap: Bitmap): MobNetEvent()
}
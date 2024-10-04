package com.jerson.soundeyes.feature_app.presentation.yoloClassifier

import android.graphics.Bitmap
import com.jerson.soundeyes.feature_app.domain.model.DetectionResult

data class YoloState(
    val result: List<DetectionResult> = emptyList(),
    val labels: List<String> = emptyList(),
    val image: Bitmap? = null
)
package com.jerson.soundeyes.feature_app.domain.repository

import android.graphics.Bitmap
import com.jerson.soundeyes.feature_app.domain.model.DetectionResult

interface YoloRepository {
    fun classifyImage(bitmap: Bitmap): List<DetectionResult>
    fun closeClassifier()
}
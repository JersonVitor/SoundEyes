package com.jerson.soundeyes.feature_app.domain.repository

import android.graphics.Bitmap
import com.jerson.soundeyes.feature_app.domain.model.DetectionResult

interface MobNetRepository {
    fun classifyImage(bitmap: Bitmap): List<DetectionResult>

}
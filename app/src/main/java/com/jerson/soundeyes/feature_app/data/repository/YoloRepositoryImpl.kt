package com.jerson.soundeyes.feature_app.data.repository

import android.graphics.Bitmap
import com.jerson.soundeyes.feature_app.data.api.YOLOClassifier
import com.jerson.soundeyes.feature_app.domain.model.DetectionResult
import com.jerson.soundeyes.feature_app.domain.repository.YoloRepository



class YoloRepositoryImpl(private val yoloClassifier:YOLOClassifier): YoloRepository {
    override fun classifyImage(bitmap: Bitmap): List<DetectionResult> {
        return yoloClassifier.classifyImage(bitmap)
    }

    override fun closeClassifier() {
        yoloClassifier.close()
    }
}
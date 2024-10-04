package com.jerson.soundeyes.feature_app.data.repository

import android.graphics.Bitmap
import com.jerson.soundeyes.feature_app.data.api.MobileNetClassifier
import com.jerson.soundeyes.feature_app.domain.model.DetectionResult
import com.jerson.soundeyes.feature_app.domain.repository.MobNetRepository

class MobNetRepositoryImpl( private val mobileNetClassifier: MobileNetClassifier): MobNetRepository {
    override fun classifyImage(bitmap: Bitmap): List<DetectionResult> {
        return mobileNetClassifier.classifyImage(bitmap)
    }



}
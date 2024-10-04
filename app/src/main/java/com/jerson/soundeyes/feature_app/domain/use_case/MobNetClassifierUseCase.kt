package com.jerson.soundeyes.feature_app.domain.use_case

import android.graphics.Bitmap
import com.jerson.soundeyes.feature_app.domain.model.DetectionResult
import com.jerson.soundeyes.feature_app.domain.repository.MobNetRepository

class MobNetClassifierUseCase(private val mobNetRepository: MobNetRepository)  {
    operator fun invoke(bitmap: Bitmap): List<DetectionResult>{
        return mobNetRepository.classifyImage(bitmap)
    }
}
package com.jerson.soundeyes.feature_app.domain.model

data class DetectionResult(
    val classId: Int,
    val confidence: Float,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
)
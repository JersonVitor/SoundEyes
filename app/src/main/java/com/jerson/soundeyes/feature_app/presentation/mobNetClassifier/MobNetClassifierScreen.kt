package com.jerson.soundeyes.feature_app.presentation.mobNetClassifier

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jerson.soundeyes.feature_app.presentation.yoloClassifier.YoloEvent

@Composable
fun MobNetScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MobNetClassifierViewModel = hiltViewModel(),
    imageUri: String
) {
    val state by viewModel.state
    // Carregar o Bitmap a partir da URI
    val context = LocalContext.current
    val bitmap = remember(imageUri) {
        loadBitmapFromUri(
            context,
            imageUri
        )
    }

    LaunchedEffect(bitmap) {
        bitmap?.let {
            viewModel.onEvent(MobNetEvent.ClassifyImage(it))
        }
    }
    Column(modifier = modifier.padding(16.dp)) {
        // Exibir a imagem

        Image(bitmap = bitmap!!.asImageBitmap(), contentDescription = "Classify Image", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        // Exibir os resultados da classificação
        if (state.result.isNotEmpty()) {
            LazyColumn {
                items(state.result.size) { index ->
                    val detection = state.result[index]
                    val label = if (detection.classId < state.labels.size) state.labels[detection.classId] else "Unknown"
                    Text(text = "Label: $label, Confidence: ${detection.confidence}, Box: (${detection.x}, ${detection.y}, ${detection.width}, ${detection.height})")
                }
            }
        } else {
            Text(text = "No detections found.")
        }
    }
    BackHandler {
        navController.popBackStack()
    }
}
private fun loadBitmapFromUri(context: Context, uri: String): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(Uri.parse(uri))
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
package com.jerson.soundeyes.feature_app.presentation.yoloClassifier

import CameraPreviewView
import ObjectDetectionOverlay
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController



@Composable
fun YoloClassifierScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: YoloClassifierViewModel = hiltViewModel(),

) {
    val state by viewModel.state
    var bitmapState by remember{ mutableStateOf<Bitmap?>(null)}



    LaunchedEffect(bitmapState) {
        bitmapState?.let {
            viewModel.onEvent(YoloEvent.ClassifyImage(it))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Visualização da câmera
        CameraPreviewView(modifier = Modifier.fillMaxSize(), processImage = {
            bitmapState = it
        })

        // Desenho dos quadrados sobre a câmera
        state.result?.let { detections ->
            ObjectDetectionOverlay(
                detections = detections,
                modifier = Modifier.fillMaxSize()
            )
        }
    }


    /* Column(modifier = modifier.padding(16.dp)) {
         // Exibir a imagem capturada
         bitmapState?.let { bitmap ->
             Image(
                 bitmap = bitmap.asImageBitmap(),
                 contentDescription = "Captured Image",
                 modifier = Modifier.fillMaxWidth()
             )
         }

         Spacer(modifier = Modifier.height(16.dp))

         // Exibir os resultados da classificação do YOLO
         if (state.result.isNotEmpty()) {
             LazyColumn {
                 items(state.result.size) { index ->
                     val detection = state.result[index]
                     val label = if (detection.classId < state.labels.size) state.labels[detection.classId] else "Unknown"
                     Text(
                         text = "Label: $label, Confidence: ${detection.confidence}, Box: (${detection.x}, ${detection.y}, ${detection.width}, ${detection.height})"
                     )
                 }
             }
         } else {
             Text(text = "No detections found.")
         }
     }*/

    // Gerenciar o botão de voltar para a navegação
    BackHandler {
        navController.popBackStack()
    }
}


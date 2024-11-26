package com.jerson.soundeyes.feature_app.presentation.yoloClassifier

import CameraPreviewView
import ConectionBluetooth
import ConectionBluetooth.receiveImageFromESP32
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun YoloClassifierScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: YoloClassifierViewModel = hiltViewModel(),
) {
    val state by viewModel.state
    val context: Context = LocalContext.current
    var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
    var classificationTime by remember { mutableStateOf("") } // Variável para armazenar o tempo de classificação
    var sendingImage by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            try {
                /*ConectionBluetooth.envioBool(context,true)
                // Recebe a imagem do ESP32*/
                val startTime = System.currentTimeMillis() // Marcar
              val receivedImage = receiveImageFromESP32(context)
                val endTime = System.currentTimeMillis()
                receivedImage?.let {
                    bitmapState = it
                }
                sendingImage = "${endTime - startTime} ms"

            } catch (e: Exception) {
                e.printStackTrace() // Lidar com erros de conexão ou outros problemas
            }
        }
    }
    LaunchedEffect(bitmapState) {
        bitmapState?.let {
            val startTime = System.currentTimeMillis() // Marcar o tempo de início
            viewModel.onEvent(YoloEvent.ClassifyImage(it))
            val endTime = System.currentTimeMillis() // Marcar o tempo de fim
            classificationTime = "${endTime - startTime} ms" // Calcular o tempo gasto
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
      /*  // Visualização da câmera
        CameraPreviewView(modifier = Modifier.fillMaxSize(), processImage = {
            bitmapState = it
        })*/
       bitmapState?.let { Image(bitmap = it.asImageBitmap(),
                              contentDescription = null,
                               modifier = Modifier.fillMaxSize()) }
//
//        // Desenho dos quadrados sobre a câmera
      state.result?.let { detections ->
           ObjectDetectionOverlay(
              detections = detections,
             modifier = Modifier.fillMaxSize()
           )
      }

        // Exibir o tempo de classificação no canto superior direito
        Text(
            text = "tempo Classify : $classificationTime",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd), // Posiciona no canto superior direito
            color = Color.Black // Ou a cor que preferir
        )
        Text(
            text = "Tempo receber: $sendingImage",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart), // Posiciona no canto superior direito
            color = Color.Black // Ou a cor que preferir
        )
    }

    // Gerenciar o botão de voltar para a navegação
    BackHandler {
        navController.popBackStack()
    }
}

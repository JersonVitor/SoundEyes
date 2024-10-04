package com.jerson.soundeyes.feature_app.data.api

import android.content.Context
import android.graphics.*
import android.os.Environment
import android.util.Log
import com.jerson.soundeyes.feature_app.domain.model.DetectionResult
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object MobileNetClassifier {
    private lateinit var interpreter: Interpreter
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext // Armazena o contexto da aplicação
        if (!MobileNetClassifier::interpreter.isInitialized) {
            interpreter = Interpreter(loadModelFile())
        }
    }

    //Todo: Generalizar

    // Método para carregar o arquivo TFLite do diretório assets
    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = appContext.assets.openFd("mobilenet_v2.tflite") // Altere para o nome do seu modelo MobileNet
        FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }
    }

    // Método para realizar a predição a partir de uma imagem Bitmap
    fun classifyImage(bitmap: Bitmap): List<DetectionResult> {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true) // MobileNet geralmente usa 224x224
        val inputTensor = convertBitmapToTensor(resizedBitmap)

        // O tamanho de saída depende do seu modelo MobileNet
        val output = Array(1) { FloatArray(1000) } // MobileNet V2 geralmente tem 1001 classes

        try {
            interpreter.run(inputTensor, output)
        } catch (e: Exception) {
            Log.e("Error Image", e.message.toString())
            e.printStackTrace()
        }

        return parseOutput(output) // Passa o FloatArray para parseOutput
    }

    fun onClose(){
        interpreter.close()
    }
    private fun parseOutput(output: Array<FloatArray>): List<DetectionResult> {
        val results = mutableListOf<DetectionResult>()

        // Iterando sobre as detecções no tensor de saída

        for (i in output[0].indices) {
            val confidence = output[0][i] // Confiança do modelo para cada classe

            // Verifica se a confiança excede o limiar definido (por exemplo, 0.5)
            if (confidence > 0.2) {
                results.add(DetectionResult(i, confidence, 0f, 0f, 0f, 0f)) // Ajuste conforme necessário
            }
        }

        // Ordena os resultados pela confiança e pega os Top K
        results.sortByDescending { it.confidence }
        return results
    }

    private fun convertBitmapToTensor(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        val inputSize = 224
        val intValues = IntArray(inputSize * inputSize)
        val input = Array(1) { Array(inputSize) { Array(inputSize) { FloatArray(3) } } }

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixel = intValues[i * inputSize + j]
                // Normalização dos pixels para o intervalo [-1, 1]
                input[0][i][j][0] = ((pixel shr 16 and 0xFF) / 255.0f) * 2.0f - 1.0f
                input[0][i][j][1] = ((pixel shr 8 and 0xFF) / 255.0f) * 2.0f - 1.0f
                input[0][i][j][2] = ((pixel and 0xFF) / 255.0f) * 2.0f - 1.0f
            }
        }

        return input
    }
}




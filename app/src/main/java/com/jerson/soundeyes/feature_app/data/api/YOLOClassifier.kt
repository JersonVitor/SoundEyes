package com.jerson.soundeyes.feature_app.data.api

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.jerson.soundeyes.feature_app.domain.model.DetectionResult
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

//TODO: liberar dados do interpreter
//TODO: Fazer os devidos tratamentos
object YOLOClassifier {
    private lateinit var interpreter: Interpreter
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext // Armazena o contexto da aplicação
        if (!YOLOClassifier::interpreter.isInitialized) {
            interpreter = Interpreter(loadModelFile())
        }
    }

    fun onClose(){
        interpreter.close()
    }
    //Todo: Generalizar

    // Método para carregar o arquivo TFLite do diretório assets
    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = appContext.assets.openFd("yolov5s-fp16.tflite") // Altere para o nome do seu modelo YOLOv5
        FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }
    }

    // Método para realizar a predição a partir de uma imagem Bitmap
     fun classifyImage(bitmap: Bitmap): List<DetectionResult> {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 640, 640, true)
        val inputTensor = convertBitmapToTensor(resizedBitmap)

        // O tamanho de saída é baseado na estrutura do modelo
        val output = Array(1) { Array(25200) { FloatArray(85) } }

        try {
            interpreter.run(inputTensor, output)
        } catch (e: Exception) {
            Log.e("Error Image", e.message.toString())
            e.printStackTrace()
        }

        val detectionResults = parseOutput(output) // Passa o FloatArray para parseOutput

        // Desenhar caixas delimitadoras e salvar a imagem
        val bitmapWithBoxes = drawDetectionBoxes(resizedBitmap, detectionResults)
        saveImageToExternalStorage(appContext, bitmapWithBoxes)

        return detectionResults
    }

    private fun parseOutput(output: Array<Array<FloatArray>>): List<DetectionResult> {
        val results = mutableListOf<DetectionResult>()

        // Iterando sobre as detecções no tensor de saída
        for (i in 0 until 25200) {
            val detection = output[0][i]
            val confidence = detection[4] // Índice 4 contém a confiança do objeto

            // Verifica se a confiança excede o limiar definido (por exemplo, 0.5)
            if (confidence > 0.5) {
                // Extraindo as coordenadas da caixa delimitadora
                val x = detection[0]
                val y = detection[1]
                val width = detection[2]
                val height = detection[3]

                // Encontrando o índice da classe de maior probabilidade
                val classIndex = detection.sliceArray(5 until 85).indexOfMax()

                // Adicionando a detecção à lista de resultados
                results.add(DetectionResult(classIndex, confidence, x, y, width, height))
            }
        }

        return results
    }

/*// Função auxiliar para desenhar as caixas delimitadoras
    private fun drawDetectionBoxes(bitmap: Bitmap, detectionResults: List<DetectionResult>): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }

        // Itera sobre os resultados da detecção
        detectionResults.forEach { result ->
            // As coordenadas (x, y, width, height) estão normalizadas entre 0 e 1, então precisamos escalá-las
            val xCenter = result.x * bitmap.width
            val yCenter = result.y * bitmap.height
            val boxWidth = result.width * bitmap.width
            val boxHeight = result.height * bitmap.height

            // Calcula as coordenadas dos cantos da caixa
            val left = xCenter - boxWidth / 2
            val top = yCenter - boxHeight / 2
            val right = xCenter + boxWidth / 2
            val bottom = yCenter + boxHeight / 2

            // Desenha a caixa delimitadora na imagem
            canvas.drawRect(left, top, right, bottom, paint)
        }

        return mutableBitmap
    }*/
private fun drawDetectionBoxes(bitmap: Bitmap, detectionResults: List<DetectionResult>): Bitmap {
    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(mutableBitmap)
    val paintBox = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    val paintText = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        style = Paint.Style.FILL
    }
    val paintBackground = Paint().apply {
        color = Color.BLACK
        alpha = 150
    }

    // Itera sobre os resultados da detecção
    detectionResults.forEach { result ->
        // As coordenadas (x, y, width, height) estão normalizadas entre 0 e 1, então precisamos escalá-las
        val xCenter = result.x * bitmap.width
        val yCenter = result.y * bitmap.height
        val boxWidth = result.width * bitmap.width
        val boxHeight = result.height * bitmap.height

        // Calcula as coordenadas dos cantos da caixa
        val left = xCenter - boxWidth / 2
        val top = yCenter - boxHeight / 2
        val right = xCenter + boxWidth / 2
        val bottom = yCenter + boxHeight / 2

        // Desenha a caixa delimitadora na imagem
        canvas.drawRect(left, top, right, bottom, paintBox)

        // Prepara o texto com o rótulo e a confiança
        val labelText = "${result.classId} - ${"%.2f".format(result.confidence)}"

        // Desenha o fundo preto para o texto
        val textWidth = paintText.measureText(labelText)
        val textHeight = paintText.textSize
        canvas.drawRect(left, top - textHeight, left + textWidth, top, paintBackground)

        // Desenha o texto na imagem (rótulo + confiança)
        canvas.drawText(labelText, left, top - 10, paintText)
    }

    return mutableBitmap
}


    private fun saveImageToExternalStorage(context: Context, bitmap: Bitmap) {
        val filename = "detected_image_${System.currentTimeMillis()}.png"
        val resolver = context.contentResolver

        // Definir o local no MediaStore (Imagens)
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/DetectedImages") // Pasta dentro da galeria
            put(MediaStore.Images.Media.IS_PENDING, 1)  // Coloca a imagem em estado "pendente"
        }

        // Inserir a imagem no MediaStore
        val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            try {
                // Abrir o output stream para salvar a imagem
                resolver.openOutputStream(uri)?.use { outStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                }

                // Atualiza o estado "pendente" da imagem para concluída
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)

                Log.d("Save Image", "Image saved successfully: $uri")

            } catch (e: IOException) {
                Log.e("Save Image", "Error saving image", e)
            }
        } ?: run {
            Log.e("Save Image", "Error: Image Uri is null")
        }
    }

    // Função auxiliar para salvar a imagem no armazenamento interno
   /* private fun saveImageToInternalStorage(context: Context, bitmap: Bitmap) {
        val directory = context.filesDir // Use internal storage directory
        val file = File(directory, "detected_image.png")
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                Log.d("Save Image", "Image saved successfully: ${file.absolutePath}")
            }
        } catch (e: IOException) {
            Log.e("Save Image", "Error saving image", e)
        }
    }
*/

    // Função auxiliar para encontrar o índice do valor máximo
    private fun FloatArray.indexOfMax(): Int {
        return this.indices.maxByOrNull { this[it] } ?: -1
    }

    private fun convertBitmapToTensor(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        val inputSize = 640
        val intValues = IntArray(inputSize * inputSize)
        val input = Array(1) { Array(inputSize) { Array(inputSize) { FloatArray(3) } } }

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixel = intValues[i * inputSize + j]
                // Extração dos componentes RGB e normalização dos pixels para o intervalo [0, 1]
                input[0][i][j][0] = (pixel shr 16 and 0xFF) / 255.0f  // Canal R
                input[0][i][j][1] = (pixel shr 8 and 0xFF) / 255.0f   // Canal G
                input[0][i][j][2] = (pixel and 0xFF) / 255.0f         // Canal B
            }
        }

        return input
    }
    fun close() {
        if (::interpreter.isInitialized) {
            interpreter.close() // Libere os recursos do Interpreter
        }
    }

}

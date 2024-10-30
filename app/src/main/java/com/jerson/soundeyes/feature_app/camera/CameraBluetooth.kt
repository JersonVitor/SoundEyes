import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*

val UUID_SERIAL = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

suspend fun receiveImageFromESP32(context: Context, deviceAddress: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter ?: return@withContext null
            val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)

            // Verificação de permissão de Bluetooth
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // Aqui você pode informar que a permissão é necessária antes de tentar se conectar.
                return@withContext null
            }

            // Criar e conectar o socket Bluetooth
            val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(UUID_SERIAL)
            socket.connect()

            val inputStream: InputStream = socket.inputStream
            // 1. Receber o tamanho da imagem
            val sizeBuffer = ByteArray(4)
            inputStream.read(sizeBuffer, 0, 4)
            val imageSize = java.nio.ByteBuffer.wrap(sizeBuffer).int

            // 2. Receber a imagem como bytes
            val imageBuffer = ByteArray(imageSize)
            inputStream.read(imageBuffer, 0, imageSize)

            // 3. Decodificar para Bitmap
            BitmapFactory.decodeByteArray(imageBuffer, 0, imageSize)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

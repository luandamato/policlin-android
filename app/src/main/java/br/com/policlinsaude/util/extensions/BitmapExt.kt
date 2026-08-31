package br.com.policlinsaude.util.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

/**
 * Extensions de Bitmap/imagem reutilizáveis.
 * Migrado/adaptado de `_legacy/.../core/helper/{BitmapEx,FileEx,StringEx}.kt`.
 */

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.resizeAndCompress(maxWidth: Int): Bitmap {
    val resizedHeight = height.toDouble() / (width.toDouble() / maxWidth.toDouble())
    val scaledBitmap = Bitmap.createScaledBitmap(this, maxWidth, resizedHeight.roundToInt(), false)
    val os = ByteArrayOutputStream()
    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, os)
    return BitmapFactory.decodeStream(ByteArrayInputStream(os.toByteArray()))
}

/**
 * Converte a imagem para Base64, respeitando rotação do sensor quando aplicável.
 */
fun File.toBase64(maxWidth: Int, rotation: Int): String? {
    return try {
        val bitmap = BitmapFactory.decodeFile(absolutePath).resizeAndCompress(maxWidth)
        val outputStream = ByteArrayOutputStream()

        if (rotation > 0) {
            val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Decodifica uma imagem Base64 para [Bitmap].
 */
fun String.getBitmapFromImage(): Bitmap {
    val imageBytes = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

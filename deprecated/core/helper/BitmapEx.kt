package br.com.policlinsaude.core.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt


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
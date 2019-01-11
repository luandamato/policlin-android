package br.com.policlinsaude.core.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.WindowManager
import java.io.ByteArrayOutputStream
import java.io.File
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_90
import android.view.Surface.ROTATION_0
import android.R.attr.rotation
import android.graphics.Matrix
import android.hardware.Camera
import android.util.Log
import android.view.Surface


/**
 * Created by lmiyagi on 3/27/18.
 */
fun File.toBase64(maxWidth: Int, rotation: Int): String? {


    try {

        val bitmap = BitmapFactory.decodeFile(absolutePath).resizeAndCompress(maxWidth)

        //
        val outputStream = ByteArrayOutputStream()

        //ajuste de rotação de imagem devido ao problema de posição de sensor dependendo do fabricante
        if (rotation > 0) {
            val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()


            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val bitmapBytes = outputStream.toByteArray()

            return Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
        } else {

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val bitmapBytes = outputStream.toByteArray()
            return Base64.encodeToString(bitmapBytes, Base64.DEFAULT)

        }


        //-----
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
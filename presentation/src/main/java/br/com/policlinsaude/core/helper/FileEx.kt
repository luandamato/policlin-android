package br.com.policlinsaude.core.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File


/**
 * Created by lmiyagi on 3/27/18.
 */
fun File.toBase64(maxWidth: Int): String? {
    try {
        val bitmap = BitmapFactory.decodeFile(absolutePath).resizeAndCompress(maxWidth)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bitmapBytes = outputStream.toByteArray()
        return Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
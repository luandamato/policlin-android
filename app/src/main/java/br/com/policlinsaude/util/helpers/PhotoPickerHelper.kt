package br.com.policlinsaude.util.helpers

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

/**
 * Helper para seleção/tomada de imagem (câmera ou galeria).
 * Migrado/adaptado de `_legacy/.../core/helper/photoHelper.kt`.
 */
class PhotoPickerHelper(
    private val fragment: Fragment,
    private val onImageSelected: (Bitmap, File) -> Unit,
    private val onError: (String) -> Unit = {}
) {

    enum class Mode { CAMERA, GALLERY, CAMERA_AND_GALLERY }

    private var cameraFile: File? = null

    private val galleryLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) return@registerForActivityResult
            try {
                val file = copyUriToCache(uri)
                val bitmap = decodeAndValidateImage(file)
                onImageSelected(bitmap, file)
            } catch (e: Exception) {
                onError(e.message ?: "Não foi possível carregar a imagem selecionada.")
            }
        }

    private val cameraLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            val file = cameraFile
            if (!success || file == null) {
                file?.delete()
                cameraFile = null
                return@registerForActivityResult
            }
            try {
                val bitmap = decodeAndValidateImage(file)
                onImageSelected(bitmap, file)
            } catch (e: Exception) {
                file.delete()
                onError(e.message ?: "Não foi possível processar a foto tirada.")
            } finally {
                cameraFile = null
            }
        }

    private val cameraPermissionLauncher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                launchCamera()
            } else {
                onError("Permissão da câmera negada. Autorize o acesso à câmera para tirar uma foto.")
            }
        }

    fun open(mode: Mode) {
        when (mode) {
            Mode.CAMERA -> openCameraWithPermission()
            Mode.GALLERY -> openGallery()
            Mode.CAMERA_AND_GALLERY -> openGallery()
        }
    }

    fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun openCameraWithPermission() {
        val context = fragment.requireContext()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun launchCamera() {
        try {
            val context = fragment.requireContext()
            cameraFile = File.createTempFile("camera_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                cameraFile!!
            )
            cameraLauncher.launch(uri)
        } catch (e: Exception) {
            cameraFile?.delete()
            cameraFile = null
            onError("Não foi possível abrir a câmera.")
        }
    }

    private fun copyUriToCache(uri: Uri): File {
        val context = fragment.requireContext()
        val mimeType = context.contentResolver.getType(uri)
        if (mimeType != null && mimeType != "image/jpeg" && mimeType != "image/png" && mimeType != "image/webp") {
            throw IllegalArgumentException("Formato de imagem não suportado. Selecione uma imagem JPG, PNG ou WEBP.")
        }

        val file = File.createTempFile("gallery_", ".jpg", context.cacheDir)
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        } ?: throw IllegalStateException("Não foi possível acessar a imagem selecionada.")
        return file
    }

    private fun decodeAndValidateImage(file: File): Bitmap {
        if (!file.exists() || file.length() == 0L) {
            throw IllegalStateException("O arquivo da imagem está vazio ou não existe.")
        }
        if (file.length() > MAX_FILE_SIZE) {
            throw IllegalArgumentException("A imagem não pode ter mais de 2 MB.")
        }

        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            ?: throw IllegalArgumentException("Não foi possível interpretar a imagem. Selecione outra imagem.")

        if (bitmap.width <= 0 || bitmap.height <= 0) {
            bitmap.recycle()
            throw IllegalArgumentException("A imagem selecionada possui dimensões inválidas.")
        }
        if (bitmap.width < MIN_IMAGE_SIZE || bitmap.height < MIN_IMAGE_SIZE) {
            bitmap.recycle()
            throw IllegalArgumentException("A imagem é muito pequena. Selecione uma imagem com pelo menos ${MIN_IMAGE_SIZE}x${MIN_IMAGE_SIZE} pixels.")
        }
        return bitmap
    }

    companion object {
        private const val MAX_FILE_SIZE = 2L * 1024L * 1024L
        private const val MIN_IMAGE_SIZE = 100
    }
}
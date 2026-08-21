package br.com.policlinsaude.core.helper

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

class PhotoPickerHelper(
    private val fragment: Fragment,
    private val onImageSelected: (File) -> Unit,
    private val onError: (Exception?) -> Unit = {}
) {

    enum class Mode {
        CAMERA,
        GALLERY,
        CAMERA_AND_GALLERY
    }

    private var cameraFile: File? = null

    private val galleryLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri == null) return@registerForActivityResult

            try {
                val file = File.createTempFile(
                    "gallery_",
                    ".jpg",
                    fragment.requireContext().cacheDir
                )

                fragment.requireContext().contentResolver
                    .openInputStream(uri)
                    ?.use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    ?: throw IllegalStateException("Não foi possível abrir a imagem.")

                onImageSelected(file)
            } catch (e: Exception) {
                onError(e)
            }
        }

    private val cameraLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                cameraFile?.let(onImageSelected)
            } else {
                cameraFile?.delete()
            }
        }

    private val cameraPermissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                launchCamera()
            } else {
                onError(SecurityException("Permissão da câmera negada."))
            }
        }

    fun open(mode: Mode) {
        when (mode) {
            Mode.CAMERA -> openCameraWithPermission()
            Mode.GALLERY -> openGallery()
            Mode.CAMERA_AND_GALLERY -> showChooser()
        }
    }

    private fun showChooser() {
        androidx.appcompat.app.AlertDialog.Builder(fragment.requireContext())
            .setTitle("Selecionar imagem")
            .setItems(arrayOf("Câmera", "Galeria")) { _, which ->
                when (which) {
                    0 -> openCameraWithPermission()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openGallery() {
        // GetContent funciona sem READ_EXTERNAL_STORAGE /
        // READ_MEDIA_IMAGES porque o usuário escolhe explicitamente a imagem.
        galleryLauncher.launch("image/*")
    }

    private fun openCameraWithPermission() {
        val context = fragment.requireContext()

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            launchCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCamera() {
        try {
            val context = fragment.requireContext()

            cameraFile = File.createTempFile(
                "camera_",
                ".jpg",
                context.cacheDir
            )

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                cameraFile!!
            )

            cameraLauncher.launch(uri)
        } catch (e: Exception) {
            onError(e)
        }
    }
}
package br.com.policlinsaude.perfil.navigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Camera
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.PhotoPickerHelper
import br.com.policlinsaude.editPhone.view.EditPhoneActivity
import br.com.policlinsaude.editPassword.view.EditPasswordActivity
import br.com.policlinsaude.perfil.view.PerfilFragment
import com.policlinsaude.newfeature.features.deleteUser.ui.Activity.DeleteUserActivity
import com.policlinsaude.newfeature.utils.DatePickerHelper
import com.policlinsaude.newfeature.utils.DialogHelper
import java.io.File

/**
 * Created by lmiyagi on 3/27/18.
 */
class PerfilNavigatorImpl(private val fragment: PerfilFragment) : PerfilNavigator {

    override fun goToImagePicker() {

        PhotoPickerHelper(
            fragment = fragment,
            onImageSelected = { bitmap, file ->
                handleImage(file, bitmap)
            },
            onError = {
                messageError("Não foi possível selecionar a imagem.")
            }
        ).open(PhotoPickerHelper.Mode.CAMERA_AND_GALLERY)
    }
    private fun handleImage(file: File, bitmap: Bitmap) {
        try {
            print(bitmap.width)

        } catch (e: Exception) {
            messageError("Não foi possível processar a imagem.")
        }
    }
    private fun messageError(message: String) {
        fragment.context.let {
            DialogHelper.showErrorDialog(
                it!!,
                message
            )
        }

    }

    override fun goToEditPhone() {
        fragment.startActivity(Intent(fragment.context, EditPhoneActivity::class.java))
    }

    override fun goToEditPassword() {


        fragment.startActivity(Intent(fragment.context, EditPasswordActivity::class.java))
    }

    override fun goToDelete() {
        fragment.startActivityForResult(Intent(fragment.context, DeleteUserActivity::class.java), 123)
    }

}
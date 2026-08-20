package br.com.policlinsaude.ui.legacy.perfil.navigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import br.com.policlinsaude.R
import br.com.policlinsaude.editPhone.view.EditPhoneActivity
import br.com.policlinsaude.editPassword.view.EditPasswordActivity
import br.com.policlinsaude.perfil.view.PerfilFragment
import com.policlinsaude.newfeature.features.deleteUser.ui.Activity.DeleteUserActivity
import pl.aprilapps.easyphotopicker.EasyImage

/**
 * Created by lmiyagi on 3/27/18.
 */
class PerfilNavigatorImpl(private val fragment: PerfilFragment) : PerfilNavigator {

    override fun goToImagePicker() {

                EasyImage.openChooserWithGallery(fragment, fragment.getString(R.string.text_image_chooser_title), 0)
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
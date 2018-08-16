package br.com.policlinsaude.perfil.navigator

import android.content.Intent
import br.com.policlinsaude.R
import br.com.policlinsaude.editPhone.view.EditPhoneActivity
import br.com.policlinsaude.editPassword.view.EditPasswordActivity
import br.com.policlinsaude.perfil.view.PerfilFragment
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
}
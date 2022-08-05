package br.com.policlinsaude.perfil.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.*
import br.com.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.base.BaseFragmentWithInject
import br.com.policlinsaude.core.helper.DateHelper
import br.com.policlinsaude.core.helper.MaskUtils
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.core.helper.toBase64
import br.com.policlinsaude.home.view.MenuActivity
import br.com.policlinsaude.model.PresentationPerson
import br.com.policlinsaude.perfil.presenter.PerfilPresenter
import kotlinx.android.synthetic.main.fragment_perfil.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import android.media.ExifInterface


class PerfilFragment : BaseFragmentWithInject(), PerfilView {

    companion object {

        fun newInstance(): PerfilFragment {
            return PerfilFragment()
        }
    }

    @Inject
    lateinit var presenter: PerfilPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_perfil, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)

        (activity as MenuActivity).setupFragmentToolbar(toolbar, R.string.title_perfil)

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            (activity as? BaseActivity)?.onOptionsItemSelected(item)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.getPerfil()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : EasyImage.Callbacks {


            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
             //   if (source == EasyImage.ImageSource.CAMERA && imageFile != null) {
                    Log.e("TESTE", "Dentro de onImagePicked")

                    //ajuste de rotação de imagem devido ao problema de posição de sensor dependendo do fabricante
                    val rotation = applyRotationIfNeeded(imageFile!!)
                    Log.e("TESTE", "Dentro de onImagePicked - valor de rotation: " + rotation)


                    imageFile?.let {
                        presenter.onImagePicked(it.toBase64(500, rotation))
                    }
              //  }
            /*    else {
                    Log.e("TESTE", "onImagePicked == NULL")
                    imageFile?.let {
                        presenter.onImagePicked(it.toBase64(500, 0))
                    }
                }*/


            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                e?.printStackTrace()
                presenter.onImagePickError()
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                if (source === EasyImage.ImageSource.CAMERA) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(context)
                    photoFile?.delete()
                }
            }
        })
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun set(person: PresentationPerson) {
        try {
            imageView.setImageBitmap(person.photo.getBitmapFromImage())
        } catch (e: Exception) {
            e.printStackTrace()
            // ignored
        }
        nameTextView.text = person.name
        emailTextView.text = person.email
        phoneTextView.text = MaskUtils.applyMaskToString(MaskUtils.PHONE, person.phone)
        cpfTextView.text = MaskUtils.applyMaskToString(MaskUtils.CPF, person.cpf)
        registerAndOrderTextView.text = getString(R.string.text_register_and_order_number, person.plan.register.toInt(), person.plan.order.toInt())
        birthdayTextView.text = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_DDMMYYYY, person.birthday)
        (activity as MenuActivity).getCurrentPerson()
    }

    override fun showImagePickError() {
        (activity as BaseActivity).showToast(R.string.text_image_pick_error)
    }

    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    override fun showAvatarChangedSuccess() {
        (activity as BaseActivity).showToast(R.string.text_avatar_change_success)
    }

    override fun showDialogError(throwable: Throwable) {
        (activity as BaseActivity).showError(message = if (throwable is MessageErrorException) throwable.message!! else getString(R.string.msg_unexpected_error))
    }

    private fun setClickListeners() {
        phoneContainer.setOnClickListener {
            presenter.onEditPhoneClicked()
        }
        passwordContainer.setOnClickListener {
            presenter.onEditPasswordClicked()
        }
        imageView.setOnClickListener {
            presenter.onAvatarChangeClicked()
        }
    }


    private fun applyRotationIfNeeded(imageFile: File): Int {
        val exif = ExifInterface(imageFile.absolutePath)
        val exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        return when(exifRotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }




}
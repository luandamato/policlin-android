package br.com.policlinsaude.notHasPassword.view.steppers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.domain.AppConstants.REGEX_MIN_AND_MAX_LENGTH_PASSWORD
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.core.helper.toBase64
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_create_password.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.lang.Exception

class CreatePasswordFragment : BaseFragment(), Step {

    companion object {
        private const val EXTRA_PRESENTER = "presenter"

        fun newStep(): Step {
            return CreatePasswordFragment()
        }
    }

    private lateinit var presenter: NotHasPasswordPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_create_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = (activity as NotHasPasswordActivity).presenter

        addValidationFields()
        setFields()
        setClickListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : EasyImage.Callbacks {

            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                imageFile?.let {
                    val image = it.toBase64(500)
                    presenter.onImagePicked(image)
                    if (!image.isNullOrEmpty()) {
                        imageView.setImageBitmap(image!!.getBitmapFromImage())
                    }
                }
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

    private fun setClickListener() {
        imageView.setOnClickListener {
            EasyImage.openChooserWithGallery(this, getString(R.string.text_image_chooser_title), 0)
        }
    }

    private fun setFields() {
        val person = presenter.getPresentationPerson()
        editTextPassword.setText(person.password)
        editTextConfirmPassword.setText(person.password)
    }

    private fun addValidationFields() {
        awesomeValidation.addValidation(editTextPassword,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))
        awesomeValidation.addValidation(editTextConfirmPassword,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))

        awesomeValidation.addValidation(editTextConfirmPassword,
                RegexTemplate.NOT_EMPTY, getString(R.string.text_field_required))


        awesomeValidation.addValidation(editTextConfirmPassword,
                REGEX_MIN_AND_MAX_LENGTH_PASSWORD, getString(R.string.text_min_and_max_password))

        awesomeValidation.addValidation(editTextConfirmPassword, editTextPassword,
                getString(R.string.text_two_password_invalid))
    }


    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? {
        if (awesomeValidation.validate()) {
            val person = presenter.getPresentationPerson()
            person.password = editTextPassword.text.toString()
            presenter.setPresentationPerson(person)
            return null
        }
        return VerificationError("Validation error")
    }

    override fun onError(error: VerificationError) {

    }
}
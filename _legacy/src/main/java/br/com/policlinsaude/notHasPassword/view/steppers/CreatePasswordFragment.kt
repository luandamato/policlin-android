package br.com.policlinsaude.notHasPassword.view.steppers

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.PhotoPickerHelper
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.core.helper.toBase64
import br.com.policlinsaude.databinding.FragmentCreatePasswordBinding
import br.com.policlinsaude.domain.AppConstants.REGEX_MIN_AND_MAX_LENGTH_PASSWORD
import br.com.policlinsaude.notHasPassword.presenter.NotHasPasswordPresenter
import br.com.policlinsaude.notHasPassword.view.NotHasPasswordActivity
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import java.io.File

class CreatePasswordFragment : BaseFragment() {

    companion object {
        fun newInstance() = CreatePasswordFragment()
    }

    private lateinit var presenter: NotHasPasswordPresenter
    private lateinit var binding: FragmentCreatePasswordBinding
    private lateinit var photoPickerHelper: PhotoPickerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoPickerHelper = PhotoPickerHelper(
            fragment = this,
            onImageSelected = { bitmap, file ->
                handleImage(file, bitmap)
            },
            onError = {
                presenter.onImagePickError()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePasswordBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        presenter = (activity as NotHasPasswordActivity).presenter

        addValidationFields()
        setFields()
        setClickListener()
    }

    private fun handleImage(file: File, bitmap: Bitmap) {
        try {
            val image = file.toBase64(500, 0)

            presenter.onImagePicked(image)

            if (image?.isNotEmpty() == true) {
                binding.imageView.setImageBitmap(
                    image.getBitmapFromImage()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            presenter.onImagePickError()
        }
    }

    private fun setClickListener() {
        binding.imageView.setOnClickListener {
            photoPickerHelper.open(
                PhotoPickerHelper.Mode.CAMERA_AND_GALLERY
            )
        }
    }

    private fun setFields() {
        val person = presenter.getPresentationPerson()

        binding.editTextPassword.setText(person.password)
        binding.editTextConfirmPassword.setText(person.password)
    }

    private fun addValidationFields() {
        awesomeValidation.addValidation(
            binding.editTextPassword,
            RegexTemplate.NOT_EMPTY,
            getString(R.string.text_field_required)
        )

        awesomeValidation.addValidation(
            binding.editTextConfirmPassword,
            RegexTemplate.NOT_EMPTY,
            getString(R.string.text_field_required)
        )

        awesomeValidation.addValidation(
            binding.editTextConfirmPassword,
            REGEX_MIN_AND_MAX_LENGTH_PASSWORD,
            getString(R.string.text_min_and_max_password)
        )

        awesomeValidation.addValidation(
            binding.editTextConfirmPassword,
            binding.editTextPassword,
            getString(R.string.text_two_password_invalid)
        )
    }

    fun validateAndSave(): Boolean {
        if (awesomeValidation.validate()) {
            val person = presenter.getPresentationPerson()

            person.password = binding.editTextPassword.text.toString()

            presenter.setPresentationPerson(person)

            return true
        }

        return false
    }
}
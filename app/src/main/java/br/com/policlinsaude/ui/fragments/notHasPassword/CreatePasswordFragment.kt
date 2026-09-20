package br.com.policlinsaude.ui.fragments.notHasPassword

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentCreatePasswordBinding
import br.com.policlinsaude.ui.activities.notHasPassword.NotHasPasswordActivity
import br.com.policlinsaude.ui.views.BaseFragment
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import br.com.policlinsaude.util.extensions.toBase64
import br.com.policlinsaude.util.helpers.PhotoPickerHelper
import java.io.File

/**
 * Passo 3 do cadastro (não tem senha): Criar Senha + foto.
 *
 * Mantém o papel do legado `CreatePasswordFragment` (foto via
 * [PhotoPickerHelper], senha + confirmação) lendo/salvando no ViewModel.
 */
class CreatePasswordFragment : BaseFragment() {

    companion object {
        private const val REGEX_MIN_AND_MAX_LENGTH_PASSWORD = "^[\\s\\S]{8,32}$"

        fun newInstance() = CreatePasswordFragment()
    }

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
                (activity as? NotHasPasswordActivity)?.showToastMessage(it)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFields()
        setClickListener()
    }

    private fun handleImage(file: File, bitmap: Bitmap) {
        try {
            val image = file.toBase64(500, 0)
            (activity as NotHasPasswordActivity).viewModel.onImagePicked(image)

            if (image?.isNotEmpty() == true) {
                binding.imageView.setImageBitmap(image.getBitmapFromImage())
            }
        } catch (e: Exception) {
            (activity as? NotHasPasswordActivity)?.showToastMessage(
                getString(R.string.text_image_pick_error)
            )
        }
    }

    private fun setClickListener() {
        binding.imageView.setOnClickListener {
            photoPickerHelper.open(PhotoPickerHelper.Mode.CAMERA_AND_GALLERY)
        }
    }

    private fun setFields() {
        val person = (activity as NotHasPasswordActivity).viewModel.getPresentationPerson()
        binding.editTextPassword.setText(person.password)
        binding.editTextConfirmPassword.setText(person.password)
    }

    /** Valida os campos e salva a senha no ViewModel. Retorna true se válido. */
    fun validateAndSave(): Boolean {
        if (validateFields()) {
            val activity = activity as NotHasPasswordActivity
            val person = activity.viewModel.getPresentationPerson()
            activity.viewModel.setPresentationPerson(
                person.copy(password = binding.editTextPassword.text.toString())
            )
            return true
        }
        return false
    }

    private fun validateFields(): Boolean {
        var valid = true
        binding.inputLayoutPassword.error = null
        binding.inputLayoutConfirmPassword.error = null

        val password = binding.editTextPassword.text.toString()
        val confirmPassword = binding.editTextConfirmPassword.text.toString()

        if (password.isBlank()) {
            binding.inputLayoutPassword.error = getString(R.string.text_field_required)
            valid = false
        }

        if (confirmPassword.isBlank()) {
            binding.inputLayoutConfirmPassword.error = getString(R.string.text_field_required)
            valid = false
        } else if (!confirmPassword.matches(Regex(REGEX_MIN_AND_MAX_LENGTH_PASSWORD))) {
            binding.inputLayoutConfirmPassword.error = getString(R.string.text_min_and_max_password)
            valid = false
        } else if (confirmPassword != password) {
            binding.inputLayoutConfirmPassword.error = getString(R.string.text_two_password_invalid)
            valid = false
        }

        return valid
    }
}
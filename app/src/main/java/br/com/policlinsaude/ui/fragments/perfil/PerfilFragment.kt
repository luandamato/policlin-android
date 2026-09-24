package br.com.policlinsaude.ui.fragments.perfil

import android.content.Intent
import android.graphics.Bitmap
import android.media.ExifInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentPerfilBinding
import br.com.policlinsaude.data.models.PresentationPerson
import br.com.policlinsaude.ui.activities.deleteUser.DeleteUserActivity
import br.com.policlinsaude.ui.activities.editPassword.EditPasswordActivity
import br.com.policlinsaude.ui.activities.editPhone.EditPhoneActivity
import br.com.policlinsaude.ui.activities.home.MenuActivity
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import br.com.policlinsaude.ui.views.BaseActivity
import br.com.policlinsaude.util.extensions.DateHelper
import br.com.policlinsaude.util.extensions.toBase64
import br.com.policlinsaude.util.helpers.PhotoPickerHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import br.com.policlinsaude.util.helpers.Mask
import java.io.File

/**
 * Fragment de Perfil migrado para MVVM com Koin.
 *
 * Renderiza os dados cadastrais do beneficiário e permite a alteração do avatar,
 * além de direcionar para exclusão de conta ou telas em construção.
 */
class PerfilFragment : Fragment() {

    private val viewModel: PerfilViewModel by viewModel()
    private lateinit var photoPickerHelper: PhotoPickerHelper

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)

        val toolbar = binding.toolbar.toolbar
        (activity as MenuActivity).setupFragmentToolbar(toolbar, R.string.title_perfil)

        photoPickerHelper = PhotoPickerHelper(
            fragment = this,
            onImageSelected = { bitmap, file ->
                handleImage(file, bitmap)
            },
            onError = {
                viewModel.onImagePickError()
            }
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            (activity as? BaseActivity)?.onOptionsItemSelected(item)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPerfil()
    }

    private fun setupObservers() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingView.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.person.observe(viewLifecycleOwner) { person ->
            setProfileData(person)
        }

        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is PerfilEvent.ShowError -> {
                    (activity as? BaseActivity)?.showError(message = event.message)
                }
                is PerfilEvent.ShowImagePickError -> {
                    (activity as? BaseActivity)?.showToast(R.string.text_image_pick_error)
                }
                is PerfilEvent.AvatarChangedSuccess -> {
                    (activity as? BaseActivity)?.showToast(R.string.text_avatar_change_success)
                }
            }
        }
    }

    private fun handleImage(file: File, bitmap: Bitmap) {
        try {
            val image = file.toBase64(500, applyRotationIfNeeded(file))
            if (image != null) {
                viewModel.updateAvatar(image)
            } else {
                viewModel.onImagePickError()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            viewModel.onImagePickError()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 123) {
            val returnIntent = Intent()
            activity?.setResult(AppCompatActivity.RESULT_OK, returnIntent)
            activity?.finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setProfileData(person: PresentationPerson) {
        try {
            if (person.photo.isNotEmpty()) {
                binding.imageView.setImageBitmap(person.photo.getBitmapFromImage())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.nameTextView.text = person.name
        binding.textViewWelcome.text = person.name
        binding.emailTextView.text = person.email
        binding.phoneTextView.text = Mask.applyToString(Mask.PHONE, person.phone)
        binding.cpfTextView.text = Mask.applyToString(Mask.CPF, person.cpf)
        binding.registerAndOrderTextView.text = "${person.plan.register.toIntOrNull() ?: 0} - ${person.plan.order.toIntOrNull() ?: 0}"
        binding.birthdayTextView.text = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_DDMMYYYY, person.birthday)
    }

    private fun setClickListeners() {
        binding.phoneContainer.setOnClickListener {
            EditPhoneActivity.start(requireContext())
        }
        binding.passwordContainer.setOnClickListener {
            EditPasswordActivity.start(requireContext())
        }
        binding.imageView.setOnClickListener {
            photoPickerHelper.open(PhotoPickerHelper.Mode.CAMERA_AND_GALLERY)
        }
        binding.deleteAccountContainer.setOnClickListener {
            DeleteUserActivity.start(requireContext())
        }
    }

    private fun applyRotationIfNeeded(imageFile: File): Int {
        return try {
            val exif = ExifInterface(imageFile.absolutePath)
            val exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            when (exifRotation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }
}

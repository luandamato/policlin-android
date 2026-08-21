package br.com.policlinsaude.perfil.view

import android.content.Intent
import android.media.ExifInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.base.BaseFragmentWithInject
import br.com.policlinsaude.core.helper.DateHelper
import br.com.policlinsaude.core.helper.MaskUtils
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.core.helper.toBase64
import br.com.policlinsaude.databinding.FragmentPerfilBinding
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.home.view.MenuActivity
import br.com.policlinsaude.model.PresentationPerson
import br.com.policlinsaude.perfil.presenter.PerfilPresenter
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject

class PerfilFragment : BaseFragmentWithInject(), PerfilView {

    companion object {
        fun newInstance(): PerfilFragment {
            return PerfilFragment()
        }
    }

    @Inject
    lateinit var presenter: PerfilPresenter

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)

        val toolbar: Toolbar = binding.root.findViewById(R.id.toolbar)
        (activity as MenuActivity).setupFragmentToolbar(toolbar, R.string.title_perfil)

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
    }

    override fun onResume() {
        super.onResume()
        presenter.getPerfil()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 123) {
            val returnIntent = Intent()
            activity?.setResult(AppCompatActivity.RESULT_OK, returnIntent)
            activity?.finish()
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : EasyImage.Callbacks {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                imageFile?.let {
                    val rotation = applyRotationIfNeeded(it)
                    presenter.onImagePicked(it.toBase64(500, rotation))
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

    override fun set(person: PresentationPerson) {
        try {
            binding.imageView.setImageBitmap(person.photo.getBitmapFromImage())
        } catch (e: Exception) {
            e.printStackTrace()
            // ignored
        }
        binding.nameTextView.text = person.name
        binding.emailTextView.text = person.email
        binding.phoneTextView.text = MaskUtils.applyMaskToString(MaskUtils.PHONE, person.phone)
        binding.cpfTextView.text = MaskUtils.applyMaskToString(MaskUtils.CPF, person.cpf)
        binding.registerAndOrderTextView.text = getString(
            R.string.text_register_and_order_number,
            person.plan.register.toInt(),
            person.plan.order.toInt()
        )
        binding.birthdayTextView.text = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_DDMMYYYY, person.birthday)
        (activity as MenuActivity).getCurrentPerson()
    }

    override fun showImagePickError() {
        (activity as BaseActivity).showToast(R.string.text_image_pick_error)
    }

    override fun showLoading() {
        binding.loadingView.root.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingView.root.visibility = View.GONE
    }

    override fun showAvatarChangedSuccess() {
        (activity as BaseActivity).showToast(R.string.text_avatar_change_success)
    }

    override fun showDialogError(throwable: Throwable) {
        (activity as BaseActivity).showError(
            message = if (throwable is MessageErrorException) throwable.message!! else getString(
                R.string.msg_unexpected_error
            )
        )
    }

    private fun setClickListeners() {
        binding.phoneContainer.setOnClickListener {
            presenter.onEditPhoneClicked()
        }
        binding.passwordContainer.setOnClickListener {
            presenter.onEditPasswordClicked()
        }
        binding.imageView.setOnClickListener {
            presenter.onAvatarChangeClicked()
        }
        binding.deleteAccountContainer.setOnClickListener {
            presenter.onDeleteClicked()
        }
    }

    private fun applyRotationIfNeeded(imageFile: File): Int {
        val exif = ExifInterface(imageFile.absolutePath)
        val exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        return when (exifRotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }
}

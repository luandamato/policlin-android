package br.com.policlinsaude.ui.activity_healthInsurancePhoto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityHealthInsurancePhotoBinding
import br.com.policlinsaude.domain.models.HealthInsurancePhotoList
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.views.BaseActivity
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import br.com.policlinsaude.util.extensions.rotate
import br.com.policlinsaude.util.helpers.ConnectivityHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Carteirinha virtual (frente/verso).
 *
 * Migrado do legado `HealthInsurancePhotoActivity` (MVP → MVVM):
 * UI → HealthInsurancePhotoViewModel → AppRepository.onGetHealthInsurancePhoto.
 *
 * Frente em ViewPager2 (fotos) + indicador de pontos; verso em imagem única.
 */
class HealthInsurancePhotoActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, HealthInsurancePhotoActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityHealthInsurancePhotoBinding

    private val viewModel: HealthInsurancePhotoViewModel by viewModel()

    private lateinit var adapter: PhotoPageAdapter

    private var isImageVerso: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthInsurancePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)

        adapter = PhotoPageAdapter(this)
        binding.viewPagerPhoto.adapter = adapter

        observeViewModel()
        viewModel.getImage(isOnline = ConnectivityHelper.isOnline(this))
    }

    // =====================================================================
    // Observers (estado/eventos do ViewModel)
    // =====================================================================
    private fun observeViewModel() {
        viewModel.loading.observe(this) { loading ->
            binding.bannerProgressPhoto.isVisible = loading
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is HealthInsurancePhotoEvent.ShowImage -> showImage(event.photoList)
                is HealthInsurancePhotoEvent.ShowError -> showDialogError(event.message)
                is HealthInsurancePhotoEvent.ShowWithoutNetwork -> showWithoutNetworkDialog(event.photoList)
            }
        }
    }

    // =====================================================================
    // Rendering da carteirinha
    // =====================================================================
    private fun showImage(photoList: HealthInsurancePhotoList) {
        val photos = photoList.listaimgFrente.orEmpty()
        adapter.setPhotos(photos)
        updateBannerIndicator(photos.size)
        photoVerso = photoList.imgVerso
    }

    private fun showImageVerse(photo: String?) {
        if (!photo.isNullOrEmpty()) {
            binding.imageViewVerso.setImageBitmap(
                photo.getBitmapFromImage().rotate(90f)
            )
        }
    }

    private fun updateBannerIndicator(count: Int) {
        binding.pageIndicatorViewPhoto.removeAllViews()
        for (i in 0 until count) {
            val lp = android.widget.LinearLayout.LayoutParams(12, 12)
            lp.marginStart = 4
            lp.marginEnd = 4
            val dot = View(this).apply {
                layoutParams = lp
                setBackgroundColor(ContextCompat.getColor(this@HealthInsurancePhotoActivity, R.color.colorPrimary))
            }
            binding.pageIndicatorViewPhoto.addView(dot)
        }
    }

// =====================================================================
    // Menu (verso)
    // =====================================================================
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_health_insurance_photo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_verso -> {
                if (!isImageVerso) {
                    binding.bannersContainerPhoto.visibility = View.GONE
                    binding.imageViewVerso.visibility = View.VISIBLE
                    showImageVerse(photoVerso)
                    isImageVerso = true
                } else {
                    binding.bannersContainerPhoto.visibility = View.VISIBLE
                    binding.imageViewVerso.visibility = View.GONE
                    isImageVerso = false
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // =====================================================================
    // Dialogs de erro / sem rede
    // =====================================================================
    private fun showDialogError(message: String) {
        showDialogTryAgain(
            listenerPositiveButton = { viewModel.getImage(isOnline = ConnectivityHelper.isOnline(this)) },
            message = message
        )
    }

    private fun showWithoutNetworkDialog(photoList: HealthInsurancePhotoList?) {
        if (photoList != null && !photoList.listaimgFrente.isNullOrEmpty()) {
            showImage(photoList)
            DialogHelper.showDialog(
                this,
                getString(R.string.title_advise),
                getString(R.string.text_no_internet_short),
                getString(R.string.text_ok)
            )
        } else {
            DialogHelper.showDialog(
                this,
                getString(R.string.title_no_internet_connection),
                getString(R.string.text_no_internet_no_photo),
                getString(R.string.text_ok)
            )
        }
    }
}
    private var photoVerso: String? = null
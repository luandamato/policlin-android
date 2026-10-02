package br.com.policlinsaude.ui.activities.unitDetail

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.JsonMedicalGuidePlanResponse
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.databinding.ActivityUnitDetailBinding
import br.com.policlinsaude.ui.activities.login.LoginActivity
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.views.BaseActivity
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import br.com.policlinsaude.util.helpers.ConnectivityHelper
import br.com.policlinsaude.util.helpers.LocationHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Detalhe da unidade.
 *
 * Migrado de `_legacy/.../medicalGuideDetails/view/MedicalGuideDetailsActivity.kt`.
 * Pantalla compartida para:
 * - caller "Units" (lista de unidades): sem favorito/planos (comportamento atual).
 * - caller "OwnNetwork" (rede própria): favoritos e planos habilitados
 *   (mesmo comportamento do legado para caller != "Units").
 *
 * Fluxo: UI + navegación de intents direto na UI (sem Navigator/Presenter).
 */
class UnitDetailActivity : BaseActivity() {

    companion object {
        private const val EXTRA_ESTABLISHMENT = "extra_establishment"
        private const val EXTRA_CALLER = "extra_caller"
        private const val CALLER_UNITS = "Units"

        fun start(activity: Activity, establishment: PresentationEstablishment, caller: String = CALLER_UNITS) {
            val intent = Intent(activity, UnitDetailActivity::class.java)
            intent.putExtra(EXTRA_ESTABLISHMENT, Gson().toJson(establishment))
            intent.putExtra(EXTRA_CALLER, caller)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityUnitDetailBinding

    private val viewModel: UnitDetailViewModel by viewModel()

    private lateinit var establishment: PresentationEstablishment

    private val adapter by lazy {
        UnitDetailAdapter(
            onClickListenerWpp = { phone -> openWhatsApp(phone) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnitDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbar)

        val json = intent.getStringExtra(EXTRA_ESTABLISHMENT)
        establishment = viewModel.parseEstablishment(json) ?: run {
            finish()
            return
        }

        val caller = intent.getStringExtra(EXTRA_CALLER) ?: CALLER_UNITS

        // No modo "Units" não há favorito nem planos (mesmo comportamento do legado).
        if (caller == CALLER_UNITS) {
            binding.imageButtonFavorite.visibility = View.GONE
            binding.plansTextView.visibility = View.GONE
        } else {
            setupFavoriteButton(establishment)
            setupPlansButton(establishment)
        }

        observeViewModel()
        renderEstablishment(establishment, caller)
    }

    // =====================================================================
    // Favoritos / Planos (modo Rede Propia)
    // =====================================================================
    private fun setupFavoriteButton(e: PresentationEstablishment) {
        setFavorited(e.favorited)
        binding.imageButtonFavorite.setOnClickListener {
            if (ConnectivityHelper.isOnline(this)) {
                viewModel.toggleFavorite(e)
            } else {
                showWithoutNetworkDialog()
            }
        }
    }

    private fun setFavorited(favorited: Boolean) {
        binding.imageButtonFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (favorited) R.drawable.ic_favorite_full else R.drawable.ic_favorite
            )
        )
    }

    private fun setupPlansButton(e: PresentationEstablishment) {
        binding.plansTextView.setOnClickListener {
            viewModel.loadPlans(e)
        }
    }

    // =====================================================================
    // Render
    // =====================================================================
    private fun renderEstablishment(e: PresentationEstablishment, caller: String) {
        binding.recyclerView.apply {
            adapter = this@UnitDetailActivity.adapter
            layoutManager = LinearLayoutManager(this@UnitDetailActivity)
            addItemDecoration(
                DividerItemDecoration(this@UnitDetailActivity, DividerItemDecoration.VERTICAL)
            )
        }

        loadFrontImage(e)

        binding.titleTextView.text = e.title
        adapter.setEstablishment(this, e, caller)

        setupPhoneButton(e)
        setupWhatsAppButton(e)
        binding.imageButtonMap.setOnClickListener { openMap(e) }
        binding.imageButtonShare.setOnClickListener { openShare(e) }
    }

    private fun loadFrontImage(e: PresentationEstablishment) {
        val hasImage = !e.photoFront.isNullOrEmpty()
        if (hasImage) {
            try {
                binding.imageViewFront.setImageBitmap(e.photoFront.getBitmapFromImage())
                binding.imageViewFront.scaleType = ImageView.ScaleType.CENTER_CROP
            } catch (ex: Exception) {
                loadStaticMap(e)
            }
        } else if (e.latitude.isNotEmpty() && e.longitude.isNotEmpty()) {
            loadStaticMap(e)
        }
    }

    private fun loadStaticMap(e: PresentationEstablishment) {
        try {
            Glide.with(this)
                .load(
                    LocationHelper.provideStaticMapUrl(
                        e.latitude.replace(",", ".").toDouble(),
                        e.longitude.replace(",", ".").toDouble()
                    )
                )
                .apply(RequestOptions().centerCrop().placeholder(R.drawable.logo))
                .into(binding.imageViewFront)
            binding.imageViewFront.setOnClickListener { openMap(e) }
        } catch (ex: Exception) {
            // mantém placeholder
        }
    }
// =====================================================================
    // Observers (estado/eventos do ViewModel)
    // =====================================================================
    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            binding.loadingContainer.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is UnitDetailEvent.FavoriteChanged -> {
                    setFavorited(event.favorited)
                    showToast(
                        if (event.favorited) R.string.text_favorite_success
                        else R.string.text_remove_favorite_success
                    )
                }
                is UnitDetailEvent.ShowError -> showError(event.message)
                UnitDetailEvent.ShowLogin -> showLoginDialog()
                is UnitDetailEvent.ShowPlans -> showPlansDialog(event.plans)
                UnitDetailEvent.ShowEmptyPlans -> showEmptyPlansDialog()
            }
        }
    }

    // =====================================================================
    // Ações (telefone / WhatsApp / mapa / compartir)
    // =====================================================================
    private fun setupPhoneButton(e: PresentationEstablishment) {
        binding.imageButtonPhone.setOnClickListener {
            if (e.phoneTwo.isNotEmpty()) {
                showSelectPhones(e.phoneOne, e.phoneTwo)
            } else {
                openCall(e.phoneOne)
            }
        }
    }

    private fun setupWhatsAppButton(e: PresentationEstablishment) {
        when {
            e.typePhoneOne == "2" || e.typePhoneTwo == "2" -> {
                binding.imageButtonWhatsApp.visibility = View.VISIBLE
                binding.imageButtonWhatsApp.setOnClickListener {
                    if (e.typePhoneOne == "2" && e.typePhoneTwo == "2") {
                        showSelectWhats(e.phoneOne, e.phoneTwo)
                    } else {
                        openWhatsApp(if (e.typePhoneOne == "2") e.phoneOne else e.phoneTwo)
                    }
                }
            }
            else -> binding.imageButtonWhatsApp.visibility = View.GONE
        }
    }

    private fun showSelectPhones(phoneOne: String, phoneTwo: String) {
        val phones = arrayOf(phoneOne, phoneTwo)
        AlertDialog.Builder(this)
            .setItems(phones) { _, index -> openCall(phones[index]) }
            .create()
            .show()
    }

    private fun showSelectWhats(phoneOne: String, phoneTwo: String) {
        val phones = arrayOf(phoneOne, phoneTwo)
        AlertDialog.Builder(this)
            .setItems(phones) { _, index -> openWhatsApp(phones[index]) }
            .create()
            .show()
    }

    private fun openCall(phone: String) {
        if (phone.isNotEmpty()) {
            startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0$phone")))
        }
    }

    private fun openWhatsApp(phone: String) {
        try {
            val clean = phone.replace(" ", "")
                .replace("(", "")
                .replace(")", "")
                .replace("-", "")
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=55$clean")
                )
            )
        } catch (e: Exception) {
            // sem app de WhatsApp
        }
    }

    private fun openMap(e: PresentationEstablishment) {
        val value = "geo:0,0?q=${e.latitude.replace(",", ".")},${e.longitude.replace(",", ".")}(${e.name})"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(value)))
    }

    private fun openShare(e: PresentationEstablishment) {
        var textToShare = "${e.title}\n\n${e.phoneOne}\n"
        if (e.phoneTwo.isNotEmpty()) {
            textToShare += "${e.phoneTwo}\n"
        }
        textToShare += "\n${e.getFullAddress()}"
        if (e.latitude.isNotEmpty() && e.longitude.isNotEmpty()) {
            textToShare += "\nhttps://www.google.com/maps?q=${e.latitude.replace(",", ".")},${e.longitude.replace(",", ".")}"
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, textToShare)
        }
        startActivity(intent)
    }
// =====================================================================
    // Diálogos / navegación
    // =====================================================================
    private fun showPlansDialog(plans: List<JsonMedicalGuidePlanResponse>) {
        val descriptions = plans.map { it.description.orEmpty() }
        val builder = AlertDialog.Builder(this)
            .setAdapter(
                object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, descriptions.toTypedArray()) {},
                null
            )
        builder.setPositiveButton(R.string.text_ok, null)
        val dialog = builder.create()
        dialog.listView.divider = ColorDrawable(ContextCompat.getColor(this, R.color.divider))
        dialog.listView.dividerHeight = 1
        dialog.show()
    }

    private fun showEmptyPlansDialog() {
        DialogHelper.showDialog(
            this,
            getString(R.string.title_error_oops),
            getString(R.string.text_nothing_to_show),
            getString(R.string.text_ok),
            null
        )
    }

    private fun showLoginDialog() {
        DialogHelper.showDialog(
            this,
            getString(R.string.title_login),
            getString(R.string.text_login),
            getString(R.string.global_yes),
            getString(R.string.action_cancel),
            listenerPositiveButton = { navigateToLogin() }
        )
    }

    private fun showWithoutNetworkDialog() {
        DialogHelper.showDialog(
            this,
            getString(R.string.title_no_internet_connection),
            getString(R.string.text_no_internet),
            getString(R.string.text_ok),
            null
        )
    }

    private fun navigateToLogin() {
        LoginActivity.start(this)
    }
}
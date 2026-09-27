package br.com.policlinsaude.ui.activities.unitDetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.databinding.ActivityUnitDetailBinding
import br.com.policlinsaude.ui.views.BaseActivity
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import br.com.policlinsaude.util.helpers.LocationHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Detalhe da unidade (exibido a partir da lista de unidades).
 *
 * Migrado de `_legacy/.../medicalGuideDetails/view/MedicalGuideDetailsActivity.kt`
 * restrito ao caso "Units" (sem favorito/planos): mostra imagem/frente ou mapa,
 * telefones (com WhatsApp quando tipo = 2), mapa e compartilhar.
 *
 * Fluxo: UI + navegação de intents direto na UI (sem Navigator).
 */
class UnitDetailActivity : BaseActivity() {

    companion object {
        private const val EXTRA_ESTABLISHMENT = "extra_establishment"

        fun start(activity: Activity, establishment: PresentationEstablishment) {
            val intent = Intent(activity, UnitDetailActivity::class.java)
            intent.putExtra(EXTRA_ESTABLISHMENT, Gson().toJson(establishment))
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

        // No modo "Units" não há favorito nem planos.
        binding.imageButtonFavorite.visibility = View.GONE
        binding.plansTextView.visibility = View.GONE

        val json = intent.getStringExtra(EXTRA_ESTABLISHMENT)
        establishment = viewModel.parseEstablishment(json) ?: run {
            finish()
            return
        }
        renderEstablishment(establishment)
    }

    // =====================================================================
    // Render
    // =====================================================================
    private fun renderEstablishment(e: PresentationEstablishment) {
        binding.recyclerView.apply {
            adapter = this@UnitDetailActivity.adapter
            layoutManager = LinearLayoutManager(this@UnitDetailActivity)
            addItemDecoration(
                DividerItemDecoration(this@UnitDetailActivity, DividerItemDecoration.VERTICAL)
            )
        }

        loadFrontImage(e)

        binding.titleTextView.text = e.title
        adapter.setEstablishment(this, e)

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
    // Ações (telefone / WhatsApp / mapa / compartilhar)
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
}
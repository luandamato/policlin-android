package br.com.policlinsaude.medicalGuideDetails.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.os.Parcel
import android.os.Parcelable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.domain.model.MedicalGuidePlan
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.core.helper.LocationHelper
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.medicalGuideDetails.presenter.FavoritesStorePrefsPresenter
import br.com.policlinsaude.medicalGuideDetails.presenter.MedicalGuideDetailsPresenter
import br.com.policlinsaude.medicalGuideDetails.view.adapter.MedicalGuideDetailsAdapter
import br.com.policlinsaude.model.PresentationEstablishment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_medical_guide_details.*
import kotlinx.android.synthetic.main.list_item_medical_guide_details.*
import javax.inject.Inject


class MedicalGuideDetailsActivity : BaseActivity(), MedicalGuideDetailsView {


    companion object {
        private const val EXTRA_ESTABLISHMENT = "extra_establishment"
        private const val EXTRA_CALLER_ACTIVITY = "extra_caller_activity"


        fun start(activity: Activity, establishment: PresentationEstablishment, caller: String) {
            val intent = Intent(activity, MedicalGuideDetailsActivity::class.java)
            Log.d("FAVORITOS", "TESTEEEE cidade: " + establishment.city + " Favoritado: " + establishment.favorited)
            intent.putExtra(EXTRA_ESTABLISHMENT,establishment)
            intent.putExtra(EXTRA_CALLER_ACTIVITY, caller)
            activity.startActivity(intent)
        }

        private const val  myPreferences = "myPrefs"
        private const val FAVORITES = "favoritesPref"
    }

    @Inject
    lateinit var presenter: MedicalGuideDetailsPresenter
    @Inject
    lateinit var adapter: MedicalGuideDetailsAdapter
    @Inject
    lateinit var storeFavorites: FavoritesStorePrefsPresenter

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_guide_details)
        AndroidInjection.inject(this)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

       execGetFavorites()

        setupToolbar()
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.onClickListenerWpp = {
            presenter.onWhatsClicked(it)
        }
        getEstablishment()
        setupOnClickListeners()
    }


    private fun setupOnClickListeners() {
        imageButtonFavorite.setOnClickListener {
            presenter.onFavoriteClicked(this)
        }
        imageButtonPhone.setOnClickListener {
            presenter.onPhoneClicked()
        }
        imageButtonMap.setOnClickListener {
            presenter.onMapClicked()
        }
        imageButtonShare.setOnClickListener {
            presenter.onShareClicked()
        }
        plansTextView.setOnClickListener {
           presenter.onPlansClicked()
        }
    }

    private fun openWhatsApp() {
        presenter.onWhatsClicked()
    }

    private fun getEstablishment() {
        val x = intent.getParcelableExtra(EXTRA_ESTABLISHMENT)?: PresentationEstablishment()
        presenter.setEstablishment(x)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showMedicalGuideDetails(presentationEstablishment: PresentationEstablishment) {
        try {
            imageViewFront.setImageBitmap(presentationEstablishment.photoFront.getBitmapFromImage())
            //imageViewFront.scaleType = ImageView.ScaleType.FIT_XY
            imageViewFront.scaleType = ImageView.ScaleType.CENTER_CROP


        } catch (exception: Exception) {
            if (presentationEstablishment.latitude.isEmpty() || presentationEstablishment.longitude.isEmpty()) {
                // do nothing
            } else {
                Glide.with(this)
                        .load(LocationHelper.provideStaticMapUrl(
                                presentationEstablishment.latitude.replace(",", ".").toDouble(),
                                presentationEstablishment.longitude.replace(",", ".").toDouble()))
                        .apply(RequestOptions().centerCrop().placeholder(R.drawable.logo))
                        .into(imageViewFront)
                imageViewFront.setOnClickListener {
                    presenter.onMapClicked()
                }
            }
        }
        val caller = intent.getStringExtra(EXTRA_CALLER_ACTIVITY)

        when {
            presentationEstablishment.typePhoneOne == "2" -> imageButtonWhatsApp.setOnClickListener { openWhatsApp() }
            presentationEstablishment.typePhoneTwo == "2" -> imageButtonWhatsApp.setOnClickListener { openWhatsApp() }
            else -> imageButtonWhatsApp.visibility = View.GONE
        }

        if (caller == "Units"){
            imageButtonFavorite.visibility = View.GONE
            plansTextView.visibility = View.GONE
//            textViewTitle.visibility = View.GONE
          //  textViewInfo.visibility = View.GONE

        }



        title_text_view.text = presentationEstablishment.title
        adapter.setPresentationEstablishment(this, presentationEstablishment, caller.orEmpty())
        adapter.notifyDataSetChanged()
    }

    override fun showDialogError(it: Throwable) {
        val listener = { getEstablishment() }
        showDialogTryAgain(listenerPositiveButton = listener,
                message = if (it is MessageErrorException) it.message!! else "")
    }

    override fun showPlansDialog(plans: List<MedicalGuidePlan>?) {
        val builder = AlertDialog.Builder(this)
                .setAdapter(object : ArrayAdapter<MedicalGuidePlan>(this, android.R.layout.simple_list_item_1, plans ?: arrayListOf()) {}, null)
        builder.setPositiveButton(R.string.text_ok, null)
        val dialog = builder.create()
        dialog.listView.divider = ColorDrawable(ContextCompat.getColor(this, R.color.divider))
        dialog.listView.dividerHeight = 1
        dialog.show()
    }

    override fun showPlansLoading() {
        loading_container.visibility = View.VISIBLE
    }

    override fun hidePlansLoading() {
        loading_container.visibility = View.GONE
    }

    override fun showEmptyPlansDialog() {
        DialogHelper.showDialog(this, getString(R.string.title_error_oops),
                getString(R.string.text_nothing_to_show),
                getString(R.string.text_ok),
                null)
    }

    override fun showLoading() {
        loading_container.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading_container.visibility = View.GONE
    }

    override fun showFavoriteSuccessMessage() {
        showToast(R.string.text_favorite_success)
    }

    override fun showLoginDialog() {
        DialogHelper.showDialog(this,
                R.string.title_login,
                R.string.text_login,
                R.string.global_yes,
                R.string.action_cancel,
                { presenter.onLoginClicked() })
    }

    override fun setFavorited(favorited: Boolean) {
        imageButtonFavorite.setImageDrawable(ContextCompat.getDrawable(this, if (favorited) R.drawable.ic_favorite_full else R.drawable.ic_favorite))
    }

    override fun showRemoveFavoriteSuccessMessage() {
        showToast(R.string.text_remove_favorite_success)
    }

    override fun showSelectPhones(phoneOne: String, phoneTwo: String) {
        val phones = arrayOf(phoneOne, phoneTwo)
        val builder = AlertDialog.Builder(this)
        builder.setItems(phones) { _, index ->
            presenter.onPhoneSelected(phones[index])
        }
        builder.create().show()
    }

    override fun showSelectWhats(phoneOne: String, phoneTwo: String) {
        val phones = arrayOf(phoneOne, phoneTwo)
        val builder = AlertDialog.Builder(this)
        builder.setItems(phones) { _, index ->
            presenter.onWhatsSelected(phones[index])
        }
        builder.create().show()
    }

    override fun showWithoutNetworkDialog() {
        DialogHelper.showDialog(this, getString(R.string.title_no_internet_connection),
                getString(R.string.text_no_internet),
                getString(R.string.text_ok),
                null)
    }
    override fun execGetFavorites() {
        storeFavorites.getFavorites()

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun saveFavoritesInPrefs(favorites: List<PresentationEstablishment>) {
        val gsonFavorites = Gson()

        val strJsonFavoritses: String = gsonFavorites.toJson(favorites)

        val editor = sharedPreferences.edit()

        editor.putString(FAVORITES, strJsonFavoritses)
        editor.apply()
    }

}
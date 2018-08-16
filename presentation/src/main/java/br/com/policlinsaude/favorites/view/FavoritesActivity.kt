package br.com.policlinsaude.favorites.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import br.com.domain.exception.MessageErrorException
import br.com.domain.model.HealthInsurancePhoto
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.favorites.adapter.FavoritesAdapter
import br.com.policlinsaude.favorites.presenter.FavoritesPresenter
import br.com.policlinsaude.model.PresentationEstablishment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_favorites.*
import javax.inject.Inject


class FavoritesActivity : BaseActivity(), FavoritesView, FavoritesAdapter.OnItemClickListener {

    companion object {

        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, FavoritesActivity::class.java))
        }
    }

    @Inject
    lateinit var presenter: FavoritesPresenter

    private lateinit var adapter: FavoritesAdapter

    private lateinit var sharedPreferences: SharedPreferences

    private var myPreferences = "myPrefs"
    private var FAVORITES = "favoritesPref"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

        setupToolbar()

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        presenter.getFavorites(this)
    }

    private fun setupRecyclerView() {
        // todo fix favorites
        adapter = FavoritesAdapter(this)
        favoritesRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        favoritesRecyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        favoritesRecyclerView.layoutManager = layoutManager
    }

    override fun onItemClick(establishmentIndex: Int) {
        presenter.onEstablishmentClicked(establishmentIndex)
    }

    override fun renderEstablishments(establishments: List<PresentationEstablishment>) {
        adapter.setEstablishemtns(ArrayList(establishments))
    }

    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    override fun showDialogError(throwable: Throwable, tryAgainAction: (() -> Unit)?) {
        val message = if (throwable is MessageErrorException) throwable.message!! else ""
        if (tryAgainAction == null) {
            showError(message)
        } else {
            showDialogTryAgain(tryAgainAction, message)
        }
    }


    override fun showWithoutNetworkDialog() {
        DialogHelper.showDialog(this, getString(R.string.title_no_internet_connection),
                getString(R.string.text_no_internet_favorites),
                getString(R.string.text_ok),
                null)

  /*      var favoritesList: List<PresentationEstablishment>

        var gsonFavoritesRetorno = Gson()

        var strJsonRetorno: String = sharedPreferences.getString(FAVORITES, null)

        favoritesList  = gsonFavoritesRetorno.fromJson(strJsonRetorno,object: TypeToken<MutableList<PresentationEstablishment>>(){}.type)

        Log.d("FAVORITOS","NUMERO DE FAVORITOS : " + favoritesList.size)
        renderEstablishments(favoritesList)*/

    }

    override fun createFavoritesList(): List<PresentationEstablishment> {

        var favoritesList: List<PresentationEstablishment>

        var gsonFavoritesRetorno = Gson()


        try {
            var strJsonRetorno: String = sharedPreferences.getString(FAVORITES, null)

            favoritesList  = gsonFavoritesRetorno.fromJson(strJsonRetorno,object: TypeToken<MutableList<PresentationEstablishment>>(){}.type)

            Log.d("FAVORITOS","NUMERO DE FAVORITOS : " + favoritesList.size)
            renderEstablishments(favoritesList)

            return favoritesList

        } catch (e: Exception) {
            return emptyList()
        }

    }
    override fun saveFavoritesInPrefs(favorites: List<PresentationEstablishment>) {

         var gsonFavorites = Gson()

        var strJsonFavoritses: String = gsonFavorites.toJson(favorites)

        val editor = sharedPreferences.edit()

        editor.putString(FAVORITES, strJsonFavoritses)
        editor.apply()

        Log.d("FAVORITOS", "Salvando em PREFERENCES")

    }

}

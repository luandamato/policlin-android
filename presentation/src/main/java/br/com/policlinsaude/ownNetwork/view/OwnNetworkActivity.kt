package br.com.policlinsaude.ownNetwork.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.domain.exception.MessageErrorException
import br.com.domain.model.Qualification
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.ownNetwork.presenter.OwnNetworkPresenter
import br.com.policlinsaude.ownNetwork.view.adapter.OwnNetworkPageAdapter
import kotlinx.android.synthetic.main.activity_own_network.*
import javax.inject.Inject

class OwnNetworkActivity : BaseActivity(), OwnNetworkView {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, OwnNetworkActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: OwnNetworkPresenter

    @Inject
    lateinit var adapter: OwnNetworkPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_own_network)

        setupToolbar()

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
       // getOwnNetworks()
    }

    override fun onResume() {
        super.onResume()
        Log.d("FILTRO", "DENTRO DO ONRESUME")
     //   setupToolbar()

    //    viewPager.adapter = adapter
    //    tabs.setupWithViewPager(viewPager)

        getOwnNetworks()


    }

    private fun getOwnNetworks() {
        presenter.getOwnNetworks()
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle?) {
        // do nothing
        // this is because the amount of data in the pagers

        Log.d("FILTRO", "DENTRO do onSaveInstanceState de OwnNetWorkActivity")
    }

    override fun showOwnNetworks(ownNetworks: List<Pair<String, List<PresentationEstablishment>>>,
                                 qualifications: MutableList<PresentationQualification>) {
        adapter.setOwnNetworks(ownNetworks, qualifications)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_medical_guide_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                R.id.action_map -> {
                    presenter.onMapClicked(tabs.selectedTabPosition)
                    return true
                }
                else -> {
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showDialogError(it: Throwable) {
        val listener = { getOwnNetworks() }
        showDialogTryAgain(listenerPositiveButton = listener,
                message = if (it is MessageErrorException) it.message!! else "")
    }

    override fun showLoading() {
        login_progressbar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        login_progressbar.visibility = View.GONE
    }

}
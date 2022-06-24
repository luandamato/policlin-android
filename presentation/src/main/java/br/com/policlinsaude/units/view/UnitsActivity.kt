package br.com.policlinsaude.units.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.units.presenter.UnitsPresenter
import br.com.policlinsaude.units.view.adapter.UnitsAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_units.*
import javax.inject.Inject

class UnitsActivity : BaseActivity(), UnitsView, UnitsAdapter.OnItemClickListener {


    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, UnitsActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: UnitsPresenter

   // @Inject //Lista
    lateinit var adapter: UnitsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_units)
        AndroidInjection.inject(this)//Lista

        setupToolbar()

        adapter = UnitsAdapter(this)
        recyclerViewUnits.adapter = adapter
        recyclerViewUnits.layoutManager =
            LinearLayoutManager(this)

        getUnits()
    }

    private fun getUnits() {
        presenter.getUnits()
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {}

     override fun showUnits(units: MutableList<PresentationEstablishment>?,
                            qualifications: MutableList<PresentationQualification>) {
       if (units != null) {
           adapter.setEstablishments(units, qualifications)
       }
    }



    override fun onItemClick(establishment: PresentationEstablishment) {
        presenter.onItemClick(establishment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
     //   menuInflater.inflate(R.menu.activity_medical_guide_list, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            when (item.itemId) {
                R.id.action_map -> {
                    //presenter.onMapClicked(tabs.selectedTabPosition)
                    return true
                }
                else -> {
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showDialogError(it: Throwable) {
        val listener = { getUnits() }
        showDialogTryAgain(listenerPositiveButton = listener,
                message = if (it is MessageErrorException) it.message!! else "")
    }

    override fun showLoading() {
        login_progressbarunits.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        login_progressbarunits.visibility = View.GONE
    }

}
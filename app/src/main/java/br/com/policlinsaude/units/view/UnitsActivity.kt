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
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseActivity
import br.com.policlinsaude.databinding.ActivityUnitsBinding
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.units.presenter.UnitsPresenter
import br.com.policlinsaude.units.view.adapter.UnitsAdapter
import dagger.android.AndroidInjection
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

    private lateinit var binding: ActivityUnitsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUnitsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidInjection.inject(this)//Lista

        setupToolbar(binding.toolbar.toolbar)

        adapter = UnitsAdapter(this)
        binding.recyclerViewUnits.adapter = adapter
        binding.recyclerViewUnits.layoutManager =
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
        binding.loginProgressbarunits.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loginProgressbarunits.visibility = View.GONE
    }

}
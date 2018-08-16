package br.com.policlinsaude.links

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.home.view.MenuActivity
import kotlinx.android.synthetic.main.fragment_links.view.*

/**
 * Created by lmiyagi on 3/26/18.
 */
class LinksFragment : BaseFragment() {

    companion object {

        fun newInstance(): LinksFragment {
            return LinksFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_links, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)

        (activity as MenuActivity).setupFragmentToolbar(toolbar, R.string.title_links)

        setupView(view)
        return view
    }

    private fun setupView(view: View) {
        view.policlinButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_policlin)) }
        }
        view.policlinGroupButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_policlin_group)) }
        }
        view.cibButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_cib)) }
        }
        view.ombudsmanButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_ombudsman)) }
        }
        view.contactPhonesButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_contact_phones)) }
        }
        view.abramgeButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_abramge)) }
        }
        view.ansButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_ans)) }
        }
    }
}
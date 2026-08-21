package br.com.policlinsaude.links

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.databinding.FragmentLinksBinding
import br.com.policlinsaude.home.view.MenuActivity

/**
 * Created by lmiyagi on 3/26/18.
 */
class LinksFragment : BaseFragment() {

    companion object {

        fun newInstance(): LinksFragment {
            return LinksFragment()
        }
    }

    private lateinit var binding: FragmentLinksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentLinksBinding.inflate(inflater, container, false)
        val toolbar: Toolbar = binding.toolbar.toolbar

        (activity as MenuActivity).setupFragmentToolbar(toolbar, R.string.title_links)

        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.policlinButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_policlin)) }
        }
        binding.policlinGroupButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_policlin_group)) }
        }
        binding.cibButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_cib)) }
        }
        binding.ombudsmanButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_ombudsman)) }
        }
        binding.contactPhonesButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_contact_phones)) }
        }
        binding.abramgeButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_abramge)) }
        }
        binding.ansButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_ans)) }
        }
    }
}
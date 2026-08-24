package br.com.policlinsaude.informations

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.multidex.BuildConfig
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.databinding.FragmentInformationsBinding
import br.com.policlinsaude.home.view.MenuActivity

/**
 * Created by lmiyagi on 3/26/18.
 */
class InformationsFragment : BaseFragment() {

    companion object {

        fun newInstance(): InformationsFragment {
            return InformationsFragment()
        }
    }

    private lateinit var binding: FragmentInformationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentInformationsBinding.inflate(inflater, container, false)
        val toolbar: Toolbar = binding.toolbar.toolbar

        (activity as MenuActivity).setupFragmentToolbar(toolbar, R.string.title_information)

        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.versionTextView.text = getString(R.string.text_version, BuildConfig.VERSION_NAME)
        binding.reviewTutorialScreensButton.setOnClickListener {
            Toast.makeText(context, "Rever", Toast.LENGTH_SHORT).show()
        }
        binding.privacyPolicyButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_privacy_policy)) }
        }
        binding.termsOfUseButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_terms)) }
        }
    }
}
package br.com.policlinsaude.informations

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.policlinsaude.BuildConfig
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragment
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.home.view.MenuActivity
import kotlinx.android.synthetic.main.fragment_informations.view.*

/**
 * Created by lmiyagi on 3/26/18.
 */
class InformationsFragment : BaseFragment() {

    companion object {

        fun newInstance(): InformationsFragment {
            return InformationsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_informations, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)

        (activity as MenuActivity).setupFragmentToolbar(toolbar, R.string.title_information)

        setupView(view)
        return view
    }

    private fun setupView(view: View) {
        view.versionTextView.text = getString(R.string.text_version, BuildConfig.VERSION_NAME)
        view.reviewTutorialScreensButton.setOnClickListener {
            Toast.makeText(context, "Rever", Toast.LENGTH_SHORT).show()
        }
        view.privacyPolicyButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_privacy_policy)) }
        }
        view.termsOfUseButton.setOnClickListener {
            context?.let { IntentHelper.openUrlInBrowser(it, getString(R.string.url_terms)) }
        }
    }
}
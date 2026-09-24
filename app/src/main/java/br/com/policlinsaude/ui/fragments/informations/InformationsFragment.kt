package br.com.policlinsaude.ui.fragments.informations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentInformationsBinding
import br.com.policlinsaude.ui.activities.home.MenuActivity
import br.com.policlinsaude.util.extensions.openBrowser
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment de Informações migrado para MVVM com Koin.
 * Exibe a versão do app e links para políticas e termos.
 */
class InformationsFragment : Fragment() {
    private val viewModel: InformationsViewModel by viewModel()
    private var _binding: FragmentInformationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupViews()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar.toolbar
        (activity as? MenuActivity)?.setupFragmentToolbar(toolbar, R.string.title_information)
    }

    private fun setupViews() {
        binding.versionTextView.text = getString(R.string.text_version, viewModel.getAppVersion())

        binding.reviewTutorialScreensButton.setOnClickListener {
            // Comportamento mantido do legado
            Toast.makeText(context, "Rever", Toast.LENGTH_SHORT).show()
        }

        binding.privacyPolicyButton.setOnClickListener {
            context?.openBrowser(getString(R.string.url_privacy_policy))
        }

        binding.termsOfUseButton.setOnClickListener {
            context?.openBrowser(getString(R.string.url_terms))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

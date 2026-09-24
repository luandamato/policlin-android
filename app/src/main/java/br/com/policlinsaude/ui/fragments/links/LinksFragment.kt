package br.com.policlinsaude.ui.fragments.links

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentLinksBinding
import br.com.policlinsaude.ui.activities.home.MenuActivity
import br.com.policlinsaude.util.extensions.openBrowser
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment de Links Úteis migrado para MVVM com Koin.
 * Centraliza o acesso a sites externos do grupo e órgãos reguladores.
 */
class LinksFragment : Fragment() {

    private val viewModel: LinksViewModel by viewModel()
    private var _binding: FragmentLinksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupViews()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar.toolbar
        (activity as? MenuActivity)?.setupFragmentToolbar(toolbar, R.string.title_links)
    }

    private fun setupViews() {
        binding.policlinButton.setOnClickListener {
            context?.openBrowser(getString(R.string.url_policlin))
        }
        binding.policlinGroupButton.setOnClickListener {
            context?.openBrowser(getString(R.string.url_policlin_group))
        }
        binding.cibButton.setOnClickListener {
            context?.openBrowser(getString(R.string.url_cib))
        }
        binding.ombudsmanButton.setOnClickListener {
            context?.openBrowser(getString(R.string.url_ombudsman))
        }
        binding.contactPhonesButton.setOnClickListener {
            context?.openBrowser(getString(R.string.url_contact_phones))
        }
        binding.abramgeButton.setOnClickListener {
            context?.openBrowser(getString(R.string.url_abramge))
        }
        binding.ansButton.setOnClickListener {
            context?.openBrowser(getString(R.string.url_ans))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

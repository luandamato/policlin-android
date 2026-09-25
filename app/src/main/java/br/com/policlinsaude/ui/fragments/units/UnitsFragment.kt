package br.com.policlinsaude.ui.fragments.units

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentUnitsBinding
import br.com.policlinsaude.ui.activities.home.MenuActivity
import br.com.policlinsaude.ui.views.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment de Unidades (Rede Própria) migrado para MVVM com Koin.
 * Lista as unidades da Policlin com suas respectivas qualificações.
 */
class UnitsFragment : Fragment() {

    companion object {
        fun newInstance(): UnitsFragment = UnitsFragment()
    }

    private val viewModel: UnitsViewModel by viewModel()
    private var _binding: FragmentUnitsBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        UnitsAdapter { establishment ->
            viewModel.onItemClick(establishment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        setupObservers()

        viewModel.getUnits()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar.toolbar
        (activity as? MenuActivity)?.setupFragmentToolbar(toolbar, R.string.title_unities)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewUnits.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UnitsFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingView.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.units.observe(viewLifecycleOwner) { units ->
            val qualifications = viewModel.qualifications.value ?: emptyList()
            adapter.update(units, qualifications)
        }

        viewModel.qualifications.observe(viewLifecycleOwner) { qualifications ->
            val units = viewModel.units.value ?: emptyList()
            adapter.update(units, qualifications)
        }

        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is UnitsEvent.ShowError -> {
                    (activity as? BaseActivity)?.showError(message = event.message)
                }
                is UnitsEvent.NavigateToDetails -> {
                    // TODO: Navegar para detalhes quando a feature estiver migrada
                    (activity as? BaseActivity)?.showToast("Detalhes em construção")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

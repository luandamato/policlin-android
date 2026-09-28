package br.com.policlinsaude.ui.fragments.coparticipation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.CoParticipationItemsDetails
import br.com.policlinsaude.databinding.FragmentFactorExtractorDetailBinding
import br.com.policlinsaude.ui.activities.coparticipation.ResearchCoParticipationActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Resultado (itens) da Pesquisa de Valores de Coparticipação (MVVM).
 *
 * Migrado de `_legacy/.../coparticipation/ui/fragments/ResearchCoParticipationItemsFragment.kt`.
 * No legado os itens chegavam via argumento do Navigation Component; aqui chegam via
 * JSON (Gson) em `arguments` — mesma premissa da migração UnitDetail (sem Parcelize).
 */
class ResearchCoParticipationItemsFragment : Fragment() {

    companion object {
        private const val ARG_DETAILS = "details"
        private val gson = Gson()

        fun newInstance(details: ArrayList<CoParticipationItemsDetails>) =
            ResearchCoParticipationItemsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DETAILS, gson.toJson(details))
                }
            }
    }

    private lateinit var binding: FragmentFactorExtractorDetailBinding
    private lateinit var details: ArrayList<CoParticipationItemsDetails>

    private val viewModel: CoParticipationViewModel by sharedViewModel()

    private val adapter by lazy { ResearchCoParticipationItemsAdapter(viewModel.isCoPartFm) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFactorExtractorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        details = readDetails()
        setupView()
    }

    private fun readDetails(): ArrayList<CoParticipationItemsDetails> {
        val json = requireArguments().getString(ARG_DETAILS) ?: return arrayListOf()
        val type = object : TypeToken<ArrayList<CoParticipationItemsDetails>>() {}.type
        return gson.fromJson<ArrayList<CoParticipationItemsDetails>>(json, type) ?: arrayListOf()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.research_co_participation_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                activity?.onBackPressedDispatcher?.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupView() {
        (activity as? ResearchCoParticipationActivity)?.showBackButton()
        binding.recyclerViewFactorExtractorDetail.adapter = adapter
        adapter.update(details)
    }
}

package com.policlinsaude.newfeature.features.coparticipation.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentFactorExtractorDetailBinding
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationItemsDetails
import com.policlinsaude.newfeature.features.coparticipation.ui.adapters.ResearchCoParticipationItemsAdapter
import com.policlinsaude.newfeature.features.coparticipation.ui.viewmodels.CoParticipationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ResearchCoParticipationItemsFragment : Fragment() {

    private lateinit var binding: FragmentFactorExtractorDetailBinding
    private lateinit var details: ArrayList<CoParticipationItemsDetails>

    private val viewModel: CoParticipationViewModel by sharedViewModel()

    private val adapter by lazy { ResearchCoParticipationItemsAdapter(viewModel.isCoPartFm) }

    private val args: ResearchCoParticipationItemsFragmentArgs by navArgs<ResearchCoParticipationItemsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFactorExtractorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        details = args.details.toCollection(ArrayList())
        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.research_co_participation_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_filter -> {
                activity?.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupView() {
        binding.recyclerViewFactorExtractorDetail.adapter = adapter
        adapter.update(details)
    }


}
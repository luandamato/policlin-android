package com.policlinsaude.newfeature.features.coparticipation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.policlinsaude.newfeature.databinding.FragmentResearchCoParticipationFiltersBinding
import com.policlinsaude.newfeature.features.coparticipation.ui.activities.ResearchCoParticipationActivity


class ResearchCoParticipationFiltersFragment : Fragment() {

    private lateinit var binding: FragmentResearchCoParticipationFiltersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentResearchCoParticipationFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        (activity as ResearchCoParticipationActivity).showBackButton()
    }

}
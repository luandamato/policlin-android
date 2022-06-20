package com.policlinsaude.newfeature.features.extractor.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.policlinsaude.newfeature.databinding.FragmentFactorExtractorDetailBinding
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorDetailItemsModel
import com.policlinsaude.newfeature.features.extractor.ui.adapters.FactorExtractorDetailAdapter

class FactorExtractorDetailFragment : Fragment() {

    private lateinit var binding: FragmentFactorExtractorDetailBinding
    private lateinit var details: ArrayList<FactorExtractorDetailItemsModel>

    private val adapter by lazy { FactorExtractorDetailAdapter() }

    private val args: FactorExtractorDetailFragmentArgs by navArgs<FactorExtractorDetailFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFactorExtractorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        details = args.details.toCollection(ArrayList())
        setupView()
    }

    private fun setupView() {
        binding.recyclerViewFactorExtractorDetail.adapter = adapter
        adapter.update(details)
    }


}
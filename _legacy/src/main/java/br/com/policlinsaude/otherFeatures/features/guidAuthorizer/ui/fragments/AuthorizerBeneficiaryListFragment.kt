package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.FragmentAuthorizerBeneficiaryListBinding
import com.policlinsaude.newfeature.features.Token.models.BeneficiarioModel
import com.policlinsaude.newfeature.features.Token.ui.adapters.DependetAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities.GuideAuthorizerActivity
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AuthorizerBeneficiaryListFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizerBeneficiaryListBinding
    private val adapter by lazy { DependetAdapter() }

    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAuthorizerBeneficiaryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as GuideAuthorizerActivity).showButtonEdit(false)
        (activity as GuideAuthorizerActivity).showButtonCancel(false)
        setupRecycler(viewModel.dependets.value?.getData()?.listaBeneficiario)
    }


    private fun setupRecycler(guides: MutableList<BeneficiarioModel>?) {
        binding.recyclerView.adapter = adapter
        adapter.update(guides)
        adapter.setOnClickListener = {
            viewModel.setUser(nome = it!!.Nome_Beneficiario, matricula = it!!.matricula, ordem = it!!.ordem)
            findNavController().popBackStack()
            findNavController().navigate(R.id.navigate_to_beneficiary_data)
        }
    }
}

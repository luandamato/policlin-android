package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.policlinsaude.newfeature.databinding.FragmentRequestDetailBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerResponseItemModel
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities.GuideAuthorizerActivity
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import com.policlinsaude.newfeature.utils.toDDMMYYYY
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProcessRequestDetailFragment: Fragment() {

    private lateinit var binding: FragmentRequestDetailBinding
    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()

    private var request: GuideAuthorizerResponseItemModel? = GuideAuthorizerResponseItemModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request = viewModel.details.value?.getData()?.sdtAutCabecalho

        setupViews()
    }

    private fun setupViews() {
        (activity as GuideAuthorizerActivity).showButtonEdit(false)
        (activity as GuideAuthorizerActivity).showButtonCancel(false)
        (activity as GuideAuthorizerActivity).showBackButton {
            viewModel.isFromActivity = true
        }

        with(binding) {
            textviewName.text = request?.interlocutor
            textviewMatricula.text = request?.matricula
            textviewOrder.text = request?.ordem.toString()
            textviewEmail.text = request?.email
            textviewTelephone.text = request?.telefone
            semanGest.text = request?.semanaGestacional.toString()
            textviewProtocol.text = request?.protocolo
            textviewCovid.text = if(request?.referenteCOVID?.lowercase() == "s") "Sim" else "Não"
            textviewAgend.text = if(request?.agendado?.lowercase() == "s") "Sim" else "Não"
            if(request?.agendado?.lowercase() == "s") {
                textviewDate.text = if(request?.dataAtendimento.isNullOrEmpty()) "--" else request?.dataAtendimento?.toDDMMYYYY()
            } else {
                textviewDate.text = "--"
            }

            textviewCity.text = request?.cidadeAtendimento
            textviewPrestador.text = if(request?.prestador.isNullOrEmpty()) "--" else request?.prestador

            if(request?.data.isNullOrEmpty())
                linearDate.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.isFromActivity = true
    }

}
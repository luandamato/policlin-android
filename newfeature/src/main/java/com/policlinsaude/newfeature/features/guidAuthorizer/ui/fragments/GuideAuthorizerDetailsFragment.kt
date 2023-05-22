package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentGuideAuthorizerDetailsBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerQuestionsItemsModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerReponsePicturesItemsModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerResponseItemModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideItemsModel
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities.GuideAuthorizerActivity
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.GuideAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.PicturesAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.QuestionAndAnswerAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import com.policlinsaude.newfeature.utils.openBrowser
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GuideAuthorizerDetailsFragment: Fragment() {

    private lateinit var binding: FragmentGuideAuthorizerDetailsBinding
    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()

    private val args by navArgs<GuideAuthorizerDetailsFragmentArgs>()
    private val adapter by lazy { PicturesAdapter() }
    private val adapterGuide by lazy { GuideAdapter() }
    private val adapterQuestions by lazy { QuestionAndAnswerAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuideAuthorizerDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as GuideAuthorizerActivity).showButtonEdit(false)
        (activity as GuideAuthorizerActivity).showButtonCancel(false)
        (activity as GuideAuthorizerActivity).showBackButton {
            viewModel.isFromActivity = true
        }

        with(binding) {
            imageviewRightRequest.rotation = -90f
        }

        viewModel.onPostGuideAuthorizerDetails(args.numberWeb)
        viewModel.onPostGuide(args.numberWeb)
        viewModel.onPostGuideAuthorizerPicturesDetails(args.numberWeb)
        viewModel.onPostGuideQuestions(args.numberWeb)
        setupObservables()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(data: GuideAuthorizerResponseItemModel?) {
        with(binding) {
            titleStatus.text = "Status da solicitação Nº WEB: ${data?.numeroWEB}"
            descriptionStatus.text = data?.statusDes
            descriptionProtocol.text = data?.protocolo
            linearRequest.setOnClickListener {
                findNavController().navigate(GuideAuthorizerDetailsFragmentDirections.toRequestRequestDetails())
            }
            descriptionObservable.apply {
                text = "Observação: ${data?.observacao}"
                isVisible = !data?.observacao.isNullOrEmpty()
            }

            if(data?.protocolo.isNullOrEmpty()) {
                dividerRequest.isVisible = false
            }


            consulting.setOnClickListener {
                findNavController().navigate(GuideAuthorizerDetailsFragmentDirections.toRequestDeadlinesFragment())
            }

            resume.setOnClickListener {
                context?.openBrowser(data?.linkRelatorio.orEmpty())
            }

            if(!data?.observacao.isNullOrEmpty())
                dividerGuideAuthorization.isVisible = true

            adapter.setOnClickListener = {
                if(it?.link?.contains(".pdf") == true)
                    context?.openBrowser("https://docs.google.com/gview?embedded=true&url=${it?.link}")
                else
                    context?.openBrowser(it?.link.orEmpty())

            }
        }
    }

    private fun setupGuide(guides: MutableList<GuideItemsModel>?) {
        with(binding) {
            recyclerViewGuide.adapter = adapterGuide
            accordionGuideAuthorizer.title = "Acesse suas Guias"
            adapterGuide.update(guides)
            adapterGuide.setOnClickListener = {
                context?.openBrowser("https://docs.google.com/gview?embedded=true&url=${it?.link.orEmpty()}")
            }
        }
    }

    private fun setupPictures(sdtAutAnexos: MutableList<GuideAuthorizerReponsePicturesItemsModel>?) {
        with(binding) {
            recyclerViewPictures.adapter = adapter
            accordionPictures.title = "Anexos"
            adapter.update(sdtAutAnexos)
        }
    }

    private fun setupQuestions(sdtAutPerguntas: MutableList<GuideAuthorizerQuestionsItemsModel>?) {
        with(binding) {
            recyclerViewAsks.adapter = adapterQuestions
            adapterQuestions.update(sdtAutPerguntas)
            adapterQuestions.setOnClickListener {
                it?.let { q ->
                    viewModel.questionsAndAnswers = q
                    findNavController().navigate(
                        GuideAuthorizerDetailsFragmentDirections.toQuestionsDetails(args.numberWeb)
                    )
                }

            }
        }
    }

    private fun setupObservables() {
        with(viewModel) {
            guide.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.FAILED -> hideLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        if (!it.getData()?.sdtAutGuias.isNullOrEmpty()) {
                            setupGuide(it.getData()?.sdtAutGuias)
                            binding.dividerGuideAuthorization.isVisible = true
                        } else
                            binding.apply {
                                accordionGuideAuthorizer.isVisible = false
                                dividerGuideAuthorization.isVisible = false
                            }
                    }
                }
            }

            details.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        setupViews(it.getData()?.sdtAutCabecalho)
                    }
                    else -> { hideLoading() }
                }
            }

            picturesDetails.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        if (!it.getData()?.sdtAutAnexos.isNullOrEmpty())
                            setupPictures(it.getData()?.sdtAutAnexos)
                        else
                            binding.apply {
                                accordionPictures.isVisible = false
                                dividerPictures.isVisible = false
                            }
                    }
                    else -> hideLoading()
                }
            }

            questions.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.FAILED -> hideLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        if (!it.getData()?.sdtAutPerguntas.isNullOrEmpty())
                            setupQuestions(it.getData()?.sdtAutPerguntas)
                        else
                            binding.apply {
                                linearQuestions.isVisible = false
                                dividerQuestions.isVisible = false
                            }

                    }
                }
            }

        }
    }

    private fun showLoading() {
        with(binding) {
            nestedScroolView.alpha = .1F
            progressBarTickets.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            nestedScroolView.alpha = 1F
            progressBarTickets.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.isFromActivity = true
    }

}
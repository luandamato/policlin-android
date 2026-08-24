package com.policlinsaude.newfeature.features.Token.ui.ui.freagments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.databinding.FragmentTokenBeneficiarioBinding
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.features.Token.models.BeneficiarioModel
import com.policlinsaude.newfeature.features.Token.ui.TokenActivity
import com.policlinsaude.newfeature.features.Token.ui.TokenViewModel
import com.policlinsaude.newfeature.features.Token.ui.adapters.DependetAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class TokenBeneficiarioFragment : Fragment() {
    private lateinit var binding: FragmentTokenBeneficiarioBinding
    private val adapter by lazy { DependetAdapter() }

    private val viewModel by sharedViewModel<TokenViewModel>(owner = { requireActivity() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTokenBeneficiarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.onGetDependents()
        setupViews()
//        setupObservables()

    }

    private fun setupViews() {
        (activity as TokenActivity).showBackButton()
        setupRecycler(viewModel.dependets.value?.getData()?.listaBeneficiario)
    }

    private fun setupRecycler(guides: MutableList<BeneficiarioModel>?) {
        with(binding) {
            recyclerView.adapter = adapter
            adapter.update(guides)
            adapter.setOnClickListener = {
                viewModel.setUser(nome = it!!.Nome_Beneficiario, matricula = it!!.matricula, ordem = it!!.ordem)
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setupObservables() {
        with(viewModel) {
            dependets.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()

                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        if (!it.getData()?.listaBeneficiario.isNullOrEmpty()) {
                            setupRecycler(it.getData()?.listaBeneficiario)
                        }
                    }

                    else -> {}
                }
            }
        }
    }



    private fun showLoading() {
        with(binding) {
//            view_schedule_central.alpha = .1F
//            progress_bar_schedule_central.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
//            view_schedule_central.alpha = 1F
//            progress_bar_schedule_central.visibility = View.GONE
        }
    }

}
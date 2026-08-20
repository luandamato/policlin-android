package com.policlinsaude.newfeature.features.Token.ui.ui.freagments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentTokenBinding
import com.policlinsaude.newfeature.features.Token.ui.TokenActivity
import com.policlinsaude.newfeature.features.Token.ui.TokenViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TokenFragment : Fragment() {
    private var tempoRestante = 60
    private lateinit var binding: FragmentTokenBinding
    private var timer: CountDownTimer? = null

    private val viewModel by sharedViewModel<TokenViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTokenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setDefaultUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timer?.cancel()
        setupViews()
        setupObservables()
        setupListeners()
        verifyDependets()

    }

    private fun setupViews() {
        (activity as TokenActivity).showBackButton()
    }

    private fun verifyDependets(){
        binding.linearBeneficiaryData.isVisible = false
        viewModel.onGetDependents()
    }

    private fun setupListeners() {
        with(binding) {
            btnToken.setOnClickListener {
                viewModel.onGetData()
            }
            linearBeneficiaryData.setOnClickListener {
                findNavController().navigate(R.id.request_to_beneficiary_data_fragment)
            }

        }
    }

    private fun setupObservables() {
        with(viewModel) {
            data.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()

                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { income ->
                            viewModel.validade = income.minutosValidade!! * 60
                            viewModel.serviceToken = income.tokenAtendimento.toString()
                            setupTimer(viewModel.validade)
                        }
                    }
                }
            }
            userSelected.observe(viewLifecycleOwner){
                binding.textviewName.text = it
            }
            dependets.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()

                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                         !it.getData()?.listaBeneficiario.isNullOrEmpty()
                        binding.linearBeneficiaryData.isVisible = !it.getData()?.listaBeneficiario.isNullOrEmpty() && it.getData()?.listaBeneficiario!!.count() > 1
                    }
                }
            }
        }
    }

    private fun setupTimer(time: Int){
        binding.lblToken.setText(viewModel.serviceToken)
        binding.lblTokenDisponivel.setText("Seu token expira em:")
        binding.lblTimer.setText(formatTime(time))
        binding.btnToken.visibility = View.GONE
        binding.progressBar.max = time
        binding.progressBar.progress = time

        tempoRestante = time
        timer = object: CountDownTimer((time * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {updateTime()}
            override fun onFinish() {configureWithoutToken()}
        }
        timer?.start()
    }

    private fun formatTime(seconds: Int): String{
        var minutes = (seconds % 3600) / 60;
        var seconds = seconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    private fun updateTime(){
        tempoRestante --
        ObjectAnimator.ofInt(binding.progressBar, "progress", tempoRestante).setDuration(1000).start()
        binding.progressBar.setProgress(tempoRestante)
        binding.lblTimer.setText(formatTime(tempoRestante))
        if (tempoRestante == 0) configureWithoutToken()
    }

    private fun configureWithoutToken(){
        binding.lblToken.setText("- - -")
        binding.lblTokenDisponivel.setText("Sem Token disponível")
        binding.lblTimer.setText("")
        binding.btnToken.visibility = View.VISIBLE
        timer?.cancel()
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
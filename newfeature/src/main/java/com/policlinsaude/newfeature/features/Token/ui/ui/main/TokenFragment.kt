package com.policlinsaude.newfeature.features.Token.ui.ui.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentTokenBinding
import com.policlinsaude.newfeature.features.Token.ui.TokenActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class TokenFragment : Fragment() {
    private var tempoRestante = 60
    private lateinit var binding: FragmentTokenBinding

    private val viewModel: TokenViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTokenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservables()
        setupListeners()

    }

    private fun setupViews() {
        (activity as TokenActivity).showBackButton()
    }

    private fun setupListeners() {
        with(binding) {
            btnToken.setOnClickListener {
                viewModel.onGetData()
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
        val timer = object: CountDownTimer((time * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {updateTime()}
            override fun onFinish() {configureWithoutToken()}
        }
        timer.start()
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
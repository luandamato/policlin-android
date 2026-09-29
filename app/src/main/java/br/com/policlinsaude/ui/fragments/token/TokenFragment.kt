package br.com.policlinsaude.ui.fragments.token

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import br.com.policlinsaude.data.models.BeneficiarioModel
import br.com.policlinsaude.data.models.TokenResponseModel
import br.com.policlinsaude.databinding.FragmentTokenBinding
import br.com.policlinsaude.ui.activities.token.TokenActivity
import br.com.policlinsaude.ui.activities.token.TokenEvent
import br.com.policlinsaude.ui.activities.token.TokenViewModel
import br.com.policlinsaude.utils.LogManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.getValue

class TokenFragment : Fragment() {

    companion object {

        fun newInstance(): TokenFragment {
            return TokenFragment()
        }
    }

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
        viewModel.getUsers()
    }

    private fun setupListeners() {
        with(binding) {
            btnToken.setOnClickListener {
                viewModel.getData()
            }
            linearBeneficiaryData.setOnClickListener {
                (activity as TokenActivity).showList()
            }

        }
    }

    private fun setupObservables() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is TokenEvent.ShowError -> showDialogError(event.message)
                is TokenEvent.ShowToken  -> onToken(event.token)
                is TokenEvent.SetUser -> onSetUser(event.user)
                is TokenEvent.SetDependents -> onDependents(event.dependents)
            }
        }
    }

    private fun showDialogError(message: String) {

    }

    private fun onToken(token: TokenResponseModel) {
        viewModel.validade = token.minutosValidade!! * 60
        viewModel.serviceToken = token.tokenAtendimento.toString()
        setupTimer(viewModel.validade)
    }

    private fun onSetUser(user: BeneficiarioModel){
        binding.textviewName.text = "${user.Nome_Beneficiario} ${user.matricula}-${user.ordem}"
    }

    private fun onDependents(dependents: MutableList<BeneficiarioModel>){
        binding.linearBeneficiaryData.isVisible = !dependents.isEmpty() && dependents.count() > 1
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
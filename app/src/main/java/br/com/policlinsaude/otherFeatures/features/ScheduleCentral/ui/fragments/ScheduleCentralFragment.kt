package com.policlinsaude.newfeature.features.ScheduleCentral.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.policlinsaude.databinding.FragmentScheduleCentralBinding
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.features.ScheduleCentral.ui.activities.ScheduleCentralActivity
import com.policlinsaude.newfeature.features.incometax.ui.viewmodels.ScheduleCentralViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScheduleCentralFragment : Fragment() {
    private lateinit var binding: FragmentScheduleCentralBinding

    private val viewModel: ScheduleCentralViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentScheduleCentralBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservables()
        setupListeners()

        viewModel.onGetData()
    }

    private fun setupViews() {
        (activity as ScheduleCentralActivity).showBackButton()
    }

    private fun setupListeners() {
        with(binding) {
            btnEmail.setOnClickListener {
                goToEmail(viewModel.email)
            }

            btnWpp.setOnClickListener {
                goToWhatsApp(viewModel.wpp)
            }

            btnPhone.setOnClickListener {
                goToCallIntent(viewModel.phone)
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
                            viewModel.phone = income.telefone.toString()
                            viewModel.wpp = income.whatsapp.toString()
                            viewModel.email = income.email.toString()
                            binding.txtAviso.setText(income.mensagem)
                            if(income.whatsapp.isNullOrEmpty()) {
                                binding.btnWpp.visibility = View.GONE
                            }
                            if(income.telefone.isNullOrEmpty()) {
                                binding.btnPhone.visibility = View.GONE
                            }
                            if(income.email.isNullOrEmpty()) {
                                binding.btnEmail.visibility = View.GONE
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun goToCallIntent(phone: String) {
        val temp: String = "0$phone"
        (activity as ScheduleCentralActivity).startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:$temp")))
    }

    private fun goToWhatsApp(phone: String) {
        try {
            (activity as ScheduleCentralActivity).startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=55${phone.replace(" ", "").replace("(", "").replace(")", "").replace("-","")}")
                )
            )
        } catch (e: Exception) {}
    }


    fun goToEmail(addresses: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(addresses))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Agendamento Policlin")
        (activity as ScheduleCentralActivity).startActivity(intent)
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
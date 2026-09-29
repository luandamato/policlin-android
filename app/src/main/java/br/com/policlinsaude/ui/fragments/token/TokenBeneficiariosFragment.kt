package br.com.policlinsaude.ui.fragments.token

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.BeneficiarioModel
import br.com.policlinsaude.databinding.FragmentTokenBeneficiariosBinding
import br.com.policlinsaude.ui.activities.token.TokenViewModel
import com.policlinsaude.newfeature.features.Token.ui.adapters.DependetAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.getValue

class TokenBeneficiariosFragment : Fragment() {

    companion object {
        fun newInstance() : TokenBeneficiariosFragment {
            return TokenBeneficiariosFragment()
        }
    }

    private lateinit var binding: FragmentTokenBeneficiariosBinding
    private val adapter by lazy { DependetAdapter() }

    private val viewModel by sharedViewModel<TokenViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTokenBeneficiariosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
//        (activity as TokenActivity).showBackButton()
        setupRecycler(viewModel.dependents)
    }

    private fun setupRecycler(guides: MutableList<BeneficiarioModel>?) {
        with(binding) {
            recyclerView.adapter = adapter
            adapter.update(guides)
            adapter.setOnClickListener = {
                viewModel.setUser(it!!)
                requireActivity().onBackPressed()
            }
        }
    }

}
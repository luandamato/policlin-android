package br.com.policlinsaude.ui.feature.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import br.com.policlinsaude.R
import br.com.policlinsaude.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : BaseFragment() {

    private val viewModel: UserProfileViewModel by viewModels()

    private lateinit var progressBar: ProgressBar
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_user_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Inicializar views
        progressBar = view.findViewById(R.id.progressBar)
        userNameTextView = view.findViewById(R.id.userNameTextView)
        userEmailTextView = view.findViewById(R.id.userEmailTextView)

        // Observar dados do ViewModel
        setupObservers()

        // Carregar dados do usuário
        val token = "seu_token_aqui" // Obtenha do SharedPreferences ou similar
        viewModel.loadUserProfile(token)
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.userProfile.observe(viewLifecycleOwner, Observer { user ->
            userNameTextView.text = user.name
            userEmailTextView.text = user.email
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        })
    }
}

package br.com.policlinsaude.ui.activities.deleteUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ActivityDeleteUserBinding
import br.com.policlinsaude.ui.activities.editPassword.EditPasswordEvent
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.ui.views.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class DeleteUserActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DeleteUserActivity::class.java))
        }
    }
    private val viewModel: DeleteUserViewModel by viewModel()
    private lateinit var binding: ActivityDeleteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar(binding.toolbar.toolbar)
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.buttonDelete.setOnClickListener {
            showConfirmDeleteDialog()
        }
    }
    
    private fun setupObservers() {
        viewModel.loading.observe(this) { isLoading ->
            binding.loadingContainerDelete.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.event.observe(this) { event ->
            when (event) {
                is DeleteUserEvent.DeleteSuccess -> {
                    // Após deletar com sucesso, o fluxo legado fazia logout
                    viewModel.logout()
                }
                is DeleteUserEvent.LogoutSuccess -> {
                    showDeletedDialog()
                }
                is DeleteUserEvent.ShowError -> {
                    DialogHelper.showErrorDialog(this, event.message)
                }
            }
        }
    }

    private fun showConfirmDeleteDialog() {
        DialogHelper.showDialog(
            context = this,
            title = getString(R.string.delete_profile_title_modal),
            message = getString(R.string.delete_profile_modal_title),
            messagePositiveButton = getString(R.string.delete_profile_button_modal),
            messageNegativeButton = getString(R.string.delete_profile_button_cancel),
            listenerPositiveButton = { viewModel.deleteUser() }
        )
    }

    private fun showDeletedDialog() {
        DialogHelper.showDialog(
            context = this,
            title = getString(R.string.delete_profile_deleted),
            message = getString(R.string.delete_profile_exit),
            messagePositiveButton = getString(R.string.text_ok),
        )
    }
}

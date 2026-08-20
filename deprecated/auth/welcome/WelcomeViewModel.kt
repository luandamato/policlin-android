package br.com.policlinsaude.ui.auth.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.policlinsaude.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for Welcome screen
 * Handles initial navigation choices (I am a client / I am not a client)
 */
@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel() {

    sealed class WelcomeAction {
        object GoToLogin : WelcomeAction()
        object GoToHome : WelcomeAction()
    }

    private val _action = MutableLiveData<WelcomeAction?>(null)
    val action: LiveData<WelcomeAction?> get() = _action

    /**
     * User tapped "I am a client" button
     */
    fun onIAmClientClicked() {
        _action.value = WelcomeAction.GoToLogin
    }

    /**
     * User tapped "I am not a client" button
     */
    fun onIAmNotClientClicked() {
        _action.value = WelcomeAction.GoToHome
    }

    /**
     * User clicked on terms/privacy link
     */
    fun onTermsLinkClicked() {
        // TODO: Open URL in browser
    }

    /**
     * Reset navigation action after handling
     */
    fun actionHandled() {
        _action.value = null
    }
}

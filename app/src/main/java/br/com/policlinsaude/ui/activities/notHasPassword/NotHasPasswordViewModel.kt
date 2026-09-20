package br.com.policlinsaude.ui.activities.notHasPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.policlinsaude.data.models.PresentationPerson
import br.com.policlinsaude.data.models.PresentationPlan
import br.com.policlinsaude.data.models.RegisterPasswordMapper
import br.com.policlinsaude.data.repositories.AppRepository
import br.com.policlinsaude.util.extensions.DateHelper
import br.com.policlinsaude.util.helpers.SingleLiveEvent
import kotlinx.coroutines.launch

/** Eventos únicos do fluxo de cadastro (não tem senha). */
sealed interface NotHasPasswordEvent {
    data class ShowError(val message: String) : NotHasPasswordEvent
    data class ShowSuccess(val message: String) : NotHasPasswordEvent
    data object Finish : NotHasPasswordEvent
}

/**
 * ViewModel do fluxo de cadastro (não tem senha).
 *
 * Compartilhado entre a [NotHasPasswordActivity] e os steppers
 * (Dados Pessoais → Dados do Plano → Criar Senha), mantendo o mesmo papel
 * do antigo `NotHasPasswordPresenter`.
 *
 * Fluxo: UI → ViewModel → AppRepository (checkPlan / registerPassword) → API.
 */
class NotHasPasswordViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    private var presentationPerson: PresentationPerson = PresentationPerson()
    private var presentationPlan: PresentationPlan = PresentationPlan()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _event = SingleLiveEvent<NotHasPasswordEvent>()
    val event: LiveData<NotHasPasswordEvent> = _event

    // =====================================================================
    // Estado compartilhado entre os steppers
    // =====================================================================
    fun setPresentationPerson(person: PresentationPerson) {
        presentationPerson = person
    }

    fun getPresentationPerson(): PresentationPerson = presentationPerson

    fun setPresentationPlan(plan: PresentationPlan) {
        presentationPlan = plan
    }

    fun getPresentationPlan(): PresentationPlan = presentationPlan

    /** Guarda a foto (base64) enviada pelo usuário no passo "criar senha". */
    fun onImagePicked(image: String?) {
        if (image.isNullOrEmpty()) {
            _event.value = NotHasPasswordEvent.ShowError("Não foi possível carregar a imagem selecionada.")
        } else {
            presentationPerson = presentationPerson.copy(photo = image)
        }
    }
/**
     * Executa o `checkPlan` (valida plano) e entrega o resultado via callback.
     * Mantém a semântica do legado: `onComplete` = sucesso; erro = dialog + false.
     */
    fun checkPlan(onResult: (Boolean) -> Unit) {
        val person = presentationPerson
        val plan = presentationPlan
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onCheckPlan(
                    register = plan.register,
                    order = plan.order,
                    cpf = person.cpf,
                    contract = plan.contract,
                    email = person.email,
                    name = person.name.uppercase(),
                    ddd = RegisterPasswordMapper.getCodeArea(person.phone),
                    phone = RegisterPasswordMapper.getPhoneWithoutCodeArea(person.phone),
                    expirationDate = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_YYYYMMDD, plan.validationRegister),
                    termAccepted = 1,
                    birthday = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_YYYYMMDD, person.birthday),
                    method = "Validar",
                    mothersName = person.mothersName
                )

                val isSuccess = response.msgInternal.equals("OK", ignoreCase = true)
                if (isSuccess) {
                    onResult(true)
                } else {
                    _event.value = NotHasPasswordEvent.ShowError(
                        response.msgExternal?.takeIf { it.isNotBlank() } ?: "Não foi possível validar o plano."
                    )
                    onResult(false)
                }
            } catch (e: Exception) {
                _event.value = NotHasPasswordEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado."
                )
                onResult(false)
            } finally {
                _loading.value = false
            }
        }
    }

    /** Conclui o cadastro chamando o `registerPassword` (MAPP_ManutencaoBeneficiario). */
    fun clickedButtonComplete() {
        val body = RegisterPasswordMapper.toRegisterPasswordBody(presentationPerson, presentationPlan)
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = appRepository.onRegisterPassword(body)
                val isSuccess = response.msgInternal.equals("OK", ignoreCase = true)

                if (isSuccess) {
                    _event.value = NotHasPasswordEvent.ShowSuccess(
                        response.msgExternal?.takeIf { it.isNotBlank() }
                            ?: "Cadastro realizado com sucesso."
                    )
                } else {
                    _event.value = NotHasPasswordEvent.ShowError(
                        response.msgExternal?.takeIf { it.isNotBlank() }
                            ?: "Não foi possível concluir o cadastro."
                    )
                }
            } catch (e: Exception) {
                _event.value = NotHasPasswordEvent.ShowError(
                    e.message?.takeIf { it.isNotBlank() } ?: "Ocorreu um erro inesperado."
                )
            } finally {
                _loading.value = false
            }
        }
    }

    /** Após o sucesso no dialog final, encerra a tela. */
    fun onSuccessDialogDismissed() {
        _event.value = NotHasPasswordEvent.Finish
    }
}
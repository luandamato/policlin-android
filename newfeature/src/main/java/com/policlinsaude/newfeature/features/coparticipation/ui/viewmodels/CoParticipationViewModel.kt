package com.policlinsaude.newfeature.features.coparticipation.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationBodyCombo
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationBodyValue
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationItems
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationModel
import com.policlinsaude.newfeature.features.coparticipation.data.repositories.CoParticipationRepository
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorBodyModel
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorModel
import com.policlinsaude.newfeature.utils.SharedPreferences
import kotlinx.coroutines.launch

class CoParticipationViewModel(
    val repository: CoParticipationRepository,
    private val preferences: SharedPreferences,
): ViewModel() {

    var itemsComboOne: MutableList<String>? = null
    var itemsComboTwo: MutableList<String>? = null
    var itemsComboThree: MutableList<String>? = null

    var optionComboOneSelection: String? = null
    var optionComboTwoSelection: String? = null
    var optionComboThreeSelection: String? = null
    var optionDescription: String? = null

    var isCoPartFm: Boolean = false

    var token: String = preferences.getToken()

    private val _responseComboOne: MutableLiveData<ViewModelResponse<CoParticipationModel, ServerErrorResponse>> = MutableLiveData()
    val comboOne: LiveData<ViewModelResponse<CoParticipationModel, ServerErrorResponse>> get() = _responseComboOne

    private val _responseComboTwo: MutableLiveData<ViewModelResponse<CoParticipationModel, ServerErrorResponse>> = MutableLiveData()
    val comboTwo: LiveData<ViewModelResponse<CoParticipationModel, ServerErrorResponse>> get() = _responseComboTwo

    private val _responseComboThree: MutableLiveData<ViewModelResponse<CoParticipationModel, ServerErrorResponse>> = MutableLiveData()
    val comboThree: LiveData<ViewModelResponse<CoParticipationModel, ServerErrorResponse>> get() = _responseComboThree

    private val _responseItemsCoParticipation: MutableLiveData<ViewModelResponse<CoParticipationItems, ServerErrorResponse>> = MutableLiveData()
    val itemsCoParticipation: LiveData<ViewModelResponse<CoParticipationItems, ServerErrorResponse>> get() = _responseItemsCoParticipation


    fun onGetComboOne() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<CoParticipationModel, ServerErrorResponse>()
            try {
                _responseComboOne.postValue(viewModelResponse)
                _responseComboOne.postValue(
                    viewModelResponse.setData(
                        repository.onPostOptionCombo(CoParticipationBodyCombo(token,1))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseComboOne.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onGetComboTwo() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<CoParticipationModel, ServerErrorResponse>()
            try {
                _responseComboTwo.postValue(viewModelResponse)
                _responseComboTwo.postValue(
                    viewModelResponse.setData(
                        repository.onPostOptionCombo(CoParticipationBodyCombo(token,2))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseComboTwo.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onGetComboThree() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<CoParticipationModel, ServerErrorResponse>()
            try {
                _responseComboThree.postValue(viewModelResponse)
                _responseComboThree.postValue(
                    viewModelResponse.setData(
                        repository.onPostOptionCombo(CoParticipationBodyCombo(token,3))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseComboThree.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onGetItemCoParticipation() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<CoParticipationItems, ServerErrorResponse>()
            try {
                _responseItemsCoParticipation.postValue(viewModelResponse)
                _responseItemsCoParticipation.postValue(
                    viewModelResponse.setData(
                        repository.onPostValuesCoParticipation(CoParticipationBodyValue(
                            token = preferences.getToken(),
                            codeComboOne = getCodeComboOne()?.toInt(),
                            codeComboTwo = getCodeComboTwo()?.toInt(),
                            codeComboThree = getCodeComboThree(),
                            description = optionDescription
                        ))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseItemsCoParticipation.postValue(viewModelResponse.setError(e))
            }
        }
    }

    private fun getCodeComboOne(): String? {
        return comboOne.value?.getData()?.items?.firstOrNull {
            it.description == optionComboOneSelection
        }?.code
    }

    private fun getCodeComboTwo(): String? {
        return comboTwo.value?.getData()?.items?.firstOrNull {
            it.description == optionComboTwoSelection
        }?.code
    }

    private fun getCodeComboThree(): String? {
        return comboThree.value?.getData()?.items?.firstOrNull {
            it.description == optionComboThreeSelection
        }?.code
    }

    fun onClearSelectedComboOne() {
        optionComboOneSelection = null
    }

    fun onClearSelectedComboTwo() {
       optionComboTwoSelection  = null
    }

    fun onClearSelectedComboThree() {
        optionComboThreeSelection = null
    }

    fun isEnabledButtonSearch(): Boolean {
        return  !optionComboOneSelection.isNullOrEmpty() ||
                !optionComboTwoSelection.isNullOrEmpty() ||
                !optionComboThreeSelection.isNullOrEmpty() ||
                !optionDescription.isNullOrEmpty()
    }

}
package com.policlinsaude.newfeature.features.tickets.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.TicketRepository
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import kotlinx.coroutines.launch

class TicketViewModel(
    val repository: TicketRepository
): ViewModel() {

    private val _responseTickets: MutableLiveData<ViewModelResponse<TicketModel, ServerErrorResponse>> = MutableLiveData()
    val tickets: LiveData<ViewModelResponse<TicketModel, ServerErrorResponse>> get() = _responseTickets

    fun onGetTickets(option: Int = 1, year: Int = 0, month: Int = 0) {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<TicketModel, ServerErrorResponse>()
            try {
                _responseTickets.postValue(viewModelResponse)
                _responseTickets.postValue(
                    viewModelResponse.setData(
                        repository.onGetTickets("ANvS4u5P2kg7e5HLyaVbQ7QXcXLe/qfH", option, year, month)
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseTickets.postValue(viewModelResponse.setError(e))
            }
        }
    }
}
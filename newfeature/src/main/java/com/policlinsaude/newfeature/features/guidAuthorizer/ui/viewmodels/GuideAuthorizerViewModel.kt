package com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import android.util.Base64OutputStream
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.data.networking.ServerErrorResponse
import com.policlinsaude.newfeature.data.networking.ViewModelResponse
import com.policlinsaude.newfeature.data.repositories.GuideRepository
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.*
import com.policlinsaude.newfeature.utils.SharedPreferences
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

class GuideAuthorizerViewModel(
    val repository: GuideRepository,
    val preferences: SharedPreferences
): ViewModel() {

    var isFromActivity: Boolean = false

    private var token: String = preferences.getToken()

    private val _responseGuideAuthorizer: MutableLiveData<ViewModelResponse<GuideAuthorizerResponseModel, ServerErrorResponse>> = MutableLiveData()
    val guideAuthorizer: LiveData<ViewModelResponse<GuideAuthorizerResponseModel, ServerErrorResponse>> get() = _responseGuideAuthorizer

    private val _responseUser: MutableLiveData<ViewModelResponse<UserModel, ServerErrorResponse>> = MutableLiveData()
    val user: LiveData<ViewModelResponse<UserModel, ServerErrorResponse>> get() = _responseUser

    private val _responseCities: MutableLiveData<ViewModelResponse<CitiesResponseModel, ServerErrorResponse>> = MutableLiveData()
    val cities: LiveData<ViewModelResponse<CitiesResponseModel, ServerErrorResponse>> get() = _responseCities

    private val _responseGuideAuthorizerRequest: MutableLiveData<ViewModelResponse<GuideAuthorizerRequestResponseModel, ServerErrorResponse>> = MutableLiveData()
    val authorizer: LiveData<ViewModelResponse<GuideAuthorizerRequestResponseModel, ServerErrorResponse>> get() = _responseGuideAuthorizerRequest

    private val _responseGuideAuthorizerCancel: MutableLiveData<ViewModelResponse<GuideAuthorizerResponseCancelModel, ServerErrorResponse>> = MutableLiveData()
    val guideAuthorizerCancel: LiveData<ViewModelResponse<GuideAuthorizerResponseCancelModel, ServerErrorResponse>> get() = _responseGuideAuthorizerCancel

    private val _responsePhotos: MutableLiveData<ViewModelResponse<ProcessResponsePhotosModel, ServerErrorResponse>> = MutableLiveData()
    val photosResponse: LiveData<ViewModelResponse<ProcessResponsePhotosModel, ServerErrorResponse>> get() = _responsePhotos

    private val _responseDeadlines: MutableLiveData<ViewModelResponse<ResponseDeadlineModel, ServerErrorResponse>> = MutableLiveData()
    val deadlines: LiveData<ViewModelResponse<ResponseDeadlineModel, ServerErrorResponse>> get() = _responseDeadlines

    private val _responseGuideDetail: MutableLiveData<ViewModelResponse<GuideAuthorizerRequestResponseModel, ServerErrorResponse>> = MutableLiveData()
    val details: LiveData<ViewModelResponse<GuideAuthorizerRequestResponseModel, ServerErrorResponse>> get() = _responseGuideDetail

    private val _responsePicturesDetail: MutableLiveData<ViewModelResponse<GuideAuthorizerResponsePicturesDetailsModel, ServerErrorResponse>> = MutableLiveData()
    val picturesDetails: LiveData<ViewModelResponse<GuideAuthorizerResponsePicturesDetailsModel, ServerErrorResponse>> get() = _responsePicturesDetail

    private val _responseQuestions: MutableLiveData<ViewModelResponse<GuideAuthorizerQuestionsModel, ServerErrorResponse>> = MutableLiveData()
    val questions: LiveData<ViewModelResponse<GuideAuthorizerQuestionsModel, ServerErrorResponse>> get() = _responseQuestions

    private val _responseGuide: MutableLiveData<ViewModelResponse<GuideModel, ServerErrorResponse>> = MutableLiveData()
    val guide: LiveData<ViewModelResponse<GuideModel, ServerErrorResponse>> get() = _responseGuide

    private val _responseSendAnswer: MutableLiveData<ViewModelResponse<ComumModel, ServerErrorResponse>> = MutableLiveData()
    val sendAnswer: LiveData<ViewModelResponse<ComumModel, ServerErrorResponse>> get() = _responseSendAnswer

    private val _responseSendAnswerAttachment: MutableLiveData<ViewModelResponse<ComumModel, ServerErrorResponse>> = MutableLiveData()
    val sendAnswerAttachment: LiveData<ViewModelResponse<ComumModel, ServerErrorResponse>> get() = _responseSendAnswerAttachment

    var guideAuthorizerSelected: MutableLiveData<MutableList<GuideAuthorizerItemsModel>> = MutableLiveData(arrayListOf())

    var processRequestModel: ProcessRequestModel = ProcessRequestModel()

    var fotos: MutableList<Bitmap?> = mutableListOf()

    var attachmentResponse: MutableList<PictureSave> = mutableListOf()
    var attachmentQuestion: MutableList<PictureSave> = mutableListOf()

    var questionsAndAnswers: GuideAuthorizerQuestionsItemsModel = GuideAuthorizerQuestionsItemsModel()

    fun attachmentResponseJPG(bitmap: Bitmap?, format: String, file: File?): PictureSave {

       val bos = ByteArrayOutputStream()
       val b64 = if(format == ".jpeg") {

           val bitmap2 = if(bitmap?.height!! > 1920 && bitmap.width > 1080) {
               Bitmap.createScaledBitmap(bitmap, 1920, 1080, false);
           }  else {
               bitmap
           }
           bitmap2.compress(Bitmap.CompressFormat.JPEG, 95, bos)
           Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT)
       } else {
           file?.let { convertImageFileToBase64(it) }
       }

        return PictureSave(
            base64 = b64,
            bitmap = bitmap,
            format = format,
            file = file
        )
    }

    fun convertImageFileToBase64(imageFile: File?): String {
        return ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream,  Base64.NO_WRAP).use { base64FilterStream ->
                imageFile?.inputStream().use { inputStream ->
                    inputStream?.copyTo(base64FilterStream)
                }
            }
            return@use outputStream.toString()
        }
    }

    fun guidAuthorizerSelectedClean() {
        guideAuthorizerSelected =  MutableLiveData(arrayListOf())
    }

    fun addSelectedGuideAuthorizer(item: GuideAuthorizerItemsModel) {
        guideAuthorizerSelected.value?.add(item)
    }

    fun removeSelectedGuideAuthorizer(item: GuideAuthorizerItemsModel) {
        guideAuthorizerSelected.value?.remove(item)
    }

    fun setUserProcessRequest(name: String?, phone: String?, emailuser: String? ,semGes: Int) {
        processRequestModel.apply {
            token
            sdtAutCabecalho.apply {
                interlocutor = name.orEmpty()
                telefone = phone.orEmpty()
                email = emailuser.orEmpty()
                semanaGestacional = semGes
            }
        }
    }

    fun setRequestData(covid: String) {
        processRequestModel.sdtAutCabecalho.referenteCOVID = covid
    }

    fun setSchedule(schedule: String) {
        processRequestModel.sdtAutCabecalho.agendado = schedule
    }

    fun setDataService(dataService: String) {
        processRequestModel.sdtAutCabecalho.dataAtendimento = dataService
    }

    fun setCity(city: String) {
        val filter = cities.value?.getData()?.sdtAutCidades?.firstOrNull { it.descricao == city }
        processRequestModel.sdtAutCabecalho.apply {
            cidadeDes = filter?.descricao.orEmpty()
            cidadeCod = filter?.codigo ?: 0
        }
    }

    fun setCityDesc(city: String) {
        processRequestModel.sdtAutCabecalho.cidadeAtendimento = city
    }

    fun setLocationService(location: String) {
        processRequestModel.sdtAutCabecalho.prestador = location
    }

    fun clearUserAll() {
        _responseUser.value?.setData(UserModel())
        processRequestModel = ProcessRequestModel()
    }

    fun clearAnswer() {
        val empty = ViewModelResponse<ComumModel, ServerErrorResponse>()
        empty.setEmpty()
        _responseSendAnswer.value = empty
        attachmentQuestion = mutableListOf()
        _responseSendAnswerAttachment.value = empty
    }

    fun onGetListGuideAuthorizer() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<GuideAuthorizerResponseModel, ServerErrorResponse>()
            try {
                _responseGuideAuthorizer.postValue(viewModelResponse)
                _responseGuideAuthorizer.postValue(
                    viewModelResponse.setData(
                        repository.onGetGuideAuthorizer(GuideAuthorizerRequestModel(token = token))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseGuideAuthorizer.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onGetUser() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<UserModel, ServerErrorResponse>()
            try {
                _responseUser.postValue(viewModelResponse)
                _responseUser.postValue(
                    viewModelResponse.setData(
                        repository.onGetProfile(token = token, verify = 1)
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseUser.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onGetCities() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<CitiesResponseModel, ServerErrorResponse>()
            try {
                _responseCities.postValue(viewModelResponse)
                _responseCities.postValue(
                    viewModelResponse.setData(
                        repository.onGetCities(CityRequestModel(token))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseCities.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onPostRequestGuideAuthorizer() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<GuideAuthorizerRequestResponseModel, ServerErrorResponse>()
            try {
                processRequestModel.token = token
                _responseGuideAuthorizerRequest.postValue(viewModelResponse)
                _responseGuideAuthorizerRequest.postValue(
                    viewModelResponse.setData(
                        repository.onPostGuideAuthorizer(processRequestModel)
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseGuideAuthorizerRequest.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onPostRequestDeadlines() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<ResponseDeadlineModel, ServerErrorResponse>()
            try {
                _responseDeadlines.postValue(viewModelResponse)
                _responseDeadlines.postValue(
                    viewModelResponse.setData(
                        repository.onPostRequestDeadlines(token)
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseDeadlines.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onPostRequestGuideAuthorizerCancel() {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<GuideAuthorizerResponseCancelModel, ServerErrorResponse>()
            val body = GuideAuthorizerRequestCancelModel()
            guideAuthorizerSelected.value?.forEachIndexed { index, notificationsModel ->
                body.token = token
                body.numeroWEB = notificationsModel.numeroWEB.orEmpty()
                if(index == guideAuthorizerSelected.value!!.lastIndex) {
                    try {
                        _responseGuideAuthorizerCancel.postValue(viewModelResponse)
                        _responseGuideAuthorizerCancel.postValue(
                            viewModelResponse.setData(
                                repository.onPostGuideAuthorizerCancel(body)
                            )
                        )
                    } catch (e: ServerErrorResponse) {
                        _responseGuideAuthorizerCancel.postValue(viewModelResponse.setError(e))
                    }
                }
                else {
                    repository.onPostGuideAuthorizerCancel(body)
                }
            }
        }
    }

    fun onPostRequestGuideAuthorizerPhotos(number: String) {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<ProcessResponsePhotosModel, ServerErrorResponse>()
            val pictures = attachmentResponse.map {
                ProcessRequestPhotosModel(
                    token = token,
                    numeroWEB = number,
                    arquivo = it.base64,
                    extensao = it.format
                )
            }


            pictures.forEachIndexed { index, processRequestPhotosModel ->
                if (index == pictures.lastIndex) {
                    try {
                        _responsePhotos.postValue(viewModelResponse)
                        _responsePhotos.postValue(
                            viewModelResponse.setData(
                                repository.onPostGuideAuthorizerPhotos(processRequestPhotosModel)
                            )
                        )
                    } catch (e: ServerErrorResponse) {
                        _responsePhotos.postValue(viewModelResponse.setError(e))
                    }
                } else {
                    repository.onPostGuideAuthorizerPhotos(processRequestPhotosModel)
                }
            }

        }
    }

    fun onPostAnswer(number: String, id: String, answer: String) {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<ComumModel, ServerErrorResponse>()
            try {
                _responseSendAnswer.postValue(viewModelResponse)
                _responseSendAnswer.postValue(
                    viewModelResponse.setData(
                        repository.onPostSendAnswer(ProcessRequestSendAnswerModel(token, number.toInt(), id.toInt(), answer))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseSendAnswer.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onPostAnswerAttachment(number: String, answerID: Int?) {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<ComumModel, ServerErrorResponse>()
            val pictures = attachmentQuestion.map {
                ProcessRequestSendAnswerAttachmentModel(
                    token = token,
                    numeroWEB = number,
                    arquivo = it.base64,
                    perguntaID = answerID,
                    extensao = it.format
                )
            }

            pictures.forEachIndexed { index, processRequestPhotosModel ->
                if (index == pictures.lastIndex) {
                    try {
                        _responseSendAnswerAttachment.postValue(viewModelResponse)
                        _responseSendAnswerAttachment.postValue(
                            viewModelResponse.setData(
                                repository.onPostSendAnswerAttachment(processRequestPhotosModel)
                            )
                        )
                    } catch (e: ServerErrorResponse) {
                        _responseSendAnswerAttachment.postValue(viewModelResponse.setError(e))
                    }
                } else {
                    repository.onPostSendAnswerAttachment(processRequestPhotosModel)
                }
            }

        }
    }

    fun onPostGuideAuthorizerDetails(number: String) {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<GuideAuthorizerRequestResponseModel, ServerErrorResponse>()
            try {
                _responseGuideDetail.postValue(viewModelResponse)
                _responseGuideDetail.postValue(
                    viewModelResponse.setData(
                        repository.onPostRequestDetails(GuideAuthorizerRequestCancelModel(token, number))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseGuideDetail.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onPostGuide(number: String) {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<GuideModel, ServerErrorResponse>()
            try {
                _responseGuide.postValue(viewModelResponse)
                _responseGuide.postValue(
                    viewModelResponse.setData(
                        repository.onPostGuide(GuideAuthorizerRequestCancelModel(token, number))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseGuide.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onPostGuideQuestions(number: String) {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<GuideAuthorizerQuestionsModel, ServerErrorResponse>()
            try {
                _responseQuestions.postValue(viewModelResponse)
                _responseQuestions.postValue(
                    viewModelResponse.setData(
                        repository.onPostRequestQuestions(GuideAuthorizerRequestCancelModel(token, number))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responseQuestions.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun onPostGuideAuthorizerPicturesDetails(number: String) {
        viewModelScope.launch {
            val viewModelResponse = ViewModelResponse<GuideAuthorizerResponsePicturesDetailsModel, ServerErrorResponse>()
            try {
                _responsePicturesDetail.postValue(viewModelResponse)
                _responsePicturesDetail.postValue(
                    viewModelResponse.setData(
                        repository.onPostRequestPicturesDetails(GuideAuthorizerRequestCancelModel(token, number))
                    )
                )
            } catch (e: ServerErrorResponse) {
                _responsePicturesDetail.postValue(viewModelResponse.setError(e))
            }
        }
    }

    fun verifyButtonNewRequest(): Boolean {
        if(processRequestModel.sdtAutCabecalho.interlocutor.isNotEmpty()
                && processRequestModel.sdtAutCabecalho.telefone.isNotEmpty()
                && processRequestModel.sdtAutCabecalho.email.isNotEmpty()
                && processRequestModel.sdtAutCabecalho.referenteCOVID.isNotEmpty()
                && processRequestModel.sdtAutCabecalho.agendado.isNotEmpty()
                &&  processRequestModel.sdtAutCabecalho.cidadeCod > 0
                && attachmentResponse.size > 0) {
            if(processRequestModel.sdtAutCabecalho.agendado.lowercase() == "sim")
                return !processRequestModel.sdtAutCabecalho.dataAtendimento.isNullOrEmpty()
            if (processRequestModel.sdtAutCabecalho.cidadeDes.lowercase().contains("outra"))
                return !processRequestModel.sdtAutCabecalho.cidadeAtendimento.isNullOrEmpty()
            return true
        }

        return false
    }

    fun onClearRequest() {
        fotos = mutableListOf()
        processRequestModel = ProcessRequestModel()
        _responseUser.postValue(null)
    }
}
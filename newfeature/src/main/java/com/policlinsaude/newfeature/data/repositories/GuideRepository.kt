package com.policlinsaude.newfeature.data.repositories

import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosRequestModel
import com.policlinsaude.newfeature.features.Token.models.BeneficiariosResponseModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.*

interface GuideRepository {

    suspend fun onGetGuideAuthorizer(token: GuideAuthorizerRequestModel): GuideAuthorizerResponseModel

    suspend fun onGetProfile(token: String, verify: Int): UserModel

    suspend fun onGetCities(body: CityRequestModel): CitiesResponseModel

    suspend fun onPostGuideAuthorizer(form: ProcessRequestModel): GuideAuthorizerRequestResponseModel

    suspend fun onPostGuideAuthorizerPhotos(form: ProcessRequestPhotosModel): ProcessResponsePhotosModel

    suspend fun onPostGuideAuthorizerCancel(cancel: GuideAuthorizerRequestCancelModel): GuideAuthorizerResponseCancelModel

    suspend fun onPostRequestDeadlines(token: String): ResponseDeadlineModel

    suspend fun onPostRequestDetails(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerRequestResponseModel

    suspend fun onPostRequestPicturesDetails(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerResponsePicturesDetailsModel

    suspend fun onPostRequestQuestions(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerQuestionsModel

    suspend fun onPostGuide(body: GuideAuthorizerRequestCancelModel): GuideModel

    suspend fun onPostSendAnswer(body: ProcessRequestSendAnswerModel): ComumModel

    suspend fun onPostSendAnswerAttachment(body: ProcessRequestSendAnswerAttachmentModel): ComumModel

    suspend fun onGetDependents(token: BeneficiariosRequestModel): BeneficiariosResponseModel
}
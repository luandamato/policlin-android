package com.policlinsaude.newfeature.data.services

import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST

interface GuideService {

    @POST("rest/apiAutorizadorPesquisa")
    suspend fun getGuideAuthorizer(
        @Body token: GuideAuthorizerRequestModel
    ): Response<GuideAuthorizerResponseModel>

    @FormUrlEncoded
    @POST("MAPP_RetornaBeneficiario")
    suspend fun getPerson(
        @Field("token") token: String,
        @Field("verificaAlteracao") verifyChanges: Int
    ): Response<UserModel>

    @POST("rest/apiAutorizadorCombo")
    suspend fun getCities(
        @Body token: CityRequestModel
    ): Response<CitiesResponseModel>

    @POST("rest/apiAutorizadorGravarCabecalho")
    suspend fun postGuideAuthorizer(
        @Body body: ProcessRequestModel
    ): Response<GuideAuthorizerRequestResponseModel>

    @POST("apiAutorizadorGravarAnexo")
    suspend fun postGuideAuthorizerPhotos(
        @Body body: ProcessRequestPhotosModel
    ): Response<ProcessResponsePhotosModel>

    @POST("rest/apiAutorizadorCancelar")
    suspend fun postGuideAuthorizerCancel(
        @Body body: GuideAuthorizerRequestCancelModel
    ): Response<GuideAuthorizerResponseCancelModel>

    @POST("rest/apiAutorizadorPrazoResposta")
    suspend fun postRequestDeadline(
        @Body body: CityRequestModel
    ): Response<ResponseDeadlineModel>

    @POST("rest/apiAutorizadorBuscaCabecalho")
    suspend fun postDetails(
        @Body body: GuideAuthorizerRequestCancelModel
    ): Response<GuideAuthorizerRequestResponseModel>

    @POST("rest/apiAutorizadorBuscaAnexos")
    suspend fun postGetPicturesDetails(
        @Body body: GuideAuthorizerRequestCancelModel
    ): Response<GuideAuthorizerResponsePicturesDetailsModel>

    @POST("rest/apiAutorizadorBuscaPerguntas")
    suspend fun postGetQuestions(
        @Body body: GuideAuthorizerRequestCancelModel
    ): Response<GuideAuthorizerQuestionsModel>

    @POST("rest/apiAutorizadorBuscaGuias")
    suspend fun postGuide(
        @Body body: GuideAuthorizerRequestCancelModel
    ): Response<GuideModel>

    @POST("rest/apiAutorizadorGravarResposta")
    suspend fun sendAnswer(
        @Body body: ProcessRequestSendAnswerModel
    ): Response<ComumModel>

    @POST("apiAutorizadorGravarRespostaAnexo")
    suspend fun sendAnswerAttachment(
        @Body body: ProcessRequestSendAnswerAttachmentModel
    ): Response<ComumModel>

}
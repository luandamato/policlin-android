package br.com.policlinsaude.data.services

import br.com.policlinsaude.data.models.*
import br.com.policlinsaude.domain.models.UserConnected
import br.com.policlinsaude.domain.models.ValidateButtonBody
import br.com.policlinsaude.domain.models.ValidateButtons
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Service único que contém todos os endpoints chamados pelo app.
 *
 * Cada endpoint usa apenas o caminho relativo; a base URL é resolvida
 * pelo [RetrofitProvider.createService] conforme [NetworkConstants].
 */
interface AppService {

    // =====================================================================
    // TICKETS / 2ª VIA DE BOLETO
    // =====================================================================
    @POST("rest/apiBoletos")
    suspend fun getTickets(@Body body: TicketBodyModel): Response<TicketModel>

    @POST("rest/apiIR")
    suspend fun getIncomeTax(@Body body: IncomeTaxBodyModel): Response<IncomeTaxResponseModel>

    @POST("rest/apiGetDadosCentral")
    suspend fun getScheduleCentral(@Body body: ScheduleCentralBodyModel): Response<ScheduleCentralResponseModel>

    @POST("rest/apiTokenAtendimento")
    suspend fun getAttendanceToken(@Body body: TokenBodyModel): Response<TokenResponseModel>

    @POST("rest/apiListaBeneficiario")
    suspend fun getDependents(@Body body: BeneficiariosRequestModel): Response<BeneficiariosResponseModel>

    @POST("MAPP_Sair")
    suspend fun logout(@Body body: DeleteUserRequest): Response<ComumModel>

    // =====================================================================
    // AUTORIZADOR / GUIAS
    // =====================================================================
    @POST("rest/apiAutorizadorPesquisa")
    suspend fun getGuideAuthorizer(@Body body: GuideAuthorizerRequestModel): Response<GuideAuthorizerResponseModel>

    @POST("rest/apiAutorizadorCombo")
    suspend fun getCities(@Body body: CityRequestModel): Response<CitiesResponseModel>

    @POST("rest/apiAutorizadorGravarCabecalho")
    suspend fun postGuideAuthorizer(@Body body: ProcessRequestModel): Response<GuideAuthorizerRequestResponseModel>

    @POST("rest/apiAutorizadorGravarAnexo")
    suspend fun postGuideAuthorizerPhotos(@Body body: ProcessRequestPhotosModel): Response<ProcessResponsePhotosModel>

    @POST("rest/apiAutorizadorCancelar")
    suspend fun postGuideAuthorizerCancel(@Body body: GuideAuthorizerRequestCancelModel): Response<GuideAuthorizerResponseCancelModel>

    @POST("rest/apiAutorizadorPrazoResposta")
    suspend fun postRequestDeadline(@Body body: CityRequestModel): Response<ResponseDeadlineModel>

    @POST("rest/apiAutorizadorBuscaCabecalho")
    suspend fun postGuideDetails(@Body body: GuideAuthorizerRequestCancelModel): Response<GuideAuthorizerRequestResponseModel>

    @POST("rest/apiAutorizadorBuscaAnexos")
    suspend fun postGuidePictures(@Body body: GuideAuthorizerRequestCancelModel): Response<GuideAuthorizerResponsePicturesDetailsModel>

    @POST("rest/apiAutorizadorBuscaPerguntas")
    suspend fun postGuideQuestions(@Body body: GuideAuthorizerRequestCancelModel): Response<GuideAuthorizerQuestionsModel>

    @POST("rest/apiAutorizadorBuscaGuias")
    suspend fun postGuide(@Body body: GuideAuthorizerRequestCancelModel): Response<GuideModel>

    @POST("rest/apiAutorizadorGravarResposta")
    suspend fun postGuideAnswer(@Body body: ProcessRequestSendAnswerModel): Response<ComumModel>

    @POST("rest/apiAutorizadorGravarRespostaAnexo")
    suspend fun postGuideAnswerAttachment(@Body body: ProcessRequestSendAnswerAttachmentModel): Response<ComumModel>

    // =====================================================================
    // NOTIFICAÇÕES
    // =====================================================================
    @GET("MAPP_Notificacoes")
    suspend fun getNotifications(@Header("token") token: String): Response<NotificationsResponseModel>

    @POST("MAPP_Notificacoes")
    suspend fun postNotifications(
        @Header("token") token: String,
        @Body body: NotificationDeleteRequest
    ): Response<NotificationsResponseModel>

    // =====================================================================
    // EXTRATOR (COPARTICIPAÇÃO)
    // =====================================================================
    @POST("rest/apiExtratoCoparticipacao")
    suspend fun postFactorExtractor(@Body body: FactorExtractorBodyModel): Response<FactorExtractorModel>

    @POST("rest/apiExtratoCopartComboAno")
    suspend fun getFactorExtractorYears(): Response<FactorExtractorYearsModel>

    @POST("rest/apiExtratoCopartComboMes")
    suspend fun getFactorExtractorMonths(@Body body: FactorExtractorMonthsBody): Response<FactorExtractorMonthsModel>

    // =====================================================================
    // COPARTICIPAÇÃO
    // =====================================================================
    @POST("rest/APIValoresCopartCombos")
    suspend fun postCoParticipationCombos(@Body body: CoParticipationBodyCombo): Response<CoParticipationModel>

    @POST("rest/apiValoresCopart")
    suspend fun postCoParticipationValues(@Body body: CoParticipationBodyValue): Response<CoParticipationItems>

    // =====================================================================
    // PERFIL / SESSÃO
    // =====================================================================
    @FormUrlEncoded
    @POST("MAPP_RetornaBeneficiario")
    suspend fun getPerson(
        @Field("token") token: String,
        @Field("verificaAlteracao") verifyChanges: Int
    ): Response<UserModel>
    // =====================================================================
    // AUTENTICAÇÃO / SESSÃO (legado)
    // =====================================================================
    @FormUrlEncoded
    @POST("MAPP_Login")
    suspend fun login(
        @Field("matricula") register: String,
        @Field("ordem") order: String,
        @Field("senha") password: String,
        @Field("firebaseToken") firebaseToken: String,
        @Header("verOS") osVersion: String
    ): Response<JsonLoginResponse>

    @FormUrlEncoded
    @POST("MAPP_RecuperarSenha")
    suspend fun recoverPassword(
        @Field("matricula") register: String,
        @Field("ordem") order: String,
        @Field("email") email: String
    ): Response<JsonRecoverPasswordResponse>

    //TODO: verificar qual está sendo usado - apagar checkPlan
    @POST("MAPP_ManutencaoBeneficiario")
    suspend fun registerPassword(@Body json: JsonRegisterPasswordBody): Response<JsonRecoverPasswordResponse>

    @FormUrlEncoded
    @POST("MAPP_ManutencaoBeneficiario")
    suspend fun checkPlan(
        @Field("matricula") register: String,
        @Field("ordem") order: String,
        @Field("cpf") cpf: String,
        @Field("contrato") contract: String,
        @Field("email") email: String,
        @Field("nome") name: String,
        @Field("ddd") ddd: String,
        @Field("celular") phone: String,
        @Field("validadeCarterinha") expirationDate: String,
        @Field("aceiteTermo") termAccepted: Int,
        @Field("dtaNasc") birthday: String,
        @Field("metodo") method: String,
        @Field("nomeMae") mothersName: String
    ): Response<JsonCheckPlanResponse>

    @FormUrlEncoded
    @POST("MAPP_CarterinhaVirtualv2")
    suspend fun getHealthInsurancePhoto(
        @Field("token") token: String,
        @Field("verificaAlteracao") checkUpdated: Int
    ): Response<JsonHealthInsurancePhotoResponse>

    @GET("MAPP_RetornaGuiaMedicoFiltrov2")
    suspend fun getMedicalGuideOptions(): Response<JsonMedicalGuideOptionsResponse>
    @GET("MAPP_Retorna_GuiaMedicov4")
    suspend fun getMedicalGuideList(
        @Query("Codigo_Plano") codePlan: Int,
        @Query("Codigo_Cidade") codeCity: Int,
        @Query("Codigo_Especialidade_Servico") codeSpecialityService: Int,
        @Query("RedePropria") ownNetwork: Int,
        @Query("locLatitude") latitude: Double? = null,
        @Query("locLongitude") longitude: Double? = null,
        @Query("FilAvanc_ClasseProfissional") professionalClassOption: String? = null,
        @Query("FilAvanc_TipoServico") serviceTypeOption: String? = null,
        @Query("FilAvanc_TipoEstabelecimentoCNES") establishmentType: String? = null,
        @Query("Ent_Tipo") specialityType: String? = null,
        @Query("FilAvanc_Endereco") address_filter: String? = null,
        @Query("FilAvanc_Bairro") neighborhood_filter: String? = null,
        @Query("FilAvanc_CEP") zipcode_filter: String? = null,
        @Query("FilAvanc_NumeroConselho") number_on_the_board_filter: String? = null,
        @Query("FilAvanc_NomeProfissional_NomeFantasia_RazaoSocial") prof_fantasy_filter: String? = null,
        @Query("FilAvanc_CNPJ") cnpj_filter: String? = null,
        @Query("FilAvanc_Telefone") phones_filter: String? = null,
        @Query("FilAvanc_Qualificacoes") qualificationsSearch: String? = null,
        @Query("token") token: String? = null
    ): Response<JsonMedicalGuideListResponse>

    @GET("MAPP_RetornaGuiaMedicoDetalhes")
    suspend fun getMedicalGuideDetails(
        @Query("PROUF") proUF: String,
        @Query("PRSCOD") prsCod: String,
        @Query("PROCLS") proCls: String,
        @Query("PROCOD") proCod: String
    ): Response<JsonMedicalGuidePlansResponse>

    @GET("MAPP_RetornaRedePropria")
    suspend fun getOwnNetwork(@Query("token") token: String? = null): Response<JsonOwnNetworkResponse>

    @GET("MAPP_RetornaRedeUnidades")
    suspend fun getUnits(@Query("token") token: String? = null): Response<JsonUnitsResponse>
    // =====================================================================
    // FAVORITOS (legado)
    // =====================================================================
    @FormUrlEncoded
    @POST("MAPP_ManutencaoFavoritos")
    suspend fun addToFavorite(
        @Field("token") token: String,
        @Field("metodo") method: String,
        @Field("_PROCLS") proCls: String,
        @Field("_PROCOD") proCod: String,
        @Field("_PROUF") proUF: String,
        @Field("_PRSCOD") prsCod: String,
        @Field("_PRSSEQ") prsSeq: String,
        @Field("_ESCOD") esCod: String,
        @Field("_Tipo") type: String
    ): Response<JsonFavoriteResponse>

    @FormUrlEncoded
    @POST("MAPP_ManutencaoFavoritos")
    suspend fun removeFromFavorites(
        @Field("token") token: String,
        @Field("metodo") method: String,
        @Field("_PROCLS") proCls: String,
        @Field("_PROCOD") proCod: String,
        @Field("_PROUF") proUF: String,
        @Field("_PRSCOD") prsCod: String,
        @Field("_PRSSEQ") prsSeq: String,
        @Field("_ESCOD") esCod: String,
        @Field("_Tipo") type: String
    ): Response<JsonRemoveFromFavoritesResponse>

    @FormUrlEncoded
    @POST("MAPP_RetornaFavoritos")
    suspend fun getFavorites(@Field("token") token: String): Response<JsonFavoritesResponse>
    // =====================================================================
    // PERFIL / CONTA (legado)
    // =====================================================================
    @FormUrlEncoded
    @POST("MAPP_AtualizaAvatar")
    suspend fun updateAvatar(
        @Field("token") token: String,
        @Field("avatar") imageString: String
    ): Response<JsonUpdateAvatarResponse>

    @FormUrlEncoded
    @POST("MAPP_Sair")
    suspend fun doLogoff(@Field("token") token: String): Response<JsonLogoffResponse>

    @FormUrlEncoded
    @POST("MAPP_AtualizaSenha")
    suspend fun editPassword(
        @Field("token") token: String,
        @Field("senha") password: String
    ): Response<JsonEditPasswordResponse>

    @FormUrlEncoded
    @POST("MAPP_AtualizaTelefone")
    suspend fun editPhone(
        @Field("token") token: String,
        @Field("DDD") codeArea: String,
        @Field("Telefone") phone: String
    ): Response<JsonEditPhoneResponse>

    @FormUrlEncoded
    @POST("MAPP_ValidaBeneficiario")
    suspend fun validateUserConnected(
        @Field("matricula") registration: String,
        @Field("ordem") order: String,
        @Field("token") token: String
    ): Response<UserConnected>

    @POST("rest/apiAcessoBotoes")
    suspend fun validateButtons(@Body body: ValidateButtonBody): Response<ValidateButtons>

    // =====================================================================
    // EXCLUIR CADASTRO
    // =====================================================================
    @POST("rest/apiExcluirCadastroApp")
    suspend fun deleteUser(@Body body: DeleteUserRequest): Response<ComumModel>
    @FormUrlEncoded
    @POST
    suspend fun getBanners(
        @Url bannersUrl: String,
        @Field("retornaFoto") returnImage: Boolean,
        @Field("id") id: Int
    ): Response<JsonGetBannersResponse>
}
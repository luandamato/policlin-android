package br.com.policlinsaude.data.repositories

import android.util.Log
import br.com.policlinsaude.data.models.*
import br.com.policlinsaude.data.services.AppService
import br.com.policlinsaude.data.services.NetworkConstants
import br.com.policlinsaude.data.services.RetrofitProvider
import br.com.policlinsaude.data.services.ServerErrorResponse
import br.com.policlinsaude.domain.models.UserConnected
import br.com.policlinsaude.domain.models.ValidateButtonBody
import br.com.policlinsaude.domain.models.ValidateButtons
import br.com.policlinsaude.utils.LogManager
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

/**
 * Repositório único do app: centraliza todos os endpoints ([AppService])
 * expondo métodos de negócio.
 */
class AppRepository {

    private val service = RetrofitProvider.createService(AppService::class.java)
    private val notificationService =
        RetrofitProvider.createService(AppService::class.java, baseUrl = NetworkConstants.BASE_URL_APIAPP)

    // =====================================================================
    // TICKETS / 2ª VIA DE BOLETO
    // =====================================================================
    suspend fun onGetTickets(body: TicketBodyModel): TicketModel =
        request { service.getTickets(body) }

    suspend fun onGetIncomeTax(body: IncomeTaxBodyModel): IncomeTaxResponseModel =
        request { service.getIncomeTax(body) }

    suspend fun onGetScheduleCentral(body: ScheduleCentralBodyModel): ScheduleCentralResponseModel =
        request { service.getScheduleCentral(body) }

    suspend fun onGetToken(body: TokenBodyModel): TokenResponseModel =
        request { service.getAttendanceToken(body) }

    suspend fun onGetDependents(body: BeneficiariosRequestModel): BeneficiariosResponseModel =
        request { service.getDependents(body) }

    suspend fun onLogout(body: DeleteUserRequest): ComumModel =
        request { notificationService.logout(body) }

    // =====================================================================
    // AUTORIZADOR / GUIAS
    // =====================================================================
    suspend fun onGetGuideAuthorizer(body: GuideAuthorizerRequestModel): GuideAuthorizerResponseModel =
        request { service.getGuideAuthorizer(body) }

    suspend fun onGetCities(body: CityRequestModel): CitiesResponseModel =
        request { service.getCities(body) }

    suspend fun onPostGuideAuthorizer(body: ProcessRequestModel): GuideAuthorizerRequestResponseModel =
        request { service.postGuideAuthorizer(body) }

    suspend fun onPostGuideAuthorizerPhotos(body: ProcessRequestPhotosModel): ProcessResponsePhotosModel =
        request { notificationService.postGuideAuthorizerPhotos(body) }

    suspend fun onPostGuideAuthorizerCancel(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerResponseCancelModel =
        request { service.postGuideAuthorizerCancel(body) }

    suspend fun onPostRequestDeadlines(body: CityRequestModel): ResponseDeadlineModel =
        request { service.postRequestDeadline(body) }

    suspend fun onPostGuideDetails(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerRequestResponseModel =
        request { service.postGuideDetails(body) }

    suspend fun onPostGuidePictures(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerResponsePicturesDetailsModel =
        request { service.postGuidePictures(body) }

    suspend fun onPostGuideQuestions(body: GuideAuthorizerRequestCancelModel): GuideAuthorizerQuestionsModel =
        request { service.postGuideQuestions(body) }

    suspend fun onPostGuide(body: GuideAuthorizerRequestCancelModel): GuideModel =
        request { service.postGuide(body) }

    suspend fun onPostGuideAnswer(body: ProcessRequestSendAnswerModel): ComumModel =
        request { service.postGuideAnswer(body) }

    suspend fun onPostGuideAnswerAttachment(body: ProcessRequestSendAnswerAttachmentModel): ComumModel =
        request { notificationService.postGuideAnswerAttachment(body) }

    // =====================================================================
    // NOTIFICAÇÕES
    // =====================================================================
    suspend fun onGetNotifications(token: String): NotificationsResponseModel =
        request { notificationService.getNotifications(token) }

    suspend fun onDeleteNotifications(token: String, body: NotificationDeleteRequest): NotificationsResponseModel =
        request { notificationService.postNotifications(token, body) }

    // =====================================================================
    // EXTRATOR (COPARTICIPAÇÃO)
    // =====================================================================
    suspend fun onPostFactorExtractor(body: FactorExtractorBodyModel): FactorExtractorModel =
        request { service.postFactorExtractor(body) }

    suspend fun onGetFactorExtractorYears(): FactorExtractorYearsModel =
        request { service.getFactorExtractorYears() }

    suspend fun onGetFactorExtractorMonths(body: FactorExtractorMonthsBody): FactorExtractorMonthsModel =
        request { service.getFactorExtractorMonths(body) }

    // =====================================================================
    // COPARTICIPAÇÃO
    // =====================================================================
    suspend fun onPostCoParticipationCombos(body: CoParticipationBodyCombo): CoParticipationModel =
        request { service.postCoParticipationCombos(body) }

    suspend fun onPostCoParticipationValues(body: CoParticipationBodyValue): CoParticipationItems =
        request { service.postCoParticipationValues(body) }

    // =====================================================================
    // PERFIL / SESSÃO
    // =====================================================================
    suspend fun onGetProfile(token: String, verify: Int): UserModel =
        request { notificationService.getPerson(token, verify) }

    // =====================================================================
    // AUTENTICAÇÃO / SESSÃO (legado)
    // =====================================================================
    suspend fun onLogin(register: String, order: String, password: String, firebaseToken: String, osVersion: String): JsonLoginResponse =
        request { service.login(register, order, password, firebaseToken, osVersion) }

    suspend fun onRecoverPassword(register: String, order: String, email: String): JsonRecoverPasswordResponse =
        request { service.recoverPassword(register, order, email) }

    suspend fun onRegisterPassword(json: JsonRegisterPasswordBody): JsonRecoverPasswordResponse =
        request { service.registerPassword(json) }

    suspend fun onCheckPlan(
        register: String, order: String, cpf: String, contract: String, email: String, name: String,
        ddd: String, phone: String, expirationDate: String, termAccepted: Int, birthday: String,
        method: String, mothersName: String
    ): JsonCheckPlanResponse =
        request {
            service.checkPlan(
                register, order, cpf, contract, email, name, ddd, phone,
                expirationDate, termAccepted, birthday, method, mothersName
            )
        }

    suspend fun onGetHealthInsurancePhoto(token: String, checkUpdated: Int): JsonHealthInsurancePhotoResponse =
        request { service.getHealthInsurancePhoto(token, checkUpdated) }

    // =====================================================================
    // GUIA MÉDICO / REDE (legado)
    // =====================================================================
    suspend fun onGetMedicalGuideOptions(): JsonMedicalGuideOptionsResponse =
        request { service.getMedicalGuideOptions() }

    suspend fun onGetMedicalGuideList(
        codePlan: Int, codeCity: Int, codeSpecialityService: Int, ownNetwork: Int,
        latitude: Double?, longitude: Double?, professionalClassOption: String?, serviceTypeOption: String?,
        establishmentType: String?, specialityType: String?, addressFilter: String?, neighborhoodFilter: String?,
        zipcodeFilter: String?, numberOnBoardFilter: String?, profFantasyFilter: String?, cnpjFilter: String?,
        phonesFilter: String?, qualificationsSearch: String?, token: String?
    ): JsonMedicalGuideListResponse =
        request {
            service.getMedicalGuideList(
                codePlan, codeCity, codeSpecialityService, ownNetwork, latitude, longitude,
                professionalClassOption, serviceTypeOption, establishmentType, specialityType,
                addressFilter, neighborhoodFilter, zipcodeFilter, numberOnBoardFilter, profFantasyFilter,
                cnpjFilter, phonesFilter, qualificationsSearch, token
            )
        }

    suspend fun onGetMedicalGuideDetails(proUF: String, prsCod: String, proCls: String, proCod: String): JsonMedicalGuidePlansResponse =
        request { service.getMedicalGuideDetails(proUF, prsCod, proCls, proCod) }

    suspend fun onGetOwnNetwork(token: String?): JsonOwnNetworkResponse =
        request { service.getOwnNetwork(token) }

    suspend fun onGetUnits(token: String?): JsonUnitsResponse =
        request { service.getUnits(token) }

    suspend fun onGetBanners(url: String, returnImage: Boolean, id: Int): JsonGetBannersResponse =
        request { service.getBanners(url, returnImage, id) }

    // =====================================================================
    // FAVORITOS (legado)
    // =====================================================================
    suspend fun onAddToFavorite(
        token: String, method: String, proCls: String, proCod: String, proUF: String,
        prsCod: String, prsSeq: String, esCod: String, type: String
    ): JsonFavoriteResponse =
        request { service.addToFavorite(token, method, proCls, proCod, proUF, prsCod, prsSeq, esCod, type) }

    suspend fun onRemoveFromFavorites(
        token: String, method: String, proCls: String, proCod: String, proUF: String,
        prsCod: String, prsSeq: String, esCod: String, type: String
    ): JsonRemoveFromFavoritesResponse =
        request { service.removeFromFavorites(token, method, proCls, proCod, proUF, prsCod, prsSeq, esCod, type) }

    suspend fun onGetFavorites(token: String): JsonFavoritesResponse =
        request { service.getFavorites(token) }

    // =====================================================================
    // PERFIL / CONTA (legado)
    // =====================================================================
    suspend fun onUpdateAvatar(token: String, imageString: String): JsonUpdateAvatarResponse =
        request { service.updateAvatar(token, imageString) }

    suspend fun onDoLogoff(token: String): JsonLogoffResponse =
        request { service.doLogoff(token) }

    suspend fun onEditPassword(token: String, password: String): JsonEditPasswordResponse =
        request { service.editPassword(token, password) }

    suspend fun onEditPhone(token: String, codeArea: String, phone: String): JsonEditPhoneResponse =
        request { service.editPhone(token, codeArea, phone) }

    suspend fun onValidateUserConnected(registration: String, order: String, token: String): UserConnected =
        request { service.validateUserConnected(registration, order, token) }

    suspend fun onValidateButtons(body: ValidateButtonBody): ValidateButtons =
        request { service.validateButtons(body) }

    suspend fun onDeleteUser(body: DeleteUserRequest): ComumModel =
        request { service.deleteUser(body) }


    // =====================================================================
    // REQUEST HELPER
    // =====================================================================
    private suspend fun <T> request(call: suspend () -> Response<T>): T =
        coroutineScope {
            LogManager.d(TAG_REPO, "Chamando API: $call")
            val response = try {
                call.invoke()
            } catch (ex: Exception) {
                LogManager.e(TAG_REPO, "Erro de transporte ao chamar API: ${ex.message}", ex)
                throw ServerErrorResponse.verifyError(ex)
            }
            LogManager.d(TAG_REPO, "RESPONSE code=${response.code()} body=${response.body()}")
            if (!response.isSuccessful) {
                LogManager.e(TAG_REPO, "RESPONSE HTTP ${response.code()} - erroBody=${response.errorBody()}")
                throw ServerErrorResponse.verifyError(response.errorBody(), response.code())
            }
            @Suppress("UNCHECKED_CAST")
            response.body() as T
        }

    private companion object {
        const val TAG_REPO = "AppRepository"
    }
}
package br.com.data.datasource.networking

import br.com.domain.model.*
import io.reactivex.Completable
import io.reactivex.Flowable

interface NetworkingDatasource {
    fun doLogin(register: String, order: String, password: String, firebaseToken: String): Flowable<Pair<Person, String>>
    fun recoverPassword(register: String, order: String, email: String): Completable
    fun registerPassword(person: Person, plan: Plan): Flowable<String>
    //fun getHealthInsurancePhoto(token: String): Flowable<String>
    fun getHealthInsurancePhoto(token: String): Flowable<HealthInsurancePhotoList>
    fun getMedicalGuideOptions(): Flowable<MedicalGuideOptions>
    fun getMedicalGuideList(codePlan: Int, codeCity: Int, codeSpecialityService: Int,//Andre
                            ownNetwork: Int, latitude: Double?, longitude: Double?, codeProfessionalClass: String?, codeServiceType: String?,
                            codeEstablishmentType: String?,specialityType: String?,
                            address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,//Andre
                            prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?,
                            token: String?): Flowable<MedicalGuideList>

    fun getOwnNetwork(token: String?): Flowable<OwnNetworkList>
    fun getUnits(token: String?): Flowable<UnitsList>//Andre
    fun getMedicalGuideDetails(proUF: String, prsCod: String, proCls: String, proCod: String): Flowable<List<MedicalGuidePlan>>
    fun addToFavorite(token: String, establishment: Establishment): Completable
    fun getFavorites(token: String): Flowable<List<Establishment>>
    fun updateAvatar(token: String, imageString: String): Completable
    fun doLogoff(token: String): Completable
    fun editPassword(token: String, password: String): Completable
    fun editPhone(token: String, codeArea: String, phone: String): Completable
    fun getBanners(): Flowable<List<Banner>>
    fun getPerson(token: String): Flowable<Person>
    fun checkPlan(person: Person, plan: Plan): Completable
    fun removeFromFavorites(establishment: Establishment, token: String): Completable
    fun onValidateUserConnected(registration: String, order: String): Flowable<UserConnected>
    fun onValidateButtons(token: String): Flowable<ValidateButtons>
}
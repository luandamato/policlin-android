package br.com.domain.repository

import br.com.domain.model.*
import io.reactivex.Completable
import io.reactivex.Flowable

interface Repository {
    fun doLogin(register: String, order: String, password: String, firebaseToken: String): Completable
    fun doLogoff(): Completable
    fun recoverPassword(register: String, order: String, email: String): Completable
    fun registerPassword(person: Person, plan: Plan): Flowable<String>
    fun getToken(): Flowable<String>
    fun getCurrentPerson(): Flowable<Person>
    //fun getHealthInsurancePhoto(): Flowable<String>
    fun getHealthInsurancePhoto(): Flowable<HealthInsurancePhotoList>
    fun getMedicalGuideOptions(): Flowable<MedicalGuideOptions>
    fun getMedicalGuideList(codePlan: Int, codeCity: Int, codeSpecialityService: Int,
                            ownNetwork: Int, latitude: Double?, longitude: Double?, codeProfessionalClass: String?, codeEstablishmentType: String?, codeServiceType: String?, specialityType:String?,
                            address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,//Andre
                            prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String? //Andre




    ): Flowable<MedicalGuideList> //Andre

    fun getOwnNetwork(): Flowable<OwnNetworkList>
    fun getUnits(): Flowable<UnitsList> //Andre
    fun getMedicalGuidePlans(proUF: String, prsCod: String, proCls: String, proCod: String): Flowable<List<MedicalGuidePlan>>
    fun setNotificationPreference(value: Boolean): Completable
    fun setLocationPreference(value: Boolean): Completable
    fun getBooleanPreference(key: String, defaultValue: Boolean): Flowable<Boolean>
    fun addToFavorites(establishment: Establishment): Completable
    fun getFavorites(): Flowable<List<Establishment>>
    fun updateAvatar(imageString: String): Completable
    fun editPassword(password: String): Completable
    fun updatePhone(codeArea: String, phone: String): Completable
    fun getBanners(): Flowable<List<Banner>>
    fun checkPlan(person: Person, plan: Plan): Completable
    fun removeFromFavorites(establishment: Establishment): Completable
    fun validateUserConnected(registration: String, order: String): Flowable<UserConnected>
    fun validateButtons(): Flowable<ValidateButtons>

}
package br.com.policlinsaude.data

import br.com.policlinsaude.data.datasource.networking.NetworkingDatasource
import br.com.policlinsaude.data.datasource.preferences.PreferencesDatasource
import br.com.policlinsaude.domain.AppConstants
import br.com.policlinsaude.model.*
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.model.Person
import br.com.policlinsaude.domain.model.Plan
import io.reactivex.Completable
import io.reactivex.Flowable

class RepositoryImpl(private val networkingDatasource: NetworkingDatasource,
                     private val preferencesDatasource: PreferencesDatasource) : Repository {

    override fun getToken(): Flowable<String> = preferencesDatasource.getToken()

    override fun getCurrentPerson(): Flowable<Person> = preferencesDatasource.getToken()
            .flatMap { token ->
                networkingDatasource.getPerson(token)
                    .firstOrError()
                    .flatMap { person ->
                        preferencesDatasource.savePerson(person).toSingleDefault(person)
                    }
                    .toFlowable()
                    .onErrorResumeNext(preferencesDatasource.getPerson())
            }

    override fun recoverPassword(register: String, order: String, email: String): Completable = networkingDatasource
            .recoverPassword(register = register, order = order, email = email)

    override fun doLogin(register: String, order: String, password: String, firebaseToken: String, osVersion: String): Completable = networkingDatasource
            .doLogin(register = register, order = order, password = password, firebaseToken = firebaseToken, osVersion = osVersion)
            .flatMapCompletable {
                preferencesDatasource.savePerson(it.first)
                        .andThen(preferencesDatasource.saveToken(it.second))
            }

    override fun doLogoff(): Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.doLogoff(it) }
            .onErrorComplete()
            .andThen(preferencesDatasource.removeToken())
            .andThen(preferencesDatasource.removePerson())

    override fun registerPassword(person: Person, plan: Plan): Flowable<String> = networkingDatasource.registerPassword(person = person, plan = plan)

    override fun getHealthInsurancePhoto(): Flowable<HealthInsurancePhotoList> = preferencesDatasource.getToken()
            .flatMap { networkingDatasource.getHealthInsurancePhoto(it) }

    override fun getMedicalGuideOptions(): Flowable<MedicalGuideOptions> = networkingDatasource.getMedicalGuideOptions()

    override fun getMedicalGuideList(
        codePlan: Int, codeCity: Int, codeSpecialityService: Int,
        ownNetwork: Int, latitude: Double?, longitude: Double?, codeProfessionalClass: String?, codeServiceType: String?,
        codeEstablishmentType: String?, specialityType: String?,
        address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,
        prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?) 
    : Flowable<MedicalGuideList> = preferencesDatasource.getToken().onErrorReturnItem("").flatMap { token ->
        networkingDatasource.getMedicalGuideList(
            codePlan, codeCity, codeSpecialityService, ownNetwork, latitude, longitude,
            codeProfessionalClass, codeServiceType, codeEstablishmentType, specialityType,
            address_filter, neighborhood_filter, zipcode_filter, number_on_the_board_filter,
            prof_fantasy_filter, cnpj_filter, phones_filter, qualificationsSearch,
            if (token.isEmpty()) null else token
        )
    }

    override fun getOwnNetwork(): Flowable<OwnNetworkList> = preferencesDatasource.getToken().onErrorReturnItem("").flatMap { token ->
        networkingDatasource.getOwnNetwork(if (token.isEmpty()) null else token)
    }

    override fun getUnits(): Flowable<UnitsList> = preferencesDatasource.getToken().onErrorReturnItem("").flatMap { token ->
        networkingDatasource.getUnits(if (token.isEmpty()) null else token)
    }

    override fun getMedicalGuidePlans(proUF: String, prsCod: String, proCls: String, proCod: String): Flowable<List<MedicalGuidePlan>> = networkingDatasource.getMedicalGuideDetails(proUF, prsCod, proCls, proCod)

    override fun setNotificationPreference(value: Boolean): Completable = preferencesDatasource.putBoolean(AppConstants.PREFERENCES_NOTIFICATION, value)

    override fun setLocationPreference(value: Boolean): Completable = preferencesDatasource.putBoolean(AppConstants.PREFERENCES_LOCATION, value)

    override fun getBooleanPreference(key: String, defaultValue: Boolean): Flowable<Boolean> = preferencesDatasource.getBoolean(key, defaultValue)

    override fun addToFavorites(establishment: Establishment): Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.addToFavorite(it, establishment) }

    override fun getFavorites(): Flowable<List<Establishment>> = preferencesDatasource.getToken()
            .flatMap { networkingDatasource.getFavorites(it) }

    override fun updateAvatar(imageString: String): Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.updateAvatar(it, imageString) }

    override fun editPassword(password: String): Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.editPassword(it, password) }

    override fun updatePhone(codeArea: String, phone: String): Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.editPhone(it, codeArea, phone) }

    override fun getBanners(): Flowable<List<Banner>> = networkingDatasource.getBanners()

    override fun checkPlan(person: Person, plan: Plan): Completable = networkingDatasource.checkPlan(person, plan)

    override fun removeFromFavorites(establishment: Establishment): Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.removeFromFavorites(establishment, it) }

    override fun validateUserConnected(registration: String, order: String): Flowable<UserConnected> = preferencesDatasource.getToken()
        .flatMap { networkingDatasource.onValidateUserConnected(registration, order, it) }

    override fun validateButtons(): Flowable<ValidateButtons> = preferencesDatasource.getToken().onErrorReturnItem("")
        .flatMap { networkingDatasource.onValidateButtons(it) }
}

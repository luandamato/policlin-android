package br.com.data

import android.util.Log
import br.com.data.datasource.networking.NetworkingDatasource
import br.com.data.datasource.preferences.PreferencesDatasource
import br.com.data.datasource.realm.RealmDatasource
import br.com.domain.AppConstants
import br.com.domain.model.*
import br.com.domain.repository.Repository
import io.reactivex.Completable
import io.reactivex.Flowable

class RepositoryImpl(private val networkingDatasource: NetworkingDatasource,
                     private val realmDatasource: RealmDatasource,
                     private val preferencesDatasource: PreferencesDatasource) : Repository {


    override fun getToken(): Flowable<String> = preferencesDatasource.getToken()

    override fun getCurrentPerson(): Flowable<Person> = preferencesDatasource.getToken()
            .flatMap { token ->
                networkingDatasource.getPerson(token)
                        .firstOrError()
                        .doOnSuccess({ it -> realmDatasource.savePerson(it, token) })
                        .toFlowable()
                        .onErrorResumeNext(realmDatasource.getPersonByToken(token))
            }

    override fun recoverPassword(register: String, order: String, email: String): Completable = networkingDatasource
            .recoverPassword(register = register, order = order, email = email)

    override fun doLogin(register: String, order: String, password: String): Completable = networkingDatasource
            .doLogin(register = register, order = order, password = password)
            .flatMapCompletable {
                realmDatasource.savePerson(it.first, it.second)
                        .andThen(preferencesDatasource.saveToken(it.second))
            }

    override fun doLogoff(): Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.doLogoff(it) }
            .onErrorComplete()
            .andThen(preferencesDatasource.removeToken())


    override fun registerPassword(person: Person, plan: Plan): Flowable<String> = networkingDatasource.registerPassword(person = person, plan = plan)

//    override fun getHealthInsurancePhoto(): Flowable<String> = preferencesDatasource.getToken()
//            .flatMap { networkingDatasource.getHealthInsurancePhoto(it) }

    override fun getHealthInsurancePhoto(): Flowable<HealthInsurancePhotoList> = preferencesDatasource.getToken()
            .flatMap { networkingDatasource.getHealthInsurancePhoto(it) }

    override fun getMedicalGuideOptions(): Flowable<MedicalGuideOptions> = networkingDatasource.getMedicalGuideOptions()

    override fun getMedicalGuideList(
        codePlan: Int, codeCity: Int, codeSpecialityService: Int,
        ownNetwork: Int, latitude: Double?, longitude: Double?, codeProfessionalClass: String?, codeServiceType: String?,
        codeEstablishmentType: String?, specialityType: String?,
        address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,//Andre
        prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?) //Andre
    : Flowable<MedicalGuideList> = preferencesDatasource.getToken().onErrorReturnItem("").flatMap { token ->
        networkingDatasource.getMedicalGuideList(
            codePlan,
            codeCity,
            codeSpecialityService,
            ownNetwork,
            latitude,
            longitude,
            codeProfessionalClass,
            codeServiceType,
            codeEstablishmentType,
            specialityType,
            address_filter,
            neighborhood_filter,
            zipcode_filter,
            number_on_the_board_filter,
            prof_fantasy_filter,
            cnpj_filter,
            phones_filter,
            qualificationsSearch,
            if (token.isEmpty()) null else token
        )//Andre
    }

    override fun getOwnNetwork()
            : Flowable<OwnNetworkList> = preferencesDatasource.getToken().onErrorReturnItem("").flatMap { token ->
        networkingDatasource.getOwnNetwork(if (token.isEmpty()) null else token)
    }

    override fun getUnits()//Andre
            : Flowable<UnitsList> = preferencesDatasource.getToken().onErrorReturnItem("").flatMap { token ->
        networkingDatasource.getUnits(if (token.isEmpty()) null else token)

    }


    override fun getMedicalGuidePlans(proUF: String, prsCod: String, proCls: String, proCod: String): Flowable<List<MedicalGuidePlan>> = networkingDatasource.getMedicalGuideDetails(proUF, prsCod, proCls, proCod)

    override fun setNotificationPreference(value: Boolean)
            : Completable = preferencesDatasource.putBoolean(AppConstants.PREFERENCES_NOTIFICATION, value)

    override fun setLocationPreference(value: Boolean)
            : Completable = preferencesDatasource.putBoolean(AppConstants.PREFERENCES_LOCATION, value)

    override fun getBooleanPreference(key: String, defaultValue: Boolean)
            : Flowable<Boolean> = preferencesDatasource.getBoolean(key, defaultValue)

    override fun addToFavorites(establishment: Establishment)
            : Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.addToFavorite(it, establishment) }

    override fun getFavorites(): Flowable<List<Establishment>> = preferencesDatasource.getToken()
            .flatMap {
                networkingDatasource.getFavorites(it)
            }

    override fun updateAvatar(imageString: String)
            : Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.updateAvatar(it, imageString) }

    override fun editPassword(password: String)
            : Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.editPassword(it, password) }

    override fun updatePhone(codeArea: String, phone: String)
            : Completable = preferencesDatasource.getToken()
            .flatMapCompletable { networkingDatasource.editPhone(it, codeArea, phone) }

    override fun getBanners()
            : Flowable<List<Banner>> = networkingDatasource.getBanners()

    override fun checkPlan(person: Person, plan: Plan)
            : Completable = networkingDatasource.checkPlan(person, plan)

    override fun removeFromFavorites(establishment: Establishment)
            : Completable = preferencesDatasource.getToken()
            .flatMapCompletable {
                networkingDatasource.removeFromFavorites(establishment, it)
            }

    override fun validateUserConnected(registration: String, order: String)
        : Flowable<UserConnected> = networkingDatasource.onValidateUserConnected(registration, order)

}
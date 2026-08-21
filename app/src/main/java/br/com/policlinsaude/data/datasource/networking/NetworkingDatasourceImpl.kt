package br.com.data.datasource.networking

import android.os.Build
import android.util.Log
import br.com.data.datasource.networking.rest.NetworkingService
import br.com.data.datasource.networking.rest.mapper.*
import br.com.data.datasource.networking.rest.model.JsonUnitsResponse
import br.com.data.helper.DateHelper
import br.com.policlinsaude.domain.exception.MessageErrorException
import br.com.policlinsaude.domain.model.*
import io.reactivex.Completable
import io.reactivex.Flowable

class NetworkingDatasourceImpl(private val networkingService: NetworkingService) :
        NetworkingDatasource {

    override fun recoverPassword(register: String, order: String, email: String): Completable = networkingService.recoverPassword(register = register, order = order, email = email)
            .flatMapCompletable {
                if (validateMsgIsSuccess(it.msgInternal)) {
                    Completable.error(MessageErrorException(it.msgExternal ?: ""))
                } else {
                    Completable.complete()
                }
            }

    override fun doLogin(register: String, order: String, password: String, firebaseToken: String, osVersion: String): Flowable<Pair<Person, String>> = networkingService.login(register = register, order = order, password = password, firebaseToken = firebaseToken, osVersion = osVersion)
            .flatMap {
                val atualizacaoStatus = intArrayOf(450, 455)
                val update = !atualizacaoStatus.contains(it.actionCode ?: 0)

                if ((validateMsgIsSuccess(it.msgInternal) && update) || it.user == null || it.token == null) {
                    if(it.actionCode == 450 || it.actionCode == 455) {
                        Flowable.error(MessageErrorException("${it.actionCode}-${it.msgExternal}"))
                    } else {
                        Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                    }
                } else {
                    Flowable.just(Pair(JsonUserResponseMapper.transform(it.user), it.token))
                }
            }

    override fun registerPassword(person: Person, plan: Plan): Flowable<String> =
            networkingService.registerPassword(RegisterPasswordMapper.transform(person = person, plan = plan))
                    .flatMap {
                        if (validateMsgIsSuccess(it.msgInternal)) {
                            Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                        } else {
                            Flowable.just(it.msgExternal)
                        }
                    }

    override fun getHealthInsurancePhoto(token: String): Flowable<HealthInsurancePhotoList> = networkingService.getHealthInsurancePhoto(token = token, checkUpdated = 0)
            .flatMap {
                if (validateMsgIsSuccess(it.msgInternal)
                        || it.imgVerso == null) {
                    Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                } else {
                    Flowable.just(JsonHealthInsurancePhotoResponseMapper.transform(it))
                }
            }

    override fun getMedicalGuideOptions(): Flowable<MedicalGuideOptions> = networkingService.getMedicalGuideOptions()
            .flatMap {
                if (validateMsgIsSuccess(it.msgInternal)) {
                    Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                } else {
                    Flowable.just(JsonMedicalGuideOptionsResponseMapper.transform(it))
                }
            }

    override fun getMedicalGuideList(
        codePlan: Int, codeCity: Int, codeSpecialityService: Int,//Andre
        ownNetwork: Int, latitude: Double?, longitude: Double?, codeProfessionalClass: String?,codeServiceType: String?, codeEstablishmentType: String? ,
        specialityType :String?,
        address_filter: String?, neighborhood_filter: String?, zipcode_filter: String?, number_on_the_board_filter: String?,//Andre
        prof_fantasy_filter: String?, cnpj_filter: String?, phones_filter: String?, qualificationsSearch: String?, //Andre
        token: String?
    ): Flowable<MedicalGuideList> =
            networkingService.getMedicalGuideList(
                codePlan = codePlan, codeCity = codeCity,
                codeSpecialityService = codeSpecialityService, ownNetwork = ownNetwork,
                latitude = latitude, longitude = longitude,professionalClassOption = codeProfessionalClass,
                serviceTypeOption = codeServiceType, establishmentType = codeEstablishmentType, specialityType = specialityType ,
                address_filter = address_filter, neighborhood_filter = neighborhood_filter, zipcode_filter = zipcode_filter,
                number_on_the_board_filter = number_on_the_board_filter, prof_fantasy_fliter = prof_fantasy_filter, cnpj_filter = cnpj_filter,
                phones_filter = phones_filter, qualificationsSearch = qualificationsSearch, token = token
            ).flatMap {
                if (validateMsgIsSuccess(it.msgInternal)) {
                    Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                } else {
                    Flowable.just(JsonMedicalGuideListResponseMapper.transform(it))
                }
            }

    override fun getOwnNetwork(token: String?): Flowable<OwnNetworkList> {
        Log.d("REDEPROPRIA","DENTRO DO NETWORKDATASOURCE" )
        return networkingService.getOwnNetwork(token)
                .flatMap {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Flowable.just(JsonOwnNetworkResponseMapper.transform(it))
                    }
                }
    }

    //Andre
    override fun getUnits(token: String?): Flowable<UnitsList> {
        Log.d("UNIDADES","DENTRO DO NETWORKDATASOURCE" )

        return networkingService.getUnits(token)
                .flatMap {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Flowable.just(JsonUnitsMapper.transform(it))
                    }
                }
    }

    override fun getMedicalGuideDetails(proUF: String, prsCod: String, proCls: String, proCod: String): Flowable<List<MedicalGuidePlan>> {
        return networkingService.getMedicalGuideDetails(proUF = proUF, prsCod = prsCod, proCls = proCls, proCod = proCod)
                .flatMap {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Flowable.just(JsonMedicalGuidePlanResponseMapper.transform(it))
                    }
                }
    }

    override fun addToFavorite(token: String, establishment: Establishment): Completable {
        return networkingService.addToFavorite(token = token,
                method = "INSERIR",
                esCod = establishment.esCod,
                proCls = establishment.proCls,
                proCod = establishment.proCod,
                proUF = establishment.proUf,
                prsCod = establishment.prsCod,
                prsSeq = establishment.prsSeq,
                type = establishment.uType).flatMapCompletable {
            if (validateMsgIsSuccess(it.msgInternal)) {
                Completable.error(MessageErrorException(it.msgExternal ?: ""))
            } else {
                Completable.complete()
            }
        }
    }

    override fun getFavorites(token: String): Flowable<List<Establishment>> {
        return networkingService.getFavorites(token)
                .flatMap {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Flowable.just(JsonFavoritesResponseMapper.transform(it))
                    }
                }
    }

    override fun updateAvatar(token: String, imageString: String): Completable {
        return networkingService.updateAvatar(token, imageString)
                .flatMapCompletable {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Completable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Completable.complete()
                    }
                }
    }

    override fun doLogoff(token: String): Completable {
        return networkingService.doLogoff(token)
                .flatMapCompletable {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Completable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Completable.complete()
                    }
                }
    }

    override fun editPassword(token: String, password: String): Completable {
        return networkingService.editPassword(token, password)
                .flatMapCompletable {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Completable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Completable.complete()
                    }
                }
    }

    override fun editPhone(token: String, codeArea: String, phone: String): Completable {
        return networkingService.editPhone(token, codeArea, phone)
                .flatMapCompletable {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Completable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Completable.complete()
                    }
                }
    }

    override fun getBanners(): Flowable<List<Banner>> {
        return networkingService.getBanners("http://mpoli.tangram.net.br/appplcsa/api/Banner", true, 0)
                .flatMap {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Flowable.just(JsonBannerResponseMapper.transform(it))
                    }
                }
    }

    override fun getPerson(token: String): Flowable<Person> {
        return networkingService.getPerson(token, 1)
                .flatMap {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Flowable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Flowable.just(JsonUserResponseMapper.transform(it))
                    }
                }
    }

    override fun checkPlan(person: Person, plan: Plan): Completable {
        return networkingService.checkPlan(register = plan.register,
                order = plan.order,
                birthday = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_YYYYMMDD, person.birthday),
                contract = plan.contract,
                cpf = person.cpf,
                ddd = person.getCodeArea(),
                email = person.email,
                expirationDate = DateHelper.getStringFromDate(DateHelper.FORMAT_DATE_YYYYMMDD, plan.validationRegister),
                method = "Validar",
                name = person.name.uppercase(),
                phone = person.getPhoneWithoutCodeArea(),
                termAccepted = 1,
                mothersName = person.mothersName
        ).flatMapCompletable {
            if (validateMsgIsSuccess(it.msgInternal)) {
                Completable.error(MessageErrorException(it.msgExternal ?: ""))
            } else {
                Completable.complete()
            }
        }
    }

    override fun removeFromFavorites(establishment: Establishment, token: String): Completable {
        return networkingService.removeFromFavorites(token = token,
                type = establishment.uType,
                prsSeq = establishment.prsSeq,
                prsCod = establishment.prsCod,
                proUF = establishment.proUf,
                proCod = establishment.proCod,
                proCls = establishment.proCls,
                esCod = establishment.esCod,
                method = "DELETAR")
                .flatMapCompletable {
                    if (validateMsgIsSuccess(it.msgInternal)) {
                        Completable.error(MessageErrorException(it.msgExternal ?: ""))
                    } else {
                        Completable.complete()
                    }
                }
    }

    override fun onValidateUserConnected(registration: String, order: String, token: String): Flowable<UserConnected> {
        val version = Build.VERSION.RELEASE
        return networkingService.validateUserConnected(registration, order, token, version)
            .flatMap {
                if (!validateMsgIsSuccess(it.msgInterna)) {
                    Flowable.error(MessageErrorException(it.msgExterna ?: ""))
                } else {
                    Flowable.just(it)
                }
            }
    }

    override fun onValidateButtons(token: String): Flowable<ValidateButtons> {

        return networkingService.validateButtons(body = ValidateButtonBody(token = token))
            .flatMap {
                if (validateMsgIsSuccess(it.msgInterna)) {
                    Flowable.error(MessageErrorException(it.msgExterna ?: ""))
                } else {
                    Flowable.just(it)
                }
            }
    }

    fun validateMsgIsSuccess(message: String?): Boolean = (message.isNullOrEmpty() || message != "OK")
}
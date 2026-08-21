package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.exception.RequestValuesNotImplementedException
import br.com.policlinsaude.domain.helper.InvalidData
import br.com.policlinsaude.domain.model.MedicalGuideList
import br.com.policlinsaude.domain.repository.Repository
import br.com.policlinsaude.domain.usecase.base.BaseUseCase
import br.com.policlinsaude.domain.usecase.requestvalues.MedicalGuideListRV
import io.reactivex.Flowable

class GetMedicalGuideListUseCase(private val repository: Repository) :
        BaseUseCase<MedicalGuideListRV, MedicalGuideList>() {

    override fun executeUseCase(requestValues: MedicalGuideListRV?):
            Flowable<MedicalGuideList> {
        requestValues?.let {
            val codePlan = if (it.codePlan == InvalidData.UNINITIALIZED.getInt()) 0 else it.codePlan
            val codeCity = if (it.codeCity == InvalidData.UNINITIALIZED.getInt()) 0 else it.codeCity
            val codeSpecialityService = if (it.codeSpecialityService == InvalidData.UNINITIALIZED.getInt()) 0 else it.codeSpecialityService
            val ownNetwork = if (it.ownNetwork == InvalidData.UNINITIALIZED.getInt()) 0 else it.ownNetwork
            val latitude = if (it.latitude == InvalidData.UNINITIALIZED.getDouble()) null else it.latitude
            val longitude = if (it.longitude == InvalidData.UNINITIALIZED.getDouble()) null else it.longitude
            val codeProfessionalClass =  if (it.codeProfessionalClass == InvalidData.UNINITIALIZED.getString()) null else it.codeProfessionalClass//Andre
            val codeServiceType =  if (it.codeServiceType == InvalidData.UNINITIALIZED.getString()) null else it.codeServiceType//Andre
            val establishmentType =  if (it.codeEstablishmentType == InvalidData.UNINITIALIZED.getString()) null else it.codeEstablishmentType//Andre
            val specialityType =  if (it.specialityType == InvalidData.UNINITIALIZED.getString()) null else it.specialityType//Andre
            val address_filter = if (it.address_filter== InvalidData.UNINITIALIZED.getString()) null else it.address_filter//Andre
            val neighborhood_filter = if (it.neighborhood_filter== InvalidData.UNINITIALIZED.getString()) null else it.neighborhood_filter//Andre
            val zipcode_filter = if (it.zipcode_filter== InvalidData.UNINITIALIZED.getString()) null else it.zipcode_filter//Andre
            val number_on_the_board_filter = if (it.number_on_the_board_filter== InvalidData.UNINITIALIZED.getString()) null else it.number_on_the_board_filter//Andre
            val prof_fantasy_filter = if (it.prof_fantasy_filter== InvalidData.UNINITIALIZED.getString()) null else it.prof_fantasy_filter//Andre
            val cnpj_filter = if (it.cnpj_filter== InvalidData.UNINITIALIZED.getString()) null else it.cnpj_filter//Andre
            val phones_filter = if (it.phones_filter == InvalidData.UNINITIALIZED.getString()) null else it.phones_filter//Andre
            val qualificationsSearch = if (it.qualificationsSearch == InvalidData.UNINITIALIZED.getString()) null else it.qualificationsSearch

            return repository.getMedicalGuideList(codePlan, codeCity, codeSpecialityService,
                    ownNetwork, latitude, longitude, codeProfessionalClass, codeServiceType, establishmentType, specialityType,
                    address_filter, neighborhood_filter, zipcode_filter, number_on_the_board_filter,//Andre
                    prof_fantasy_filter, cnpj_filter, phones_filter,qualificationsSearch  //Andre
            )//Andre
        }
        return Flowable.error(RequestValuesNotImplementedException(this.javaClass.name))
    }
}
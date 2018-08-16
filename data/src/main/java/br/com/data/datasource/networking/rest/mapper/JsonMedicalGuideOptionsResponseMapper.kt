package br.com.data.datasource.networking.rest.mapper

import android.util.Log
import br.com.data.datasource.networking.rest.model.*
import br.com.data.helper.InvalidData
import br.com.domain.model.*

object JsonMedicalGuideOptionsResponseMapper {

    fun transform(jsonMedicalGuideOptionsResponse: JsonMedicalGuideOptionsResponse): MedicalGuideOptions {
        val medicalGuide = MedicalGuideOptions()

        jsonMedicalGuideOptionsResponse.city?.let {
            medicalGuide.cityOptions = transform(it)
        }

        jsonMedicalGuideOptionsResponse.plan?.let {
            medicalGuide.planOptions = transform(it)
        }

        jsonMedicalGuideOptionsResponse.specialityService?.let {
            medicalGuide.specialityServiceOptions = transform(it)
        }

        //Andre
        jsonMedicalGuideOptionsResponse.professionalClass?.let {
            medicalGuide.professionalClassOptions = transform(it)
        }

        jsonMedicalGuideOptionsResponse.serviceType?.let {
            medicalGuide.serviceTypeOptions = transform(it)
        }

        jsonMedicalGuideOptionsResponse.establishmentType?.let {
            medicalGuide.establishmentTypeOptions = transform(it)
        }

        jsonMedicalGuideOptionsResponse.qualification?.let {
            medicalGuide.qualificationOptions = transform(it)

        }
        Log.d("Andre", "Valor de lista de QUALIFICAÇÔES NA CARGA " + medicalGuide.qualificationOptions.toString())

        return medicalGuide
    }

        //Andre ----



    fun transform(jsonCityOptionsResponse: JsonCityOptionsResponse): List<CityOptions> {
        jsonCityOptionsResponse.cityOptions?.let {
            return it.map { jsonCity ->
                CityOptions(code = jsonCity.code ?: InvalidData.UNINITIALIZED.getInt(),
                        description = jsonCity.description ?: InvalidData.UNINITIALIZED.getString())
            }
        }
        return mutableListOf()
    }

    fun transform(jsonPlanOptionsResponse: JsonPlanOptionsResponse): List<PlanOptions> {
        jsonPlanOptionsResponse.planOptions?.let {
            return it.map { jsonPlan ->
                PlanOptions(codeGuide = jsonPlan.codeGuide ?: InvalidData.UNINITIALIZED.getInt(),
                        codePlan = jsonPlan.codePlan ?: InvalidData.UNINITIALIZED.getInt(),
                        description = jsonPlan.description ?: InvalidData.UNINITIALIZED.getString())
            }
        }
        return mutableListOf()
    }

    fun transform(jsonSpecialityServiceOptionsResponse: JsonSpecialityServiceOptionsResponse):  List<SpecialityServiceOptions> {
        jsonSpecialityServiceOptionsResponse.specialityServiceOptions?.let {
            return it.map { jsonSpecialityService ->
                SpecialityServiceOptions(code = jsonSpecialityService.code ?: InvalidData.UNINITIALIZED.getInt(),
                        type = jsonSpecialityService.type ?: InvalidData.UNINITIALIZED.getString(),
                        description = jsonSpecialityService.description ?: InvalidData.UNINITIALIZED.getString())
            }
        }
        return mutableListOf()
    }

    //Andre

    fun transform(jsonProfessionalClassResponse: JsonProfessionalClassResponse): List<ProfessionalClassOptions> {
        jsonProfessionalClassResponse.professionalClassOptions?.let {
            return it.map { jsonProfessionalClassService ->
                ProfessionalClassOptions(codeProfessionalClassOptions = jsonProfessionalClassService.codeProfessionalClass ?: InvalidData.UNINITIALIZED.getString(),
                                         descriptionProfessionalClassOptions = jsonProfessionalClassService.descriptionProfessionalClass ?: InvalidData.UNINITIALIZED.getString())
            }
        }
        return mutableListOf()
    }

    fun transform(jsonServiceTypeResponse: JsonServiceTypeResponse): List<ServiceTypeOptions> {
        jsonServiceTypeResponse.serviceTypeOptions?.let {
            return it.map { jsonServiceTypeService ->
                ServiceTypeOptions(codeServiceTypeOptions = jsonServiceTypeService.codeServiceType?: InvalidData.UNINITIALIZED.getString(),
                        descriptionServiceTypeOptions = jsonServiceTypeService.descriptionServiceType ?: InvalidData.UNINITIALIZED.getString())
            }
        }
        return mutableListOf()
    }

    fun transform(jsonEstablishmentTypeResponse: JsonEstablishmentTypeResponse): List<EstablishmentTypeOptions> {
        jsonEstablishmentTypeResponse.establishmentTypeOptions?.let {
            return it.map { jsonEstablishmentType ->
                EstablishmentTypeOptions(codeEstablishmentTypeOptions = jsonEstablishmentType.codeEstablishmentType?: InvalidData.UNINITIALIZED.getString(),
                        descriptionEstablishmentTypeOptions = jsonEstablishmentType.descriptionEstablishmentType ?: InvalidData.UNINITIALIZED.getString())
            }
        }
        return mutableListOf()
    }

    fun transform(jsonQualificationsForFilterResponse: JsonQualificationsForFilterResponse): List<QualificationForFilter> {
        jsonQualificationsForFilterResponse.qualificationsOptions?.let {
            return it.map { jsonQualificationFilter ->
                QualificationForFilter(imgQualificacao = jsonQualificationFilter.image?: InvalidData.UNINITIALIZED.getString(),
                       descricao = jsonQualificationFilter.description ?: InvalidData.UNINITIALIZED.getString(),
                       cod = jsonQualificationFilter.cod?: InvalidData.UNINITIALIZED.getString() )
            }
        }
        return mutableListOf()
    }


// Andre----


}
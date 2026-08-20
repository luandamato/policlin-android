package br.com.policlinsaude.ui.legacy.mapper

import android.util.Log
import br.com.domain.model.*
import br.com.policlinsaude.model.*

object PresentationMedicalGuideOptionsMapper {

    fun transform(domain: MedicalGuideOptions): PresentationMedicalGuideOptions {
        val presentation = PresentationMedicalGuideOptions()
        presentation.cityOptions = transform(domain.cityOptions.toTypedArray())
        Log.d("Andre","valor de   presentation.cityOptions: " +  presentation.cityOptions.toString() );
        presentation.planOptions = transform(domain.planOptions.toTypedArray())
        presentation.specialityServiceOptions = transform(domain.specialityServiceOptions.toTypedArray())
        presentation.professionalClassOptions = transform(domain.professionalClassOptions.toTypedArray())//Andre
        presentation.serviceTypeOptions = transform(domain.serviceTypeOptions.toTypedArray())//Andre
        presentation.establishmentTypeOptions = transform(domain.establishmentTypeOptions.toTypedArray())//Andre
        presentation.qualificationOptions = transform(domain.qualificationOptions.toTypedArray())//Andre

        Log.d("Andre","valor de  presentation.professionalClassOptions: " +  presentation.professionalClassOptions.toString() )
        Log.d("Andre","valor de   presentation.serviceTypeOptions : " +  presentation.serviceTypeOptions .toString() )
        Log.d("Andre","valor de  presentation.establishmentTypeOptions: " +  presentation.establishmentTypeOptions.toString() )
        return presentation
    }

    fun transform(cityOptions: Array<CityOptions>): List<PresentationCityOptions> {
        return cityOptions.map { domain ->
            PresentationCityOptions(code = domain.code, description = domain.description)
        }
    }

    fun transform(planOptions: Array<PlanOptions>): List<PresentationPlanOptions> {
        return planOptions.map { domain ->
            PresentationPlanOptions(codeGuide = domain.codeGuide, codePlan = domain.codePlan,
                    description = domain.description)
        }
    }

    fun transform(specialityServiceOptions: Array<SpecialityServiceOptions>): List<PresentationSpecialityServiceOptions> {
        return specialityServiceOptions.map { domain ->
            PresentationSpecialityServiceOptions(code = domain.code, type = domain.type,
                    description = domain.description )
        }
    }

    //Andre
    fun transform(professionalClassOptions: Array<ProfessionalClassOptions>): List<PresentationProfessionalClass> {
        return professionalClassOptions.map { domain ->
            PresentationProfessionalClass(codeProfessionalClass = domain.codeProfessionalClassOptions, descriptionProfessionalClass = domain.descriptionProfessionalClassOptions)
        }
    }

    fun transform(serviceTypeOptions: Array<ServiceTypeOptions>): List<PresentationServiceType> {
        return serviceTypeOptions.map { domain ->
            PresentationServiceType(codeServiceType = domain.codeServiceTypeOptions, descriptionServiceType = domain.descriptionServiceTypeOptions)
        }
    }
    fun transform(establishmentType: Array<EstablishmentTypeOptions>): List<PresentationEstablishmentType> {
        return establishmentType.map { domain ->
            PresentationEstablishmentType(codeEstablishmentType = domain.codeEstablishmentTypeOptions, descriptionEstablishmentType = domain.descriptionEstablishmentTypeOptions)
        }
    }
    fun transform(qualificationOptions: Array<QualificationForFilter>): List<PresentationQualificationForFilter> {
        return qualificationOptions.map { domain ->
            PresentationQualificationForFilter(imgQualificacao = domain.imgQualificacao, descricao = domain.descricao, cod = domain.cod)
        }
    }

    //Andre



}
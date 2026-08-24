package br.com.policlinsaude.domain.model

data class MedicalGuideOptions(var cityOptions: List<CityOptions> = mutableListOf(),
                               var planOptions: List<PlanOptions> = mutableListOf(),
                               var specialityServiceOptions: List<SpecialityServiceOptions> = mutableListOf(),
                               var professionalClassOptions: List<ProfessionalClassOptions>  = mutableListOf(),
                               var serviceTypeOptions: List<ServiceTypeOptions>  = mutableListOf(),
                               var establishmentTypeOptions: List<EstablishmentTypeOptions>  = mutableListOf(),
                               var qualificationOptions: List<QualificationForFilter> = mutableListOf()
                               )
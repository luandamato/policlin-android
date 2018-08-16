package br.com.policlinsaude.model


data class PresentationMedicalGuideOptions(var cityOptions: List<PresentationCityOptions> = mutableListOf(),
                                           var planOptions: List<PresentationPlanOptions> = mutableListOf(),
                                           var specialityServiceOptions: List<PresentationSpecialityServiceOptions> = mutableListOf(), //Andre insercao de mutablrlist
                                           var professionalClassOptions: List<PresentationProfessionalClass> = mutableListOf(),//Andre
                                           var serviceTypeOptions: List<PresentationServiceType>  = mutableListOf(),
                                           var establishmentTypeOptions: List<PresentationEstablishmentType> = mutableListOf(),
                                           var qualificationOptions: List<PresentationQualificationForFilter> = mutableListOf()
                                           )
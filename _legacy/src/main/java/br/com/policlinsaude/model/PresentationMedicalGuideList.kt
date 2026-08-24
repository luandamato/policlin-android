package br.com.policlinsaude.model


data class PresentationMedicalGuideList(var qualifications: List<PresentationQualification> = mutableListOf(),
                                        var establishments: List<PresentationEstablishment> = mutableListOf(),
                                        var medicalGuideListPlansV4: List<PresentationMedicalGuideListPlansV4> = mutableListOf())
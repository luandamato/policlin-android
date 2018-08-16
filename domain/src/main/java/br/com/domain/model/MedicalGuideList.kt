package br.com.domain.model

data class MedicalGuideList(var qualifications: List<Qualification> = mutableListOf(),
                            var establishments: List<Establishment> = mutableListOf(),
                            var  medicalGuideListPlansV4: List<MedicalGuideListPlans> = mutableListOf())
//data class MedicalGuideList(var plans: List<MedicalGuideListPlans> = mutableListOf())


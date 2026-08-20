package br.com.data.datasource.networking.rest.mapper

import br.com.data.datasource.networking.rest.model.JsonMedicalGuidePlanResponse
import br.com.data.datasource.networking.rest.model.JsonMedicalGuidePlansResponse
import br.com.data.helper.InvalidData
import br.com.domain.model.MedicalGuidePlan

/**
 * Created by lmiyagi on 3/21/18.
 */
object JsonMedicalGuidePlanResponseMapper {

    fun transform(jsonMedicalGuidePlanResponse: JsonMedicalGuidePlanResponse): MedicalGuidePlan {
        val medicalGuidePlan = MedicalGuidePlan()
        medicalGuidePlan.description = jsonMedicalGuidePlanResponse.description ?: InvalidData.UNINITIALIZED.getString()
        return medicalGuidePlan
    }

    fun transform(jsonMedicalGuidePlansResponse: JsonMedicalGuidePlansResponse): List<MedicalGuidePlan> {
        jsonMedicalGuidePlansResponse.plans?.let {
            val plans = ArrayList<MedicalGuidePlan>()
            it.forEach {
                plans.add(transform(it))
            }
            return plans
        }
        return mutableListOf()
    }
}
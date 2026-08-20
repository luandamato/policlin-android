package br.com.policlinsaude.domain.repository

import br.com.policlinsaude.domain.model.MedicalGuide
import br.com.policlinsaude.domain.model.Person
import br.com.policlinsaude.domain.model.Plan
import io.reactivex.Flowable
import io.reactivex.Single

interface PersonRepository {
    fun getPerson(token: String): Single<Person>
    fun updatePerson(token: String, person: Person): Single<Person>
}

interface MedicalGuideRepository {
    fun getMedicalGuides(token: String, filters: Map<String, String>): Flowable<List<MedicalGuide>>
    fun getMedicalGuideDetails(token: String, guideId: String): Single<MedicalGuide>
    fun addToFavorite(token: String, guideId: String): Single<Unit>
    fun removeFavorite(token: String, guideId: String): Single<Unit>
}

interface PlanRepository {
    fun getPlans(token: String): Flowable<List<Plan>>
    fun getPlanDetails(token: String, planId: String): Single<Plan>
}

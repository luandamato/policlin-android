package br.com.policlinsaude.domain.usecase

import br.com.policlinsaude.domain.model.MedicalGuide
import br.com.policlinsaude.domain.repository.MedicalGuideRepository
import io.reactivex.Flowable
import javax.inject.Inject

class GetMedicalGuidesUseCase @Inject constructor(
    private val repository: MedicalGuideRepository
) {
    operator fun invoke(token: String, filters: Map<String, String>): Flowable<List<MedicalGuide>> {
        return repository.getMedicalGuides(token, filters)
    }
}

class GetMedicalGuideDetailsUseCase @Inject constructor(
    private val repository: MedicalGuideRepository
) {
    operator fun invoke(token: String, guideId: String) =
        repository.getMedicalGuideDetails(token, guideId)
}

class AddToFavoriteUseCase @Inject constructor(
    private val repository: MedicalGuideRepository
) {
    operator fun invoke(token: String, guideId: String) =
        repository.addToFavorite(token, guideId)
}

class RemoveFavoriteUseCase @Inject constructor(
    private val repository: MedicalGuideRepository
) {
    operator fun invoke(token: String, guideId: String) =
        repository.removeFavorite(token, guideId)
}

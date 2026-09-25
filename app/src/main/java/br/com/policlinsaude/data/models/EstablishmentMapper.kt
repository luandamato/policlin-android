package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

/**
 * Mapeadores para a feature de Unidades/Guia Médico.
 */

fun JsonEstablishmentResponse.toPresentation(): PresentationEstablishment {
    return PresentationEstablishment(
        name = name ?: InvalidData.UNINITIALIZED.getString(),
        socialName = socialName ?: InvalidData.UNINITIALIZED.getString(),
        cnpj = cnpj ?: InvalidData.UNINITIALIZED.getString(),
        qualifications = qualifications?.map { it.toPresentation() } ?: emptyList(),
        type = type ?: InvalidData.UNINITIALIZED.getString(),
        title = title ?: InvalidData.UNINITIALIZED.getString(),
        subTitle = subTitle ?: InvalidData.UNINITIALIZED.getString(),
        speciality = speciality ?: InvalidData.UNINITIALIZED.getString(),
        publicPlace = publicPlace ?: InvalidData.UNINITIALIZED.getString(),
        number = number ?: InvalidData.UNINITIALIZED.getString(),
        neighborhood = neighborhood ?: InvalidData.UNINITIALIZED.getString(),
        zipCode = zipCode ?: InvalidData.UNINITIALIZED.getString(),
        complement = complement ?: InvalidData.UNINITIALIZED.getString(),
        city = city ?: InvalidData.UNINITIALIZED.getString(),
        state = state ?: InvalidData.UNINITIALIZED.getString(),
        phoneOne = phoneOne ?: InvalidData.UNINITIALIZED.getString(),
        phoneTwo = phoneTwo ?: InvalidData.UNINITIALIZED.getString(),
        typePhoneOne = typePhoneOne ?: InvalidData.UNINITIALIZED.getString(),
        typePhoneTwo = typePhoneTwo ?: InvalidData.UNINITIALIZED.getString(),
        latitude = latitude ?: InvalidData.UNINITIALIZED.getString(),
        longitude = longitude ?: InvalidData.UNINITIALIZED.getString(),
        distance = distance ?: InvalidData.UNINITIALIZED.getDouble(),
        photoFront = photoFront ?: InvalidData.UNINITIALIZED.getString(),
        cidCod = cidCod ?: InvalidData.UNINITIALIZED.getString(),
        esCod = esCod ?: InvalidData.UNINITIALIZED.getString(),
        prsSeq = prsSeq ?: InvalidData.UNINITIALIZED.getString(),
        prsCod = prsCod ?: InvalidData.UNINITIALIZED.getString(),
        proUf = proUf ?: InvalidData.UNINITIALIZED.getString(),
        proCls = proCls ?: InvalidData.UNINITIALIZED.getString(),
        proCod = proCod ?: InvalidData.UNINITIALIZED.getString(),
        isOwnNetwork = isOwnNetwork ?: false,
        favorited = favorited ?: false,
        uType = uType ?: InvalidData.UNINITIALIZED.getString()
    )
}

fun JsonQualificationResponse.toPresentation(): PresentationQualification {
    return PresentationQualification(
        image = image ?: InvalidData.UNINITIALIZED.getString(),
        initial = initial ?: InvalidData.UNINITIALIZED.getString(),
        description = description ?: InvalidData.UNINITIALIZED.getString()
    )
}

package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationEstablishmentType(var codeEstablishmentType: String = InvalidData.UNINITIALIZED.getString(),
                                         var descriptionEstablishmentType: String = InvalidData.UNINITIALIZED.getString()) {

    override fun toString(): String {
        return descriptionEstablishmentType
    }
}
package br.com.policlinsaude.model

import br.com.policlinsaude.domain.helper.InvalidData
import java.io.Serializable

data class PresentationSpecialityServiceOptions(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                                                var description: String = InvalidData.UNINITIALIZED.getString(),
                                                var type: String = InvalidData.UNINITIALIZED.getString())
    : Serializable {

    override fun toString(): String {
        return description
    }
}
package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationSpecialityServiceOptions(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                                                var description: String = InvalidData.UNINITIALIZED.getString(),
                                                var type: String = InvalidData.UNINITIALIZED.getString())
    {

    override fun toString(): String {
        return description
    }
}
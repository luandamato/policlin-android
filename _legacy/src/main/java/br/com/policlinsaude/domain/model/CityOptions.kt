package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData

data class CityOptions(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                       var description: String = InvalidData.UNINITIALIZED.getString())
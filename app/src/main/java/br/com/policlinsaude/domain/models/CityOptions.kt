package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

data class CityOptions(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                       var description: String = InvalidData.UNINITIALIZED.getString())
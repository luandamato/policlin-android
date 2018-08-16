package br.com.domain.model

import br.com.domain.helper.InvalidData

data class CityOptions(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                       var description: String = InvalidData.UNINITIALIZED.getString())
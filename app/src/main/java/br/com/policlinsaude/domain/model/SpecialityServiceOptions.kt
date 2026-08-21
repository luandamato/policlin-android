package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData

data class SpecialityServiceOptions(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                                    var description: String = InvalidData.UNINITIALIZED.getString(),
                                    var type: String = InvalidData.UNINITIALIZED.getString())



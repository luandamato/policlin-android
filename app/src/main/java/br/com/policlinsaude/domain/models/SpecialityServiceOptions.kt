package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

data class SpecialityServiceOptions(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                                    var description: String = InvalidData.UNINITIALIZED.getString(),
                                    var type: String = InvalidData.UNINITIALIZED.getString())



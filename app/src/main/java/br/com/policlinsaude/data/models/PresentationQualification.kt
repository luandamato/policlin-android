package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationQualification(var image: String = InvalidData.UNINITIALIZED.getString(),
                                     var initial: String = InvalidData.UNINITIALIZED.getString(),
                                     var description: String = InvalidData.UNINITIALIZED.getString()
) {


}
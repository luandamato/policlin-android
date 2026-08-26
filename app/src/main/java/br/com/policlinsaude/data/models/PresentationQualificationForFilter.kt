package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationQualificationForFilter(var imgQualificacao: String = InvalidData.UNINITIALIZED.getString(),
                                              var descricao: String = InvalidData.UNINITIALIZED.getString(),
                                              var cod: String = InvalidData.UNINITIALIZED.getString()
) {


}
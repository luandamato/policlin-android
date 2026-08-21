package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData

data class QualificationForFilter(var imgQualificacao: String = InvalidData.UNINITIALIZED.getString(),
                                  var descricao: String = InvalidData.UNINITIALIZED.getString(),
                                  var cod: String = InvalidData.UNINITIALIZED.getString())
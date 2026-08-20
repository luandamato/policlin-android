package br.com.domain.model

import br.com.domain.helper.InvalidData

data class QualificationForFilter(var imgQualificacao: String = InvalidData.UNINITIALIZED.getString(),
                                  var descricao: String = InvalidData.UNINITIALIZED.getString(),
                                  var cod: String = InvalidData.UNINITIALIZED.getString())
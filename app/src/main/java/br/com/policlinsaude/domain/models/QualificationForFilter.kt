package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

data class QualificationForFilter(var imgQualificacao: String = InvalidData.UNINITIALIZED.getString(),
                                  var descricao: String = InvalidData.UNINITIALIZED.getString(),
                                  var cod: String = InvalidData.UNINITIALIZED.getString())
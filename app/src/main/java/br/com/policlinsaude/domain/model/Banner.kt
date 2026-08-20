package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData

data class Banner(var id: String = InvalidData.UNINITIALIZED.getString(),
                  var image: String = InvalidData.UNINITIALIZED.getString(),
                  var url: String = InvalidData.UNINITIALIZED.getString() )

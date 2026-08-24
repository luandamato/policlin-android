package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

data class Banner(var id: String = InvalidData.UNINITIALIZED.getString(),
                  var image: String = InvalidData.UNINITIALIZED.getString(),
                  var url: String = InvalidData.UNINITIALIZED.getString() )

package br.com.domain.model

import br.com.domain.helper.InvalidData

data class Banner(var id: String = InvalidData.UNINITIALIZED.getString(),
                  var image: String = InvalidData.UNINITIALIZED.getString(),
                  var url: String = InvalidData.UNINITIALIZED.getString() )
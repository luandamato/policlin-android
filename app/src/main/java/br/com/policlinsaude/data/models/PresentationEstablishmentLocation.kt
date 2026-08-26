package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

/**
 * Created by lmiyagi on 11/04/18.
 */
data class PresentationEstablishmentLocation(val latitude: Double = 0.0,
                                             val longitude: Double = 0.0,
                                             val title: String = InvalidData.UNINITIALIZED.getString())
 {




}
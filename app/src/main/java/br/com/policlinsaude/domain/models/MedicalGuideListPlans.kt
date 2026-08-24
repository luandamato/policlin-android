package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData
import java.util.*

/**
 *
 * Andre em 31/05/2018
 */

/*root: plano -array

campos plano: PlanoNome
              _PlanoCod
              cidades -->array
                      CidadeNome
                      _CIDCOD
                      tiposervico --> array
                      TIPOSERV
                      especialidade --> array
                                    EspecialidadeNome
                                    _ESCOD
                                    _Ent_Tipo
                                            guiamedico --> array*/

data class MedicalGuideListPlans(var planName: String = InvalidData.UNINITIALIZED.getString(),
                                 var planCod: String = InvalidData.UNINITIALIZED.getString(),
                                 var cities : List<MedicalGuideListCities> = mutableListOf())
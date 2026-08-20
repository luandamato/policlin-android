package br.com.domain.model

import br.com.domain.helper.InvalidData

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


data class MedicalGuideListCities(var cityName: String = InvalidData.UNINITIALIZED.getString(),
                                  var cityCod: String = InvalidData.UNINITIALIZED.getString(),
                                  var serviceType: List<MedicalGuideListServiceTypes> = mutableListOf()
                                  )
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


data class MedicalGuideListServiceTypes(var serviceType: String = InvalidData.UNINITIALIZED.getString(),
                                        var speciality: List<MedicalGuideListSpecialities> = mutableListOf()
                                  )
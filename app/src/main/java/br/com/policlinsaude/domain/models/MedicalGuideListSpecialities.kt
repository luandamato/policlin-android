package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

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


data class MedicalGuideListSpecialities(var specialityName: String = InvalidData.UNINITIALIZED.getString(),
                                        var specialityCod: String = InvalidData.UNINITIALIZED.getString(),//List<MedicalGuideListSpecialities> = mutableListOf(),
                                        var ent_tipo: String = InvalidData.UNINITIALIZED.getString(),
                                        var medicalGuide: List<Establishment> = mutableListOf()
                                  )
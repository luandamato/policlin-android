package br.com.data.datasource.networking.rest.mapper

import android.util.Log
import br.com.data.datasource.networking.rest.model.*
import br.com.data.helper.InvalidData
import br.com.policlinsaude.domain.model.*

object JsonMedicalGuideListResponseMapper {

    fun transform(jsonMedicalGuideListResponse: JsonMedicalGuideListResponse): MedicalGuideList {
        val medicalGuideList = MedicalGuideList()

         /*   jsonMedicalGuideListResponse.establishments?.let {
              medicalGuideList.establishments = transform(it.toTypedArray())
          }*/

             jsonMedicalGuideListResponse.qualifications?.let {
               medicalGuideList.qualifications = transform(it.toTypedArray())
           }

          jsonMedicalGuideListResponse.plans?.let {
               medicalGuideList. medicalGuideListPlansV4 = transform(it.toTypedArray())
           }
        Log.d("AndreMapper","-----------------------------------")
        Log.d("AndreMapper", "Valor de medicalGuideList: " + medicalGuideList.toString())
        Log.d("AndreMapper","-----------------------------------")
        return medicalGuideList
    }

          /*  fun transform(jsonEstablishmentResponse: Array<JsonEstablishmentResponse>): List<Establishment> {
        return jsonEstablishmentResponse.map {
            transform(it)
        }
    }*/

    fun transform(jsonEstablishment: JsonEstablishmentResponse): Establishment {
        return Establishment(name = jsonEstablishment.name ?: InvalidData.UNINITIALIZED.getString(),
                socialName = jsonEstablishment.socialName
                        ?: InvalidData.UNINITIALIZED.getString(),
                cnpj = jsonEstablishment.cnpj ?: InvalidData.UNINITIALIZED.getString(),
                qualifications = transform((jsonEstablishment.qualifications?: mutableListOf()).toTypedArray()),
                type = jsonEstablishment.type ?: InvalidData.UNINITIALIZED.getString(),
                title = jsonEstablishment.title ?: InvalidData.UNINITIALIZED.getString(),
                subTitle = jsonEstablishment.subTitle ?: InvalidData.UNINITIALIZED.getString(),
                speciality = jsonEstablishment.speciality
                        ?: InvalidData.UNINITIALIZED.getString(),
                publicPlace = jsonEstablishment.publicPlace
                        ?: InvalidData.UNINITIALIZED.getString(),
                number = jsonEstablishment.number ?: InvalidData.UNINITIALIZED.getString(),
                neighborhood = jsonEstablishment.neighborhood
                        ?: InvalidData.UNINITIALIZED.getString(),
                zipCode = jsonEstablishment.zipCode ?: InvalidData.UNINITIALIZED.getString(),
                complement = jsonEstablishment.complement
                        ?: InvalidData.UNINITIALIZED.getString(),
                city = jsonEstablishment.city ?: InvalidData.UNINITIALIZED.getString(),
                state = jsonEstablishment.state ?: InvalidData.UNINITIALIZED.getString(),
                phoneOne = jsonEstablishment.phoneOne ?: InvalidData.UNINITIALIZED.getString(),
                phoneTwo = jsonEstablishment.phoneTwo ?: InvalidData.UNINITIALIZED.getString(),
                typePhoneOne =  jsonEstablishment.typePhoneOne ?: InvalidData.UNINITIALIZED.getString(),
                typePhoneTwo =  jsonEstablishment.typePhoneTwo ?: InvalidData.UNINITIALIZED.getString(),
                latitude = jsonEstablishment.latitude ?: InvalidData.UNINITIALIZED.getString(),
                longitude = jsonEstablishment.longitude
                        ?: InvalidData.UNINITIALIZED.getString(),
                distance = jsonEstablishment.distance ?: InvalidData.UNINITIALIZED.getDouble(),
                photoFront = jsonEstablishment.photoFront
                        ?: InvalidData.UNINITIALIZED.getString(),
                cidCod = jsonEstablishment.cidCod ?: InvalidData.UNINITIALIZED.getString(),
                esCod = jsonEstablishment.esCod ?: InvalidData.UNINITIALIZED.getString(),
                prsSeq = jsonEstablishment.prsSeq ?: InvalidData.UNINITIALIZED.getString(),
                prsCod = jsonEstablishment.prsCod ?: InvalidData.UNINITIALIZED.getString(),
                proUf = jsonEstablishment.proUf ?: InvalidData.UNINITIALIZED.getString(),
                proCls = jsonEstablishment.proCls ?: InvalidData.UNINITIALIZED.getString(),
                proCod = jsonEstablishment.proCod ?: InvalidData.UNINITIALIZED.getString(),
                isOwnNetwork = jsonEstablishment.isOwnNetwork ?: false,
                favorited = jsonEstablishment.favorited ?: false,
                uType = jsonEstablishment.uType ?: InvalidData.UNINITIALIZED.getString())
    }


    fun transform(jsonQualificationResponse: Array<JsonQualificationResponse>): List<Qualification> {
        return jsonQualificationResponse.map { jsonQualification ->
            Qualification(image = jsonQualification.image ?: InvalidData.UNINITIALIZED.getString(),
                    initial = jsonQualification.initial ?: InvalidData.UNINITIALIZED.getString(),
                    description = jsonQualification.description
                            ?: InvalidData.UNINITIALIZED.getString())
        }
    }

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

    fun transform(jsonMedicalGuideListPlansResponse: Array<JsonMedicalGuideListPlansResponse>): List<MedicalGuideListPlans> {
        return jsonMedicalGuideListPlansResponse.map {
            transform(it)
        }
    }
    //Plano
    fun transform(jsonMedicalGuideListPlan: JsonMedicalGuideListPlansResponse): MedicalGuideListPlans {
        return MedicalGuideListPlans(planName = jsonMedicalGuideListPlan.planName ?: InvalidData.UNINITIALIZED.getString(),
                planCod = jsonMedicalGuideListPlan.planCod ?: InvalidData.UNINITIALIZED.getString(),
                cities = transform((jsonMedicalGuideListPlan.cities ?: mutableListOf()).toTypedArray()))

    }
    //Cidade
    fun transform(jsonMedicalGuideListCities: Array<JsonMedicalGuideListCitiesResponse>): List<MedicalGuideListCities> {
        return jsonMedicalGuideListCities.map { jsonMedicalGuideListCity ->
            MedicalGuideListCities(cityName = jsonMedicalGuideListCity.cityName ?: InvalidData.UNINITIALIZED.getString(),
                                   cityCod = jsonMedicalGuideListCity.cidCod ?: InvalidData.UNINITIALIZED.getString(),
                                   serviceType = transform((jsonMedicalGuideListCity.serviceType ?: mutableListOf()).toTypedArray()))
        }
    }

    //Tipo Serviço
    fun transform(jsonMedicalGuideServiceTypeResponse: Array<JsonMedicalGuideServiceTypeResponse>): List<MedicalGuideListServiceTypes> {
        return jsonMedicalGuideServiceTypeResponse.map { jsonMedicalGuideListServiceType ->
            MedicalGuideListServiceTypes( serviceType = jsonMedicalGuideListServiceType.serviceType ?: InvalidData.UNINITIALIZED.getString(),
                    speciality = transform((jsonMedicalGuideListServiceType.speciality ?: mutableListOf()).toTypedArray()))
        }
    }

    //Especialidade
    fun transform(jsonMedicalGuideSpecialitiesResponse: Array<JsonMedicalGuideSpecialitiesResponse>): List<MedicalGuideListSpecialities> {
        return jsonMedicalGuideSpecialitiesResponse.map { jsonMedicalGuideSpeciality ->
            MedicalGuideListSpecialities( specialityName = jsonMedicalGuideSpeciality.specialityName ?: InvalidData.UNINITIALIZED.getString(),
                                          specialityCod = jsonMedicalGuideSpeciality.specialityCode ?: InvalidData.UNINITIALIZED.getString(),
                                          ent_tipo =  jsonMedicalGuideSpeciality.entType ?: InvalidData.UNINITIALIZED.getString(),
                                          medicalGuide =  transform((jsonMedicalGuideSpeciality.medicalGuide ?: mutableListOf()).toTypedArray()))




        }
    }

      fun transform(jsonEstablishmentResponse: Array<JsonEstablishmentResponse>): List<Establishment> {
           return jsonEstablishmentResponse.map {
               transform(it)
           }
       }







}
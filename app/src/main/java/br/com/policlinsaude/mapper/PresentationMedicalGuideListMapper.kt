package br.com.policlinsaude.mapper

import android.util.Log
import br.com.policlinsaude.domain.model.*
import br.com.policlinsaude.model.*

object PresentationMedicalGuideListMapper {

    fun transform(domain: MedicalGuideList): PresentationMedicalGuideList {
        val presentation = PresentationMedicalGuideList()
        presentation.qualifications = transform(domain.qualifications.toTypedArray())
        Log.d("PRESENTATION","-----------------------------------")
        Log.d("PRESENTATION", "Valor de medicalGuideList dentro de Presentation: " + domain.toString())
        Log.d("PRESENTATION","-----------------------------------")

   //     presentation.establishments = transform(domain.establishments.toTypedArray())
        presentation. medicalGuideListPlansV4 = transform(domain.medicalGuideListPlansV4.toTypedArray())

        Log.d("PRESENTATION","-----------------------------------")
        Log.d("PRESENTATION", "Valor demedicalGuideListPlansV4: " + presentation.medicalGuideListPlansV4.toString())
        Log.d("PRESENTATION","-----------------------------------")



        return presentation
    }
//Andre
    fun transform(establishments: Array<Establishment>): List<PresentationEstablishment> {

        Log.d("PRESENTATION","-----------------------------------")
        Log.d("PRESENTATION", "TRANSFORM ESTABLISHMENT  Inicial - PresentationEstablishment"  )
        Log.d("PRESENTATION","-----------------------------------")

        return establishments.map { domain ->
            PresentationEstablishment(name = domain.name,
                    socialName = domain.socialName,
                    cnpj = domain.cnpj,
                    qualifications = transform(domain.qualifications.toTypedArray()),
                    type = domain.type,
                    title = domain.title,
                    subTitle = domain.subTitle,
                    speciality = domain.speciality,
                    publicPlace = domain.publicPlace,
                    number = domain.number,
                    neighborhood = domain.neighborhood,
                    zipCode = domain.zipCode,
                    complement = domain.complement,
                    city = domain.city,
                    state = domain.state,
                    phoneOne = domain.phoneOne,
                    phoneTwo = domain.phoneTwo,
                    typePhoneOne = domain.typePhoneOne,
                    typePhoneTwo = domain.typePhoneTwo,
                    latitude = domain.latitude,
                    longitude = domain.longitude,
                    distance = domain.distance,
                    photoFront = domain.photoFront,
                    cidCod = domain.cidCod,
                    esCod = domain.esCod,
                    prsSeq = domain.prsSeq,
                    prsCod = domain.prsCod,
                    proUf = domain.proUf,
                    proCls = domain.proCls,
                    proCod = domain.proCod,
                    isOwnNetwork = domain.isOwnNetwork,
                    favorited = domain.favorited,
                    uType = domain.uType)
        }
    }

    fun transform(presentationEstablishment: PresentationEstablishment): Establishment {

        Log.d("PRESENTATION","-----------------------------------")
        Log.d("PRESENTATION", "TRANSFORM ESTABLISHMENT  Inicial - Establishment"  )
        Log.d("PRESENTATION","-----------------------------------")
        return Establishment(name = presentationEstablishment.name,
                socialName = presentationEstablishment.socialName,
                cnpj = presentationEstablishment.cnpj,
                qualifications = transform(presentationEstablishment.qualifications.toTypedArray()),
                type = presentationEstablishment.type,
                title = presentationEstablishment.title,
                subTitle = presentationEstablishment.subTitle,
                speciality = presentationEstablishment.speciality,
                publicPlace = presentationEstablishment.publicPlace,
                number = presentationEstablishment.number,
                neighborhood = presentationEstablishment.neighborhood,
                zipCode = presentationEstablishment.zipCode,
                complement = presentationEstablishment.complement,
                city = presentationEstablishment.city,
                state = presentationEstablishment.state,
                phoneOne = presentationEstablishment.phoneOne,
                phoneTwo = presentationEstablishment.phoneTwo,
                latitude = presentationEstablishment.latitude,
                longitude = presentationEstablishment.longitude,
                distance = presentationEstablishment.distance,
                photoFront = presentationEstablishment.photoFront,
                cidCod = presentationEstablishment.cidCod,
                esCod = presentationEstablishment.esCod,
                prsSeq = presentationEstablishment.prsSeq,
                prsCod = presentationEstablishment.prsCod,
                proUf = presentationEstablishment.proUf,
                proCls = presentationEstablishment.proCls,
                proCod = presentationEstablishment.proCod,
                isOwnNetwork = presentationEstablishment.isOwnNetwork,
                favorited = presentationEstablishment.favorited,
                uType = presentationEstablishment.uType)

    }

    fun transform(qualifications: Array<Qualification>): List<PresentationQualification> {
        return qualifications.map { domain ->
            PresentationQualification(image = domain.image,
                    initial = domain.initial,
                    description = domain.description)
        }
    }

    fun transform(presentationQualifications: Array<PresentationQualification>): List<Qualification> {


        return presentationQualifications.map { presentation ->
            Qualification(image = presentation.image,
                    initial = presentation.initial,
                    description = presentation.description)
        }
    }
//-----------------------------------------------------------------------------------

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


//-----------------------------------------------------------------------------------

   fun transform(medicalGuideListPlansV4 : Array<MedicalGuideListPlans>): List<PresentationMedicalGuideListPlansV4> {
        return medicalGuideListPlansV4.map { domain ->

            PresentationMedicalGuideListPlansV4(
                    planName = domain.planName,
                    planCod  = domain.planCod,
                    cities   = transform(domain.cities.toTypedArray()))

        }
    }

    //Cidade
    fun transform(medicalGuideListCitiesV4: Array<MedicalGuideListCities>): List<PresentationMedicalGuideListCitiesV4> {
        return medicalGuideListCitiesV4.map { domain ->
            PresentationMedicalGuideListCitiesV4(
                    cityName = domain.cityName,
                    cityCod  = domain.cityCod,
                    serviceType = transform(domain.serviceType.toTypedArray())

            )

        }
    }

    //ServiceType
    fun transform(medicalGuideListServiceTypesV4: Array<MedicalGuideListServiceTypes>): List<PresentationMedicalGuideListServiceTypesV4> {
        return medicalGuideListServiceTypesV4.map { domain ->
            PresentationMedicalGuideListServiceTypesV4(
                    serviceType = domain.serviceType,
                    specialities = transform(domain.speciality.toTypedArray())

            )

        }
    }

    //Specialties
    fun transform(medicalGuideListSpecialitiesV4: Array<MedicalGuideListSpecialities>): List<PresentationMedicalGuideListSpecialitiesV4> {
        return medicalGuideListSpecialitiesV4.map { domain ->
            PresentationMedicalGuideListSpecialitiesV4(
                    specialityName = domain.specialityName,
                    specialityCod = domain.specialityCod,
                    ent_tipo = domain.ent_tipo,
                    medicalGuide = transform(domain.medicalGuide.toTypedArray())

            )

        }
    }













}
package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationEstablishment(
    var showPlansV4: Int = 0,//Andre
    var planNameV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                     var showCityV4: Int = 0,//Andre
                                     var cityV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                     var showServiceTypesV4: Int = 0,//Andre
                                     var serviceTypeV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                     var showEspecialityV4: Int = 0,
                                     var especialityNameV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                     var name: String = InvalidData.UNINITIALIZED.getString(),
                                     var socialName: String = InvalidData.UNINITIALIZED.getString(),
                                     var cnpj: String = InvalidData.UNINITIALIZED.getString(),
                                     var qualifications: List<PresentationQualification> = mutableListOf(),
                                     var type: String = InvalidData.UNINITIALIZED.getString(),
                                     var title: String = InvalidData.UNINITIALIZED.getString(),
                                     var subTitle: String = InvalidData.UNINITIALIZED.getString(),
                                     var speciality: String = InvalidData.UNINITIALIZED.getString(),
                                     var publicPlace: String = InvalidData.UNINITIALIZED.getString(),
                                     var number: String = InvalidData.UNINITIALIZED.getString(),
                                     var neighborhood: String = InvalidData.UNINITIALIZED.getString(),
                                     var zipCode: String = InvalidData.UNINITIALIZED.getString(),
                                     var complement: String = InvalidData.UNINITIALIZED.getString(),
                                     var city: String = InvalidData.UNINITIALIZED.getString(),
                                     var state: String = InvalidData.UNINITIALIZED.getString(),
                                     var phoneOne: String = InvalidData.UNINITIALIZED.getString(),
                                     var phoneTwo: String = InvalidData.UNINITIALIZED.getString(),
                                     var typePhoneOne: String = InvalidData.UNINITIALIZED.getString(),
                                     var typePhoneTwo: String = InvalidData.UNINITIALIZED.getString(),
                                     var latitude: String = InvalidData.UNINITIALIZED.getString(),
                                     var longitude: String = InvalidData.UNINITIALIZED.getString(),
                                     var distance: Double = InvalidData.UNINITIALIZED.getDouble(),
                                     var photoFront: String = InvalidData.UNINITIALIZED.getString(),
                                     var cidCod: String = InvalidData.UNINITIALIZED.getString(),
                                     var esCod: String = InvalidData.UNINITIALIZED.getString(),
                                     var prsSeq: String = InvalidData.UNINITIALIZED.getString(),
                                     var prsCod: String = InvalidData.UNINITIALIZED.getString(),
                                     var proUf: String = InvalidData.UNINITIALIZED.getString(),
                                     var proCls: String = InvalidData.UNINITIALIZED.getString(),
                                     var proCod: String = InvalidData.UNINITIALIZED.getString(),
                                     var isOwnNetwork: Boolean = false,
                                     var favorited: Boolean = false,
                                     var uType: String = InvalidData.UNINITIALIZED.getString()
) {




    fun getFullAddress(): String {
        return "$publicPlace - $number\n" +
                "$neighborhood\n" +
                "$city - $state\n" +
                "CEP: $zipCode"
    }
}
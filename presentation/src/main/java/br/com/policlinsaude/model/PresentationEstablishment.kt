package br.com.policlinsaude.model

import android.os.Parcel
import android.os.Parcelable
import br.com.domain.helper.InvalidData
import java.io.Serializable  //tirado por Andre

data class PresentationEstablishment(var showPlansV4: Int = 0,//Andre
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
                                     var uType: String = InvalidData.UNINITIALIZED.getString()) : Parcelable {//, Serializable {
constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(PresentationQualification),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()) {
}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(showPlansV4)
        parcel.writeString(planNameV4)
        parcel.writeInt(showCityV4)
        parcel.writeString(cityV4)
        parcel.writeInt(showServiceTypesV4)
        parcel.writeString(serviceTypeV4)
        parcel.writeInt(showEspecialityV4)
        parcel.writeString(especialityNameV4)
        parcel.writeString(name)
        parcel.writeString(socialName)
        parcel.writeString(cnpj)
        parcel.writeTypedList(qualifications)
        parcel.writeString(type)
        parcel.writeString(title)
        parcel.writeString(subTitle)
        parcel.writeString(speciality)
        parcel.writeString(publicPlace)
        parcel.writeString(number)
        parcel.writeString(neighborhood)
        parcel.writeString(zipCode)
        parcel.writeString(complement)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(phoneOne)
        parcel.writeString(phoneTwo)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeDouble(distance)
        parcel.writeString(photoFront)
        parcel.writeString(cidCod)
        parcel.writeString(esCod)
        parcel.writeString(prsSeq)
        parcel.writeString(prsCod)
        parcel.writeString(proUf)
        parcel.writeString(proCls)
        parcel.writeString(proCod)
        parcel.writeByte(if (isOwnNetwork) 1 else 0)
        parcel.writeByte(if (favorited) 1 else 0)
        parcel.writeString(uType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationEstablishment> {
        override fun createFromParcel(parcel: Parcel): PresentationEstablishment {
            return PresentationEstablishment(parcel)
        }

        override fun newArray(size: Int): Array<PresentationEstablishment?> {
            return arrayOfNulls(size)
        }
    }

    fun getFullAddress(): String {
        return "$publicPlace - $number\n" +
                "$neighborhood\n" +
                "$city - $state\n" +
                "CEP: $zipCode"
    }
}
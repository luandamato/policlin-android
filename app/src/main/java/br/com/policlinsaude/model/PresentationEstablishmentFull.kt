package br.com.policlinsaude.model

import android.os.Parcel
import android.os.Parcelable
import br.com.domain.helper.InvalidData
import java.io.Serializable  //tirado por Andre

data class PresentationEstablishmentFull(var planNameV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                         var cityV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                         var serviceTypeV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                         var especialityNameV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                         var medicalGuide: List<PresentationEstablishment> = mutableListOf()
                                     ) : Parcelable {//, Serializable {
    constructor(parcel: Parcel) : this(
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.createTypedArrayList(PresentationEstablishment) ?: arrayListOf()
 ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(planNameV4)
        parcel.writeString(cityV4)
        parcel.writeString(serviceTypeV4)
        parcel.writeString(especialityNameV4)
        parcel.writeTypedList(medicalGuide)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationEstablishmentFull> {
        override fun createFromParcel(parcel: Parcel): PresentationEstablishmentFull {
            return PresentationEstablishmentFull(parcel)
        }

        override fun newArray(size: Int): Array<PresentationEstablishmentFull?> {
            return arrayOfNulls(size)
        }
    }


}
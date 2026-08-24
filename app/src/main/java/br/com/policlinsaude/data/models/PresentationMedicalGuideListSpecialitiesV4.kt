package br.com.policlinsaude.data.models

import android.os.Parcel
import android.os.Parcelable
import br.com.policlinsaude.util.helpers.InvalidData
import br.com.policlinsaude.domain.model.MedicalGuideListCities
import java.io.Serializable  //tirado por Andre

/**
 *
 * Andre em 03/06/2018
 */


data class PresentationMedicalGuideListSpecialitiesV4(var specialityName: String = InvalidData.UNINITIALIZED.getString(),
                                                      var specialityCod: String = InvalidData.UNINITIALIZED.getString(),
                                                      var ent_tipo: String = InvalidData.UNINITIALIZED.getString(),
                                                      var medicalGuide: List<PresentationEstablishment> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.createTypedArrayList(PresentationEstablishment) ?: arrayListOf()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(specialityName)
        parcel.writeString(specialityCod)
        parcel.writeString(ent_tipo)
        parcel.writeTypedList(medicalGuide)
          }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationMedicalGuideListSpecialitiesV4> {
        override fun createFromParcel(parcel: Parcel): PresentationMedicalGuideListSpecialitiesV4 {
            return PresentationMedicalGuideListSpecialitiesV4(parcel)
        }

        override fun newArray(size: Int): Array<PresentationMedicalGuideListSpecialitiesV4?> {
            return arrayOfNulls(size)
        }
    }


}
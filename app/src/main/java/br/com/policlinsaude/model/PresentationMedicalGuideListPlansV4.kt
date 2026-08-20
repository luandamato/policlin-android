package br.com.policlinsaude.model

import android.os.Parcel
import android.os.Parcelable
import br.com.domain.helper.InvalidData
import br.com.domain.model.MedicalGuideListCities
import java.io.Serializable  //tirado por Andre

/**
 *
 * Andre em 03/06/2018
 */

data class PresentationMedicalGuideListPlansV4(var planName: String = InvalidData.UNINITIALIZED.getString(),
                                               var planCod: String = InvalidData.UNINITIALIZED.getString(),
                                               var cities : List<PresentationMedicalGuideListCitiesV4> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.createTypedArrayList(PresentationMedicalGuideListCitiesV4) ?: arrayListOf()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(planName)
        parcel.writeString(planCod)
        parcel.writeTypedList(cities)
          }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationMedicalGuideListPlansV4> {
        override fun createFromParcel(parcel: Parcel): PresentationMedicalGuideListPlansV4 {
            return PresentationMedicalGuideListPlansV4(parcel)
        }

        override fun newArray(size: Int): Array<PresentationMedicalGuideListPlansV4?> {
            return arrayOfNulls(size)
        }
    }


}
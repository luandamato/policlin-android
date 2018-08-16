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

data class PresentationMedicalGuideListServiceTypesV4(var serviceType: String = InvalidData.UNINITIALIZED.getString(),
                                                      var specialities: List<PresentationMedicalGuideListSpecialitiesV4> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArrayList(PresentationMedicalGuideListSpecialitiesV4)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(serviceType)
        parcel.writeTypedList(specialities)
          }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationMedicalGuideListServiceTypesV4> {
        override fun createFromParcel(parcel: Parcel): PresentationMedicalGuideListServiceTypesV4 {
            return PresentationMedicalGuideListServiceTypesV4(parcel)
        }

        override fun newArray(size: Int): Array<PresentationMedicalGuideListServiceTypesV4?> {
            return arrayOfNulls(size)
        }
    }
}
package br.com.policlinsaude.data.models

import android.os.Parcel
import android.os.Parcelable
import br.com.policlinsaude.util.helpers.InvalidData

/**
 *
 * Andre em 03/06/2018
 */

data class PresentationMedicalGuideListServiceTypesV4(
    var serviceType: String = InvalidData.UNINITIALIZED.getString(),
    var specialities: List<PresentationMedicalGuideListSpecialitiesV4> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().orEmpty(),
            parcel.createTypedArrayList(PresentationMedicalGuideListSpecialitiesV4) ?: arrayListOf()) {
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
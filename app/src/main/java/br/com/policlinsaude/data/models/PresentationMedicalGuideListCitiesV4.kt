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

data class PresentationMedicalGuideListCitiesV4(var cityName: String = InvalidData.UNINITIALIZED.getString(),
                                                var cityCod: String = InvalidData.UNINITIALIZED.getString(),
                                                var serviceType: List<PresentationMedicalGuideListServiceTypesV4> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.createTypedArrayList(PresentationMedicalGuideListServiceTypesV4) ?: arrayListOf()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cityName)
        parcel.writeString(cityCod)
        parcel.writeTypedList(serviceType)
          }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationMedicalGuideListCitiesV4> {
        override fun createFromParcel(parcel: Parcel): PresentationMedicalGuideListCitiesV4 {
            return PresentationMedicalGuideListCitiesV4(parcel)
        }

        override fun newArray(size: Int): Array<PresentationMedicalGuideListCitiesV4?> {
            return arrayOfNulls(size)
        }
    }


}
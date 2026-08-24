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

data class PresentationHealthInsurancePhoto(var imgFrente:  String? = InvalidData.UNINITIALIZED.getString(),
                                            var titular:    String? = InvalidData.UNINITIALIZED.getString(),
                                            var ordem:      String? = InvalidData.UNINITIALIZED.getString()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imgFrente)
        parcel.writeString(titular)
        parcel.writeString(ordem)
          }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationHealthInsurancePhoto> {
        override fun createFromParcel(parcel: Parcel): PresentationHealthInsurancePhoto {
            return PresentationHealthInsurancePhoto(parcel)
        }

        override fun newArray(size: Int): Array<PresentationHealthInsurancePhoto?> {
            return arrayOfNulls(size)
        }
    }


}
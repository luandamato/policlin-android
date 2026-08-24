package br.com.policlinsaude.data.models

import android.os.Parcel
import android.os.Parcelable
import br.com.policlinsaude.util.helpers.InvalidData

/**
 * Created by lmiyagi on 11/04/18.
 */
data class PresentationEstablishmentLocation(val latitude: Double = 0.0,
                                             val longitude: Double = 0.0,
                                             val title: String = InvalidData.UNINITIALIZED.getString())
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString().orEmpty()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationEstablishmentLocation> {
        override fun createFromParcel(parcel: Parcel): PresentationEstablishmentLocation {
            return PresentationEstablishmentLocation(parcel)
        }

        override fun newArray(size: Int): Array<PresentationEstablishmentLocation?> {
            return arrayOfNulls(size)
        }
    }
}
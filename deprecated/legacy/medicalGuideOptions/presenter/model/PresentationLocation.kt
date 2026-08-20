package br.com.policlinsaude.ui.legacy.medicalGuideOptions.presenter.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

/**
 * Created by lmiyagi on 3/23/18.
 */
data class PresentationLocation(val latitude: Double,
                                val longitude: Double) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        Log.d("Andre", "PresentationLocation: latitude: "+ latitude + " longitude: " + longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationLocation> {
        override fun createFromParcel(parcel: Parcel): PresentationLocation {
            return PresentationLocation(parcel)
        }

        override fun newArray(size: Int): Array<PresentationLocation?> {
            return arrayOfNulls(size)
        }
    }

}
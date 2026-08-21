package br.com.policlinsaude.model

import android.os.Parcel
import android.os.Parcelable
import br.com.policlinsaude.domain.helper.InvalidData



data class PresentationHealthInsurancePhotoList( var listaimgFrente: List<PresentationHealthInsurancePhoto>? = mutableListOf(),
                                     var imgVerso:         String?  = InvalidData.UNINITIALIZED.getString(),
                                     var codAcao:          String?  = InvalidData.UNINITIALIZED.getString(),
                                     var msgInterna:       String?  = InvalidData.UNINITIALIZED.getString(),
                                     var msgExterna:       String?  = InvalidData.UNINITIALIZED.getString()


) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(PresentationHealthInsurancePhoto) ?: arrayListOf(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(listaimgFrente)
        parcel.writeString(imgVerso)
        parcel.writeString(codAcao)
        parcel.writeString(msgInterna)
        parcel.writeString(msgExterna)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationHealthInsurancePhotoList> {
        override fun createFromParcel(parcel: Parcel): PresentationHealthInsurancePhotoList {
            return PresentationHealthInsurancePhotoList(parcel)
        }

        override fun newArray(size: Int): Array<PresentationHealthInsurancePhotoList?> {
            return arrayOfNulls(size)
        }
    }


}
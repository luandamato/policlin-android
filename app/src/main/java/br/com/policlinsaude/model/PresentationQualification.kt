package br.com.policlinsaude.model

import android.os.Parcel
import android.os.Parcelable
import br.com.domain.helper.InvalidData
import java.io.Serializable

data class PresentationQualification(var image: String = InvalidData.UNINITIALIZED.getString(),
                                     var initial: String = InvalidData.UNINITIALIZED.getString(),
                                     var description: String = InvalidData.UNINITIALIZED.getString()
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(initial)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationQualification> {
        override fun createFromParcel(parcel: Parcel): PresentationQualification {
            return PresentationQualification(parcel)
        }

        override fun newArray(size: Int): Array<PresentationQualification?> {
            return arrayOfNulls(size)
        }
    }
}
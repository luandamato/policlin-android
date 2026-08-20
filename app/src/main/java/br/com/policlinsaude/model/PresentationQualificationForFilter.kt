package br.com.policlinsaude.model

import android.os.Parcel
import android.os.Parcelable
import br.com.domain.helper.InvalidData
import java.io.Serializable

data class PresentationQualificationForFilter(var imgQualificacao: String = InvalidData.UNINITIALIZED.getString(),
                                              var descricao: String = InvalidData.UNINITIALIZED.getString(),
                                              var cod: String = InvalidData.UNINITIALIZED.getString()
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty(),
            parcel.readString().orEmpty())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imgQualificacao)
        parcel.writeString(descricao)
        parcel.writeString(cod)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PresentationQualificationForFilter> {
        override fun createFromParcel(parcel: Parcel): PresentationQualificationForFilter {
            return PresentationQualificationForFilter(parcel)
        }

        override fun newArray(size: Int): Array<PresentationQualificationForFilter?> {
            return arrayOfNulls(size)
        }
    }
}
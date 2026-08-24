package br.com.policlinsaude.data.models

import android.os.Parcelable
import br.com.policlinsaude.util.helpers.InvalidData
import java.io.Serializable

data class PresentationEstablishmentType(var codeEstablishmentType: String = InvalidData.UNINITIALIZED.getString(),
                                         var descriptionEstablishmentType: String = InvalidData.UNINITIALIZED.getString()):



Serializable {

    override fun toString(): String {
        return descriptionEstablishmentType
    }
}

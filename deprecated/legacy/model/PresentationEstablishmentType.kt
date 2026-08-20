package br.com.policlinsaude.ui.legacy.model

import android.os.Parcelable
import br.com.domain.helper.InvalidData
import java.io.Serializable

data class PresentationEstablishmentType(var codeEstablishmentType: String = InvalidData.UNINITIALIZED.getString(),
                                         var descriptionEstablishmentType: String = InvalidData.UNINITIALIZED.getString()):



Serializable {

    override fun toString(): String {
        return descriptionEstablishmentType
    }
}

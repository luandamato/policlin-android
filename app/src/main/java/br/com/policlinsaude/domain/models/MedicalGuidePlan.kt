package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

/**
 * Created by lmiyagi on 3/21/18.
 */
data class MedicalGuidePlan(var description: String = InvalidData.UNINITIALIZED.getString()) {

    override fun toString(): String {
        return description
    }
}
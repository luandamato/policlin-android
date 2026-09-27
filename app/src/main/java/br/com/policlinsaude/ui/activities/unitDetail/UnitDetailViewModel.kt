package br.com.policlinsaude.ui.activities.unitDetail

import androidx.lifecycle.ViewModel
import br.com.policlinsaude.data.models.PresentationEstablishment
import com.google.gson.Gson

/**
 * ViewModel da tela de detalhe da unidade.
 *
 * Como o [PresentationEstablishment] não usa Parcelize (premissa do projeto),
 * o estabelecimento é recebido via Intent como JSON string e convertido aqui.
 */
class UnitDetailViewModel : ViewModel() {

    private val gson = Gson()

    fun parseEstablishment(json: String?): PresentationEstablishment? {
        if (json.isNullOrBlank()) return null
        return try {
            gson.fromJson(json, PresentationEstablishment::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun toJson(establishment: PresentationEstablishment): String = gson.toJson(establishment)
}
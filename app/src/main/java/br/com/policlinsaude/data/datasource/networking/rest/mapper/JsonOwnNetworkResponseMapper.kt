package br.com.data.datasource.networking.rest.mapper

import br.com.data.datasource.networking.rest.model.JsonOwnNetworkResponse
import br.com.policlinsaude.domain.model.Establishment
import br.com.policlinsaude.domain.model.OwnNetworkList

object JsonOwnNetworkResponseMapper {

    fun transform(jsonOwnNetworkResponse: JsonOwnNetworkResponse)
            : OwnNetworkList {


        val establishments = if (jsonOwnNetworkResponse.establishments == null) {
            mutableListOf()
        } else {
            val establishment = JsonMedicalGuideListResponseMapper.transform(jsonOwnNetworkResponse.establishments.toTypedArray())
            val list: MutableList<Pair<String, MutableList<Establishment>>> = mutableListOf()
            establishment.forEach {
                if (list.isEmpty()) {
                    list.add(Pair(it.cidCod, mutableListOf(it)))
                    return@forEach
                }
                list.forEachIndexed { index, inside ->
                    if (inside.first == it.cidCod) {
                        inside.second.add(it)
                        return@forEach
                    } else if ((index + 1) == list.count()) {
                        list.add(Pair(it.cidCod, mutableListOf(it)))
                    }
                }
            }
            list
        }

        return OwnNetworkList(
                qualifications = JsonMedicalGuideListResponseMapper.transform((jsonOwnNetworkResponse.qualifications
                        ?: mutableListOf()).toTypedArray()),
                establishments = establishments)
    }
}
package br.com.policlinsaude.data.datasource.networking.rest.mapper

import br.com.policlinsaude.data.datasource.networking.rest.model.JsonFavoritesResponse
import br.com.policlinsaude.domain.model.Establishment

/**
 * Created by lmiyagi on 3/27/18.
 */
object JsonFavoritesResponseMapper {

    fun transform(jsonFavoritesResponse: JsonFavoritesResponse): List<Establishment> {
        jsonFavoritesResponse.infs?.let {
            val establishments = ArrayList<Establishment>()
            it.forEach {
                it.establishment?.let {
                    establishments.add(JsonMedicalGuideListResponseMapper.transform(it))
                }
            }
            return establishments
        }
        return mutableListOf()
    }
}
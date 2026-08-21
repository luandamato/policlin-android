package br.com.policlinsaude.data.datasource.networking.rest.mapper

import br.com.policlinsaude.data.datasource.networking.rest.model.JsonBannerResponse
import br.com.policlinsaude.data.datasource.networking.rest.model.JsonGetBannersResponse
import br.com.policlinsaude.data.helper.InvalidData
import br.com.policlinsaude.domain.model.Banner

object JsonBannerResponseMapper {

    fun transform(jsonGetBannersResponse: JsonGetBannersResponse): List<Banner> {
        jsonGetBannersResponse.banners?.let {
            return it.map { jsonBanner ->
                transform(jsonBanner)
            }
        }
        return mutableListOf()
    }

    fun transform(jsonBannerResponse: JsonBannerResponse): Banner {
        return Banner(id = jsonBannerResponse.id ?: InvalidData.UNINITIALIZED.getString(),
                image = jsonBannerResponse.image ?: InvalidData.UNINITIALIZED.getString(),
                url = jsonBannerResponse.url ?: InvalidData.UNINITIALIZED.getString() )
    }
}
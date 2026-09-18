package br.com.policlinsaude.data.models

import br.com.policlinsaude.domain.models.Banner
import br.com.policlinsaude.util.helpers.InvalidData

/**
 * Converte [JsonGetBannersResponse]/[JsonBannerResponse] en [Banner].
 * Migrado de `_legacy/.../data/datasource/networking/rest/mapper/JsonBannerResponseMapper.kt`.
 */

fun JsonGetBannersResponse.toBanners(): List<Banner> =
    banners?.map { it.toBanner() } ?: mutableListOf()

fun JsonBannerResponse.toBanner(): Banner = Banner(
    id = id ?: InvalidData.UNINITIALIZED.getString(),
    image = image ?: InvalidData.UNINITIALIZED.getString(),
    url = url ?: InvalidData.UNINITIALIZED.getString()
)
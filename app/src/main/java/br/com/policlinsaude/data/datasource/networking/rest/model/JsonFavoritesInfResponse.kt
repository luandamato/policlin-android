package br.com.data.datasource.networking.rest.model

import com.google.gson.annotations.SerializedName

/**
 * Created by lmiyagi on 05/04/18.
 */
data class JsonFavoritesInfResponse(@SerializedName("inf") val establishment: JsonEstablishmentResponse? = null)
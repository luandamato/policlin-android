package br.com.policlinsaude.data.datasource.networking.rest.mapper

import android.util.Log
import br.com.policlinsaude.data.datasource.networking.rest.model.JsonOwnNetworkResponse
import br.com.policlinsaude.data.datasource.networking.rest.model.JsonUnitsResponse
import br.com.policlinsaude.domain.model.Establishment
import br.com.policlinsaude.domain.model.OwnNetworkList
import br.com.policlinsaude.domain.model.UnitsList

object JsonUnitsMapper {

    fun transform(jsonUnitsResponse: JsonUnitsResponse)
            : UnitsList {


        val establishments = if (jsonUnitsResponse.establishments == null) {
            mutableListOf()
        } else {
            val establishment = JsonMedicalGuideListResponseMapper.transform(jsonUnitsResponse.establishments.toTypedArray())
            Log.d("UNIDADES","NO MAPPER -  TAMANHO DE ESTABLISHMENT:  " + establishment.size )


         //   val list: MutableList<Pair<String, MutableList<Establishment>>> = mutableListOf()
            val list: MutableList<Establishment> = mutableListOf()
            establishment.forEach {
              //  if (list.isEmpty()) {
                    //list.add(Pair(it.cidCod, mutableListOf(it)))
                    list.add( it)
                  //  return@forEach
          //      }
            /*    list.forEachIndexed { index, inside ->
                    if (inside.first == it.cidCod) {
                        inside.second.add(it)
                        return@forEach
                    } else if ((index + 1) == list.count()) {
                        list.add(Pair(it.cidCod, mutableListOf(it)))
                    }
                }*/
            }
            list
        }

        Log.d("UNIDADES","NO MAPPER -  TAMANHO DE ESTABLISHMENT NO FINAL:  " + establishments.size )
        return UnitsList(
                qualifications = JsonMedicalGuideListResponseMapper.transform((jsonUnitsResponse.qualifications
                        ?: mutableListOf()).toTypedArray()),
                establishments = establishments)
    }


  /*  fun transform(jsonUnitsResponse: JsonUnitsResponse)
            : UnitsList {


        val establishments = if (jsonUnitsResponse.establishments == null) {
            mutableListOf()
        } else {
            val establishment = JsonMedicalGuideListResponseMapper.transform(jsonUnitsResponse.establishments.toTypedArray())
            //   val list: MutableList<Pair<String, MutableList<Establishment>>> = mutableListOf()
            val list: MutableList<Establishment> = mutableListOf()
            establishment.forEach {
                if (list.isEmpty()) {
                    //list.add(Pair(it.cidCod, mutableListOf(it)))
                    list.addAll( mutableListOf(it))
                    return@forEach
                }
                /*    list.forEachIndexed { index, inside ->
                        if (inside.first == it.cidCod) {
                            inside.second.add(it)
                            return@forEach
                        } else if ((index + 1) == list.count()) {
                            list.add(Pair(it.cidCod, mutableListOf(it)))
                        }
                    }*/
            }
            list
        }

        return UnitsList(
                qualifications = JsonMedicalGuideListResponseMapper.transform((jsonUnitsResponse.qualifications
                        ?: mutableListOf()).toTypedArray()),
                establishments = establishments)
    }*/

}
package com.policlinsaude.newfeature.utils

import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationItemsModel
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorDatesItemModel

object DateMapper {

    fun toFactorExtractorYearsModelToYears(items: ArrayList<FactorExtractorDatesItemModel>?): MutableList<String>? {
        return items?.map { values ->
            values.valor.toString()
        }?.toMutableList()
    }

    fun toFactorExtractorMonthsModelToMonths(items: ArrayList<FactorExtractorDatesItemModel>?): MutableList<String>? {
        return items?.map { values ->
            values.valor.getMonth()
        }?.toMutableList()
    }

    fun toCoParticipationToItems(items: ArrayList<CoParticipationItemsModel>?): MutableList<String>? {
        return items?.map { values ->
            values.description
        }?.toMutableList()
    }

}
package br.com.domain.model

data class UnitsList(var qualifications: List<Qualification> = mutableListOf(),
                   //  val establishments: List<Pair<String, List<Establishment>>>)
                     val establishments: List<Establishment>  = mutableListOf())
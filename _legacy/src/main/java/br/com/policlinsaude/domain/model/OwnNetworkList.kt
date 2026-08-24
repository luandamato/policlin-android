package br.com.policlinsaude.domain.model

data class OwnNetworkList(var qualifications: List<Qualification> = mutableListOf(),
                          val establishments: List<Pair<String, List<Establishment>>>)
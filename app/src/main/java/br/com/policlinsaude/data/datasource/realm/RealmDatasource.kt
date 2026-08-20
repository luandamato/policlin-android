package br.com.data.datasource.realm

import br.com.domain.model.Person
import io.reactivex.Completable
import io.reactivex.Flowable

interface RealmDatasource {

    fun savePerson(person: Person, token: String): Completable

    fun getPersonByToken(token: String): Flowable<Person>


}
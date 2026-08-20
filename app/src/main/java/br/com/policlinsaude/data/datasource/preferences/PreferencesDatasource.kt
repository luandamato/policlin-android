package br.com.policlinsaude.data.datasource.preferences

import br.com.policlinsaude.domain.model.Person
import io.reactivex.Completable
import io.reactivex.Flowable

interface PreferencesDatasource {

    fun saveToken(token: String): Completable

    fun getToken(): Flowable<String>

    fun putBoolean(key: String, value: Boolean): Completable

    fun getBoolean(key: String, defaultValue: Boolean): Flowable<Boolean>

    fun removeToken(): Completable

    fun savePerson(person: Person): Completable

    fun getPerson(): Flowable<Person>

    fun removePerson(): Completable
}
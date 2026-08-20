package br.com.data.datasource.realm

import br.com.data.datasource.realm.mapper.RealmPersonMapper
import br.com.data.datasource.realm.model.RealmPerson
import br.com.data.exception.RealmNotFoundException
import br.com.domain.model.Person
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.Realm

class RealmDatasourceImpl : RealmDatasource {

    override fun savePerson(person: Person, token: String): Completable = Completable.create {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealm(RealmPersonMapper.transform(person, token))
        realm.commitTransaction()
        realm.close()
        it.onComplete()
    }

    override fun getPersonByToken(token: String): Flowable<Person> =
            Flowable.create({
                val realm = Realm.getDefaultInstance()
                realm.beginTransaction()
                val realmPersonFind = realm.where(RealmPerson::class.java)
                        .equalTo("token", token)
                        .findFirst()
                if (realmPersonFind == null) {
                    it.onError(RealmNotFoundException(RealmPerson::class.java))
                } else {
                    val realmPerson = realm.copyFromRealm(realmPersonFind)
                    it.onNext(RealmPersonMapper.transform(realmPerson!!))
                }
                realm.commitTransaction()
                realm.close()
            }, BackpressureStrategy.LATEST)

}
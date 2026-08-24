package br.com.policlinsaude.data.datasource.realm

import br.com.policlinsaude.domain.model.Person
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Implementação mantida apenas para compatibilidade com a estrutura existente.
 * Não depende mais de Realm e funciona como armazenamento em memória simples.
 */
class RealmDatasourceImpl : RealmDatasource {

    private var mockPerson: Person? = null
    private var mockToken: String? = null

    override fun savePerson(person: Person, token: String): Completable =
        Completable.fromAction {
            mockPerson = person
            mockToken = token
        }

    override fun getPersonByToken(token: String): Flowable<Person> {
        val person = mockPerson ?: return Flowable.empty()
        return if (mockToken == token) Flowable.just(person) else Flowable.empty()
    }
}

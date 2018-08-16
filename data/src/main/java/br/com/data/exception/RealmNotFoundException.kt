package br.com.data.exception

import io.realm.RealmObject

/**
 * Created by lmiyagi on 3/22/18.
 */
class RealmNotFoundException : Exception {

    constructor(typeOfClass: Class<out RealmObject>)
            : super("Could not find any records for ${typeOfClass.name}")
}
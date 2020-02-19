package com.kotlingithub.dbRealm

import androidx.room.PrimaryKey
import io.realm.RealmObject
import io.realm.annotations.Required

open class MyRealmTestModel : RealmObject() {

    @PrimaryKey
    var id: Int = 0
    @Required
    var title: String? = ""

}
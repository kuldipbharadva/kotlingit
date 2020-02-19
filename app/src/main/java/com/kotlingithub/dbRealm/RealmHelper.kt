package com.kotlingithub.dbRealm

import android.content.Context
import io.realm.*
import java.util.*


object RealmHelper {

    private var mRealm: Realm? = null

    fun getRealmInstance(context: Context): Realm {
        val config = RealmConfiguration.Builder(context)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
        mRealm = Realm.getInstance(config)
        mRealm!!.isAutoRefresh = true
        return mRealm as Realm
    }

    /**
     * this function get all record from give model class of realm db
     */
    fun getAllRecords(aClass: Class<RealmObject>): RealmResults<*> {
        return mRealm!!.allObjects<RealmObject>(aClass as Class<RealmObject>?)
    }

    /**
     * this function give you record from Realm db. If you pass null in 'where' then return all records.
     * you can also use this function to check record is available or not in given model class of realm.
     */
    fun getWhere(
        aClass: Class<*>,
        wheres: ArrayList<String>?,
        `val`: ArrayList<Any>,
        aCase: Case?
    ): RealmResults<*> {
        var aCase = aCase
        val query = mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?)
        if (aCase == null) {
            aCase = Case.INSENSITIVE
        }
        if (wheres != null) {
            for (i in wheres.indices) {
                when {
                    `val`[i] is String -> query.equalTo(wheres[i], `val`[i] as String, aCase)
                    `val`[i] is Int -> query.equalTo(wheres[i], `val`[i] as Int)
                    `val`[i] is Double -> query.equalTo(wheres[i], `val`[i] as Double)
                    `val`[i] is Float -> query.equalTo(wheres[i], `val`[i] as Float)
                    `val`[i] is Boolean -> query.equalTo(wheres[i], `val`[i] as Boolean)
                }
            }
        }
        return query.findAll()
    }

    /**
     * this function check is record exist in Realm db or not from given model class
     */
    fun isExist(aClass: Class<*>, where: String?, `val`: Any?, aCase: Case?): Boolean {
        var aCase = aCase
        var realmObject: RealmObject? = null
        if (aCase == null) aCase = Case.INSENSITIVE
        if (where != null && `val` != null) {
            when (`val`) {
                is String -> realmObject =
                    mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val` as String?, aCase)
                        .findFirst()
                is Int -> realmObject =
                    mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val` as Int?).findFirst()
                is Double -> realmObject =
                    mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val` as Double?).findFirst()
                is Float -> realmObject =
                    mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val` as Float?).findFirst()
                is Boolean -> realmObject = mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val` as Boolean?)
                    .findFirst()
            }
        }
        return realmObject != null
    }

    fun isExist(
        aClass: Class<*>,
        wheres: ArrayList<String>?,
        `val`: ArrayList<Any>,
        aCase: Case?
    ): Boolean {
        var aCase = aCase
        val realmObject: RealmObject?
        val query = mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?)
        if (aCase == null) aCase = Case.INSENSITIVE
        if (wheres != null) {
            for (i in wheres.indices) {
                when {
                    `val`[i] is String -> query.equalTo(wheres[i], `val`[i] as String, aCase)
                    `val`[i] is Int -> query.equalTo(wheres[i], `val`[i] as Int)
                    `val`[i] is Double -> query.equalTo(wheres[i], `val`[i] as Double)
                    `val`[i] is Float -> query.equalTo(wheres[i], `val`[i] as Float)
                    `val`[i] is Boolean -> query.equalTo(wheres[i], `val`[i] as Boolean)
                }
            }
        }
        realmObject = query.findFirst()
        return realmObject != null
    }

    /**
     * this function delete all record from given class of realm model
     */
    fun deleteRealm(aClass: Class<*>) {
        mRealm!!.executeTransaction { realm -> realm.clear(aClass as Class<out RealmObject>?) }
    }

    /**
     * this function delete selected record base on give where condition.
     * you can only check single column/id/where and value at a time using this function
     */
    fun deleteRealm(aClass: Class<*>, where: String?, `val`: Any?, aCase: Case?) {
        var aCase = aCase
        mRealm!!.beginTransaction()
        if (aCase == null) aCase = Case.INSENSITIVE
        var results: RealmResults<*>? = null
        if (where != null && `val` != null) {
            when (`val`) {
                is String -> results =
                    mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val`.toString(), aCase)
                        .findAll()
                is Int -> results =
                    mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val` as Int?).findAll()
                is Double -> results =
                    mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val` as Double?).findAll()
                is Float -> results =
                    mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val` as Float?).findAll()
                is Boolean -> results =
                    mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?).equalTo(where, `val` as Boolean?).findAll()
            }
        }
        if (results != null && !results.isEmpty()) {
            for (i in results.indices) {
                results[i].removeFromRealm()
            }
        }
        mRealm!!.commitTransaction()
    }

    /**
     * this function delete selected record only
     */
    fun deleteRealm(
        aClass: Class<*>,
        wheres: ArrayList<String>?,
        `val`: ArrayList<Any>,
        aCase: Case?
    ) {
        var aCase = aCase
        mRealm!!.beginTransaction()
        if (aCase == null) aCase = Case.INSENSITIVE
        val query = mRealm!!.where<RealmObject>(aClass as Class<RealmObject>?)
        val results: RealmResults<*>?
        if (wheres != null) {
            for (i in wheres.indices) {
                when {
                    `val`[i] is String -> query.equalTo(wheres[i], `val`[i] as String, aCase)
                    `val`[i] is Int -> query.equalTo(wheres[i], `val`[i] as Int)
                    `val`[i] is Double -> query.equalTo(wheres[i], `val`[i] as Double)
                    `val`[i] is Float -> query.equalTo(wheres[i], `val`[i] as Float)
                    `val`[i] is Boolean -> query.equalTo(wheres[i], `val`[i] as Boolean)
                }
            }
        }
        results = query.findAll()
        if (results != null && !results.isEmpty()) {
            for (i in results.indices) {
                results[i].removeFromRealm()
            }
        }
        mRealm!!.commitTransaction()
    }
}
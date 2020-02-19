package com.kotlingithub.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteHelper(context: Context)//        super(context, DB_NAME, null, Utility.getDataBaseVersion(context));
    : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTbl =
            "CREATE TABLE IF NOT EXISTS $TBL_NAME ($COL_TABLE_NAME TEXT, $COL_DATA TEXT, $COL_DATE TEXT);"
        db.execSQL(createTbl)
        val createTblAPI =
            "CREATE TABLE IF NOT EXISTS $TBL_NAME_API ($COL_CLASS_NAME TEXT, $COL_API_DATA TEXT);"
        db.execSQL(createTblAPI)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS $TBL_NAME")
            onCreate(db)
        }
    }

    companion object {

        var DB_NAME = "WSData.sqlite"

        //APPLICATION SERVICE TABLE
        var TBL_NAME = "WSData"
        val COL_TABLE_NAME = "tblName"
        val COL_DATA = "dataJSON"
        val COL_DATE = "updatedDate"

        //API TABLE
        var TBL_NAME_API = "APIData"
        val COL_CLASS_NAME = "className"
        val COL_API_DATA = "APIData"
    }
}
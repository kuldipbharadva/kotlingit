package com.kotlingithub.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log.d

import java.util.ArrayList

class DatabaseHelper(context: Context) {

    private val dbHelper: MySQLiteHelper = MySQLiteHelper(context)
    private var sqLiteDatabase: SQLiteDatabase? = null

    val allTableName: ArrayList<String>
        get() {
            val c =
                sqLiteDatabase!!.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
            val tName = ArrayList<String>()
            if (c.moveToFirst()) {
                while (!c.isAfterLast) {
                    tName.add(c.getString(0))
                    c.moveToNext()
                }
            }
            return tName
        }


    init {
        sqLiteDatabase = dbHelper.writableDatabase
    }


    @Throws(SQLException::class)
    fun openDatabase() {
        sqLiteDatabase = dbHelper.writableDatabase
    }

    private fun closeDatabase() {
        dbHelper.close()
    }

    fun createTbl(tableName: String, columnsNames: ArrayList<String>, columnType: ArrayList<*>) {
        var prepareQuery = ""
        for (i in columnsNames.indices) {
            prepareQuery += columnsNames[i] + " " + columnType[i] + ", "
        }
        val createTbl = "CREATE TABLE IF NOT EXISTS $tableName (" + prepareQuery.substring(
            0,
            prepareQuery.length - 2
        ) + ");"
        sqLiteDatabase!!.execSQL(createTbl)
    }

    //this method returns the id of the newly created row and returns -1 on insert none record
    //insert() of sqlite returns inserted record id
    fun insert(
        tableName: String,
        columnsNames: ArrayList<String>,
        columnValues: ArrayList<*>
    ): Long {
        return try {
            if (!sqLiteDatabase!!.isOpen)
                openDatabase()
            val `val` = sqLiteDatabase!!.insert(
                tableName,
                null,
                getContentValues(columnsNames, columnValues)
            )
            closeDatabase()
            `val`
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    //    String[] args = new String[] { "UserMasterId", "RoldeId" };
    //    String where = AssetDatabaseOpenHelper.COL_USER_ROLE_PRIVILEGE_UserMasterId + " = ? and " + AssetDatabaseOpenHelper.COL_USER_ROLE_PRIVILEGE_MasterId + " = ? ";
    //update() of sqlite returns updated record id
    fun update(
        tableName: String,
        columnsNames: ArrayList<String>,
        columnValues: ArrayList<*>,
        where: String,
        selectionArgs: ArrayList<*>
    ): Boolean {
        return try {
            if (!sqLiteDatabase!!.isOpen)
                openDatabase()
            val `val` = sqLiteDatabase!!.update(
                tableName,
                getContentValues(columnsNames, columnValues),
                where,
                getStringArgs(selectionArgs)
            )
            closeDatabase()
            return `val` != -1
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    //to delete particular records from given table
    //delete() of sqlite returns number of deleted records
    fun delete(tableName: String, where: String, args: ArrayList<*>): Boolean {
        return try {
            if (!sqLiteDatabase!!.isOpen)
                openDatabase()
            val `val` = sqLiteDatabase!!.delete(tableName, where, getStringArgs(args)) > 0
            closeDatabase()
            `val`
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }


    //http://stackoverflow.com/questions/18013912/selectionargs-in-sqlitequerybuilder-doesnt-work-with-integer-values-in-columns
    //User = CAST(? as INTEGER) for int
    //    String[] args = new String[] { "UserMasterId", "RoldeId" };
    //    String selection = AssetDatabaseOpenHelper.COL_USER_ROLE_PRIVILEGE_UserMasterId + " = ? and " + AssetDatabaseOpenHelper.COL_USER_ROLE_PRIVILEGE_MasterId + " = ? ";
    //to get records from table
    //please maintain/handle null pointer exception when use getWhere()
    fun getWhere(
        tableName: String,
        columns: ArrayList<String>,
        selection: String,
        args: ArrayList<*>,
        order: String
    ): Cursor? {
        try {
            if (!sqLiteDatabase!!.isOpen)
                openDatabase()
            val cursor = sqLiteDatabase!!.query(
                tableName,
                getStringArgs(columns),
                selection,
                getStringArgs(args),
                null,
                null,
                order
            )
            return if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                closeDatabase()
                cursor
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun truncateTable(tableName: String) {
        if (!sqLiteDatabase!!.isOpen)
            openDatabase()
        if (rowExists(tableName)) {
            sqLiteDatabase!!.delete(tableName, null, null)
            closeDatabase() // Closing database connection
        }
    }

    fun getTotalRows(tableName: String, selection: String, args: ArrayList<*>): Int {
        if (!sqLiteDatabase!!.isOpen)
            openDatabase()
        return DatabaseUtils.queryNumEntries(
            sqLiteDatabase, tableName,
            selection, getStringArgs(args)
        ).toInt()

    }

    fun rowExists(tableName: String, selection: String, args: ArrayList<*>): Boolean {
        if (!sqLiteDatabase!!.isOpen)
            openDatabase()
        return DatabaseUtils.queryNumEntries(
            sqLiteDatabase,
            tableName,
            selection,
            getStringArgs(args)
        ) > 0
    }

    fun rowExists(tableName: String): Boolean {
        if (!sqLiteDatabase!!.isOpen)
            openDatabase()
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, tableName) > 0
    }

    fun getTotalRows(tableName: String): Int {
        if (!sqLiteDatabase!!.isOpen)
            openDatabase()
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, tableName).toInt()
    }

    companion object {

        fun getContentValues(
            columnsNames: ArrayList<String>,
            columnsValues: ArrayList<*>
        ): ContentValues {
            val values = ContentValues()
            for (i in columnsNames.indices) {
                when {
                    columnsValues[i] is String -> values.put(
                        columnsNames[i],
                        columnsValues[i] as String
                    )
                    columnsValues[i] is Int -> values.put(columnsNames[i], columnsValues[i] as Int)
                    columnsValues[i] is Long -> values.put(
                        columnsNames[i],
                        columnsValues[i] as Long
                    )
                    columnsValues[i] is Float -> values.put(
                        columnsNames[i],
                        columnsValues[i] as Float
                    )
                    columnsValues[i] is Boolean -> //Here cast boolean and save its value to int 1 for true and 0 for false
                        values.put(columnsNames[i], if (columnsValues[i] as Boolean) 1 else 0)
                }
            }
            return values
        }

        fun getStringArgs(columnValues: ArrayList<*>?): Array<String?> {
            val stringArray: Array<String?> = arrayOfNulls(columnValues?.size!!)
            for (i in columnValues.indices) {
                stringArray[i] = columnValues[i].toString()
            }
            d("array", "" + stringArray)
            return stringArray
        }
    }
}
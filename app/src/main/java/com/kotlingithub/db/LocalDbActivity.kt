package com.kotlingithub.db

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kotlingithub.R
import kotlinx.android.synthetic.main.activity_local_db.*

@Suppress("UNREACHABLE_CODE")
class LocalDbActivity : AppCompatActivity() {

    private val TABLE_NAME = "TestTable"
    private var databaseHelper: DatabaseHelper? = null
    private var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_db)

        databaseHelper = DatabaseHelper(this)

        btnCreateTable.setOnClickListener {
            tableCreate()
            getTableNameFromDb()
        }
        btnInsert.setOnClickListener {
            insertDataInDb()
        }
        btnEdit.setOnClickListener {
            editDataInDatabase()
        }
        btnGetAll.setOnClickListener {
            getDataFromTable()
        }
        btnGetSelected.setOnClickListener {
            getSelectedDataFromTable()
//            d("dbData", "${databaseHelper!!.getRecordsCount()}")
        }
        btnDelete.setOnClickListener {
            deleteDataFromDatabase()
        }
    }

    /* this function create table in local database */
    private fun tableCreate() {
        val columnName = ArrayList<String>()
        columnName.add("_id")
        columnName.add("FName")
        columnName.add("LName")

        val columnType = ArrayList<String>()
        columnType.add("TEXT")
        columnType.add("TEXT")
        columnType.add("TEXT")

        databaseHelper?.createTbl(TABLE_NAME, columnName, columnType)
    }

    /* this function get all table name from created database */
    fun getTableNameFromDb() {
        val s = databaseHelper?.allTableName
        if (s != null) {
            for (i in 0 until s.size) {
                Log.d("dbName", "localDbUsage: table : " + s[i])
            }
        }
    }

    /* this function insert data in local database */
    private fun insertDataInDb() {
        val columnName = ArrayList<String>()
        columnName.add("_id")
        columnName.add("FName")
        columnName.add("LName")
        val columnValue = ArrayList<String>()
        columnValue.add(count.toString() + "")
        columnValue.add("Abc")
        columnValue.add("Def")
        databaseHelper?.insert(TABLE_NAME, columnName, columnValue)
        count++
    }

    /* this function get all data from given local database */
    private fun getDataFromTable() {
        val columnName = ArrayList<String>()
        columnName.add("_id")
        columnName.add("FName")
        columnName.add("LName")
        var cursor: Cursor? = null
        try {
            cursor = databaseHelper?.getWhere(TABLE_NAME, columnName, "", null!!, null!!)
        } catch (e: Exception) {
            e.localizedMessage
        }
        val idList = ArrayList<String>()
        val nameList = ArrayList<String>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    idList.add(cursor.getString(cursor.getColumnIndex("_id")))
                    nameList.add(cursor.getString(cursor.getColumnIndex("FName")))
                } while (cursor.moveToNext())
            }
        }

        if (idList.size > 0) {
            for (i in 0 until idList.size) {
                Log.d("localDb", "localDbUsage: id : " + idList.get(i))
                Log.d("localDb", "localDbUsage: name : " + nameList.get(i))
            }
        }

    }

    /* this function get data from local database based on selection */
    private fun getSelectedDataFromTable() {
        val columnName = ArrayList<String>()
        columnName.add("_id")
        columnName.add("FName")
        columnName.add("LName")
        val select = "_id=?"
        val selArgs = ArrayList<String>()
        selArgs.add("1") // string which you want to check and get from db
        var cursor:Cursor? = null
        try {
            cursor = databaseHelper?.getWhere(TABLE_NAME, columnName, select, selArgs, null!!)
        } catch (e: Exception) {
            e.localizedMessage
        }
        val idList = ArrayList<String>()
        val nameList = ArrayList<String>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    idList.add(cursor.getString(cursor.getColumnIndex("_id")))
                    nameList.add(cursor.getString(cursor.getColumnIndex("FName")))
                } while (cursor.moveToNext())
            }
        }

        if (idList.size > 0) {
            for (i in 0 until idList.size) {
                Log.d("localDb", "localDbUsage: selected id : " + idList[i])
                Log.d("localDb", "localDbUsage: selected name : " + nameList[i])
            }
        }
    }

    /* this function edit data in database */
    private fun editDataInDatabase() {
        val where = "_id=?"
        val colName = ArrayList<String>()
        colName.add("FName")
        colName.add("LName")
        val columnValue = ArrayList<String>()
        columnValue.add("KD")
        columnValue.add("Prajapati")
        val selArgs = ArrayList<String>()
        selArgs.add("1")
        databaseHelper?.update(TABLE_NAME, colName, columnValue, where, selArgs)
    }

    /* this function edit data in database */
    private fun deleteDataFromDatabase() {
        val where = "_id=?"
        val selArgs = ArrayList<String>()
        selArgs.add("1")
        /* this is for delete selected records */
        databaseHelper?.delete(TABLE_NAME, where, selArgs)
        /* this is for delete all records */
//        databaseHelper?.delete(TABLE_NAME, null!!, null!!)
    }
}
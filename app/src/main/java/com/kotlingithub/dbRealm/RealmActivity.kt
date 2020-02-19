package com.kotlingithub.dbRealm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_realm.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlingithub.utilities.MySharedPreference
import android.content.Context
import io.realm.Realm
import io.realm.RealmResults


class RealmActivity : AppCompatActivity() {

    private var mContext: Context? = null
    private var mRealm: Realm? = null
    private val myPref = "MyPreference"
    private val myPrefKey = "RealmId"
    private var realmId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.kotlingithub.R.layout.activity_realm)
        initBasicTask()
    }

    private fun initBasicTask() {
        mContext = this@RealmActivity
        mRealm = RealmHelper.getRealmInstance(this@RealmActivity)

        btnAdd.setOnClickListener { if (isValidateToAdd()) addRecord() }
        btnUpdate.setOnClickListener { if (isValidateToUpdate()) updateRecord() }
        setRecyclerView()
    }

    private fun getPreferenceIdForRealm() {
        realmId = MySharedPreference.getPreference(this, myPref, myPrefKey, 1) as Int
    }

    private fun setPreferenceIdForRealm(id: Int) {
        MySharedPreference.setPreference(this, myPref, myPrefKey, id)
    }

    fun clearModel(view: View) {
        clearRealmModelUsage()
    }

    /* this function get title from EditText which entered by user. */
    private fun getTrimmedTitle(): String {
        return etTitle.text.toString().trim()
    }

    /* this function add record from RealM db */
    private fun addRecord() {
        getPreferenceIdForRealm()
        mRealm?.beginTransaction()
        val myRealmTestModel = mRealm?.createObject(MyRealmTestModel::class.java)
        myRealmTestModel!!.id = realmId
        myRealmTestModel.title = getTrimmedTitle()
        mRealm?.commitTransaction()
        etTitle.setText("")
        etUpdateTitle.setText("")
        setPreferenceIdForRealm(realmId + 1)
    }

    /* this function update record in RealM db */
    private fun updateRecord() {
        val myRealmTestModel =
            mRealm?.where(MyRealmTestModel::class.java)!!.equalTo("title", getTrimmedTitle())
                .findFirst()
        if (myRealmTestModel != null) {
            mRealm?.beginTransaction()
            myRealmTestModel.title = etUpdateTitle.text.toString().trim()
            mRealm?.commitTransaction()
            etUpdateTitle.setText("")
            etTitle.setText("")
            etTitle.requestFocus()
        }
    }

    /* this function check validation before add record */
    private fun isValidateToAdd(): Boolean {
        val title = etTitle.text.toString().trim()
        if (title.equals("", ignoreCase = true)) {
            etTitle.error = "Please Enter Title!"
            etTitle.requestFocus()
            return false
        }
        /* usage of isExist method from RealmHelper class */
        if (RealmHelper.isExist(MyRealmTestModel::class.java, "title", title, null)) {
            Toast.makeText(mContext, "Already Exist!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /* this function check validation before update record */
    private fun isValidateToUpdate(): Boolean {
        val updateTitle = etUpdateTitle.text.toString().trim()
        if (updateTitle.equals("", ignoreCase = true)) {
            etUpdateTitle.error = "Please Enter Update Title!"
            etUpdateTitle.requestFocus()
            return false
        }
        return true
    }

    public override fun onDestroy() {
        super.onDestroy()
        mRealm!!.close()
    }

    private var allRecord : RealmResults<MyRealmTestModel>? = null


    /* this function set RealM db list into recycler view */
    private fun setRecyclerView() {
        /* usage of get all records from realm model */

        val c = mRealm?.where(MyRealmTestModel::class.java)?.findAll() as RealmResults<MyRealmTestModel>
        val myListAdapter =
            RealmTestingAdapter(c)
        rvList.layoutManager = LinearLayoutManager(mContext)
        rvList.adapter = myListAdapter
        rvList.setHasFixedSize(true)
        myListAdapter.setOnItemClick { o ->
            val myRealmTestModel = o as MyRealmTestModel
            /* here is usage of delete single record from realm db */
            RealmHelper.deleteRealm(
                MyRealmTestModel::class.java,
                "title",
                myRealmTestModel.title,
                null
            )
        }
    }

    /* this function show how to use RealmHelper's common method for check
       record isExist using multiple where condition checking */
    private fun isExistRecordUsage() {
        val strings = ArrayList<String>()
        val value = ArrayList<Any>()
        strings.add("id")
        strings.add("title")
        value.add(1)
        value.add("Kd")
        val isExist = RealmHelper.isExist(MyRealmTestModel::class.java, strings, value, null)
        Toast.makeText(mContext, "" + isExist, Toast.LENGTH_SHORT).show()
    }

    /* this function show how to clear model using RealmHelper class method */
    private fun clearRealmModelUsage() {
        RealmHelper.deleteRealm(MyRealmTestModel::class.java)
    }
}
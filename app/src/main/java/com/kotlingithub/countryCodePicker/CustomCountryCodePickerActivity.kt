package com.kotlingithub.countryCodePicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_country_code_picker.*
import android.text.Editable
import android.text.TextWatcher
import com.kotlingithub.R
import java.nio.charset.Charset


class CustomCountryCodePickerActivity : AppCompatActivity() {

    private lateinit var selectionAdapter : CountrySelectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_code_picker)
        initBasicTask()
    }

    private fun initBasicTask() {
        val countryCodeList = loadJSONFromAsset()
        settingListeners()
        if (countryCodeList!!.size > 0) {
            setAdapter(countryCodeList)
        }
    }

    /*Setting up listeners*/
    private fun settingListeners() {
        etSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // When user changed the Text
                selectionAdapter.filter.filter(cs.toString().trim { it <= ' ' })
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}

            override fun afterTextChanged(arg0: Editable) {}
        })
    }

    private fun loadJSONFromAsset(): ArrayList<SelectionListBean>? {
        val countryList = ArrayList<SelectionListBean>()
        val json: String?
        try {
            val `is` = application.assets.open("CountryCode.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            json = String(buffer, Charset.forName("UTF-8"))
            `is`.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        try {
            val jsonObject = JSONObject(json)
            val m_jArry = jsonObject.getJSONArray("countryDetails")
            for (i in 0 until m_jArry.length()) {
                val jo_inside = m_jArry.getJSONObject(i)
                val selectionListBean = SelectionListBean()
                val country = jo_inside.getString("name")
                val code = jo_inside.getString("dial_code")
                selectionListBean.name = country
                selectionListBean.id = code
                countryList.add(selectionListBean)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return countryList
    }

    private fun setAdapter(dataList: List<SelectionListBean>) {
        selectionAdapter = CountrySelectionAdapter(dataList, this)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCountryCodeList.layoutManager = mLayoutManager
        rvCountryCodeList.adapter = selectionAdapter
    }

    /**
     * Sending data back to previous activity
     *
     * @param selectionListBean
     */
    fun sendDataBack(selectionListBean: SelectionListBean) {
        val intent = Intent()
        name = selectionListBean.name!!
        code = selectionListBean.id!!
        intent.putExtra("Name", "" + selectionListBean.name!!)
        intent.putExtra("Id", "" + selectionListBean.id!!)
        setResult(1, intent)
        finish()
    }

    companion object {
        var name = ""
        var code = ""
    }
}
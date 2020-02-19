package com.kotlingithub.countryCodePicker

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlingithub.R


class CountrySelectionAdapter(
    private val list: List<SelectionListBean>,
    private val mActivity: Activity
) : RecyclerView.Adapter<CountrySelectionAdapter.ViewHolder>() {
    private var filterableList: List<SelectionListBean>? = null
    private val mFilter = ItemFilter()


    val filter: Filter
        get() = mFilter

    init {
        this.filterableList = list
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_selection_country, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.tvCountry.text = "" + filterableList!![position].name!!
        holder.tvCode.text = "(" + filterableList!![position].id + ")"

        holder.llContainer.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                (mActivity as CustomCountryCodePickerActivity).sendDataBack(filterableList!![position])
            }
        })
    }

    override fun getItemCount(): Int {
        return filterableList!!.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvCountry: TextView = view.findViewById(R.id.tvCountry)
        var tvCode: TextView = view.findViewById(R.id.tvCode)
        var llContainer: LinearLayout = view.findViewById(R.id.llContainer)
    }

    private inner class ItemFilter : Filter() {
        @SuppressLint("DefaultLocale")
        protected override fun performFiltering(constraint: CharSequence): FilterResults {

            val filterString = constraint.toString().toLowerCase()

            val results = FilterResults()

            val listData = list

            val count = listData.size
            val nlist = ArrayList<SelectionListBean>()

            for (i in 0 until count) {
                if (listData[i].name!!.toLowerCase().contains(filterString)) {
                    nlist.add(listData[i])
                }
            }
            results.values = nlist
            results.count = nlist.size

            return results
        }

        protected override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            filterableList = results.values as List<SelectionListBean>?
            notifyDataSetChanged()
        }
    }
}
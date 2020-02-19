package com.kotlingithub.dbRealm

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.kotlingithub.R
import io.realm.RealmChangeListener
import io.realm.RealmResults

class RealmTestingAdapterKt(private val allObjects: RealmResults<MyRealmTestModel>?) :
    RecyclerView.Adapter<RealmTestingAdapterKt.ViewHolder>(), RealmChangeListener {

    override fun getItemCount(): Int {
        return allObjects!!.size
    }

    private var onItemClickListener: OnItemClickListener? = null

    init {
        allObjects?.addChangeListener(this)
    }

    internal fun setOnItemClick(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_realm_item, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, @SuppressLint("RecyclerView") i: Int) {
        viewHolder.tvId.text = allObjects!![i].id as String
        viewHolder.tvTitle.text = allObjects[i].title
        viewHolder.btnRemove.setOnClickListener {
            if (onItemClickListener != null) onItemClickListener!!.onClickItem(
                allObjects[i]
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onChange() {
        notifyDataSetChanged()
    }

    internal interface OnItemClickListener {
        fun onClickItem(myDemoModel: Any)
    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
    }
}
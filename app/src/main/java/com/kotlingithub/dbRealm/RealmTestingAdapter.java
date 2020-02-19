package com.kotlingithub.dbRealm;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kotlingithub.R;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class RealmTestingAdapter extends RecyclerView.Adapter<RealmTestingAdapter.ViewHolder> implements RealmChangeListener {

    private RealmResults<MyRealmTestModel> allObjects;
    private OnItemClickListener onItemClickListener;

    public RealmTestingAdapter(RealmResults<MyRealmTestModel> allObjects) {
        this.allObjects = allObjects;
        allObjects.addChangeListener(this);
    }

    void setOnItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_realm_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.tvId.setText(allObjects.get(i).getId() + "");
        viewHolder.tvTitle.setText(allObjects.get(i).getTitle());
        viewHolder.btnRemove.setOnClickListener(v -> {
            if (onItemClickListener != null) onItemClickListener.onClickItem(allObjects.get(i));
        });
    }

    @Override
    public int getItemCount() {
        return allObjects != null ? allObjects.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    interface OnItemClickListener {
        void onClickItem(Object myDemoModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvId;
        private Button btnRemove;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
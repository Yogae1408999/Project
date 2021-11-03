package com.example.rybd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCatatan extends RecyclerView.Adapter<AdapterCatatan.CatatanViewHolder> {

    private ArrayList<Catatan> dataList;

    public AdapterCatatan(ArrayList<Catatan> dataList) {
        this.dataList = dataList;
    }
    @Override
    public CatatanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new CatatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CatatanViewHolder holder, int position) {
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtNpm.setText(dataList.get(position).getNpm());
        holder.txtNoHp.setText(dataList.get(position).getNohp());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class CatatanViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtNpm, txtNoHp;

        public CatatanViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.catatan1);
            txtNpm = (TextView) itemView.findViewById(R.id.catatan2);
            txtNoHp = (TextView) itemView.findViewById(R.id.catatan3);
        }
    }
}

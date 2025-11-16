package com.example.mhikenativeapp.adapters; // Thay bằng package của bạn

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhikenativeapp.R;
import com.example.mhikenativeapp.models.Hike;

import java.util.ArrayList;

/**
 * Adapter (bộ điều hợp) để "bơm" dữ liệu Hike vào RecyclerView.
 * Giống như ContactAdapter.
 */
public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private ArrayList<Hike> hikeList;
    private OnHikeActionListener listener; // Biến cho "listener"

    /**
     * Đây là "ViewHolder", nó "ôm" các View trong file item_hike.xml.
     * [cite: 1233-1244]
     */
    public static class HikeViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHikeName, tvHikeDate;
        public Button btnEdit, btnDelete;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các ID từ item_hike.xml
            tvHikeName = itemView.findViewById(R.id.tvHikeName);
            tvHikeDate = itemView.findViewById(R.id.tvHikeDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Cần lấy vị trí (position)
                    // Sẽ làm ở onBindViewHolder
                }
            });
        }
    }

    /**
     * Interface (giao diện) để gửi sự kiện click về Activity.
     *
     */
    public interface OnHikeActionListener {
        void onEditClick(Hike hike);
        void onDeleteClick(Hike hike);
        void onHikeClick(Hike hike);
    }

    // Constructor của Adapter, nhận vào danh sách và "listener"
    public HikeAdapter(ArrayList<Hike> hikeList, OnHikeActionListener listener) {
        this.hikeList = hikeList;
        this.listener = listener;
    }

    /**
     * Tạo ra một ViewHolder mới (bằng cách inflate layout item_hike.xml).
     * [cite: 1183-1188]
     */
    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hike, parent, false);
        return new HikeViewHolder(v);

    }

    /**
     * Lấy dữ liệu từ một Hike và gán nó vào các View trong ViewHolder.
     * [cite: 1190-1209]
     */
    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        // Lấy hike ở vị trí hiện tại
        Hike currentHike = hikeList.get(position);

        // Gán dữ liệu
        holder.tvHikeName.setText(currentHike.getName());
        holder.tvHikeDate.setText("Date: " + currentHike.getDate());

        // Gán sự kiện click cho nút Edit
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditClick(currentHike); // Gửi hike này về Activity
                }
            }
        });

        // Gán sự kiện click cho nút Delete
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteClick(currentHike); // Gửi hike này về Activity
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onHikeClick(currentHike); // <-- GỌI LISTENER MỚI
                }
            }
        });
    }

    /**
     * Trả về tổng số lượng item.
     * [cite: 1211-1213]
     */
    @Override
    public int getItemCount() {
        return hikeList.size();
    }
}
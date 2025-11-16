package com.example.mhikenativeapp.adapters; // Thay bằng package của bạn

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhikenativeapp.R;
import com.example.mhikenativeapp.models.Observation; // Import Observation model

import java.util.ArrayList;

/**
 * Adapter (bộ điều hợp) để "bơm" dữ liệu Observation vào RecyclerView.
 * Giống hệt HikeAdapter.
 */
public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {

    private ArrayList<Observation> observationList;
    private OnObservationActionListener listener; // Biến cho "listener"

    /**
     * ViewHolder: "ôm" các View trong file item_observation.xml.
     */
    public static class ObservationViewHolder extends RecyclerView.ViewHolder {
        public TextView tvObsText, tvObsTime, tvObsComments;
        public Button btnEditObs, btnDeleteObs;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các ID từ item_observation.xml
            tvObsText = itemView.findViewById(R.id.tvObsText);
            tvObsTime = itemView.findViewById(R.id.tvObsTime);
            tvObsComments = itemView.findViewById(R.id.tvObsComments);
            btnEditObs = itemView.findViewById(R.id.btnEditObs);
            btnDeleteObs = itemView.findViewById(R.id.btnDeleteObs);
        }
    }

    /**
     * Interface (giao diện) để gửi sự kiện click về Activity.
     * [cite_start]Cần thiết cho tính năng (c) [cite: 1779-1785].
     */
    public interface OnObservationActionListener {
        void onEditObsClick(Observation observation);
        void onDeleteObsClick(Observation observation);
    }

    // Constructor của Adapter, nhận vào danh sách và "listener"
    public ObservationAdapter(ArrayList<Observation> observationList, OnObservationActionListener listener) {
        this.observationList = observationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp (inflate) layout item_observation.xml
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        // Lấy observation ở vị trí hiện tại
        Observation currentObs = observationList.get(position);

        // Gán dữ liệu
        holder.tvObsText.setText(currentObs.getObservation());
        holder.tvObsTime.setText("Time: " + currentObs.getTime());
        holder.tvObsComments.setText("Comments: " + currentObs.getComments());

        // Gán sự kiện click cho nút Edit
        holder.btnEditObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditObsClick(currentObs);
                }
            }
        });

        // Gán sự kiện click cho nút Delete
        holder.btnDeleteObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteObsClick(currentObs);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }
}
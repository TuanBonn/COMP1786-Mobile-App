package com.example.mhikenativeapp; // Thay bằng package của bạn

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mhikenativeapp.adapters.HikeAdapter;
import com.example.mhikenativeapp.helpers.DatabaseHelper;
import com.example.mhikenativeapp.models.Hike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HikeListActivity extends AppCompatActivity implements HikeAdapter.OnHikeActionListener {

    private RecyclerView rvHikes;
    private FloatingActionButton fabAddHike;
    private DatabaseHelper dbHelper;

    // Khai báo 2 biến này ở cấp class
    private ArrayList<Hike> hikeList;
    private HikeAdapter hikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_list);

        dbHelper = new DatabaseHelper(this);
        rvHikes = findViewById(R.id.rvHikes);
        fabAddHike = findViewById(R.id.fabAddHike);

        // Cài đặt RecyclerView MỘT LẦN
        setupRecyclerView();

        fabAddHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HikeListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView() {
        // KHỞI TẠO DANH SÁCH VÀ ADAPTER Ở ĐÂY
        hikeList = new ArrayList<>(); // 1. Tạo 1 danh sách MỚI (List A)
        hikeAdapter = new HikeAdapter(hikeList, this); // 2. "Giao" List A cho Adapter

        rvHikes.setLayoutManager(new LinearLayoutManager(this));
        rvHikes.setAdapter(hikeAdapter); // 3. Set Adapter (đang dùng List A)
    }

    /**
     * Hàm này chỉ xóa List A và "bơm" dữ liệu mới vào nó.
     * Nó KHÔNG tạo ra list hay adapter mới.
     */
    private void loadHikes() {
        hikeList.clear(); // 1. Xóa sạch List A
        hikeList.addAll(dbHelper.getAllHikes()); // 2. Bơm dữ liệu mới từ DB vào List A
        hikeAdapter.notifyDataSetChanged(); // 3. Báo Adapter (vẫn đang dùng List A) "Vẽ lại đi!"
    }

    /**
     * Hàm này được gọi MỖI KHI bạn quay lại màn hình này.
     * Đây là chìa khóa để làm mới danh sách.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadHikes(); // Tải lại danh sách
    }

    // --- Các hàm onEditClick và onDeleteClick (giữ nguyên) ---
    @Override
    public void onEditClick(Hike hike) {
        Toast.makeText(this, "Editing: " + hike.getName(), Toast.LENGTH_SHORT).show();
        // (Sẽ làm màn hình Edit sau)
    }

    @Override
    public void onDeleteClick(Hike hike) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Hike")
                .setMessage("Are you sure you want to delete '" + hike.getName() + "'?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteHike(hike.getId());
                        loadHikes(); // Tải lại danh sách ngay sau khi xóa
                        Toast.makeText(HikeListActivity.this, "Hike deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
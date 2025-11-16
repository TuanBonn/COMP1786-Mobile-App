package com.example.mhikenativeapp; // Thay bằng package của bạn

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
// THÊM IMPORT NÀY
import androidx.appcompat.widget.SearchView;

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

/**
 * Màn hình danh sách.
 * ĐÃ NÂNG CẤP: Implement SearchView.OnQueryTextListener (Tính năng d).
 */
public class HikeListActivity extends AppCompatActivity
        implements HikeAdapter.OnHikeActionListener, SearchView.OnQueryTextListener { // <-- THÊM INTERFACE NÀY

    // Khai báo Views
    private RecyclerView rvHikes;
    private FloatingActionButton fabAddHike;
    private SearchView searchView; // <-- TÍNH NĂNG MỚI

    // Khai báo Data helpers
    private DatabaseHelper dbHelper;
    private ArrayList<Hike> hikeList;
    private HikeAdapter hikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_list);

        dbHelper = new DatabaseHelper(this);

        // Ánh xạ Views từ XML
        rvHikes = findViewById(R.id.rvHikes);
        fabAddHike = findViewById(R.id.fabAddHike);
        searchView = findViewById(R.id.searchView); // <-- TÍNH NĂNG MỚI

        setupRecyclerView();

        // Cài đặt nút "+"
        fabAddHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HikeListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // CÀI ĐẶT SEARCHVIEW
        searchView.setOnQueryTextListener(this); // Gán "listener" là Activity này
    }

    private void setupRecyclerView() {
        hikeList = new ArrayList<>();
        hikeAdapter = new HikeAdapter(hikeList, this);
        rvHikes.setLayoutManager(new LinearLayoutManager(this));
        rvHikes.setAdapter(hikeAdapter);
    }

    /**
     * Tải lại TOÀN BỘ Hikes (khi không tìm kiếm).
     */
    private void loadAllHikes() {
        hikeList.clear();
        hikeList.addAll(dbHelper.getAllHikes());
        hikeAdapter.notifyDataSetChanged();
    }

    /**
     * Tải Hikes DỰA TRÊN TÌM KIẾM.
     * @param query Chuỗi tìm kiếm
     */
    private void searchHikes(String query) {
        hikeList.clear();
        hikeList.addAll(dbHelper.searchHikes(query)); // Gọi hàm searchHikes mới
        hikeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllHikes(); // Tải lại toàn bộ danh sách khi quay lại
    }

    // --- Xử lý sự kiện từ Adapter (giữ nguyên) ---

    @Override
    public void onEditClick(Hike hike) {
        Intent intent = new Intent(HikeListActivity.this, EditHikeActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
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
                        loadAllHikes(); // Tải lại toàn bộ danh sách
                        Toast.makeText(HikeListActivity.this, "Hike deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onHikeClick(Hike hike) {
        Intent intent = new Intent(HikeListActivity.this, HikeDetailActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
    }

    // --- 2 HÀM BẮT BUỘC MỚI cho OnQueryTextListener ---

    /**
     * Được gọi khi người dùng nhấn "Enter" (Submit) trên bàn phím.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchHikes(query); // Thực hiện tìm kiếm
        return true;
    }

    /**
     * Được gọi MỖI KHI người dùng gõ 1 chữ.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            loadAllHikes(); // Nếu thanh search rỗng, tải lại toàn bộ
        } else {
            searchHikes(newText); // Thực hiện tìm kiếm "live"
        }
        return true;
    }
}
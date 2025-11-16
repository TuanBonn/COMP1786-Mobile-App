package com.example.mhikenativeapp; // Thay bằng package của bạn

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhikenativeapp.adapters.ObservationAdapter;
import com.example.mhikenativeapp.helpers.DatabaseHelper;
import com.example.mhikenativeapp.models.Hike;
import com.example.mhikenativeapp.models.Observation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Màn hình (Activity) Chi tiết Chuyến đi.
 * Hiển thị thông tin Hike và danh sách Observations (Tính năng c).
 * Nó "lắng nghe" các sự kiện click từ ObservationAdapter.
 */
public class HikeDetailActivity extends AppCompatActivity implements ObservationAdapter.OnObservationActionListener {

    // Khai báo Views (Hike Details)
    private TextView tvDetailName, tvDetailLocation, tvDetailDate, tvDetailParking;
    private TextView tvDetailLength, tvDetailDifficulty, tvDetailWeather, tvDetailTrailCondition, tvDetailDescription;

    // Khai báo Views (Observations)
    private RecyclerView rvObservations;
    private FloatingActionButton fabAddObservation;

    // Khai báo Data helpers
    private DatabaseHelper dbHelper;
    private ArrayList<Observation> observationList;
    private ObservationAdapter observationAdapter;

    private long hikeId; // ID của chuyến đi đang xem
    private Hike currentHike; // Thông tin của chuyến đi đang xem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        dbHelper = new DatabaseHelper(this);

        // Lấy ID được gửi từ HikeListActivity
        Intent intent = getIntent();
        hikeId = intent.getLongExtra("HIKE_ID", -1);

        if (hikeId == -1) {
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ánh xạ (mapping) tất cả các Views
        setupUI();
        // Cài đặt RecyclerView
        setupRecyclerView();
        // Gán sự kiện click
        setupClickListeners();
    }

    /**
     * Tải lại dữ liệu mỗi khi quay lại màn hình này
     * (ví dụ: quay lại từ màn hình Thêm Observation).
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadHikeData(); // Tải lại thông tin Hike (phòng trường hợp nó vừa được Edit)
        loadObservations(); // Tải lại danh sách Observations
    }

    /**
     * Ánh xạ các biến Java với các ID trong file XML.
     */
    private void setupUI() {
        // Ánh xạ các TextView chi tiết
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailParking = findViewById(R.id.tvDetailParking);
        tvDetailLength = findViewById(R.id.tvDetailLength);
        tvDetailDifficulty = findViewById(R.id.tvDetailDifficulty);
        tvDetailWeather = findViewById(R.id.tvDetailWeather);
        tvDetailTrailCondition = findViewById(R.id.tvDetailTrailCondition);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);

        // Ánh xạ RecyclerView và FAB
        rvObservations = findViewById(R.id.rvObservations);
        fabAddObservation = findViewById(R.id.fabAddObservation);
    }

    /**
     * Khởi tạo RecyclerView, Adapter, và LayoutManager.
     */
    private void setupRecyclerView() {
        observationList = new ArrayList<>();
        // Khởi tạo Adapter, truyền "this" làm "listener"
        observationAdapter = new ObservationAdapter(observationList, this);

        rvObservations.setLayoutManager(new LinearLayoutManager(this));
        rvObservations.setAdapter(observationAdapter);
    }

    /**
     * Gán sự kiện click cho nút "+".
     */
    private void setupClickListeners() {
        fabAddObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở màn hình AddObservationActivity
                Intent intent = new Intent(HikeDetailActivity.this, AddObservationActivity.class);

                // Gửi ID của chuyến đi (HIKE_ID) sang
                // để màn hình mới biết nó đang thêm quan sát cho ai.
                intent.putExtra("HIKE_ID", hikeId);

                startActivity(intent);
            }
        });
    }

    /**
     * Lấy dữ liệu của Hike từ DB và "bơm" vào các TextView.
     */
    private void loadHikeData() {
        currentHike = dbHelper.getHikeById(hikeId);

        if (currentHike != null) {
            tvDetailName.setText(currentHike.getName());
            tvDetailLocation.setText("Location: " + currentHike.getLocation());
            tvDetailDate.setText("Date: " + currentHike.getDate());
            tvDetailParking.setText("Parking: " + currentHike.getParkingAvailable());
            tvDetailLength.setText("Length: " + currentHike.getLength() + " km");
            tvDetailDifficulty.setText("Difficulty: " + currentHike.getDifficulty());
            tvDetailWeather.setText("Weather: " + currentHike.getWeather());
            tvDetailTrailCondition.setText("Trail: " + currentHike.getTrailCondition());
            tvDetailDescription.setText("Description: " + currentHike.getDescription());
        } else {
            Toast.makeText(this, "Error: Hike data corrupted.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Lấy danh sách Observations từ DB và cập nhật RecyclerView.
     */
    private void loadObservations() {
        observationList.clear();
        observationList.addAll(dbHelper.getAllObservationsForHike(hikeId));
        observationAdapter.notifyDataSetChanged();
    }

    // --- Xử lý sự kiện click từ ObservationAdapter ---

    @Override
    public void onEditObsClick(Observation observation) {
        // TẠM THỜI:
        Toast.makeText(this, "Editing observation: " + observation.getObservation(), Toast.LENGTH_SHORT).show();
        // (Sẽ làm màn hình EditObservationActivity sau)
    }

    @Override
    public void onDeleteObsClick(Observation observation) {
        // Hiển thị hộp thoại xác nhận
        new AlertDialog.Builder(this)
                .setTitle("Delete Observation")
                .setMessage("Are you sure you want to delete this observation?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteObservation(observation.getId());
                        loadObservations(); // Tải lại danh sách quan sát
                        Toast.makeText(HikeDetailActivity.this, "Observation deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
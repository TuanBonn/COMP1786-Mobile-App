package com.example.mhikenativeapp; // Thay bằng package của bạn

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mhikenativeapp.helpers.DatabaseHelper;
import com.example.mhikenativeapp.models.Observation;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Màn hình (Activity) để thêm một Observation mới.
 * [cite: 380-381]
 */
public class AddObservationActivity extends AppCompatActivity {

    // Khai báo Views
    private TextInputEditText etObservationText, etObservationTime, etObservationComments;
    private Button btnSaveObservation;

    // Khai báo Data helpers
    private DatabaseHelper dbHelper;
    private long hikeId; // ID của chuyến đi mà chúng ta đang thêm quan sát

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        dbHelper = new DatabaseHelper(this);

        // Lấy HIKE_ID được gửi từ HikeDetailActivity
        Intent intent = getIntent();
        hikeId = intent.getLongExtra("HIKE_ID", -1);

        if (hikeId == -1) {
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity nếu không có ID
            return;
        }

        // Ánh xạ (mapping) các Views
        setupUI();

        // Gán sự kiện click
        setupClickListeners();

        // Tự động điền giờ hiện tại
        setDefaultTime();
    }

    /**
     * Ánh xạ các biến Java với các ID trong file XML.
     */
    private void setupUI() {
        etObservationText = findViewById(R.id.etObservationText);
        etObservationTime = findViewById(R.id.etObservationTime);
        etObservationComments = findViewById(R.id.etObservationComments);
        btnSaveObservation = findViewById(R.id.btnSaveObservation);
    }

    /**
     * Tự động điền giờ/ngày hiện tại vào ô Thời gian.
     *
     */
    private void setDefaultTime() {
        // Lấy ngày giờ hiện tại
        Calendar calendar = Calendar.getInstance();
        // Định dạng nó (ví dụ: "26/11/2025 14:30")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String currentTime = sdf.format(calendar.getTime());

        etObservationTime.setText(currentTime);
    }

    /**
     * Gán sự kiện click cho nút Save.
     */
    private void setupClickListeners() {
        btnSaveObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObservation();
            }
        });
    }

    /**
     * Kiểm tra các trường bắt buộc.
     * [cite: 384-385]
     */
    private boolean validateInput() {
        if (etObservationText.getText().toString().trim().isEmpty()) {
            etObservationText.setError("Observation text is required");
            return false;
        }
        if (etObservationTime.getText().toString().trim().isEmpty()) {
            etObservationTime.setError("Time is required");
            return false;
        }
        return true;
    }

    /**
     * Lấy dữ liệu, validation, và lưu vào SQLite.
     */
    private void saveObservation() {
        // 1. Validation
        if (!validateInput()) {
            Toast.makeText(this, "Please fix the errors", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Lấy dữ liệu từ Form
        String obsText = etObservationText.getText().toString().trim();
        String obsTime = etObservationTime.getText().toString().trim();
        String obsComments = etObservationComments.getText().toString().trim();

        // 3. Tạo đối tượng Observation
        Observation newObservation = new Observation();
        newObservation.setObservation(obsText);
        newObservation.setTime(obsTime);
        newObservation.setComments(obsComments);
        newObservation.setHikeId(hikeId); // Gán khóa ngoại (Foreign Key)

        // 4. Lưu vào Database
        long id = dbHelper.addObservation(newObservation);

        // 5. Phản hồi và đóng Activity
        if (id != -1) {
            Toast.makeText(this, "Observation added successfully!", Toast.LENGTH_LONG).show();
            finish(); // Tự động đóng và quay lại HikeDetailActivity
        } else {
            Toast.makeText(this, "Failed to add observation", Toast.LENGTH_SHORT).show();
        }
    }
}
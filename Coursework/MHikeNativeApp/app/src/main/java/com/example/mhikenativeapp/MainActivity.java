package com.example.mhikenativeapp; // Thay bằng package của bạn

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent; // Đảm bảo đã import
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mhikenativeapp.helpers.DatabaseHelper;
import com.example.mhikenativeapp.models.Hike;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Khai báo tất cả các View
    private TextInputEditText etHikeName, etHikeLocation, etHikeDate, etHikeLength, etHikeDescription, etWeather, etTrailCondition;
    private RadioGroup rgParking;
    private Spinner spinnerDifficulty;

    // Khai báo 2 nút
    private Button btnSaveHike;
    private Button btnViewAllHikes; // NÚT QUAN TRỌNG

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        setupUI();
        setupSpinner();
        setupClickListeners();
    }

    private void setupUI() {
        // Ánh xạ tất cả EditText
        etHikeName = findViewById(R.id.etHikeName);
        etHikeLocation = findViewById(R.id.etHikeLocation);
        etHikeDate = findViewById(R.id.etHikeDate);
        etHikeLength = findViewById(R.id.etHikeLength);
        etHikeDescription = findViewById(R.id.etHikeDescription);

        // Ánh xạ 2 trường mới (BẠN PHẢI THÊM VÀO XML)
        etWeather = findViewById(R.id.etWeather);
        etTrailCondition = findViewById(R.id.etTrailCondition);

        // Ánh xạ các control khác
        rgParking = findViewById(R.id.rgParking);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);

        // Ánh xạ 2 nút
        btnSaveHike = findViewById(R.id.btnSaveHike);
        btnViewAllHikes = findViewById(R.id.btnViewAllHikes); // HÃY CHẮC LÀ BẠN CÓ NÚT NÀY
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty_levels,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);
    }

    private void setupClickListeners() {
        // Sự kiện click cho nút Save
        btnSaveHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHike();
            }
        });

        // Sự kiện click cho nút View All
        btnViewAllHikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HikeListActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện click cho ô Ngày (để hiển thị lịch)
        etHikeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etHikeDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private String getParkingValue() {
        int selectedId = rgParking.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return "";
        }
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
    }

    private boolean validateInput() {
        if (etHikeName.getText().toString().trim().isEmpty()) {
            etHikeName.setError("Name is required");
            return false;
        }
        if (etHikeLocation.getText().toString().trim().isEmpty()) {
            etHikeLocation.setError("Location is required");
            return false;
        }
        if (etHikeDate.getText().toString().trim().isEmpty()) {
            etHikeDate.setError("Date is required");
            return false;
        }
        if (etHikeLength.getText().toString().trim().isEmpty()) {
            etHikeLength.setError("Length is required");
            return false;
        }
        if (getParkingValue().isEmpty()) {
            Toast.makeText(this, "Please select parking availability", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveHike() {
        if (!validateInput()) {
            Toast.makeText(this, "Please fix the errors", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etHikeName.getText().toString().trim();
        String location = etHikeLocation.getText().toString().trim();
        String date = etHikeDate.getText().toString().trim();
        String parking = getParkingValue();
        String length = etHikeLength.getText().toString().trim();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        String description = etHikeDescription.getText().toString().trim();
        String weather = etWeather.getText().toString().trim();
        String trailCondition = etTrailCondition.getText().toString().trim();

        Hike newHike = new Hike();
        newHike.setName(name);
        newHike.setLocation(location);
        newHike.setDate(date);
        newHike.setParkingAvailable(parking);
        newHike.setLength(length);
        newHike.setDifficulty(difficulty);
        newHike.setDescription(description);
        newHike.setWeather(weather);
        newHike.setTrailCondition(trailCondition);

        long id = dbHelper.addHike(newHike);

        if (id != -1) {
            Toast.makeText(this, "Hike saved successfully! ID: " + id, Toast.LENGTH_LONG).show();
            clearForm();
        } else {
            Toast.makeText(this, "Failed to save hike", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        etHikeName.setText("");
        etHikeLocation.setText("");
        etHikeDate.setText("");
        etHikeLength.setText("");
        etHikeDescription.setText("");
        etWeather.setText("");
        etTrailCondition.setText("");
        rgParking.clearCheck();
        spinnerDifficulty.setSelection(0);
        etHikeName.requestFocus();
    }
}
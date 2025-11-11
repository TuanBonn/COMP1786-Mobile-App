package com.example.imageviewer 

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.imageviewer.R

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnPrevious: Button
    private lateinit var btnNext: Button

    private val imageList = listOf(
        R.drawable.pic1,
        R.drawable.pic2,
        R.drawable.pic3
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)

        updateImage()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnNext.setOnClickListener {
            currentIndex++

            if (currentIndex >= imageList.size) {
                currentIndex = 0
            }

            updateImage()
        }

        btnPrevious.setOnClickListener {
            currentIndex--

            if (currentIndex < 0) {
                currentIndex = imageList.size - 1
            }

            updateImage()
        }
    }

    private fun updateImage() {
        imageView.setImageResource(imageList[currentIndex])
    }
}
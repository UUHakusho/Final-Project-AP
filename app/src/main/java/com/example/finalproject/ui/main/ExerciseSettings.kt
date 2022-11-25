package com.example.finalproject.ui.main

import android.R
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.OnePostBinding


class ExerciseSettings : AppCompatActivity() {
    private lateinit var binding: OnePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fetch data from the calling fragment
        val title = intent.extras?.getString("title")
        val numSets = intent.extras?.getInt("sets")
        val numReps = intent.extras?.getInt("reps")
        val numWeight = intent.extras?.getInt("weight")

        binding.InputWeight.setText(numWeight.toString())

        val setAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, arrayOf(1,2,3,4,5))
        binding.setSpinner.adapter = setAdapter
        binding.setSpinner.setSelection(numSets!! - 1)

        val repAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, arrayOf(1,2,3,4,5,6,7,8,9,10))
        binding.repSpinner.adapter = repAdapter
        binding.repSpinner.setSelection(numReps!! - 1)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
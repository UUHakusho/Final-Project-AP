package com.example.finalproject.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import com.example.finalproject.data.Repository
import com.example.finalproject.databinding.PlotViewBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class PlotViewActivity : AppCompatActivity() {
    private lateinit var binding: PlotViewBinding

    companion object {
        const val keyKey = "key"
        const val keyTitle = "title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlotViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.extras?.getString(keyKey)
        val title = intent.extras?.getString(keyTitle)

        supportActionBar?.title = "$title Progress Plot"

        val lineChart = findViewById<LineChart>(R.id.lineChart)
        val entries = Repository.getWorkoutItemHistory(key!!)
        val vl = LineDataSet(entries, title)

        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 5f

        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.data = LineData(vl)
        lineChart.axisRight.isEnabled = false

        lineChart.description.text = "Weight (lbs) over time (day of yr)"
        lineChart.xAxis.setDrawLabels(true)
        lineChart.description.textSize = 15f
        lineChart.setNoDataText("No workouts saved yet!")
        lineChart.legend.textSize = 15f
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }
}
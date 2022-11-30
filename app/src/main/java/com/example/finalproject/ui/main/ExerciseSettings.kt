package com.example.finalproject.ui.main

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ExerciseSettingsBinding


class ExerciseSettings : AppCompatActivity() {
    private lateinit var binding: ExerciseSettingsBinding
    private var numSets = 0
    private var numReps = 0
    private var numWeight = 0
    private var title = ""
    private var numMins = 0
    private var numSec = 0

    companion object {
        const val titleKey = "title"
        const val setsKey = "sets"
        const val repsKey = "reps"
        const val weightKey = "weight"
        const val minsKey = "restMins"
        const val secKey = "restSec"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExerciseSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fetch data from the calling fragment
        title = intent.extras?.getString(titleKey).toString()
        numSets = intent.extras?.getInt(setsKey)!!
        numReps = intent.extras?.getInt(repsKey)!!
        numWeight = intent.extras?.getInt(weightKey)!!
        numMins = intent.extras?.getInt(minsKey)!!
        numSec = intent.extras?.getInt(secKey)!!

        supportActionBar?.title = "$title Settings"

        binding.InputWeight.setText(numWeight.toString())

        val setAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, arrayOf(1,2,3,4,5))
        binding.setSpinner.adapter = setAdapter
        binding.setSpinner.setSelection(numSets!! - 1)
        binding.setSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                numSets = position + 1
            }
        }

        val repAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, arrayOf(1,2,3,4,5,6,7,8,9))
        binding.repSpinner.adapter = repAdapter
        binding.repSpinner.setSelection(numReps - 1)
        binding.repSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Dbg", "Position selected: $position")
                numReps = position + 1
            }
        }

        val minsArray = arrayOf(0,1,2,3,4,5,6,7,8,9)
        val restMinAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, minsArray)
        binding.minsSpinner.adapter = restMinAdapter
        Log.d("Dbg", "Num minutes: $numMins")
        binding.minsSpinner.setSelection(minsArray.indexOf(numMins))
        binding.minsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                numMins = minsArray[position]
            }
        }

        val secArray = arrayOf(0,15,30,45)
        val restSecAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, secArray)
        binding.secSpinner.adapter = restSecAdapter
        binding.secSpinner.setSelection(secArray.indexOf(numSec))
        binding.secSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                numSec = secArray[position]
            }
        }

        binding.btnCancel.setOnClickListener { cancelButton() }
        binding.btnOk.setOnClickListener { okButton() }
    }

    private fun cancelButton() {
        finish()
    }

    private fun okButton() {
        numWeight = binding.InputWeight.text.toString().toInt()

        val intent = Intent()
        intent.putExtra(titleKey, title)
        intent.putExtra(setsKey, numSets)
        intent.putExtra(repsKey, numReps)
        intent.putExtra(weightKey, numWeight)
        intent.putExtra(minsKey, numMins)
        intent.putExtra(secKey, numSec)
        setResult(RESULT_OK, intent)
        finish()
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
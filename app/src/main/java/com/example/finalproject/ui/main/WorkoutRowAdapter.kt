package com.example.finalproject.ui.main

import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.data.Exercise
import com.example.finalproject.databinding.RowWorkoutBinding
import com.example.finalproject.ui.main.exercises.ExerciseRowAdapter
import com.google.android.material.button.MaterialButton


class  WorkoutRowAdapter(private val viewModel: PageViewModel,
                        private val workoutFragment: WorkoutFragment,
                        private var optionsMenuClickListener: OptionsMenuClickListener
)
    : ListAdapter<Exercise, WorkoutRowAdapter.VH>(ExerciseRowAdapter.ExerciseDiff()) {

    private var listButton = listOf<MaterialButton>()

    inner class VH(val rowPostBinding: RowWorkoutBinding)
        : RecyclerView.ViewHolder(rowPostBinding.root) {

        init {
            rowPostBinding.root.setOnClickListener {
                val currExercise = getItem(adapterPosition)
                workoutFragment.launchExerciseSettings(currExercise)
            }

            val bind = rowPostBinding
            listButton = listOf(bind.setCount1, bind.setCount2, bind.setCount3, bind.setCount4, bind.setCount5)
            for(i in 0 until listButton.size) {
                listButton[i].setOnClickListener {
                    val currExercise = getItem(adapterPosition)
                    val idxCurrExercise = viewModel.listOfWorkouts.value?.indexOf(getItem(adapterPosition))!!

                    // Set all the previous sets to complete and disable the buttons
                    for(idx in 0..i) {
                        viewModel.listOfWorkouts.value!![idxCurrExercise].isComplete[idx] = true
                    }

                    // Set a timer based on the user's preferences from the settings
                    val mSecTimer = (currExercise.numRestMin * 60) + currExercise.numRestSec
                    Log.d("Dbg", "Num seconds: $mSecTimer")
                    currExercise.countDown.cancel()
                    currExercise.countDown = object: CountDownTimer((mSecTimer * 1000).toLong(), 1000) {
                        override fun onTick(ms: Long) {
                            val time = String.format("%02d:%02d", ms/1000/60, ms/1000%60)
                            rowPostBinding.restText.text = "Rest: $time"
                        }
                        override fun onFinish() {
                            val time = String.format("%02d:%02d", currExercise.numRestMin, currExercise.numRestSec)
                            rowPostBinding.restText.text = "Rest: $time"
                            Toast.makeText(itemView.context, "Rest finished, start next set.", Toast.LENGTH_SHORT).show()
                            this.cancel()
                        }
                    }
                    currExercise.countDown.start()
                    viewModel.listOfWorkouts.postValue(viewModel.listOfWorkouts.value)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RowWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val currExercise = getItem(holder.adapterPosition)
        holder.rowPostBinding.title.text = currExercise.title
        holder.rowPostBinding.weight.text = "Weight: ${currExercise.numWeight}"
        val time = String.format("%02d:%02d", currExercise.numRestMin, currExercise.numRestSec)
        holder.rowPostBinding.restText.text = "Rest: $time"

        val bind = holder.rowPostBinding
        listButton = listOf(bind.setCount1, bind.setCount2, bind.setCount3, bind.setCount4, bind.setCount5)
        for(i in 0 until listButton.size) {
            listButton[i].isVisible = i < currExercise.numSets
            listButton[i].text = currExercise.numReps.toString()
            if(currExercise.isComplete[i]) {
                listButton[i].setBackgroundColor(Color.GREEN)
                listButton[i].isEnabled = false
            }
            else {
                listButton[i].setBackgroundColor(Color.LTGRAY)
                listButton[i].isEnabled = true
            }
        }

        holder.rowPostBinding.textViewOptions.setOnClickListener {
            optionsMenuClickListener.onOptionsMenuClicked(position)
        }
    }

    fun clearAllExerciseData(position: Int) {
        for(idx in 0 until currentList[position].isComplete.size) {
            currentList[position].isComplete[idx] = false
            currentList[position].countDown.cancel()
        }
    }

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }
}
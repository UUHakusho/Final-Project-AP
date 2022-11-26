package com.example.finalproject.ui.main

import android.R
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.finalproject.data.Exercise
import com.example.finalproject.databinding.RowWorkoutBinding
import com.example.finalproject.ui.main.exercises.ExerciseRowAdapter
import com.google.android.material.button.MaterialButton


class WorkoutRowAdapter(private val viewModel: PageViewModel,
                        private val workoutFragment: WorkoutFragment
)
    : ListAdapter<Exercise, WorkoutRowAdapter.VH>(ExerciseRowAdapter.ExerciseDiff()) {
    inner class VH(val rowPostBinding: RowWorkoutBinding)
        : RecyclerView.ViewHolder(rowPostBinding.root) {
        init {
            rowPostBinding.title.setOnClickListener {
                val currExercise = getItem(adapterPosition)
                workoutFragment.launchExerciseSettings(currExercise)
            }

            val countDown = object : CountDownTimer(70000, 1000) {
                override fun onTick(ms: Long) {
                    val time = String.format("%02d:%02d", ms/1000/60, ms/1000%60)
                    rowPostBinding.restText.text = "Rest: $time"
                }
                override fun onFinish() {
                    rowPostBinding.restText.text = "Rest Finished"
                    Toast.makeText(itemView.context, "Rest finished, start next set.", Toast.LENGTH_SHORT).show()
                }
            }

            val bind = rowPostBinding
            val listButton = listOf(bind.setCount1, bind.setCount2, bind.setCount3, bind.setCount4, bind.setCount5)
            for(i in 0..4) {
                listButton[i].setOnClickListener {
                    listButton[i].setBackgroundColor(Color.GREEN)
                    listButton[i].isEnabled = false
                    //listButton[i].text = (listButton[i].text.toString().toInt()-1).toString()
                    countDown.cancel()
                    countDown.start()
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

        /*val constraintLayout = holder.rowPostBinding.constraintLayout
        val materialButton = MaterialButton(holder.itemView.context)
        materialButton.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            )
        materialButton.text = "5"
        materialButton.cornerRadius = 28
        constraintLayout.addView(materialButton)*/
        val bind = holder.rowPostBinding
        val listButton = listOf(bind.setCount1, bind.setCount2, bind.setCount3, bind.setCount4, bind.setCount5)
        for(i in 0..4) {
            listButton[i].isVisible = i < currExercise.numSets
            listButton[i].text = currExercise.numReps.toString()
        }
    }
}
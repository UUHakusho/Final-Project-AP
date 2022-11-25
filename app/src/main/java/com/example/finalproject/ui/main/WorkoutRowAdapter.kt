package com.example.finalproject.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.data.Exercise
import com.example.finalproject.databinding.RowWorkoutBinding
import com.example.finalproject.ui.main.exercises.ExerciseRowAdapter

class WorkoutRowAdapter(private val viewModel: PageViewModel)
    : ListAdapter<Exercise, WorkoutRowAdapter.VH>(ExerciseRowAdapter.ExerciseDiff()) {
    inner class VH(val rowPostBinding: RowWorkoutBinding)
        : RecyclerView.ViewHolder(rowPostBinding.root) {
        init {
            rowPostBinding.title.setOnClickListener {
                val currExercise = getItem(adapterPosition)
                val intent = Intent(itemView.context, ExerciseSettings::class.java)
                // Launch the OnePost intent
                intent.apply {
                    putExtra("title", currExercise.title.toString())
                    putExtra("sets", currExercise.numSets)
                    putExtra("reps", currExercise.numReps)
                    putExtra("weight", currExercise.numWeight)
                }
                itemView.context.startActivity(intent)
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
        holder.rowPostBinding.weight.text = "Weight: " + currExercise.numWeight
    }
}
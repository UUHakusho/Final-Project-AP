package com.example.finalproject.ui.main.exercises

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity
import com.example.finalproject.data.Exercise
import com.example.finalproject.databinding.RowExerciseBinding
import com.example.finalproject.ui.main.PageViewModel

class ExerciseRowAdapter(private val viewModel: PageViewModel)
    : ListAdapter<Exercise, ExerciseRowAdapter.VH>(ExerciseDiff()) {
    class ExerciseDiff : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.key == newItem.key
        }
        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description
        }
    }

    var listExercises = listOf<Exercise>()
    var isSelected = false

    inner class VH(val rowExerciseBinding: RowExerciseBinding)
        : RecyclerView.ViewHolder(rowExerciseBinding.root) {
        init {
            this.itemView.setOnClickListener {
                val currExercise = listExercises[adapterPosition]
                viewModel.modifyWorkoutList(currExercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RowExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val sub = listExercises[holder.adapterPosition]
        holder.rowExerciseBinding.subRowHeading.text = sub.title
        holder.rowExerciseBinding.subRowDetails.text = sub.description
        if(viewModel.observeWorkoutList()!!.contains(sub)) {
            holder.itemView.setBackgroundColor(Color.GREEN)
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int {
        return listExercises.size
    }
}

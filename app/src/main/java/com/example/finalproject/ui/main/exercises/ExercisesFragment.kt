package com.example.finalproject.ui.main.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.databinding.FragmentRvBinding
import com.example.finalproject.ui.main.PageViewModel

class ExercisesFragment : Fragment() {
    private lateinit var pageViewModel: PageViewModel
    private lateinit var adapter: ExerciseRowAdapter
    private var _binding: FragmentRvBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(sectionNumber: Int): ExercisesFragment {
            return ExercisesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ExercisesFragment.ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(requireActivity()).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ExercisesFragment.ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ExerciseRowAdapter(pageViewModel)
        _binding?.recyclerView?.adapter = adapter
        _binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        _binding?.recyclerView?.addItemDecoration(DividerItemDecoration
            (_binding?.recyclerView!!.context, LinearLayoutManager.VERTICAL))

        pageViewModel.mediatorListOfExercises.observe(viewLifecycleOwner) {
            adapter.listExercises = it
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        _binding = null
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onDestroyView()
    }
}
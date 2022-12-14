package com.example.finalproject

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.ui.main.SectionsPagerAdapter
import com.example.finalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val mainFragTag = "mainFragTag"
        private const val favoritesFragTag = "workoutFragTag"
        private const val subredditsFragTag = "exercisesFragTag"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }
}
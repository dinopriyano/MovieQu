package com.dupat.newsqu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dupat.newsqu.R
import com.dupat.newsqu.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationBottom.background = null
    }
}
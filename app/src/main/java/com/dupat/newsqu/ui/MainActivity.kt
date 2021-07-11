package com.dupat.newsqu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dupat.newsqu.R
import com.dupat.newsqu.databinding.ActivityMainBinding
import com.dupat.newsqu.ui.fragment.NewsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationBottom.background = null

        navigationChange(NewsFragment())

        binding.navigationBottom.setOnItemSelectedListener  { item ->
            when(item.itemId){
                R.id.menuNews -> {
                    navigationChange(NewsFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun navigationChange(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}
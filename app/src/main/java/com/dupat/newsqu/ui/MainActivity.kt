package com.dupat.newsqu.ui

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.TextPaint
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.dupat.newsqu.R
import com.dupat.newsqu.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationBottom.background = null

        setToolbarTitleGradient()
        setupNavigation()
    }

    private fun setupNavigation() {
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        NavigationUI.setupWithNavController(binding.navigationBottom, navController)
        binding.navigationBottom.setOnItemReselectedListener {}
    }

    private fun hideNavBottom(isHide: Boolean) {
        if(isHide){
            binding.bottomAppBar.performHide()
        }
        else{
            binding.bottomAppBar.performShow()
        }
    }

    private fun setToolbarTitleGradient() {

        val toolbarTitle: TextView = binding.toolbar.getChildAt(0) as TextView

        val paint: TextPaint = toolbarTitle.paint
        val width: Float = paint.measureText(toolbarTitle.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, toolbarTitle.textSize, intArrayOf(
                ContextCompat.getColor(this, R.color.orange),
                ContextCompat.getColor(this, R.color.purple),
                ContextCompat.getColor(this, R.color.blue)
            ), null, Shader.TileMode.CLAMP
        )

        toolbarTitle.paint.shader = textShader

    }
}
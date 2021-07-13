package com.dupat.newsqu.ui

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.TextPaint
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        setToolbarTitleGradient()
        navigationChange(NewsFragment())

        binding.navigationBottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuNews -> {
                    navigationChange(NewsFragment())
                    true
                }

                else -> false
            }
        }

        binding.navigationBottom.setOnItemReselectedListener {
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


    private fun navigationChange(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}
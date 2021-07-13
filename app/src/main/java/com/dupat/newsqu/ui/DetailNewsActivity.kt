package com.dupat.newsqu.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dupat.newsqu.R
import com.dupat.newsqu.databinding.ActivityDetailNewsBinding
import com.dupat.newsqu.ui.model.Article
import com.dupat.newsqu.utils.statusBarHeight
import com.dupat.newsqu.utils.toLocalDate
import com.dupat.newsqu.utils.toTimeDetail

class DetailNewsActivity : AppCompatActivity() {

    companion object {
        val NEWS_EXTRA = "news_extra"
    }

    private var _binding: ActivityDetailNewsBinding? = null
    private val binding get() = _binding
    private lateinit var newsExtra: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()
        _binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSpaceView()

        newsExtra = intent.extras?.get(NEWS_EXTRA) as Article
        initData()

        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setSpaceView() {
        val space = this.statusBarHeight()
        binding?.spaceView?.layoutParams =
            ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, space)
    }

    private fun initData() {
        with(binding!!) {
            Glide.with(this@DetailNewsActivity)
                .load(newsExtra.urlToImage)
                .error(ContextCompat.getDrawable(this@DetailNewsActivity, R.drawable.shape_gray))
                .into(ivArticle)

            val contentArticle =
                if (newsExtra.content.isNullOrEmpty()) "" else newsExtra.content!!.split(" [+")[0]
            val authorArticle = if (newsExtra.author.isNullOrEmpty()) "Anonym" else newsExtra.author

            txtAuthor.text = authorArticle
            txtContent.text = contentArticle
            txtTitle.text = newsExtra.title
            txtCreatedTime.text = newsExtra.publishedAt.toLocalDate().toTimeDetail()
        }
    }

    private fun setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.dupat.newsqu.ui.paging.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dupat.newsqu.R
import com.dupat.newsqu.databinding.ItemNewsListBinding
import com.dupat.newsqu.ui.model.Article
import com.dupat.newsqu.utils.DiffUtils
import com.dupat.newsqu.utils.toLocalDate
import com.dupat.newsqu.utils.toTimeAgo

class  NewsPagingAdapter: PagingDataAdapter<Article, NewsPagingAdapter.ViewHolder>(DiffUtils) {

    var onItemClick : ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: NewsPagingAdapter.ViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsPagingAdapter.ViewHolder {
        val binding: ItemNewsListBinding = ItemNewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemNewsListBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        lateinit var article: Article

        fun bind(article: Article) {

            this.article = article

            with(binding){
                container.setOnClickListener(this@ViewHolder)

                Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .override(500, 400)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            txtNoimage.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            txtNoimage.visibility = View.GONE
                            return false
                        }

                    })
                    .error(ContextCompat.getDrawable(itemView.context, R.drawable.shape_gray))
                    .into(ivNews)
                txtAuthor.text = article.author?:"Anonym"
                txtTitle.text = article.title
                txtWriteTime.text = article.publishedAt.toLocalDate().toTimeAgo()
            }
        }

        override fun onClick(v: View?) {
            when(v){
                binding.container -> {
                    onItemClick?.invoke(article)
                }
            }
        }
    }

}
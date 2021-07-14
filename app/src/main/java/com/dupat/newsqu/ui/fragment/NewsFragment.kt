package com.dupat.newsqu.ui.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.dupat.newsqu.R
import com.dupat.newsqu.databinding.FragmentNewsBinding
import com.dupat.newsqu.ui.DetailNewsActivity
import com.dupat.newsqu.ui.adapter.FooterAdapter
import com.dupat.newsqu.ui.paging.adapter.NewsPagingAdapter
import com.dupat.newsqu.ui.viewmodel.NewsViewModel
import com.dupat.newsqu.utils.NewsClickEnum
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding
    private val viewModel: NewsViewModel by viewModels()
    private val newsAdapter = NewsPagingAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        loadData()

        newsAdapter.onItemClick = { article, ivArticle, newsClickEnum ->
            if(newsClickEnum == NewsClickEnum.DETAIL){
                val intent = Intent(requireContext(), DetailNewsActivity::class.java)
                intent.putExtra(DetailNewsActivity.NEWS_EXTRA, article)

                val pairImageAticle = Pair.create<View, String>(ivArticle, resources.getString(R.string.transition_image_article))
                val activityOptions = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), pairImageAticle)

                requireContext().startActivity(intent, activityOptions.toBundle())
            }
            else{
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi, i've read news at ${article?.url ?: "https://newsapi.org"}")
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
        }
    }

    private fun initAdapter() {
        with(binding!!){
            rvNews.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = newsAdapter.withLoadStateHeaderAndFooter(
                    header = FooterAdapter{newsAdapter.retry()},
                    footer = FooterAdapter{newsAdapter.retry()}
                )
            }
        }
    }

    @ExperimentalPagingApi
    private fun loadData() {

        lifecycleScope.launch {
            viewModel.getPopNews().collectLatest { pageData ->
                newsAdapter.submitData(pageData)
            }
        }

        lifecycleScope.launch {
            newsAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding?.rvNews?.scrollToPosition(0) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        binding?.rvNews?.adapter = null
    }

}
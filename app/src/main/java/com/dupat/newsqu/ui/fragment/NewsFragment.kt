package com.dupat.newsqu.ui.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.dupat.newsqu.R
import com.dupat.newsqu.databinding.FragmentNewsBinding
import com.dupat.newsqu.ui.DetailNewsActivity
import com.dupat.newsqu.ui.adapter.FooterAdapter
import com.dupat.newsqu.ui.adapter.itemdecoration.VerticalSpaceItemDecoration
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
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), Pair.create(ivArticle, resources.getString(R.string.transition_image_article)))
                val extras = ActivityNavigatorExtras(options)

                val bundle = Bundle()
                bundle.putParcelable("article", article)
                view.findNavController().navigate(R.id.detailNewsActivity,bundle,null, extras)
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
        val spaceHeightInPixel = resources.getDimensionPixelSize(R.dimen.vertical_space_margin)

        with(binding!!){
            rvNews.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(VerticalSpaceItemDecoration(spaceHeightInPixel))
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
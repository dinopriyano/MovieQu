package com.dupat.newsqu.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.dupat.newsqu.R
import com.dupat.newsqu.databinding.FragmentSearchNewsBinding
import com.dupat.newsqu.ui.adapter.FooterAdapter
import com.dupat.newsqu.ui.adapter.SearchViewAdapter
import com.dupat.newsqu.ui.adapter.itemdecoration.VerticalSpaceItemDecoration
import com.dupat.newsqu.ui.paging.adapter.NewsPagingAdapter
import com.dupat.newsqu.ui.viewmodel.NewsViewModel
import com.dupat.newsqu.utils.NewsClickEnum
import com.dupat.newsqu.utils.shareNews
import com.dupat.newsqu.utils.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding
    private val viewModel: NewsViewModel by viewModels()
    private val newsAdapter = NewsPagingAdapter()
    private lateinit var searchAdapter : SearchViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvNews?.isNestedScrollingEnabled = true
        initAdapter()
        initData()

        newsAdapter.onItemClick = { article, ivArticle, newsClickEnum ->
            if(newsClickEnum == NewsClickEnum.DETAIL){
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), Pair.create(ivArticle, resources.getString(R.string.transition_image_article)))
                val extras = ActivityNavigatorExtras(options)

                val bundle = Bundle()
                bundle.putParcelable("article", article)
                view.findNavController().navigate(R.id.detailNewsActivity,bundle,null, extras)
            }
            else{
                requireContext().shareNews(article?.url)
            }
        }

        searchAdapter.onSearch = { query ->
            viewModel.searchNews(query)
        }
    }

    @ExperimentalCoroutinesApi
    private fun initData() {
        viewModel.getSearchNews().observe(viewLifecycleOwner){
            newsAdapter.submitData(lifecycle, it)
        }

        lifecycleScope.launch {
            newsAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding?.rvNews?.scrollToPosition(0) }
        }
    }

    private fun initAdapter() {

        searchAdapter = SearchViewAdapter(listOf(1), lifecycleScope)
        newsAdapter.withLoadStateFooter(
            footer = FooterAdapter{newsAdapter.retry()}
        )

        val mergeAdapter = ConcatAdapter(searchAdapter, newsAdapter)

        val spaceHeightInPixel = resources.getDimensionPixelSize(R.dimen.vertical_space_margin)
        val linearLayoutManager = LinearLayoutManager(requireContext())

        binding?.rvNews!!.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addItemDecoration(VerticalSpaceItemDecoration(spaceHeightInPixel))
            adapter = mergeAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        binding?.rvNews?.adapter = null
    }

}
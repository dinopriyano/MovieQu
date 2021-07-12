package com.dupat.newsqu.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dupat.newsqu.R
import com.dupat.newsqu.databinding.FragmentNewsBinding
import com.dupat.newsqu.ui.paging.adapter.NewsPagingAdapter
import com.dupat.newsqu.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding
    private val viewModel: NewsViewModel by viewModels()
    private val newsAdapter = NewsPagingAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.getPopNews().collectLatest { pageData ->
                newsAdapter.submitData(pageData)
            }
        }

        newsAdapter.addLoadStateListener { state ->
            when(state.refresh){
                is LoadState.Loading -> {
                    Toast.makeText(requireContext(), "Loading data...", Toast.LENGTH_SHORT).show()
                }
                is LoadState.Error -> {
                    Toast.makeText(requireContext(), "Ups, some error...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initAdapter() {
        with(binding?.rvNews!!){
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = newsAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        binding?.rvNews?.adapter = null
    }

}
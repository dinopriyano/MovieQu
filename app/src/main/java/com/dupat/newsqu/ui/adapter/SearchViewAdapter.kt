package com.dupat.newsqu.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.dupat.newsqu.databinding.ItemSearchListBinding
import com.dupat.newsqu.utils.textChanges
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class SearchViewAdapter(private val intList: List<Int>, private val lifecycleCoroutineScope: LifecycleCoroutineScope): RecyclerView.Adapter<SearchViewAdapter.ViewHolder>() {

    var onSearch : ((String) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewAdapter.ViewHolder {
        val binding = ItemSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount() = intList.size

    @FlowPreview
    inner class ViewHolder(private val binding: ItemSearchListBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.etSearch.textChanges()
                .debounce(700)
                .filter { it?.length!! >= 3 }
                .map {
                    onSearch?.invoke(it.toString())
                }
                .launchIn(lifecycleCoroutineScope)
        }

    }
}
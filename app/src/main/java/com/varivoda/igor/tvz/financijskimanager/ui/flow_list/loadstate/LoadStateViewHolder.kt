package com.varivoda.igor.tvz.financijskimanager.ui.flow_list.loadstate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.databinding.LoadStateFooterItemBinding

class LoadStateViewHolder(
    private val binding: LoadStateFooterItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_footer_item, parent, false)
            val binding = LoadStateFooterItemBinding.bind(view)
            return LoadStateViewHolder(binding, retry)
        }
    }
}
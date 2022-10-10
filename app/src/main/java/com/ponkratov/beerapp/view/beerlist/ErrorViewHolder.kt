package com.ponkratov.beerapp.view.beerlist

import androidx.recyclerview.widget.RecyclerView
import com.ponkratov.beerapp.databinding.ItemErrorBinding

class ErrorViewHolder(
    binding: ItemErrorBinding,
    onRefreshClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.button.setOnClickListener {
            onRefreshClicked()
        }
    }
}
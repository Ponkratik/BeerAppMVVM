package com.ponkratov.beerapp.view.beerlist

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ponkratov.beerapp.databinding.ItemBeerBinding
import com.ponkratov.beerapp.model.entity.Beer

class BeerViewHolder(
    private val binding: ItemBeerBinding,
    private val onBeerClicked: (Beer) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Beer) {
        with(binding) {
            beerImage.load(item.imageUrl)
            beerName.text = item.name

            root.setOnClickListener { onBeerClicked(item) }
        }
    }
}
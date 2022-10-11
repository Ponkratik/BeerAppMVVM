package com.ponkratov.beerapp.view.beerlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ponkratov.beerapp.databinding.ItemBeerBinding
import com.ponkratov.beerapp.model.entity.Beer

class BeerAdapter(
    context: Context,
    private val onBeerClicked: (Beer) -> Unit,
) : ListAdapter<Beer, BeerViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        return BeerViewHolder(
            binding = ItemBeerBinding.inflate(layoutInflater, parent, false),
            onBeerClicked = onBeerClicked
        )
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Beer>() {
            override fun areItemsTheSame(oldItem: Beer, newItem: Beer): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Beer, newItem: Beer): Boolean {
                return oldItem == newItem
            }
        }
    }
}
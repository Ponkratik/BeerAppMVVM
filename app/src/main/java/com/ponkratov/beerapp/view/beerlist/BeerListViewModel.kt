package com.ponkratov.beerapp.view.beerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.beerapp.model.BeerApi
import com.ponkratov.beerapp.model.entity.Beer
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class BeerListViewModel(
    private val dataSource: BeerApi
) : ViewModel() {

    private var isLoading = false
    private var currentPage = 0

    private val loadItemsFlow = MutableSharedFlow<LoadItemsType>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val queryFlow = MutableStateFlow("")

    val dataFlow: Flow<List<Beer>> =
        queryFlow.combine(loadDataFlow()) { query, items ->
            items.filter { it.name.contains(query, ignoreCase = true) }
        }.shareIn(
            viewModelScope,
            SharingStarted.Eagerly,
            replay = 1
        )

    var currentBeer: Beer? = null

    fun onQueryChanged(query: String) {
        queryFlow.value = query
    }

    fun onRefreshed() {
        loadItemsFlow.tryEmit(LoadItemsType.REFRESH)
    }

    fun onLoadMore() {
        if (!isLoading) {
            loadItemsFlow.tryEmit(LoadItemsType.LOAD_MORE)
        }
    }

    private fun loadDataFlow(): Flow<List<Beer>> {
        return loadItemsFlow
            .onStart { emit(LoadItemsType.REFRESH) }
            .onEach { isLoading = true }
            .map { loadType ->
                when(loadType) {
                    LoadItemsType.REFRESH -> {
                        currentPage = 1
                    }
                    LoadItemsType.LOAD_MORE -> {
                        currentPage++
                    }
                }
                dataSource.getBeers(currentPage, 25)
            }
            .onEach { isLoading = false }
            .runningReduce { accumulator, value ->
                accumulator + value
            }
    }

    enum class LoadItemsType {
        REFRESH, LOAD_MORE
    }
}
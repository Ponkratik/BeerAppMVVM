package com.ponkratov.beerapp.view.beerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.beerapp.domain.db.BeerDao
import com.ponkratov.beerapp.model.entity.Beer
import com.ponkratov.beerapp.model.repository.BeerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BeerListViewModel(
    private val dataSource: BeerApi,
    private val dbSource: BeerDao
) : ViewModel() {

    private var isLoading = false
    private var currentPage = 0

    private val loadItemsFlow = MutableSharedFlow<LoadItemsType>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val queryFlow = MutableStateFlow("")

    private val dbFlow = MutableStateFlow(emptyList<Beer>())

    val dataFlow: Flow<List<Beer>> =
        queryFlow
            .combine(loadDataFlow()) { query, items ->
                items.filter { it.name.contains(query, ignoreCase = true) }
            }.shareIn(
                viewModelScope,
                SharingStarted.Eagerly,
                replay = 1
            )

    val dataFlow1: Flow<List<Beer>> =
        dbFlow
            .onStart { emit(dbSource.getAll()) }
            .map { queryFlow.value }
            .combine(loadDataFlow()) { query, items ->
                items.filter { it.name.contains(query, ignoreCase = true) }
            }.flowOn(Dispatchers.IO)
            .shareIn(
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
            .onStart {
                emit(LoadItemsType.REFRESH)
            }
            .onEach { isLoading = true }
            .onEach { loadType ->
                when (loadType) {
                    LoadItemsType.REFRESH -> {
                        currentPage = 1
                    }
                    LoadItemsType.LOAD_MORE -> {
                        currentPage++
                    }
                }
            }
            .map {
                dataSource.getBeers(currentPage, 25)
            }
            .onEach {
                isLoading = false
            }
            .runningReduce { accumulator, value ->
                accumulator + value
            }
            .onEach {
                viewModelScope.launch(Dispatchers.IO) {
                    dbSource.deleteAll()
                    dbSource.insertAll(it)
                }
            }
    }

    enum class LoadItemsType {
        REFRESH, LOAD_MORE
    }
}
package com.ponkratov.beerapp.view.beerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ponkratov.beerapp.R
import com.ponkratov.beerapp.databinding.FragmentBeerListBinding
import com.ponkratov.beerapp.model.servicelocator.ServiceLocator
import com.ponkratov.beerapp.view.extension.addPaginationListener
import com.ponkratov.beerapp.view.extension.addVerticalSpace
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class BeerListFragment : Fragment() {

    private var _binding: FragmentBeerListBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: BeerListViewModel by navGraphViewModels(R.id.navigation) {
        viewModelFactory {
            initializer {
                BeerListViewModel(
                    ServiceLocator.provideBeerApi(),
                    ServiceLocator.provideBeerDao()
                )
            }
        }
    }

    private val adapter by lazy {
        BeerAdapter(
            context = requireContext(),
            onBeerClicked = {
                viewModel.currentBeer = it
                findNavController().navigate(BeerListFragmentDirections.toBeerInfo())
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBeerListBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val linearLayoutManager = LinearLayoutManager(requireContext())

            beerRecyclerView.adapter = adapter
            beerRecyclerView.addVerticalSpace(R.dimen.list_vertical_space)

            beerRecyclerView.addPaginationListener(linearLayoutManager, ITEMS_TO_LOAD) {
                viewModel.onLoadMore()
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(query: String): Boolean {
                    viewModel.onQueryChanged(query)
                    return true
                }
            })

            layoutSwiperefresh.setOnRefreshListener {
                viewModel.onRefreshed()
            }

            viewModel
                .dataFlow1
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .onEach { layoutSwiperefresh.isRefreshing = false }
                .onEach(adapter::submitList)
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ITEMS_TO_LOAD = 25
    }
}
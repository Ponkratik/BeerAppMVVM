package com.ponkratov.beerapp.view.beerinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import coil.load
import com.ponkratov.beerapp.R
import com.ponkratov.beerapp.databinding.FragmentBeerInfoBinding
import com.ponkratov.beerapp.model.ServiceLocator
import com.ponkratov.beerapp.view.beerlist.BeerListViewModel

class BeerInfoFragment : Fragment() {
    private var _binding: FragmentBeerInfoBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: BeerListViewModel by navGraphViewModels(R.id.navigation) {
        viewModelFactory {
            initializer {
                BeerListViewModel(ServiceLocator.provideBeerApi())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBeerInfoBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            with(viewModel) {
                image.load(currentBeer?.imageUrl)
                textViewName.text = currentBeer?.name
                textViewTagline.text = currentBeer?.tagline
                textViewBrewersTips.text = currentBeer?.brewersTips
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.mahdavi.newsapp.ui.favorite.headline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentFavouriteHeadlineBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.ui.favorite.FavoriteFragmentDirections
import com.mahdavi.newsapp.ui.favorite.FavoriteViewModel
import com.mahdavi.newsapp.ui.favorite.adapter.FavoriteAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class FavouriteHeadlineFragment : BaseFragment() {

    private var _binding: FragmentFavouriteHeadlineBinding? = null
    private val binding get() = _binding!!
    private var position = 1
    private val viewModel: FavoriteViewModel by hiltNavGraphViewModels(R.id.tabs_graph)

    companion object {
        private const val ARG_POSITION = "ARG_POSITION_FAVOURITE"
        fun getInstance(position: Int) = FavouriteHeadlineFragment().apply {
            arguments = bundleOf(ARG_POSITION to position)
        }
    }

    override fun setupUi() {
        bind()
    }

    override fun setupCollectors() {
        viewModel.favoriteUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { favoriteUiState ->
                if (position==0) {
                    favoriteUiState.headlines?.let {
                        val adapter = FavoriteAdapter() { headlineArticle ->
                            findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToDetailsGraph(headline = headlineArticle))
                        }
                        binding.recycleView.adapter = adapter
                        adapter.submitList(it)
                    }
                }
                if (position==1) {
                    favoriteUiState.favouriteHeadLines?.let {
                        val adapter = FavoriteAdapter(){}
                        binding.recycleView.adapter = adapter
                        adapter.submitList(it)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun setupListeners() {
        /*NO_OP*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteHeadlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun bind() {
        position = requireArguments().getInt(ARG_POSITION)
        when (position) {
            0 -> {
                viewModel.loadHeadlines()
            }
            1 -> {
                viewModel.loadFavouriteHeadlines()
            }
        }
    }

}
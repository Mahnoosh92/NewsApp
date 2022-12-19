package com.mahdavi.newsapp.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentSearchBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.utils.extensions.getQueryTextChangeStateFlow
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : BaseFragment(), MenuProvider {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var menu: Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        activity?.addMenuProvider(this, viewLifecycleOwner)
    }

    override fun setupCollectors() {
        viewModel.articles.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { searchUiState->
                if(searchUiState.isLoading == false) {
                    with(binding) {
                        recycleView.isVisible = true
                        shimmerFrameLayout.visibility = View.GONE

                        searchUiState.data?.let{
                            val adapter = SearchAdapter()
                            recycleView.adapter = adapter
                            adapter.submitList(it)
                        }

                    }
                    searchUiState.error?.let {
                        binding.root.shortSnackBar(it)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun setupListeners() {

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.top_menu, menu)
        this.menu = menu
        val item = menu.findItem(R.id.search)
        item.isVisible = true
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.search) {
            val searchView: SearchView = menuItem.actionView as SearchView
           viewModel.getQuery(searchView.getQueryTextChangeStateFlow())
        }
        return true
    }
    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmerAnimation()
        super.onPause()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
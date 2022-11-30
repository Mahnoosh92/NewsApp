package com.mahdavi.newsapp.ui.home


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.facebook.shimmer.ShimmerFrameLayout
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentHomeBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.utils.extensions.getQueryTextChangeStateFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment(), MenuProvider {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private lateinit var menu: Menu

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articles.collect { homeUiState ->
                    //TODO:Loading
                    homeUiState.data?.let {
                        val adapter = HomeNewsAdapter() {
                            val action = HomeFragmentDirections.actionHomeToDetailsFragment(it)
                            findNavController().navigate(action)
                        }
                        binding.recycleView.adapter = adapter
                        adapter.submitList(it)
                    }
                }
            }
        }
        shimmerFrameLayout = binding.shimmerViewContainer
        activity?.addMenuProvider(this, viewLifecycleOwner)
    }

    override fun setupCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {

            }
        }
    }

    override fun setupListeners() {
        viewModel.getQuery(binding.textInput.getQueryTextChangeStateFlow())
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        super.onPause()
        shimmerFrameLayout.stopShimmerAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.top_menu, menu)
        this.menu = menu
        val item = menu.findItem(R.id.search_cow)
        item.isVisible = true
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }
}
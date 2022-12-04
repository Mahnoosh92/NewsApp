package com.mahdavi.newsapp.ui.home


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.model.remote.ArticleResponse
import com.mahdavi.newsapp.databinding.FragmentHomeBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.utils.extensions.action
import com.mahdavi.newsapp.utils.extensions.getQueryTextChangeStateFlow
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Objects


@AndroidEntryPoint
class HomeFragment : BaseFragment(), MenuProvider {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var menu: Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articles.collectLatest { homeUiState ->
                    //TODO:Loading
                    if (homeUiState.loading) {
                        binding.recycleView.visibility = View.GONE
                        binding.shimmerFrameLayout.visibility = View.VISIBLE
                    }
                    homeUiState.data?.let {
                        /*val adapter = HomeNewsAdapter {
                            val action = HomeFragmentDirections.actionHomeToDetailsFragment(it)
                            findNavController().navigate(action)
                        }
                        binding.recycleView.adapter = adapter
                        adapter.submitList(it)*/

                        val callback = object : OnClickListener {
                            override fun onClick(articleResponse: ArticleResponse) {
                                val action = HomeFragmentDirections.actionHomeToDetailsFragment(
                                    articleResponse
                                )
                                findNavController().navigate(action)
                            }
                        }

                        val adapter = HomeNewsCallbackAdapter(callback)
                        binding.recycleView.adapter = adapter
                        adapter.submitList(it)

                        binding.recycleView.visibility = View.VISIBLE
                        binding.shimmerFrameLayout.visibility = View.GONE
                    }
                    homeUiState.error?.let {
                        binding.root.shortSnackBar(it) {
                            action("Ok") {
                                this.dismiss()
                            }
                        }
                    }
                }
            }
        }

        activity?.addMenuProvider(this, viewLifecycleOwner)
    }

    override fun setupCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {

            }
        }
    }

    override fun setupListeners() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    /*override fun onClick(articleResponse: ArticleResponse) {
        val action = HomeFragmentDirections.actionHomeToDetailsFragment(articleResponse)
        findNavController().navigate(action)
    }*/
}
package com.mahdavi.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.model.local.NetworkResult
import com.mahdavi.newsapp.data.repository.NewsRepositoryImpl
import com.mahdavi.newsapp.databinding.FragmentHomeBinding
import com.mahdavi.newsapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

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
                viewModel.articles.collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val x = result.data
                            //TODO: add recycler view
                        }
                        is NetworkResult.Error -> {
                            Snackbar.make(
                                requireView(),
                                result.message ?: getString(R.string.error_general),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is NetworkResult.Loading -> {
                            //TODO: add loader
                        }
                    }
                }
            }
        }
    }

    override fun setupObservers() {

    }

    override fun setupListeners() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
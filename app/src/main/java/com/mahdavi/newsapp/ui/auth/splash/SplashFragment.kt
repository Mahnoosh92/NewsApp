package com.mahdavi.newsapp.ui.auth.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentSplashBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.utils.InternalDeepLinkHandler
import com.mahdavi.newsapp.utils.extensions.changeNavHostGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    val binding get() = _binding!!

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        viewModel.checkUserStatus()
    }

    override fun setupCollectors() {
        viewModel.splashUiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { splashUiState ->
                splashUiState.isCurrentUserSignedIn?.let {
                    if (splashUiState.isCurrentUserSignedIn) {
                        navigateToHome()
                    } else {
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun setupListeners() {

    }

    private fun navigateToHome() {
        activity?.changeNavHostGraph(R.navigation.tabs_graph, R.id.headlineFragment)
        findNavController().navigate(InternalDeepLinkHandler.TABS.toUri())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
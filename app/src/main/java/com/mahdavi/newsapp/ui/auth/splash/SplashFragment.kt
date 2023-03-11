package com.mahdavi.newsapp.ui.auth.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentSplashBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.utils.InternalDeepLinkHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    val binding get() = _binding!!

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        viewModel.checkUserAndOnBoardingStatus()
    }

    override fun setupCollectors() {
        viewModel.splashUiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { splashUiState ->
                splashUiState.isOnBoardingRequired?.let {
                    if (it) {
                        splashUiState.isCurrentUserSignedIn?.let {
                            if (splashUiState.isCurrentUserSignedIn) {
                                viewModel.signOut()
                            }
                        }
                        lifecycleScope.launch {
                            delay(300L)
                            viewModel.consumeIsOnBoardingRequired()
                            navigateToOnBoarding()
                        }
                    } else {
                        splashUiState.isCurrentUserSignedIn?.let {
                            if (splashUiState.isCurrentUserSignedIn) {
                                Handler(Looper.getMainLooper()).postDelayed(
                                    { navigateToHome() }, 300L
                                )

                            } else {
                                lifecycleScope.launch {
                                    delay(300L)
                                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                                }
                            }
                        }
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun setupListeners() {
        /*NO_OP*/
    }

    private fun navigateToHome() {
        //activity?.changeNavHostGraph(R.navigation.tabs_graph, R.id.headlineFragment)
        findNavController().popBackStack(R.id.splashFragment, true)
        findNavController().navigate(InternalDeepLinkHandler.TABS.toUri())
    }

    private fun navigateToOnBoarding() {
        //activity?.changeNavHostGraph(R.navigation.onboarding_graph, R.id.onBoardingFragment)
        findNavController().popBackStack(R.id.splashFragment, true)
        findNavController().navigate(InternalDeepLinkHandler.ONBOARDING.toUri())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
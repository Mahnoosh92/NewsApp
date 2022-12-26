package com.mahdavi.newsapp.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentOnBoardingBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.ui.onboarding.adapter.OnBoardingFragmentStateAdapter
import com.mahdavi.newsapp.utils.InternalDeepLinkHandler
import com.mahdavi.newsapp.utils.extensions.changeNavHostGraph
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


private const val NUM_PAGES = 3

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnBoardingViewModel by hiltNavGraphViewModels(R.id.onboarding_graph)

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        binding.root.shortSnackBar(
            exception.message ?: requireContext().resources.getString(R.string.error_general)
        )
    }

    private var onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updateCircleMarker(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        val onBoardingAdapter = OnBoardingFragmentStateAdapter(this, NUM_PAGES)
        binding.vp2Pager.apply {
            adapter = onBoardingAdapter
            registerOnPageChangeCallback(onBoardingPageChangeCallback)
        }
    }

    override fun setupCollectors() {
        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler) {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.onBoardingUiState.collect { onBoardingUiState: OnBoardingUiState ->
                    onBoardingUiState.isConsumed?.let {
                        if (it) {
                            navigateToLogin()
                        }
                    }
                    onBoardingUiState.error?.let {
                        binding.root.shortSnackBar(it)
                    }
                }
            }
        }
    }

    override fun setupListeners() {
        binding.apply {
            skip.setOnClickListener {
                viewModel.consumeOnBoarding()
            }
        }
    }


    private fun updateCircleMarker(position: Int) {
        when (position) {
            0 -> {
                binding.ivFirstCircle.setColorFilter(R.color.red_500)
                binding.ivSecondCircle.setColorFilter(R.color.black)
                binding.ivThirdCircle.setColorFilter(R.color.black)
            }
            1 -> {
                binding.ivFirstCircle.setColorFilter(R.color.black)
                binding.ivSecondCircle.setColorFilter(R.color.red_500)
                binding.ivThirdCircle.setColorFilter(R.color.black)
            }
            2 -> {
                binding.ivFirstCircle.setColorFilter(R.color.black)
                binding.ivSecondCircle.setColorFilter(R.color.black)
                binding.ivThirdCircle.setColorFilter(R.color.red_500)
            }
        }
    }

    private fun navigateToLogin() {
        activity?.changeNavHostGraph(R.navigation.auth_graph, R.id.loginFragment)
        findNavController().navigate(InternalDeepLinkHandler.LOGIN.toUri())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vp2Pager.unregisterOnPageChangeCallback(onBoardingPageChangeCallback)
        _binding = null
    }
}
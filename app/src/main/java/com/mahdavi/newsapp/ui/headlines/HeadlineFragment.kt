package com.mahdavi.newsapp.ui.headlines


import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.mahdavi.newsapp.databinding.FragmentHeadlineBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.utils.extensions.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HeadlineFragment : BaseFragment(), CallBackOnTitle {

    private var _binding: FragmentHeadlineBinding? = null
    private val binding get() = _binding!!


    private val viewModel: HeadlineViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        viewModel.getHeadlinesForFirstTitle()
    }
    override fun setupCollectors() {
        viewModel.articles
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { headlineUiState ->
                headlineUiState.titles?.let { listTitles ->
                    with(binding) {
                        val adapterTitles = HeadlineTitleAdapter()
                        adapterTitles.registerCallback(this@HeadlineFragment)
                        recycleViewTitles.adapter = adapterTitles
                        adapterTitles.submitList(listTitles)
                    }
                }
                headlineUiState.data?.let { listHeadlines ->
                    binding.apply {
                        val adapterHeadline = HeadlineAdapter()
                        recycleViewHeadlines.adapter = adapterHeadline
                        adapterHeadline.submitList(listHeadlines)
                        delay(300L)
                        binding.shimmerFrameLayout.isVisible = false
                        binding.recycleViewHeadlines.isVisible = true
                    }
                }
                headlineUiState.error?.let {
                    binding.root.shortSnackBar(it)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }
    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmerAnimation()
        super.onPause()
    }
    override fun setupListeners() {
        /* NO-OP */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun titleClicked(topic: String) {
        binding.shimmerFrameLayout.isVisible = true
        binding.recycleViewHeadlines.isVisible = false
        viewModel.updateListTitles(topic)
        viewModel.getLatestHeadLines(topic)
    }

}
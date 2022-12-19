package com.mahdavi.newsapp.ui.headlines


import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentHeadlineBinding
import com.mahdavi.newsapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HeadlineFragment : BaseFragment() {

    private var _binding: FragmentHeadlineBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HeadlineViewModel by viewModels()

    private lateinit var menu: Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        viewModel.updateTitles()
    }

    override fun setupCollectors() {
        viewModel.articles.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { headlineUiState ->
                headlineUiState.titles?.let { listTitles ->
                    with(binding) {
                        val adapterTitles = HeadlineTitleAdapter() { headlineTitle ->
                            viewModel.updateListTitles(headlineTitle)
                        }
                        recycleViewTitles.adapter = adapterTitles
                        adapterTitles.submitList(listTitles)
                    }
                }
                headlineUiState.data?.let { listHeadlines ->
                    binding.apply {
                        val adapterHeadline = HeadlineAdapter()
                        recycleViewHeadlines.adapter = adapterHeadline
                        adapterHeadline.submitList(listHeadlines)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }

    override fun setupListeners() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /*override fun onClick(articleResponse: ArticleResponse) {
        val action = HomeFragmentDirections.actionHomeToDetailsFragment(articleResponse)
        findNavController().navigate(action)
    }*/
}
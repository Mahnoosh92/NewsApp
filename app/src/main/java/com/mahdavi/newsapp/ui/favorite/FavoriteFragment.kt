package com.mahdavi.newsapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.data.dataSource.local.pref.SharedPreferenceDataSource
import com.mahdavi.newsapp.databinding.FragmentFavoriteBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.ui.favorite.adapter.FavouritePageAdapter
import com.mahdavi.newsapp.ui.profile.ProfileViewModel
import com.mahdavi.newsapp.utils.InternalDeepLinkHandler
import com.mahdavi.newsapp.utils.extensions.changeNavHostGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseFragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by hiltNavGraphViewModels(R.id.tabs_graph)

    private val tabs = listOf(R.string.headlines_favourite_fragment, R.string.favourite_favourite_fragment)

    @Inject
    lateinit var sharedPreferenceDataSource: SharedPreferenceDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        binding.viewPager2.adapter = FavouritePageAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = requireContext().resources.getString(tabs[position])
        }.attach()
    }

    override fun setupCollectors() {

    }

    override fun setupListeners() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToLogin() {
        activity?.changeNavHostGraph(
            idNavGraph = R.navigation.auth_graph, destination = R.id.loginFragment
        )
        findNavController().navigate(InternalDeepLinkHandler.LOGIN.toUri())
    }
}
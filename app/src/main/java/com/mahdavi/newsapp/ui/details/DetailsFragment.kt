package com.mahdavi.newsapp.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentDetailsBinding
import com.mahdavi.newsapp.ui.BaseFragment
import com.mahdavi.newsapp.ui.favorite.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by hiltNavGraphViewModels(R.id.tabs_graph)

    private val args: DetailsFragmentArgs by navArgs()

    override fun setupUi() {
        with(binding) {
            imageDetails.load(args.headline.media)
            titleDetails.text = args.headline.title
        }
    }

    override fun setupCollectors() {
        /*NO_OP*/
    }

    override fun setupListeners() {
        binding.favouriteDetails.setOnClickListener {
            viewModel.addToFavouriteHeadlines(args.headline)
            binding.favouriteDetails.setColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.queen
                ), android.graphics.PorterDuff.Mode.SRC_IN
            );
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

}
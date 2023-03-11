package com.mahdavi.newsapp.ui.onboarding.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import coil.load
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.FragmentOnBoardingChildBinding
import com.mahdavi.newsapp.ui.BaseFragment

class OnBoardingChildFragment : BaseFragment() {

    private var _binding: FragmentOnBoardingChildBinding? = null
    private val binding get() = _binding!!

    private var position = 0

    companion object {
        private const val ARG_POSITION = "ARG_POSITION"

        fun getInstance(position: Int) = OnBoardingChildFragment().apply {
            arguments = bundleOf(ARG_POSITION to position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUi() {
        val position = requireArguments().getInt(ARG_POSITION)
        val onBoardingTitles = requireContext().resources.getStringArray(R.array.on_boarding_titles)
        val onBoardingTexts = requireContext().resources.getStringArray(R.array.on_boarding_texts)
        val onBoardingImages = getOnBoardAssetsLocation()

        with(binding) {
            onBoardingLottie.setAnimation(onBoardingImages[position])
            (onBoardingImages[position])
            onBoardingTextTitle.text = onBoardingTitles[position]
            onBoardingTextMsg.text = onBoardingTexts[position]
        }
    }

    override fun setupCollectors() {
        /* NO-OP */
    }

    override fun setupListeners() {
        /* NO-OP */
    }

    private fun getOnBoardAssetsLocation(): List<Int> {
        val onBoardAssets: MutableList<Int> = ArrayList()
        onBoardAssets.add(R.raw.news1)
        onBoardAssets.add(R.raw.news2)
        onBoardAssets.add(R.raw.news3)
        return onBoardAssets
    }
}
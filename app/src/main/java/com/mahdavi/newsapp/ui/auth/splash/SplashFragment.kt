package com.mahdavi.newsapp.ui.auth.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.utils.InternalDeepLinkHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewLifecycleOwner.lifecycleScope.launch{

            delay(3000)

            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


}
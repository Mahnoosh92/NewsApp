package com.mahdavi.newsapp.data.repository.onboarding

import com.mahdavi.newsapp.data.dataSource.local.datastore.onboarding.OnBoardingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor(private val onBoardingDataSource: OnBoardingDataSource) :
    OnBoardingRepository {
    override fun needToShowOnBoarding() = onBoardingDataSource.needToShowOnBoarding()

    override fun onBoardingConsumed() = onBoardingDataSource.onBoardingConsumed()
}
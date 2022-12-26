package com.mahdavi.newsapp.data.repository.onboarding

import kotlinx.coroutines.flow.Flow

interface OnBoardingRepository {
    fun needToShowOnBoarding(): Flow<Boolean>
    fun onBoardingConsumed(): Flow<Unit>
}
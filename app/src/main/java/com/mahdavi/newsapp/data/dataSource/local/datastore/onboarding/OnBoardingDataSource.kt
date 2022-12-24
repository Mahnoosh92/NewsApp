package com.mahdavi.newsapp.data.dataSource.local.datastore.onboarding

import kotlinx.coroutines.flow.Flow

interface OnBoardingDataSource {
    fun needToShowOnBoarding(): Flow<Boolean>
    fun onBoardingConsumed(): Flow<Unit>
}
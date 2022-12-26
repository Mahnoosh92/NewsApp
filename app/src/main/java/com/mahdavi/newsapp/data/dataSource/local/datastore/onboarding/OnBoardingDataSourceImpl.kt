package com.mahdavi.newsapp.data.dataSource.local.datastore.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val ON_BOARDING_STATUS = booleanPreferencesKey("on_boarding_status")

class OnBoardingDataSourceImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    OnBoardingDataSource {


    override fun needToShowOnBoarding() = dataStore.data.map { preferences ->
        preferences[ON_BOARDING_STATUS]?.not() ?: true
    }

    override fun onBoardingConsumed(): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences[ON_BOARDING_STATUS] = true
        }
        emit(Unit)
    }
}
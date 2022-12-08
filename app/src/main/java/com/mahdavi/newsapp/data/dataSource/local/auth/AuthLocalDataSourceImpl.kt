package com.mahdavi.newsapp.data.dataSource.local.auth


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mahdavi.newsapp.data.model.local.auth.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(private val authDataStore: DataStore<Preferences>) :
    AuthLocalDataSource {
    override suspend fun loginUser(
        usernameKey: String,
        usernameValue: String,
        passwordKey: String,
        passwordValue: String
    ) = flow {
        authDataStore.edit { preferences ->
            preferences[stringPreferencesKey(usernameKey)] = usernameValue
            preferences[stringPreferencesKey(passwordKey)] = passwordValue
        }
        emit(Unit)
    }

    override fun getUser(usernameKey: String, passwordKey: String) =
        authDataStore.data
            .map { preferences ->
                // No type safety.
                val username = preferences[stringPreferencesKey(usernameKey)] ?: ""
                val password = preferences[stringPreferencesKey(passwordKey)] ?: ""
                User(username, password)
            }


}
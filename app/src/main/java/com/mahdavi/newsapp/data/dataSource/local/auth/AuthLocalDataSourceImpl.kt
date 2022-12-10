package com.mahdavi.newsapp.data.dataSource.local.auth


import android.service.autofill.UserData
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

private const val USERNAME = "username"
private const val PASSWORD = "password"

class AuthLocalDataSourceImpl @Inject constructor(private val authDataStore: DataStore<Preferences>) :
    AuthLocalDataSource {
    override suspend fun loginUser(
        usernameValue: String,
        passwordValue: String
    ) = flow {
        authDataStore.edit { preferences ->
            preferences[stringPreferencesKey(USERNAME)] = usernameValue
            preferences[stringPreferencesKey(PASSWORD)] = passwordValue
        }
        emit(Unit)
    }

    override fun getUser() =
        authDataStore.data
            .map { preferences ->
                // No type safety.
                var user: User? = null
                val username = preferences[stringPreferencesKey(USERNAME)]
                val password = preferences[stringPreferencesKey(PASSWORD)]
                if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    user = User(username, password)
                }
                user
            }


}

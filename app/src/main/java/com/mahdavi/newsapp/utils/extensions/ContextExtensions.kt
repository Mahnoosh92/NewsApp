package com.mahdavi.newsapp.utils.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.myDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
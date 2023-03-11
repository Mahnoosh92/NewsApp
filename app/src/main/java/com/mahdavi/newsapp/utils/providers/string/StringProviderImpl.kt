package com.mahdavi.newsapp.utils.providers.string

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringProviderImpl @Inject constructor(@ApplicationContext private val context: Context):StringProvider {
    override fun getString(key: Int) = context.getString(key)
}
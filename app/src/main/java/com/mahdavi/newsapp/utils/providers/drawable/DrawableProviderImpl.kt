package com.mahdavi.newsapp.utils.providers.drawable

import android.content.Context
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DrawableProviderImpl @Inject constructor(@ApplicationContext private val context: Context):DrawableProvider {
    override fun getDrawable(key: Int) = context.getDrawable(key)
}
package com.mahdavi.newsapp.utils.providers.drawable

import android.graphics.drawable.Drawable

interface DrawableProvider {
    fun getDrawable(key: Int): Drawable?
}
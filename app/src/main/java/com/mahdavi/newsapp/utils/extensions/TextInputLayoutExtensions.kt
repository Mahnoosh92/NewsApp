package com.mahdavi.newsapp.utils.extensions

import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setStrockColor(color: Int) {
    this.boxStrokeColor = ContextCompat.getColor(context, color)
}
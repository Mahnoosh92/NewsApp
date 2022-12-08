package com.mahdavi.newsapp.utils.extensions

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun TextInputEditText.getQueryTextChangeStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")
    //anonymous inner class
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            /* No-Op */
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0.isNullOrEmpty().not()) {
                query.value = p0.toString()
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            /* No-Op */
        }
    })
    return query
}

fun TextInputEditText.getQueryTextStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")
    //anonymous inner class
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            /* No-Op */
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            /* No-Op */
        }

        override fun afterTextChanged(p0: Editable?) {
            p0?.let {
                query.value = p0.toString()
            }
        }
    })
    return query
}
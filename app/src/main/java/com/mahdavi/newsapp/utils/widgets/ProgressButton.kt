package com.mahdavi.newsapp.utils.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.ProgressButtonBinding
import timber.log.Timber

class ProgressButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ProgressButtonBinding.inflate(LayoutInflater.from(context), this)
    private var callback: ProgressButtonCallback? = null
    // todo user lambda expression

    init {
        loadAttributes(attrs, defStyleAttr)
        setupUiListener()
    }

    private fun setupUiListener() {
        binding.button.setOnClickListener {
            callback?.onClick()
        }
    }

    private fun loadAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        var typedArray: TypedArray? = null
        try {
            typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.ProgressButton, defStyleAttr, 0
            )

            binding.apply {
                buttonText.text = typedArray.getString(R.styleable.ProgressButton_pb_text)
                progressCircular.isVisible =
                    typedArray.getBoolean(R.styleable.ProgressButton_pb_loading, false)
                button.isEnabled = typedArray.getBoolean(R.styleable.ProgressButton_pb_enabled, true)
            }
        } catch (exp: Exception) {
            Timber.e(exp)
        } finally {
            typedArray?.recycle()
        }
    }

    fun setLoading(loading: Boolean) {
        isClickable = !loading //Disable clickable when loading
        with(binding) {
            if (loading) {
                buttonText.isVisible = false
                progressCircular.isVisible = true
            } else {
                buttonText.isVisible = true
                progressCircular.isVisible = false
            }
        }
    }

    fun setText(text: String?) {
        binding.buttonText.text = text
    }

    fun isEnabled(value: Boolean) {
        binding.button.isEnabled = value
    }

    fun onClick(listener: ProgressButtonCallback) {
        callback = listener
    }
}

interface ProgressButtonCallback {
    fun onClick()
}
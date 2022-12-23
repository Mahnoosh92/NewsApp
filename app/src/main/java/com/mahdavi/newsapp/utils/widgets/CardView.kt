package com.mahdavi.newsapp.utils.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.load
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.databinding.CardViewBinding
import timber.log.Timber

class CardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CardViewBinding.inflate(LayoutInflater.from(context), this)
    private lateinit var onClickFunction: () -> Unit

    init {
        loadAttributes(attrs, defStyleAttr)
        setupUiListener()
        elevation = 10.dp
        background = ContextCompat.getDrawable(context, R.drawable.bg_card_view)
    }

    private fun loadAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        var typedArray: TypedArray? = null
        try {
            typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.CardView, defStyleAttr, 0
            )
            binding.apply {
                cardViewTitle.text = typedArray.getString(R.styleable.CardView_cv_text)
                cardViewIcon.setImageResource(
                    typedArray.getResourceId(
                        R.styleable.CardView_cv_icon, -1
                    )
                )
            }
        } catch (exp: Exception) {
            Timber.e(exp)
        } finally {
            typedArray?.recycle()
        }
    }

    private fun setupUiListener() {
        binding.cardView.setOnClickListener {
            onClickFunction()
        }
    }

    fun onClick(onClickFunction: () -> Unit) {
        this.onClickFunction = onClickFunction
    }

    private val Int.dp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics
        )
}

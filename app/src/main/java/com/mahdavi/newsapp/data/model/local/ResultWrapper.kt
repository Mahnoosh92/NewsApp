package com.mahdavi.newsapp.data.model.local

/**
 * Result Wrapper <Left = Exception, Right = Value/Success>
 */
sealed class ResultWrapper<out E, out V> {
    data class Value<out V>(val value: V) : ResultWrapper<Nothing, V>()
    data class Error<out E>(val error: E) : ResultWrapper<E, Nothing>()
    companion object {
        inline fun <V> build(function: () -> V): ResultWrapper<Exception, V> =
            try {
                Value(function.invoke())
            } catch (e: java.lang.Exception) {
                Error(e)
            }
    }
}
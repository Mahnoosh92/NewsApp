package com.mahdavi.newsapp.data.dataSource.local.pref

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SharedPreferenceHelperImpl @Inject constructor(
    @ApplicationContext val context: Context
) : SharedPreferenceDataSource {

    private var sharesPreferences = context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

    override fun save(key: String, value: String) {
        sharesPreferences.edit {
            it.putString(key, value)
        }
    }

    override fun save(key: String, value: Int) {
        sharesPreferences.edit {
            it.putInt(key, value)
        }
    }

    override fun save(key: String, value: Boolean) {
        sharesPreferences.edit {
            it.putBoolean(key, value)
        }
    }

    override fun getValue(key: String, defValue: String): String? {
        return sharesPreferences.getString(key, defValue)
    }

    override fun getValue(key: String, defValue: Int): Int? {
        return sharesPreferences.getInt(key, defValue)
    }

    override fun getValue(key: String, defValue: Boolean): Boolean? {
        return sharesPreferences.getBoolean(key, defValue)
    }

    override fun delete(key: String) {
        sharesPreferences.edit().remove(key).apply()
    }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}
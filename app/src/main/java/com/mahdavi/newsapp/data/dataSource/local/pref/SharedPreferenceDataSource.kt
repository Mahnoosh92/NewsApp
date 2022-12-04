package com.mahdavi.newsapp.data.dataSource.local.pref

interface SharedPreferenceDataSource {
    fun save(key: String, value: String)
    fun save(key: String, value: Int)
    fun save(key: String, value: Boolean)
    fun getValue(key: String, defValue: String): String?
    fun getValue(key: String, defValue: Int): Int?
    fun getValue(key: String, defValue: Boolean): Boolean?
    fun delete(key: String)
}
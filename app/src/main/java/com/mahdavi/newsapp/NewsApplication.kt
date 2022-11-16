package com.mahdavi.newsapp

import android.app.Application
import com.mahdavi.newsapp.data.db.AppDataBase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApplication : Application() {
    val database: AppDataBase by lazy { AppDataBase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
    }

    init {
        instance = this
    }

    companion object {
        private var instance: NewsApplication? = null


        fun application(): NewsApplication {
            return instance!!
        }
    }
}
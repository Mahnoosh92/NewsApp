package com.mahdavi.newsapp

import android.app.Application
import com.mahdavi.newsapp.data.db.AppDataBase

class NewsApplication : Application() {
    val database: AppDataBase by lazy { AppDataBase.getDatabase(this) }



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
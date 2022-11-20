package com.mahdavi.newsapp

import android.app.Application
import com.mahdavi.newsapp.data.db.AppDataBase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApplication : Application()
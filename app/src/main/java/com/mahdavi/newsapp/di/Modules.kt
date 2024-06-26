package com.mahdavi.newsapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.mahdavi.newsapp.BuildConfig
import com.mahdavi.newsapp.data.api.ApiService
import com.mahdavi.newsapp.data.dataSource.local.headline.HeadlineLocalDataSource
import com.mahdavi.newsapp.data.dataSource.local.headline.HeadlineLocalDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.local.news.NewsLocalDataSource
import com.mahdavi.newsapp.data.dataSource.local.news.NewsLocalDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.local.pref.SharedPreferenceDataSource
import com.mahdavi.newsapp.data.dataSource.local.pref.SharedPreferenceHelperImpl
import com.mahdavi.newsapp.data.dataSource.remote.news.headline.HeadlineDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.headline.HeadlineDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.remote.user.UserDataSource
import com.mahdavi.newsapp.data.dataSource.remote.user.UserDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.local.search.SearchLocalDataSource
import com.mahdavi.newsapp.data.dataSource.local.search.SearchLocalDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.remote.news.NewsRemoteDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.NewsRemoteDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.remote.news.search.SearchedArticleDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.search.SearchedArticleDataSourceImpl
import com.mahdavi.newsapp.data.db.AppDataBase
import com.mahdavi.newsapp.data.repository.news.NewsRepository
import com.mahdavi.newsapp.data.repository.news.NewsRepositoryImpl
import com.mahdavi.newsapp.data.repository.news.headline.HeadlineRepository
import com.mahdavi.newsapp.data.repository.news.headline.HeadlineRepositoryImpl
import com.mahdavi.newsapp.data.repository.news.search.SearchRepository
import com.mahdavi.newsapp.data.repository.news.search.SearchRepositoryImpl
import com.mahdavi.newsapp.data.repository.user.UserRepository
import com.mahdavi.newsapp.data.repository.user.UserRepositoryImpl
import com.mahdavi.newsapp.utils.validate.Validate
import com.mahdavi.newsapp.utils.validate.ValidateImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true).followSslRedirects(true).addInterceptor { chain ->
                val newRequest =
                    chain.request().newBuilder().addHeader("x-api-key", BuildConfig.API_KEY).build()
                chain.proceed(newRequest)
            }
        return if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor).build()
        } else {
            builder.build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL).client(okHttpClient).build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext, AppDataBase::class.java, "item_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideHeadlineArticleDao(db: AppDataBase) = db.headlineArticleDao()

    @Provides
    @Singleton
    fun provideSearchedArticleDao(db: AppDataBase) = db.searchedArticleDao()
//    @Provides
//    @Singleton
//    fun provideMyDataStorePreferences(
//        @ApplicationContext context: Context
//    ): DataStore<Preferences> = context.myDataStore

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }),
            migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) })
    }
}

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesDispatchersModule {

    @DefaultDispatcher
    @Provides
    @Singleton
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    @Singleton
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    @Singleton
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @MainImmediateDispatcher
    @Provides
    @Singleton
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}

//////////////////////////////////////////////
//bindings
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindNewsLocalDataSource(
        newsLocalDataSourceImpl: NewsLocalDataSourceImpl
    ): NewsLocalDataSource

    @Binds
    abstract fun bindNewsRemoteDataSource(
        newsRemoteDataSourceImpl: NewsRemoteDataSourceImpl
    ): NewsRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindUserDataSource(
        userDataSourceImpl: UserDataSourceImpl
    ): UserDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedPreferencesModule {

    @Singleton
    @Binds
    abstract fun bindSharedPreferencesHelper(
        sharedPreferenceHelperImpl: SharedPreferenceHelperImpl
    ): SharedPreferenceDataSource
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class UtilsModule {

    @Binds
    abstract fun bindValidator(
        validateImpl: ValidateImpl
    ): Validate
}

/////////////////////////////////////////////
//Qualifiers
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainImmediateDispatcher
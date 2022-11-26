package com.mahdavi.newsapp.di

import android.content.Context
import android.provider.SyncStateContract
import androidx.room.Room
import com.mahdavi.newsapp.BuildConfig
import com.mahdavi.newsapp.data.api.ApiService
import com.mahdavi.newsapp.data.dataSource.local.LocalDataSource
import com.mahdavi.newsapp.data.dataSource.local.LocalDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.remote.RemoteDataSource
import com.mahdavi.newsapp.data.dataSource.remote.RemoteDataSourceImpl
import com.mahdavi.newsapp.data.db.AppDataBase
import com.mahdavi.newsapp.data.db.ArticleDao
import com.mahdavi.newsapp.data.repository.NewsRepository
import com.mahdavi.newsapp.data.repository.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

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
                    chain.request().newBuilder().addHeader("x-api-key", BuildConfig.API_KEY)
                        .build()
                chain.proceed(newRequest)
            }
        return if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
                .build()
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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticationInterceptorOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext, AppDataBase::class.java, "item_database"
    ).build()

    @Provides
    @Singleton
    fun provideArticleDao(db: AppDataBase) = db.articleDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(
        remoteDataSourceImpl: RemoteDataSourceImpl
    ): RemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository
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
package com.mahdavi.newsapp.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.mahdavi.newsapp.BuildConfig
import com.mahdavi.newsapp.R
import com.mahdavi.newsapp.UserPreferences
import com.mahdavi.newsapp.data.api.ApiService
import com.mahdavi.newsapp.data.dataSource.local.datastore.UserPreferencesSerializer
import com.mahdavi.newsapp.data.dataSource.local.datastore.onboarding.OnBoardingDataSource
import com.mahdavi.newsapp.data.dataSource.local.datastore.onboarding.OnBoardingDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.local.favourite.FavouriteLocalDataSource
import com.mahdavi.newsapp.data.dataSource.local.favourite.FavouriteLocalDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.local.news.NewsLocalDataSource
import com.mahdavi.newsapp.data.dataSource.local.news.NewsLocalDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.local.pref.SharedPreferenceDataSource
import com.mahdavi.newsapp.data.dataSource.local.pref.SharedPreferenceHelperImpl
import com.mahdavi.newsapp.data.dataSource.remote.auth.AuthDataSource
import com.mahdavi.newsapp.data.dataSource.remote.auth.AuthDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.remote.user.UserDataSource
import com.mahdavi.newsapp.data.dataSource.remote.user.UserDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.remote.news.NewsRemoteDataSource
import com.mahdavi.newsapp.data.dataSource.remote.news.NewsRemoteDataSourceImpl
import com.mahdavi.newsapp.data.dataSource.remote.source.SourceRemoteDataSource
import com.mahdavi.newsapp.data.dataSource.remote.source.SourceRemoteDataSourceImpl
import com.mahdavi.newsapp.data.db.AppDataBase
import com.mahdavi.newsapp.data.repository.auth.AuthRepository
import com.mahdavi.newsapp.data.repository.auth.AuthRepositoryImpl
import com.mahdavi.newsapp.data.repository.favourite.FavouriteRepository
import com.mahdavi.newsapp.data.repository.favourite.FavouriteRepositoryImpl
import com.mahdavi.newsapp.data.repository.news.NewsRepository
import com.mahdavi.newsapp.data.repository.news.NewsRepositoryImpl
import com.mahdavi.newsapp.data.repository.onboarding.OnBoardingRepository
import com.mahdavi.newsapp.data.repository.onboarding.OnBoardingRepositoryImpl
import com.mahdavi.newsapp.data.repository.source.SourceRepository
import com.mahdavi.newsapp.data.repository.source.SourceRepositoryImpl
import com.mahdavi.newsapp.data.repository.user.UserRepository
import com.mahdavi.newsapp.data.repository.user.UserRepositoryImpl
import com.mahdavi.newsapp.utils.providers.drawable.DrawableProvider
import com.mahdavi.newsapp.utils.providers.drawable.DrawableProviderImpl
import com.mahdavi.newsapp.utils.providers.string.StringProvider
import com.mahdavi.newsapp.utils.providers.string.StringProviderImpl
import com.mahdavi.newsapp.utils.validate.Validate
import com.mahdavi.newsapp.utils.validate.ValidateImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
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

private const val ON_BOARDING_PREFERENCES = "on_boarding_preferences"
private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

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

    @Provides
    @Singleton
    fun provideSourceDao(db: AppDataBase) = db.sourceDao()

    @Provides
    @Singleton
    fun provideFavouriteHeadlineDao(db: AppDataBase) = db.favouriteHeadlineDao()

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(ON_BOARDING_PREFERENCES) }
        )
    }

    @Singleton
    @Provides
    fun provideProtoDataStore(@ApplicationContext appContext: Context): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
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

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(
            context,
            R.string.notification_channel_id_firebase.toString()
        )
    }

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                R.string.notification_channel_id_firebase.toString(),
                R.string.notification_channel_name_firebase.toString(),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindNewsLocalDataSource(
        newsLocalDataSourceImpl: NewsLocalDataSourceImpl
    ): NewsLocalDataSource

    @Binds
    abstract fun bindNewsRemoteDataSource(
        newsRemoteDataSourceImpl: NewsRemoteDataSourceImpl
    ): NewsRemoteDataSource

    @ViewModelScoped
    @Binds
    abstract fun bindUserDataSource(
        userDataSourceImpl: UserDataSourceImpl
    ): UserDataSource

    @Binds
    abstract fun bindAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl
    ): AuthDataSource

    @Binds
    abstract fun bindOnBoardingDataSource(
        onBoardingDataSourceImpl: OnBoardingDataSourceImpl
    ): OnBoardingDataSource

    @Binds
    abstract fun bindSourceRemoteDataSource(
        sourceRemoteDataSourceImpl: SourceRemoteDataSourceImpl
    ): SourceRemoteDataSource

    @Binds
    abstract fun bindFavouriteLocalDataSource(
        favouriteLocalDataSourceImpl: FavouriteLocalDataSourceImpl
    ): FavouriteLocalDataSource
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    @ViewModelScoped
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindOnBoardingRepository(
        onBoardingRepositoryImpl: OnBoardingRepositoryImpl
    ): OnBoardingRepository

    @Binds
    abstract fun bindSourceRepository(
        sourceRepositoryImpl: SourceRepositoryImpl
    ): SourceRepository

    @Binds
    abstract fun bindFavouriteRepository(
        favouriteRepositoryImpl: FavouriteRepositoryImpl
    ): FavouriteRepository
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
@InstallIn(SingletonComponent::class)
abstract class UtilsModule {

    @Binds
    abstract fun bindValidator(
        validateImpl: ValidateImpl
    ): Validate

    @Singleton
    @Binds
    abstract fun bindStringProvider(
        stringProviderImpl: StringProviderImpl
    ): StringProvider

    @Singleton
    @Binds
    abstract fun bindDrawableProvider(
        drawableProviderImpl: DrawableProviderImpl
    ): DrawableProvider
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
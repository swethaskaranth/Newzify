package com.kaizencoder.newzify.di

import android.content.Context
import androidx.room.Room
import com.kaizencoder.newzify.BuildConfig
import com.kaizencoder.newzify.data.local.AppDatabase
import com.kaizencoder.newzify.data.local.ArticleDao
import com.kaizencoder.newzify.data.networking.NewsApiService
import com.kaizencoder.newzify.data.repository.HeadlinesRepositoryImpl
import com.kaizencoder.newzify.data.repository.SavedArticlesRepositoryImpl
import com.kaizencoder.newzify.domain.repository.HeadlinesRepository
import com.kaizencoder.newzify.domain.repository.SavedArticlesRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun moshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    fun httpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun okhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    fun retrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    fun newsApiService(retrofit: Retrofit): NewsApiService =
        retrofit.create(NewsApiService::class.java)

    @Provides
    fun appDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "article_database"
        ).build()


    @Provides
    fun articleDao(appDatabase: AppDatabase): ArticleDao = appDatabase.articleDao()


    @Provides
    fun headlinesRepository(
        newsApiService: NewsApiService,
        articleDao: ArticleDao
    ): HeadlinesRepository =
        HeadlinesRepositoryImpl(newsApiService, articleDao)

    @Provides
    fun savedArticlesRepository(articleDao: ArticleDao): SavedArticlesRepository =
        SavedArticlesRepositoryImpl(articleDao)
}

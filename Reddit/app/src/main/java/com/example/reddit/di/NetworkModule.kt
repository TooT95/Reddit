package com.example.reddit.di

import com.example.reddit.network.*
import com.example.reddit.utils.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor())
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .baseUrl(Utils.BASE_URL)
            .build()
    }

    @Provides
    fun provideSubredditApi(retrofit: Retrofit): SubredditApi {
        return retrofit.create()
    }

    @Provides
    fun provideSubredditListingApi(retrofit: Retrofit): SubredditListingApi {
        return retrofit.create()
    }

    @Provides
    fun provideCommentApi(retrofit: Retrofit): CommentApi {
        return retrofit.create(CommentApi::class.java)
    }

    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create()
    }
}
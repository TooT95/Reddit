package com.example.reddit.network

import com.example.reddit.utils.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)
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
    fun tokenApi(retrofit: Retrofit): TokenApi {
        return retrofit.create(TokenApi::class.java)
    }

}
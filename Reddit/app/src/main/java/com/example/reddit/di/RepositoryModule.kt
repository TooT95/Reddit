package com.example.reddit.di

import com.example.reddit.network.SubredditApi
import com.example.reddit.repository.SubredditRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    fun provideSubredditRepository(subredditApi: SubredditApi): SubredditRepository {
        return SubredditRepository(subredditApi)
    }

}
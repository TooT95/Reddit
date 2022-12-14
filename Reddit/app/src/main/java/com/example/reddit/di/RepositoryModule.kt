package com.example.reddit.di

import com.example.reddit.network.CommentApi
import com.example.reddit.network.SubredditApi
import com.example.reddit.network.SubredditListingApi
import com.example.reddit.network.UserApi
import com.example.reddit.repository.AccountRepository
import com.example.reddit.repository.CommentRepository
import com.example.reddit.repository.SubredditListingRepository
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

    @Provides
    fun provideSubredditListingRepository(subredditApi: SubredditListingApi): SubredditListingRepository {
        return SubredditListingRepository(subredditApi)
    }

    @Provides
    fun provideAccountRepository(subredditApi: SubredditApi, userApi: UserApi): AccountRepository {
        return AccountRepository(subredditApi, userApi)
    }

    @Provides
    fun provideCommentRepository(commentApi: CommentApi): CommentRepository {
        return CommentRepository(commentApi)
    }
}
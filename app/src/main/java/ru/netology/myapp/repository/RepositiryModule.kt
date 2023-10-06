package ru.netology.myapp.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositiryModule {
    @Singleton
    @Binds
    fun bindsPostRepositiry(imp: PostRepositoryImp): PostRepository

    @Singleton
    @Binds
    fun bindsEventRepositiry(imp: EventRepositoryImpl): EventRepository
}
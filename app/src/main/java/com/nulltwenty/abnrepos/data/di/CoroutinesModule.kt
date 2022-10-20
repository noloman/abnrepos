package com.nulltwenty.abnrepos.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {
    @Provides
    @IoCoroutineDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultCoroutineDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
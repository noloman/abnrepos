package com.nulltwenty.abnrepos.data.di

import com.nulltwenty.abnrepos.data.GithubPagingSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.nulltwenty.abnrepos.data.api.service.GithubService
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun githubService(moshi: Moshi, okHttpClient: OkHttpClient): GithubService =
        Retrofit.Builder().baseUrl(GithubService.BASE_URL).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(GithubService::class.java)

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Provides
    fun provideGithubPagingSource(
        @IoCoroutineDispatcher coroutineDispatcher: CoroutineDispatcher,
        githubService: GithubService
    ) = GithubPagingSource(coroutineDispatcher, githubService)
}
package com.nulltwenty.abnrepos.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultCoroutineDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoCoroutineDispatcher
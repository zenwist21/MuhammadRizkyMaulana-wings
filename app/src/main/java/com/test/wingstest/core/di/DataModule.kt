package com.test.wingstest.core.di

import com.test.wingstest.core.data.repository.LoginRepositoryImpl
import com.test.wingstest.core.data.repository.ProductRepositoryImpl
import com.test.wingstest.core.domain.localRepo.LoginRepository
import com.test.wingstest.core.domain.localRepo.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideLocalLogin(localData: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun provideLocalProduct(localData: ProductRepositoryImpl): ProductRepository

}
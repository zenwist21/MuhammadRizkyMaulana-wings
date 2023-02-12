package com.test.wingstest.core.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.test.wingstest.core.data.local.dao.DbDao
import com.test.wingstest.core.data.local.database.DatabaseConfig
import com.test.wingstest.core.data.local.sessionManager.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesDatabaseConfig(db: DatabaseConfig): DbDao {
        return db.configDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): DatabaseConfig {
        return Room.databaseBuilder(
            application,
            DatabaseConfig::class.java, "penjualan.db"
        ).fallbackToDestructiveMigration().build()
    }
    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }
    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }
}
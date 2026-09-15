package com.example.core_ui.di

import android.content.Context
import com.example.core_ui.preferenceDataStore.data.local.SettingsRepositoryImpl
import com.example.core_ui.preferenceDataStore.domain.repositoy.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SettingModule {

    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}
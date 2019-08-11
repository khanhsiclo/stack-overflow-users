package com.martin.stackusers.injection.modules

import android.content.Context
import com.martin.stackusers.database.DatabaseClient
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    fun provideDatabaseClient(context: Context): DatabaseClient = DatabaseClient(context)
}
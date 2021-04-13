package com.crazylegend.subhub.di.modules

import android.content.Context
import com.crazylegend.kotlinextensions.toaster.Toaster
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by funkymuse on 4/13/21 to long live and prosper !
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun gson() = Gson()

    @Provides
    @Singleton
    fun toaster(@ApplicationContext context: Context) = Toaster(context)
}
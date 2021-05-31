package com.ticonsys.hadiths.di

import android.content.Context
import androidx.room.Room
import coil.ImageLoader
import coil.util.CoilUtils
import com.google.gson.GsonBuilder
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.data.db.*
import com.zxdmjr.material_utils.store.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideDeviceLanguage(): String = Locale.getDefault().language

    @Provides
    @Singleton
    fun provideSharedPref(
        @ApplicationContext context: Context
    ) = SharedPref(context)


    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideCoinInstance(
        @ApplicationContext context: Context
    ) = ImageLoader.Builder(context)
        .error(R.drawable.ic_launcher_foreground)
        .placeholder(R.drawable.ic_launcher_foreground)
        .okHttpClient {
            OkHttpClient.Builder()
                .cache(CoilUtils.createDefaultCache(context))
                .build()
        }
        .build()


    @Singleton
    @Provides
    fun provideHadithDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        HadithDatabase::class.java,
        HADITH_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideBookDao(db: HadithDatabase): BookDao = db.getBookDao()

    @Singleton
    @Provides
    fun provideChapterDao(db: HadithDatabase): ChapterDao = db.getChapterDao()

    @Singleton
    @Provides
    fun provideHadithDao(db: HadithDatabase): HadithDao = db.getHadithDao()
}
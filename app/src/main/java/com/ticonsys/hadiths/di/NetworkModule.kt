package com.ticonsys.hadiths.di

import android.content.Context
import com.ticonsys.hadiths.BuildConfig
import com.ticonsys.hadiths.data.db.BookDao
import com.ticonsys.hadiths.data.db.ChapterDao
import com.ticonsys.hadiths.data.db.HadithDao
import com.ticonsys.hadiths.data.network.middleware.AuthInterceptor
import com.ticonsys.hadiths.data.network.middleware.AuthInterceptorImpl
import com.ticonsys.hadiths.data.network.middleware.ConnectivityInterceptor
import com.ticonsys.hadiths.data.network.middleware.ConnectivityInterceptorImpl
import com.ticonsys.hadiths.data.network.retrofit.HadithApiService
import com.ticonsys.hadiths.data.repositories.HadithRepository
import com.ticonsys.hadiths.data.repositories.HadithRepositoryImpl
import com.ticonsys.hadiths.utils.FlowCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideConnectivityInterceptor(
        @ApplicationContext context: Context
    ): ConnectivityInterceptor = ConnectivityInterceptorImpl(context)

    @Provides
    @Singleton
    fun provideAuthInterceptor(
    ): AuthInterceptor = AuthInterceptorImpl()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        connectivityInterceptor: ConnectivityInterceptor,
        authInterceptor: AuthInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(connectivityInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideHadithApiService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): HadithApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(FlowCallAdapterFactory())
        .build()
        .create(HadithApiService::class.java)


    @Singleton
    @Provides
    fun provideHadithRepository(
        bookDao: BookDao,
        chapterDao: ChapterDao,
        hadithDao: HadithDao,
        apiService: HadithApiService
    ): HadithRepository = HadithRepositoryImpl(
        bookDao, chapterDao, hadithDao, apiService
    )

}
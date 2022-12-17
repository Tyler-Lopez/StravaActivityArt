package com.company.activityart.di

import android.content.Context
import androidx.room.Room
import com.company.activityart.data.cache.ActivitiesCache
import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.models.ResolutionListFactory
import com.company.activityart.domain.use_case.activities.*
import com.company.activityart.domain.use_case.athlete.*
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.presentation.editArtScreen.subscreens.resize.ResolutionListFactoryImpl
import com.company.activityart.util.*
import com.company.activityart.util.constants.StringConstants.BASE_URL
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAthleteDatabase(@ApplicationContext appContext: Context): AthleteDatabase {
        return Room.databaseBuilder(
            appContext,
            AthleteDatabase::class.java,
            "athlete_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideActivitiesCache() = ActivitiesCache

    @Provides
    fun providesGetAthleteFromLocalUseCase(athleteDatabase: AthleteDatabase) =
        GetAthleteFromLocalUseCase(athleteDatabase)

    @Provides
    fun providesGetAthleteFromRemoteUseCase(api: AthleteApi) =
        GetAthleteFromRemoteUseCase(api)

    @Provides
    fun providesGetAthleteUseCase(
        getAthleteFromLocalUseCase: GetAthleteFromLocalUseCase,
        getAthleteFromRemoteUseCase: GetAthleteFromRemoteUseCase,
        insertAthleteUseCase: InsertAthleteUseCase,
        clearAccessTokenUseCase: ClearAccessTokenUseCase
    ): GetAthleteUseCase =
        GetAthleteUseCase(
            getAthleteFromLocalUseCase,
            getAthleteFromRemoteUseCase,
            insertAthleteUseCase,
            clearAccessTokenUseCase
        )

    @Provides
    fun providesGetCachedMonthsByYearUseCase(athleteDatabase: AthleteDatabase) =
        GetLastCachedYearMonthsUseCase(athleteDatabase)

    @Provides
    fun providesGetActivitiesByPageFromRemoteUseCase(
        api: AthleteApi
    ): GetActivitiesByPageFromRemoteUseCase =
        GetActivitiesByPageFromRemoteUseCase(api)

    @Provides
    fun providesGetActivitiesByYearFromRemoteUseCase(
        getActivitiesInYearByPageFromRemoteUseCase: GetActivitiesByPageFromRemoteUseCase,
        timeUtils: TimeUtils
    ) = GetActivitiesByYearFromRemoteUseCase(
        getActivitiesInYearByPageFromRemoteUseCase,
        timeUtils
    )

    @Provides
    fun providesGetActivitiesByYearMonthFromRemoteUseCase(
        getActivitiesInYearByPageFromRemoteUseCase: GetActivitiesByPageFromRemoteUseCase,
        timeUtils: TimeUtils
    ) = GetActivitiesByYearMonthFromRemoteUseCase(
        getActivitiesInYearByPageFromRemoteUseCase,
        timeUtils
    )

    @Provides
    fun providesGetActivitiesByYearFromLocalUseCase(
        athleteDatabase: AthleteDatabase
    ) = GetActivitiesByYearFromLocalUseCase(athleteDatabase)

    @Provides
    fun providesGetActivitiesByYearMonthFromLocalUseCase(
        athleteDatabase: AthleteDatabase
    ) = GetActivitiesByYearMonthFromLocalUseCase(athleteDatabase)

    @Provides
    fun providesGetActivitiesFromCacheUseCase(cache: ActivitiesCache) =
        GetActivitiesByYearFromCacheUseCase(cache)

    @Provides
    fun providesInsertActivitiesFromCacheUseCase(cache: ActivitiesCache) =
        InsertActivitiesIntoCacheUseCase(cache)

    @Provides
    fun providesGetActivitiesByYear(
        getAthleteLastCachedYearMonthsUseCase: GetLastCachedYearMonthsUseCase,
        getActivitiesByYearMonthFromLocalUseCase: GetActivitiesByYearMonthFromLocalUseCase,
        getActivitiesByYearFromRemoteUseCase: GetActivitiesByYearFromRemoteUseCase,
        getActivitiesFromCacheUseCase: GetActivitiesByYearFromCacheUseCase,
        insertActivitiesIntoCacheUseCase: InsertActivitiesIntoCacheUseCase,
        insertActivitiesUseCase: InsertActivitiesUseCase,
        timeUtils: TimeUtils
    ) = GetActivitiesByYearUseCase(
        getAthleteLastCachedYearMonthsUseCase,
        getActivitiesByYearMonthFromLocalUseCase,
        getActivitiesByYearFromRemoteUseCase,
        getActivitiesFromCacheUseCase,
        insertActivitiesIntoCacheUseCase,
        insertActivitiesUseCase,
        timeUtils
    )

    @Provides
    fun providesInsertAthleteFromRemoteUseCase(athleteDatabase: AthleteDatabase) =
        InsertAthleteUseCase(athleteDatabase)

    @Provides
    fun clearAccessTokenUseCase(
        athleteDatabase: AthleteDatabase,
        cache: ActivitiesCache
    ) =
        ClearAccessTokenUseCase(athleteDatabase, cache)

    @Singleton
    @Provides
    fun provideAthleteApi(): AthleteApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(AthleteApi::class.java) // Creates singleton implementation of interface
    }

    @Provides
    fun provideResolutionListFactory(): ResolutionListFactory = ResolutionListFactoryImpl()

    @Provides
    fun provideActivityFilterUtils(timeUtils: TimeUtils) = ActivityFilterUtils(
        timeUtils
    )

    @Provides
    fun provideImageSizeUtils() = ImageSizeUtils()

    @Provides
    fun provideUriUtils() = UriUtils()

    @Provides
    fun provideTimeUtils() = TimeUtils()

    @Provides
    fun provideGson() = Gson()
    
    @Provides
    fun provideVisualizationUtils(imageSizeUtils: ImageSizeUtils) =
        VisualizationUtils(imageSizeUtils)
}
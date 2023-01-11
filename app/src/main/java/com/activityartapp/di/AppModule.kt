package com.activityartapp.di

import android.content.Context
import androidx.room.Room
import com.activityartapp.data.FileRepositoryImpl
import com.activityartapp.data.cache.ActivitiesCache
import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.domain.FileRepository
import com.activityartapp.domain.models.ResolutionListFactory
import com.activityartapp.domain.use_case.authentication.ClearAccessTokenUseCase
import com.activityartapp.presentation.editArtScreen.subscreens.resize.ResolutionListFactoryImpl
import com.activityartapp.util.constants.StringConstants.BASE_URL
import com.activityartapp.domain.use_case.activities.*
import com.activityartapp.domain.use_case.athlete.*
import com.activityartapp.util.*
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
    fun provideVisualizationUtils(@ApplicationContext appContext: Context) =
        VisualizationUtils(appContext)

    @Provides
    fun provideFileRepository(@ApplicationContext appContext: Context): FileRepository =
        FileRepositoryImpl(appContext)
}
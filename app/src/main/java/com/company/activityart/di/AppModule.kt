package com.company.activityart.di

import android.content.Context
import androidx.room.Room
import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.use_case.activities.*
import com.company.activityart.domain.use_case.athlete.GetAthleteFromLocalUseCase
import com.company.activityart.domain.use_case.athlete.GetAthleteFromRemoteUseCase
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.athlete.InsertAthleteUseCase
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.util.StringConstants.BASE_URL
import com.company.activityart.util.TimeUtils
import com.company.activityart.util.UriUtils
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
    fun providesGetActivitiesByYear(
        getActivitiesByYearFromRemoteUseCase: GetActivitiesByYearFromRemoteUseCase
    ) = GetActivitiesByYearUseCase(getActivitiesByYearFromRemoteUseCase)

    @Provides
    fun providesInsertAthleteFromRemoteUseCase(athleteDatabase: AthleteDatabase) =
        InsertAthleteUseCase(athleteDatabase)

    @Provides
    fun clearAccessTokenUseCase(athleteDatabase: AthleteDatabase) =
        ClearAccessTokenUseCase(athleteDatabase)

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
    fun provideUriUtils(): UriUtils = UriUtils()

    @Provides
    fun provideTimeUtils(): TimeUtils = TimeUtils()
}
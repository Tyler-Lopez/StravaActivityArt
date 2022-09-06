package com.company.activityart.di

import android.content.Context
import androidx.room.Room
import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.use_case.athlete.GetAthleteFromLocalUseCase
import com.company.activityart.domain.use_case.athlete.GetAthleteFromRemoteUseCase
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.athlete.InsertAthleteUseCase
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.domain.use_case.authentication.GetAccessTokenUseCase
import com.company.activityart.util.StringConstants.BASE_URL
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
        ).build()
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
    fun providesInsertAthleteFromRemoteUseCase(athleteDatabase: AthleteDatabase) =
        InsertAthleteUseCase(athleteDatabase)

    @Provides
    fun clearAccessTokenUseCase(athleteDatabase: AthleteDatabase) =
        ClearAccessTokenUseCase(athleteDatabase)

    /*
    @Singleton
    @Provides
    fun provideGearUseCases(
        api: AthleteApi
    ) = GearUseCases(
        getGearFromApiUseCase = GetGearFromApiUseCase(api)
    )

    @Singleton
    @Provides
    fun provideActivitiesUseCases(
        api: AthleteApi
    ) = ActivitiesUseCases(
        getActivitiesUseCase = GetActivitiesUseCase(api),
        insertActivitiesUseCase = InsertActivitiesUseCase()
    )

     */

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
}
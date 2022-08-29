package com.company.activityart.di

import android.content.Context
import androidx.room.Room
import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.use_case.ActivitiesUseCases
import com.company.activityart.domain.use_case.AuthenticationUseCases
import com.company.activityart.domain.use_case.GearUseCases
import com.company.activityart.domain.use_case.athlete.GetAthleteFromLocalUseCase
import com.company.activityart.domain.use_case.athlete.GetAthleteFromRemoteUseCase
import com.company.activityart.domain.use_case.clear_access_token.ClearAccessTokenUseCase
import com.company.activityart.domain.use_case.get_access_token.GetAccessTokenUseCase
import com.company.activityart.domain.use_case.get_activities.GetActivitiesUseCase
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.athlete.InsertAthleteUseCase
import com.company.activityart.domain.use_case.get_gear.GetGearFromApiUseCase
import com.company.activityart.domain.use_case.insert_activities.InsertActivitiesUseCase
import com.company.activityart.domain.use_case.authentication.SetAccessTokenUseCase
import com.company.activityart.util.Constants.BASE_URL
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
    fun provideAuthenticationUseCases(
        api: AthleteApi
    ) = AuthenticationUseCases(
        getAccessTokenUseCase = GetAccessTokenUseCase(api),
        clearAccessTokenUseCase = ClearAccessTokenUseCase(),
        setAccessTokenUseCase = SetAccessTokenUseCase()
    )

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
    ): GetAthleteUseCase =
        GetAthleteUseCase(
            getAthleteFromLocalUseCase,
            getAthleteFromRemoteUseCase,
            insertAthleteUseCase
        )

    @Provides
    fun providesInsertAthleteFromRemoteUseCase(athleteDatabase: AthleteDatabase) =
        InsertAthleteUseCase(athleteDatabase)

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

    @Singleton
    @Provides
    fun provideAthleteApi(): AthleteApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(AthleteApi::class.java) // Creates singleton implementation of interface
    }


}
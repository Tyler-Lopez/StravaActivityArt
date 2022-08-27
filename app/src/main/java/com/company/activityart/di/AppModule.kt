package com.company.activityart.di

import android.content.Context
import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.use_case.ActivitiesUseCases
import com.company.activityart.domain.use_case.AthleteUseCases
import com.company.activityart.domain.use_case.AuthenticationUseCases
import com.company.activityart.domain.use_case.GearUseCases
import com.company.activityart.domain.use_case.clear_access_token.ClearAccessTokenUseCase
import com.company.activityart.domain.use_case.get_access_token.GetAccessTokenUseCase
import com.company.activityart.domain.use_case.get_activities.GetActivitiesUseCase
import com.company.activityart.domain.use_case.get_athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.get_gear.GetGearFromApiUseCase
import com.company.activityart.domain.use_case.insert_activities.InsertActivitiesUseCase
import com.company.activityart.domain.use_case.set_access_token.SetAccessTokenUseCase
import com.company.activityart.domain.use_case.set_athlete.SetAthleteUseCase
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
    fun provideAthleteUseCases(
        api: AthleteApi
    ) = AthleteUseCases(
        getAthleteUseCase = GetAthleteUseCase(api),
        setAthleteUseCase = SetAthleteUseCase()
    )

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

    // TODO come back to https://svvashishtha.medium.com/using-room-with-hilt-cb57a1bc32f
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AthleteDatabase {

    }

}
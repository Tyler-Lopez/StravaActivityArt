package com.company.athleteapiart.di

import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.domain.use_case.AuthenticationUseCases
import com.company.athleteapiart.domain.use_case.clear_access_token.ClearAccessTokenUseCase
import com.company.athleteapiart.domain.use_case.get_set_access_token.GetAccessTokenUseCase
import com.company.athleteapiart.domain.use_case.set_access_token.SetAccessTokenUseCase
import com.company.athleteapiart.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideAthleteApi(): AthleteApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(AthleteApi::class.java) // Creates singleton implementation of interface
    }

}
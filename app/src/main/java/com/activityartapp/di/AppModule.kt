package com.activityartapp.di

import android.content.Context
import androidx.room.Room
import com.activityartapp.data.cache.ActivitiesCache
import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.data.repository.AthleteUsageRepositoryImpl
import com.activityartapp.data.repository.FileRepositoryImpl
import com.activityartapp.data.repository.VersionRepositoryImpl
import com.activityartapp.domain.models.ResolutionListFactory
import com.activityartapp.domain.repository.AthleteUsageRepository
import com.activityartapp.domain.repository.FileRepository
import com.activityartapp.domain.repository.VersionRepository
import com.activityartapp.domain.useCase.activities.GetActivitiesByPageFromRemote
import com.activityartapp.domain.useCase.activities.InsertActivitiesIntoMemory
import com.activityartapp.domain.useCase.authentication.ClearAccessTokenFromDisk
import com.activityartapp.presentation.editArtScreen.subscreens.resize.ResolutionListFactoryImpl
import com.activityartapp.util.*
import com.activityartapp.util.constants.TokenConstants
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
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
    fun provideActivitiesCache() = ActivitiesCache()

    @Provides
    fun providesGetActivitiesByPageFromRemoteUseCase(
        api: AthleteApi
    ): GetActivitiesByPageFromRemote =
        GetActivitiesByPageFromRemote(api)

    @Provides
    fun providesInsertActivitiesFromCacheUseCase(cache: ActivitiesCache) =
        InsertActivitiesIntoMemory(cache)

    @Provides
    fun clearAccessTokenUseCase(
        athleteDatabase: AthleteDatabase,
        cache: ActivitiesCache
    ) =
        ClearAccessTokenFromDisk(athleteDatabase, cache)

    @Singleton
    @Provides
    fun provideAthleteApi(): AthleteApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(TokenConstants.BASE_URL)
            .build()
            .create(AthleteApi::class.java) // Creates singleton implementation of interface
    }

    @Provides
    fun provideResolutionListFactory(): ResolutionListFactory = ResolutionListFactoryImpl()

    @Provides
    fun provideActivityFilterUtils(timeUtils: TimeUtils) = ActivityFilterUtils(timeUtils)

    @Provides
    fun provideActivitySortUtils(timeUtils: TimeUtils) = ActivitySortUtils(timeUtils)

    @Provides
    fun provideColorBrightnessUtils() = ColorBrightnessUtils()

    @Provides
    fun provideImageSizeUtils() = ImageSizeUtils()

    @Provides
    fun provideUriUtils() = UriUtils()

    @Provides
    fun provideTimeUtils() = TimeUtils()

    @Provides
    fun provideParseNumberFromStringUtils() = ParseNumberFromStringUtils()

    @Provides
    fun provideGson() = Gson()

    @Provides
    fun provideVisualizationUtils(
        activitySortUtils: ActivitySortUtils,
        colorBrightnessUtils: ColorBrightnessUtils,
        @ApplicationContext appContext: Context
    ) =
        VisualizationUtils(appContext, activitySortUtils, colorBrightnessUtils)

    @Provides
    fun provideFileRepository(@ApplicationContext appContext: Context): FileRepository =
        FileRepositoryImpl(appContext)

    @Singleton
    @Provides
    fun provideFirestoreDb(): FirebaseFirestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = FirebaseFirestoreSettings
            .Builder()
            .setPersistenceEnabled(false)
            .build()
        clearPersistence()
    }

    @Singleton
    @Provides
    fun provideRealtimeDb(): FirebaseDatabase = FirebaseDatabase.getInstance().apply {
        setPersistenceEnabled(false)
    }

    @Provides
    fun provideAthleteUsageRepository(db: FirebaseDatabase): AthleteUsageRepository =
        AthleteUsageRepositoryImpl(db)

    @Provides
    fun provideVersionRepository(db: FirebaseFirestore): VersionRepository =
        VersionRepositoryImpl(db)
}
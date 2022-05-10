package com.company.athleteapiart.domain.use_case.insert_activities

import android.content.Context
import com.company.athleteapiart.data.database.ActivityDatabase
import com.company.athleteapiart.data.database.AthleteDatabase
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.data.entities.AthleteEntity

class InsertActivitiesUseCase {

    // Invoked to set access token in Room database
    suspend fun insertActivities(
        context: Context,
        activities: Array<ActivityEntity>
    ) {

        val activityDao = ActivityDatabase
            .getInstance(context.applicationContext)
            .activityDao

        activityDao.insertAllActivities(activityEntity = activities)
    }
    
}
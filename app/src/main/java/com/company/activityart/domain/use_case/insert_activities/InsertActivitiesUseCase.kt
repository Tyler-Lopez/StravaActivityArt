package com.company.activityart.domain.use_case.insert_activities

import android.content.Context
import com.company.activityart.data.database.ActivityDatabase
import com.company.activityart.data.entities.ActivityEntity

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
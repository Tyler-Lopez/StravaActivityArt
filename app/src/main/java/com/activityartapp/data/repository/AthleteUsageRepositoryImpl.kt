package com.activityartapp.data.repository

import com.activityartapp.domain.repository.AthleteUsageRepository
import com.activityartapp.util.Response
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AthleteUsageRepositoryImpl @Inject constructor(private val db: FirebaseDatabase) :
    AthleteUsageRepository {

    companion object {
        private const val TIMEOUT_TASK_MS = 7500L
    }

    override suspend fun getAthleteUsage(athleteId: String): Response<Int> {
        return try {
            val usageTask = db.reference.child("athlete_usage").child(athleteId).get()
            val usageStr = Tasks.await(usageTask, TIMEOUT_TASK_MS, TimeUnit.MILLISECONDS)?.value
            val usageInt = usageStr?.toString()?.toInt()
            Response.Success(usageInt ?: AthleteUsageRepository.NO_USAGE_FOUND)
        } catch (e: Exception) {
            Response.Error(exception = e)
        }
    }

    override suspend fun insertAthleteUsage(athleteId: String, usage: Int) {
        try {
            db
                .reference
                .child("athlete_usage")
                .child(athleteId)
                .setValue(usage)
        } catch (e: Exception) {

        }
    }
}
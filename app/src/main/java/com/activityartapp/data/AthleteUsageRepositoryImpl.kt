package com.activityartapp.data

import android.os.AsyncTask
import com.activityartapp.domain.AthleteUsageRepository
import com.activityartapp.util.Response
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AthleteUsageRepositoryImpl @Inject constructor(private val db: FirebaseDatabase) :
    AthleteUsageRepository {

    companion object {
        private const val FIRESTORE_COLLECTION_ATHLETE_USAGE = "athlete_usage"
        private const val FIRESTORE_FIELD_USAGE_KEY = "usage"
        private const val FIRESTORE_FIELD_INITIAL_USAGE = 0
        private const val TIMEOUT_TASK_MS = 5000L
    }

    override suspend fun getAthleteUsage(athleteId: String): Response<Int> {
        return try {
            val usageTask = db.reference.child("athlete_usage").child(athleteId).get()
            val usageStr = Tasks.await(usageTask, TIMEOUT_TASK_MS, TimeUnit.MILLISECONDS)?.value
            val usageInt = usageStr?.toString()?.toInt()
            Response.Success(usageInt ?: 0)
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
package com.activityartapp.data

import com.activityartapp.domain.VersionRepository
import com.activityartapp.util.Response
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class VersionRepositoryImpl @Inject constructor(private val db: FirebaseFirestore) :
    VersionRepository {

    companion object {
        private const val FIRESTORE_COLLECTION_VERSION = "athlete_usage"
        private const val FIRESTORE_DOCUMENT_LATEST_KEY = "latest"
        private const val FIRESTORE_FIELD_MAJOR_KEY = "major"
        private const val FIRESTORE_FIELD_MINOR_KEY = "minor"
    }

    override suspend fun newVersionAvailable(currMajor: Int, currMinor: Int): Response<Boolean> {
        return try {
            suspendCoroutine { continuation ->
                val versionRef =
                    db.collection(FIRESTORE_COLLECTION_VERSION)
                versionRef
                    .whereEqualTo(FieldPath.documentId(), FIRESTORE_DOCUMENT_LATEST_KEY)
                    .get()
                    .addOnSuccessListener {
                        val document: DocumentSnapshot? = it.documents.firstOrNull()
                        val major = (document?.get(FIRESTORE_FIELD_MAJOR_KEY)
                            ?.toString()
                            ?.toInt()) ?: Int.MIN_VALUE
                        val minor = (document?.get(FIRESTORE_FIELD_MINOR_KEY)
                            ?.toString()
                            ?.toInt()) ?: Int.MIN_VALUE
                        val newVersionAvailable = currMajor < major || currMinor < minor
                        continuation.resume(Response.Success(newVersionAvailable))
                    }
                    .addOnCanceledListener {
                        continuation.resume(Response.Error())
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        } catch (e: Exception) {
            Response.Error(exception = e)
        }
    }
}
package com.activityartapp.data.repository

import com.activityartapp.domain.repository.VersionRepository
import com.activityartapp.domain.models.Version
import com.activityartapp.util.Response
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VersionRepositoryImpl @Inject constructor(private val db: FirebaseFirestore) :
    VersionRepository {

    companion object {
        private const val FIRESTORE_COLLECTION_VERSION = "version"
        private const val FIRESTORE_FIELD_LATEST_KEY = "latest"
        private const val FIRESTORE_FIELD_SUPPORTED_KEY = "supported"
        private const val TIMEOUT_TASK_MS = 5000L
    }

    override suspend fun getVersion(versionStr: String): Response<Version> {
        return try {
            val versionRef = db.collection(FIRESTORE_COLLECTION_VERSION)
            val versionTask = versionRef
                .whereEqualTo(FieldPath.documentId(), versionStr.replace('.', '-'))
                .get()

            val document = Tasks.await(
                versionTask,
                TIMEOUT_TASK_MS, TimeUnit.MILLISECONDS
            ).documents.firstOrNull()

            /** If we weren't able to get the document to verify version, throw no internet exception **/
            (document ?: run {
                println("Unable to verify local version [$versionStr] on remote.")
                throw UnknownHostException()
            }).run {
                val isLatest = get(FIRESTORE_FIELD_LATEST_KEY)
                    ?.toString()
                    ?.toBoolean()
                    ?: true
                val isSupported = get(FIRESTORE_FIELD_SUPPORTED_KEY)
                    ?.toString()
                    ?.toBoolean()
                    ?: true
                Response.Success(object : Version {
                    override val isLatest: Boolean = isLatest
                    override val isSupported: Boolean = isSupported
                })
            }
        } catch (e: Exception) {
            Response.Error(exception = e)
        }
    }
}
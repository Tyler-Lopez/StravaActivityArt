package com.activityartapp.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.activityartapp.domain.repository.FileRepository
import com.activityartapp.util.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val context: Context
) : FileRepository {

    companion object {
        private const val EXTENSION_PNG = ".png"
        private const val EXTENSION_JPG = ".jpeg"
        private const val MIME_TYPE_PNG = "image/png"
        private const val MIME_TYPE_JPG = "image/jpeg"
        private const val FILENAME_CACHED = "activity_art"
    }

    override suspend fun saveBitmapToGallery(
        bitmap: Bitmap,
        withTransparency: Boolean
    ): Response<Unit> {
        return try {
            val extension = if (withTransparency) EXTENSION_PNG else EXTENSION_JPG
            val fileName = System.currentTimeMillis().toString() + extension
            val resolver = context.contentResolver

            val uri: Uri = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                /**
                 * https://stackoverflow.com/questions/8560501/android-save-image-into-gallery
                 * https://stackoverflow.com/questions/57726896/mediastore-images-media-insertimage-deprecated
                 */
                resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    initializeContentValues(fileName, withTransparency).apply {
                        put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            DIRECTORY_DOWNLOADS
                        )
                    }
                )
            } else {
                val file = File(
                    Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES),
                    fileName
                )
                /** MediaStore.Images.Media.DATA is deprecated in API 29 **/
                resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    initializeContentValues(fileName, withTransparency).apply {
                        put(MediaStore.Images.Media.DATA, file.absolutePath)
                    }
                )
            } ?: error("Null URI returned.")

            runCatching {
                resolver.openOutputStream(uri)?.apply {
                    if (withTransparency) {
                        bitmap.compressAsPng(this)
                    } else {
                        bitmap.compressAsJpeg(this)
                    }
                    flushAndClose()
                }
            }

            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Error(exception = e)
        }
    }

    override suspend fun saveBitmapToCache(
        bitmap: Bitmap,
        withTransparency: Boolean
    ): Response<Uri> {
        return try {
            val imageFolder = File(context.cacheDir, "images")
            imageFolder.mkdirs()
            val extension = if (withTransparency) EXTENSION_PNG else EXTENSION_JPG
            val file = File(imageFolder, FILENAME_CACHED + extension)

            runCatching {
                FileOutputStream(file).apply {
                    if (withTransparency) {
                        bitmap.compressAsPng(this)
                    } else {
                        bitmap.compressAsJpeg(this)
                    }
                    flushAndClose()
                }
            }

            Response.Success(FileProvider.getUriForFile(context, "com.activityartapp", file))
        } catch (e: Exception) {
            println("Here exception caught saving to cache, $e")
            Response.Error(exception = e)
        }
    }

    private fun initializeContentValues(
        fileName: String,
        withTransparency: Boolean
    ): ContentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        val mimeType = if (withTransparency) MIME_TYPE_PNG else MIME_TYPE_JPG
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    }

    private fun Bitmap.compressAsPng(outputStream: OutputStream) {
        compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    }

    private fun Bitmap.compressAsJpeg(outputStream: OutputStream) {
        compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    }

    private fun OutputStream.flushAndClose() {
        flush()
        close()
    }
}
package com.company.activityart.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.company.activityart.BuildConfig
import com.company.activityart.domain.FileRepository
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val context: Context
) : FileRepository {
    override suspend fun saveBitmap(bitmap: Bitmap) {
        val filename = "Testfile2"

        println("here save bitmap invoked")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            println("save bitmap invoked with build config greater than q ")
            val resolver = context.contentResolver
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            values.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/" + "ActivityArt"
            )
            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
            kotlin.runCatching {
                val outputStream = resolver.openOutputStream(uri!!)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream?.flush()
                outputStream?.close()
            }
        } else {
            val directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
    }
}
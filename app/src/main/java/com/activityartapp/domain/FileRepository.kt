package com.activityartapp.domain

import android.graphics.Bitmap
import android.net.Uri
import com.activityartapp.activityart.util.Response

interface FileRepository {
    suspend fun saveBitmapToGallery(bitmap: Bitmap): Response<Unit>
    suspend fun saveBitmapToCache(bitmap: Bitmap): Response<Uri>
}
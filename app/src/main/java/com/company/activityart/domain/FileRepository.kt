package com.company.activityart.domain

import android.graphics.Bitmap
import android.net.Uri
import com.company.activityart.util.Response

interface FileRepository {
    suspend fun saveBitmapToGallery(bitmap: Bitmap): Response<Unit>
    suspend fun saveBitmapToCache(bitmap: Bitmap): Response<Uri>
}
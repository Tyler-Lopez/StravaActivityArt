package com.activityartapp.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.activityartapp.util.Response

interface FileRepository {
    suspend fun saveBitmapToGallery(
        bitmap: Bitmap,
        withTransparency: Boolean
    ): Response<Unit>
    suspend fun saveBitmapToCache(
        bitmap: Bitmap,
        withTransparency: Boolean
    ): Response<Uri>
}
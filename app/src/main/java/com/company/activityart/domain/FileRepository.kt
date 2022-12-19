package com.company.activityart.domain

import android.graphics.Bitmap

interface FileRepository {
    suspend fun saveBitmap(bitmap: Bitmap)
}
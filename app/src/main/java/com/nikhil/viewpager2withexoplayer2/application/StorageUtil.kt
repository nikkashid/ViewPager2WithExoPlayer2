package com.nikhil.viewpager2withexoplayer2.application

import android.content.Context
import android.os.Environment
import android.os.StatFs
import android.util.Log
import java.io.File

object StorageUtil {

    private val GET_EXO_DIR = "ExoDir"

    fun getExoCacheDir(context: Context): File {
        val cacheDir: File = getExoCacheDir(context, true)
        return File(cacheDir, GET_EXO_DIR)
    }

    private fun getExoCacheDir(context: Context, preferExternal: Boolean): File {
        var appCacheDir: File? = null
        val externalStorageState: String
        externalStorageState = try {
            Environment.getExternalStorageState()
        } catch (e: NullPointerException) { // (sh)it happens
            ""
        }
        if (preferExternal && Environment.MEDIA_MOUNTED == externalStorageState) {
            appCacheDir = getExternalCacheDir(
                context
            )
        }

        if (appCacheDir == null) {
            val cacheDirPath = "/data/data/" + context.packageName + "/cache/ExoDir/"
            appCacheDir = File(cacheDirPath)
        }
        return appCacheDir
    }

    private fun getExternalCacheDir(context: Context): File? {
        val dataDir = File(File(Environment.getExternalStorageDirectory(), "Android"), "data")
        val appCacheDir = File(File(dataDir, context.packageName), "data")
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null
            }
        }
        return appCacheDir
    }

    fun getCacheMemoryForInternalMemory(): Long {
        var cacheMemory: Long
        val internalMemory: Long = getAvailableInternalMemory()
        cacheMemory = if (internalMemory < 3072) {
            internalMemory * 30 / 100 * 1024 * 1024
        } else {
            1024 * 1024 * 1024.toLong()
        }
        return cacheMemory
    }

    fun getAvailableInternalMemory(): Long {
        var megAvailable: Long = 0
        try {
            val stat = StatFs(Environment.getExternalStorageDirectory().path)
            val bytesAvailable: Long
            bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
            megAvailable = bytesAvailable / (1024 * 1024)
            Log.e("", "Available MB : $megAvailable")
        } catch (e: Exception) {
        }
        return megAvailable
    }
}
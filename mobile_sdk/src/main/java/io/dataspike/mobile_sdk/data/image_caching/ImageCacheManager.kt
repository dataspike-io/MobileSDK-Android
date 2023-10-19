package io.dataspike.mobile_sdk.data.image_caching

import android.graphics.Bitmap
import android.util.LruCache

internal class ImageCacheManager {

    private var memoryCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    fun putBitmapIntoCache(key: String, bitmap: Bitmap?) {
        memoryCache.put(key, bitmap)
    }

    fun getBitmapFromCache(key: String?): Bitmap? {
        return memoryCache.get(key)
    }
}
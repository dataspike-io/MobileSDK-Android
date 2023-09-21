package io.dataspike.mobile_sdk.data.image_caching

import android.graphics.Bitmap
import android.util.LruCache

internal object ImageCacheManager {

    private var memoryCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int = bitmap.byteCount / 1024
        }
    }

    fun putBitmapIntoCache(key: String, bitmap: Bitmap?) {
        memoryCache.put(key, bitmap)
    }

    fun getBitmapFromCache(key: String?): Bitmap? = memoryCache.get(key)

    fun clearImageCache() {
        memoryCache.evictAll()
    }
}
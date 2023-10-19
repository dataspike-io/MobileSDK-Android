package io.dataspike.mobile_sdk.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.ByteBuffer

val acceptableForUploadFileFormats = arrayOf(
    "image/jpeg",
    "image/jpg",
    "image/png",
    "application/pdf"
)
val displayMetrics: DisplayMetrics get() {
    return Resources.getSystem().displayMetrics
}

fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)

    return data
}

fun Fragment.launchInMain(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Main, block = block)
}

fun AppCompatActivity.launchInMain(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Main, block = block)
}

fun ViewPager2.disableOverscroll() {
    (this.getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    return Bitmap.createBitmap(
        this,
        0,
        0,
        width,
        height,
        Matrix().apply { postRotate(degrees) },
        true
    )
}

fun Bitmap.flipHorizontally(): Bitmap {
    val matrix = Matrix().apply { postScale(-1f, 1f, width / 2f, height / 2f) }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.toFile(): File? {
    return kotlin.runCatching {
        val file = File.createTempFile("DS_", ".png")
        file.createNewFile()
        val byteArrayOutputStream = ByteArrayOutputStream()

        compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val bitmapData = byteArrayOutputStream.toByteArray()
        val fileOutputStream = FileOutputStream(file)

        fileOutputStream.write(bitmapData)
        fileOutputStream.flush()
        fileOutputStream.close()

        file
    }.onFailure { e ->
        e.printStackTrace()
    }.getOrNull()
}

fun Bitmap.crop(cropArea: RectF?): Bitmap {
    cropArea ?: return this

    val x = ((width * cropArea.left) / displayMetrics.widthPixels).toInt()
    val y = ((height * cropArea.top) / displayMetrics.heightPixels).toInt()
    val croppedBitmapWidth = (
            (width * cropArea.width()) / displayMetrics.widthPixels
            ).toInt()
    val croppedBitmapHeight = (
            (height * cropArea.height()) / displayMetrics.heightPixels
            ).toInt()

    return Bitmap.createBitmap(
        this,
        x,
        y,
        croppedBitmapWidth,
        croppedBitmapHeight
    )
}

fun InputStream.toBitmap(
    fileType: String,
    width: Int,
    height: Int,
): Bitmap? {
    return kotlin.runCatching {
        if (!acceptableForUploadFileFormats.contains(fileType)) {
            Log.d("Dataspike", "File is not of a supported type")
            return null
        }

        return if (fileType == "application/pdf") {
            val bytesArray = readBytes()
            val file = File.createTempFile("DS_", ".pdf")

            file.writeBytes(bytesArray)

            val renderer = PdfRenderer(
                ParcelFileDescriptor.open(
                    file,
                    ParcelFileDescriptor.MODE_READ_ONLY
                )
            )
            val page = renderer.openPage(0)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            page.render(
                bitmap,
                null,
                null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
            )
            page.close()
            renderer.close()

            bitmap
        } else {
            BitmapFactory.decodeStream(this)
        }
    }.onFailure { throwable -> throwable.printStackTrace() }.getOrNull()
}

fun dpToPx(dp: Float): Float {
    return dpToPx(dp, displayMetrics)
}
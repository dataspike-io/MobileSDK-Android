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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.ByteBuffer

internal object Utils {

    val acceptableForUploadFileFormats = arrayOf(
        "image/jpeg",
        "image/jpg",
        "image/png",
        "application/pdf"
    )
    val displayMetrics: DisplayMetrics get() = Resources.getSystem().displayMetrics

    fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)

        return data
    }

    fun View.visible(isVisible: Boolean) {
        visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun Fragment.launchInMain(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch(Dispatchers.Main, block = block)
    }

    fun AppCompatActivity.launchInMain(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch(Dispatchers.Main, block = block)
    }

    fun Bitmap.rotate(degrees: Float): Bitmap =
        Bitmap.createBitmap(
            this,
            0,
            0,
            width,
            height,
            Matrix().apply { postRotate(degrees) },
            true
        )

    fun Bitmap.toFile(fileNameToSave: String, dir: String): File? {
        var file: File? = null
        //TODO implement with tempFile
        return try {
            file = File(dir + File.separator + fileNameToSave)
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapData = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file
        }
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

    //TODO should catch possible exceptions?
    fun InputStream.toBitmap(
        fileType: String,
        width: Int,
        height: Int,
    ): Bitmap? {
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

            //TODO does stream need to be closed?
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
            close()

            bitmap
        } else {
            val bitmap = BitmapFactory.decodeStream(this)

            close()

            bitmap
        }
    }

    fun dpToPx(dp: Float): Float = dpToPx(dp, displayMetrics)
}
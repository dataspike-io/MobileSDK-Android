package io.dataspike.mobile_sdk.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.view.MONT_BOLD
import io.dataspike.mobile_sdk.view.MONT_REGULAR
import io.dataspike.mobile_sdk.view.MONT_SEMI_BOLD
import io.dataspike.mobile_sdk.view.ROBOTO_BOLD
import io.dataspike.mobile_sdk.view.ROBOTO_REGULAR
import io.dataspike.mobile_sdk.view.ROBOTO_SEMI_BOLD
import io.dataspike.mobile_sdk.view.UIManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.ByteBuffer

internal val acceptableForUploadFileFormats = arrayOf(
    "image/jpeg",
    "image/jpg",
    "image/png",
    "application/pdf",
)
internal val displayMetrics: DisplayMetrics get() {
    return Resources.getSystem().displayMetrics
}

internal fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)

    return data
}

internal fun AppCompatActivity.darkModeIsEnabled(): Boolean {
    val darkModeIsEnabled =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        resources.configuration.isNightModeActive
    } else {
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
    } && !UIManager.getUiConfig().options.disableDarkMode

    return darkModeIsEnabled
}

internal fun AppCompatActivity.launchInMain(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Main, block = block)
}

internal fun Fragment.launchInMain(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch(Dispatchers.Main, block = block)
}

internal fun ViewPager2.disableOverscroll() {
    (this.getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
}

internal fun Bitmap.rotate(degrees: Float): Bitmap {
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

internal fun Bitmap.flipHorizontally(): Bitmap {
    val matrix = Matrix().apply { postScale(-1f, 1f, width / 2f, height / 2f) }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

internal fun Bitmap.toFile(): File? {
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
    }.onFailure { throwable ->
        throwable.printStackTrace()
    }.getOrNull()
}

internal fun Bitmap.crop(cropArea: RectF?): Bitmap {
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

internal fun InputStream.toBitmap(
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
    }.onFailure { throwable ->
        throwable.printStackTrace()
    }.getOrNull()
}

internal fun dpToPx(dp: Float): Float {
    return dpToPx(dp, displayMetrics)
}

internal fun AppCompatTextView.setup(
    font: String,
    textSize: Float,
    textColor: Int,
) {
    typeface = getFont(context, font)
    setTextSize(textSize)
    setTextColor(textColor)
}

internal fun AppCompatButton.setup(
    font: String,
    textSize: Float,
    textColor: Int,
    backgroundColor: Int,
) {
    typeface = getFont(context, font)
    setTextSize(textSize)
    setTextColor(textColor)
    background.setTint(backgroundColor)
}

internal fun MaterialButton.setup(
    isVisible: Boolean,
    isEnabled: Boolean,
    isTransparent: Boolean,
    font: String,
    textSize: Float,
    textColors: Pair<Int, Int>,
    text: String,
    backgroundColors: Pair<Int, Int>,
    margin: Float,
    cornerRadius: Float,
    iconRes: Int? = null,
    action: () -> Unit,
) {
    this.isVisible = isVisible

    if (isVisible) {
        val backgroundDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE

            if (isTransparent) {
                setStroke(
                    5,
                    ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf()),
                        intArrayOf(textColors.first, textColors.second)
                    )
                )
            }

            color = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf()),
                intArrayOf(backgroundColors.first, backgroundColors.second)
            )
            this.cornerRadius = dpToPx(cornerRadius)
        }
        val foregroundDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE

            color = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf()),
                intArrayOf(
                    ContextCompat.getColor(context, R.color.transparent_black_color),
                    ContextCompat.getColor(context, R.color.transparent)
                )
            )
            this.cornerRadius = dpToPx(cornerRadius)
        }

        backgroundTintList = null
        background = backgroundDrawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) foreground = foregroundDrawable
        iconRes?.let {
            iconTint = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf()),
                intArrayOf(backgroundColors.first, backgroundColors.second)
            )
        }
        (layoutParams as? ConstraintLayout.LayoutParams)?.leftMargin = dpToPx(margin).toInt()
        (layoutParams as? ConstraintLayout.LayoutParams)?.rightMargin = dpToPx(margin).toInt()
        typeface = getFont(context, font)
        setTextSize(textSize)
        setTextColor(
            ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf()),
                intArrayOf(textColors.first, textColors.second)
            )
        )
        this.text = text
        this.isEnabled = isEnabled
        iconRes?.let {
            icon = ResourcesCompat.getDrawable(resources, iconRes, null)
            iconTint = if (isTransparent) {
               ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf()),
                    intArrayOf(backgroundColors.first, backgroundColors.second)
                )
            } else {
                ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf()),
                    intArrayOf(textColors.first, textColors.second)
                )
            }
        }
        setOnClickListener { action.invoke() }
    }
}

internal fun AppCompatEditText.setup(
    backgroundColor: Int,
    font: String,
    textSize: Float,
    textColor: Int,
    hintColor: Int,
) {
    val backgroundDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        color = ColorStateList.valueOf(backgroundColor)
        cornerRadius = dpToPx(8F)
    }
    background = backgroundDrawable
    updatePadding(
        dpToPx(8F).toInt(),
        dpToPx(8F).toInt(),
        dpToPx(8F).toInt(),
        dpToPx(8F).toInt()
    )
    typeface = getFont(context, font)
    this.textSize = textSize
    setTextColor(textColor)
    setHintTextColor(hintColor)
}

internal fun <T> String?.deserializeFromJson(type: Class<T>): T? {
    if (isNullOrEmpty()) return null

    return runCatching {
        Gson().fromJson(this, type)
    }.onFailure { throwable ->
        throwable.printStackTrace()
    }.getOrNull()
}

internal fun getFont(context: Context, fontString: String): Typeface? {
    val fontRes = when (fontString) {
        MONT_REGULAR -> R.font.mont_regular

        MONT_SEMI_BOLD -> R.font.mont_semi_bold

        MONT_BOLD -> R.font.mont_bold

        ROBOTO_REGULAR -> R.font.roboto_regular

        ROBOTO_SEMI_BOLD -> R.font.roboto_semi_bold

        ROBOTO_BOLD -> R.font.roboto_bold

        else -> R.font.mont_regular
    }

    return runCatching {
        ResourcesCompat.getFont(context, fontRes)
    }.onFailure { throwable ->
        throwable.printStackTrace()
    }.getOrNull()
}

internal fun lightenColor(color: Int?, fraction: Double): Int? {
    color ?: return null

    return runCatching {
        ColorUtils.setAlphaComponent(color, (255 * fraction).toInt())
    }.onFailure { throwable ->
        throwable.printStackTrace()
    }.getOrNull() ?: color
}

internal fun parseColorString(colorString: String?): Int? {
    colorString ?: return null

    return runCatching {
        Color.parseColor(colorString)
    }.onFailure { throwable ->
        throwable.printStackTrace()
    }.getOrNull()
}
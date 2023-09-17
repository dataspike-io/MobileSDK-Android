package io.dataspike.mobile_sdk.view.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ViewUtils.dpToPx
import io.dataspike.mobile_sdk.R

private const val POI_OVERLAY = "0"
private const val LIVENESS_OVERLAY = "1"
private const val POA_OVERLAY = "2"

//TODO ?
@SuppressLint("RestrictedApi")
internal class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatImageView(
    context,
    attrs,
    defStyleAttr
) {

    var poiFrameRectF: RectF? = null
    var livenessFrameRectF: RectF? = null
    var poaFrameRectF: RectF? = null
    private var faceIsInFrame = false
    private var documentIsInFrame = false
    var poiBoundingBox: RectF? = null
    var livenessBoundingBox: RectF? = null
    var poaBoundingBox: RectF? = null

    private val overlayType = context.obtainStyledAttributes(
        attrs,
        R.styleable.OverlayView
    ).getString(R.styleable.OverlayView_overlay_type)

    private val cutoutAreaPaint = Paint().apply {
        color = ContextCompat.getColor(context, android.R.color.transparent)
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val backgroundPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.overlay_black)
    }

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.run {
            drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

            when (overlayType) {
                POI_OVERLAY -> {
                    drawOverlayCutoutForPOI(this)
                }

                LIVENESS_OVERLAY -> {
                    drawOverlayCutoutForLiveness(this)
                }

                POA_OVERLAY -> {
                    drawOverlayCutoutForPOA(this)
                }
            }
        }
    }

    private fun drawOverlayCutoutForPOI(canvas: Canvas) {
        val cutoutMargin = dpToPx(context, 16)
        val cutoutHeight = ((width - (cutoutMargin * 2)) * .66).toInt()
        val top = (height / 1.6f) - cutoutHeight
        val bottom = top + cutoutHeight
        val frameCornerRadius = dpToPx(context, 2)
        val cornersCornerRadius = dpToPx(context, 14)

        poiFrameRectF = RectF(
            cutoutMargin,
            top,
            width.toFloat() - cutoutMargin,
            bottom
        )

        poiFrameRectF?.let {
            canvas.drawRoundRect(
                it,
                frameCornerRadius,
                frameCornerRadius,
                cutoutAreaPaint
            )

            canvas.drawPath(
                createCornersPath(
                    left = it.left - 3,
                    top = it.top - 3,
                    right = it.right + 1,
                    bottom = it.bottom + 1,
                    cornerRadius = cornersCornerRadius,
                    cornerLength = dpToPx(context, 50)
                ),
                Paint().apply {
                    color = if (documentIsInFrame) {
                        ContextCompat.getColor(context, R.color.light_green)
                    } else {
                        ContextCompat.getColor(context, R.color.white)
                    }
                    strokeWidth = dpToPx(context, 2)
                    style = Paint.Style.STROKE
                }
            )

            poiBoundingBox?.let {
                canvas.drawRect(it, Paint().apply {
                    color = Color.RED
                    strokeWidth = 2f
                    style = Paint.Style.STROKE
                })
            }
        }
    }

    private fun drawOverlayCutoutForLiveness(canvas: Canvas) {
        val cutoutMargin = dpToPx(context, 60)
        val cutoutHeight = ((width - (cutoutMargin * 2)) * 1.35).toFloat()
        val top = (height / 1.4f) - cutoutHeight
        val bottom = top + cutoutHeight
        val livenessFrame = Paint().apply {
            color = if (faceIsInFrame) {
                ContextCompat.getColor(context, R.color.light_green)
            } else {
                ContextCompat.getColor(context, R.color.light_red)
            }
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(context, 2)
        }

        livenessFrameRectF = RectF(
           cutoutMargin,
           top,
           width.toFloat() - cutoutMargin,
           bottom
           )

        livenessFrameRectF?.let {
            canvas.drawOval(it, cutoutAreaPaint)
            canvas.drawOval(it, livenessFrame)
        }

        livenessBoundingBox?.let {
            canvas.drawRect(it, Paint().apply {
                color = Color.RED
                strokeWidth = 2f
                style = Paint.Style.STROKE
            })
        }
    }

    private fun drawOverlayCutoutForPOA(canvas: Canvas) {
        val cutoutMargin = dpToPx(context, 50)
        val cutoutHeight = ((width - (cutoutMargin * 2)) * 1.38).toInt()
        val top = (height / 1.25f) - cutoutHeight
        val bottom = top + cutoutHeight
        val frameCornerRadius = dpToPx(context, 2)
        val cornersCornerRadius = dpToPx(context, 14)

        poaFrameRectF = RectF(
            cutoutMargin,
            top,
            width.toFloat() - cutoutMargin,
            bottom
        )

        poaFrameRectF?.let {
            canvas.drawRoundRect(
                it,
                frameCornerRadius,
                frameCornerRadius,
                cutoutAreaPaint
            )

            canvas.drawPath(
                createCornersPath(
                    left = it.left - 3,
                    top = it.top - 3,
                    right = it.right + 1,
                    bottom = it.bottom + 1,
                    cornerRadius = cornersCornerRadius,
                    cornerLength = dpToPx(context, 50)
                ),
                Paint().apply {
                    color = if (documentIsInFrame) {
                        ContextCompat.getColor(context, R.color.light_green)
                    } else {
                        ContextCompat.getColor(context, R.color.white)
                    }
                    strokeWidth = dpToPx(context, 2)
                    style = Paint.Style.STROKE
                }
            )

            poaBoundingBox?.let {
                canvas.drawRect(it, Paint().apply {
                    color = Color.RED
                    strokeWidth = 2f
                    style = Paint.Style.STROKE
                })
            }
        }
    }

    fun setFaceIsInFrame(faceIsInFrame: Boolean) {
        this.faceIsInFrame = faceIsInFrame
        invalidate()
    }

    fun setDocumentIsInFrame(documentIsInFrame: Boolean) {
        this.documentIsInFrame = documentIsInFrame
        invalidate()
    }

    private fun createCornersPath(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        cornerRadius: Float,
        cornerLength: Float
    ): Path {
        val path = Path()

        // top left
        path.moveTo(left, (top + cornerRadius))
        path.arcTo(
            RectF(left, top, left + cornerRadius, top + cornerRadius),
            180f,
            90f,
            true
        )

        path.moveTo(left + (cornerRadius / 2f), top)
        path.lineTo(left + (cornerRadius / 2f) + cornerLength, top)

        path.moveTo(left, top + (cornerRadius / 2f))
        path.lineTo(left, top + (cornerRadius / 2f) + cornerLength)

        // top right
        path.moveTo(right - cornerRadius, top)
        path.arcTo(
            RectF(right - cornerRadius, top, right, top + cornerRadius),
            270f,
            90f,
            true
        )

        path.moveTo(right - (cornerRadius / 2f), top)
        path.lineTo(right - (cornerRadius / 2f) - cornerLength, top)

        path.moveTo(right, top + (cornerRadius / 2f))
        path.lineTo(right, top + (cornerRadius / 2f) + cornerLength)

        // bottom left
        path.moveTo(left, bottom - cornerRadius)
        path.arcTo(
            RectF(left, bottom - cornerRadius, left+cornerRadius, bottom),
            90f,
            90f,
            true
        )

        path.moveTo(left + (cornerRadius / 2f), bottom)
        path.lineTo(left + (cornerRadius / 2f) + cornerLength, bottom)

        path.moveTo(left, bottom - (cornerRadius / 2f))
        path.lineTo(left, bottom - (cornerRadius / 2f) - cornerLength)

        // bottom right
        path.moveTo(left, bottom - cornerRadius)
        path.arcTo(
            RectF(right - cornerRadius, bottom - cornerRadius, right, bottom),
            0f,
            90f,
            true
        )

        path.moveTo(right - (cornerRadius / 2f), bottom)
        path.lineTo(right - (cornerRadius / 2f) - cornerLength, bottom)

        path.moveTo(right, bottom - (cornerRadius / 2f))
        path.lineTo(right, bottom - (cornerRadius / 2f) - cornerLength)

        return path
    }
}
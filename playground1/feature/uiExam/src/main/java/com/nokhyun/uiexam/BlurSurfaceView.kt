package com.nokhyun.uiexam

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RenderEffect
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

class BlurSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var canvas: Canvas? = null
    private lateinit var bitmap: Bitmap
    private lateinit var parent: ViewGroup

    private var renderScript: RenderScript? = null
    private lateinit var blurScript: ScriptIntrinsicBlur
    private lateinit var outAllocation: Allocation

    private val drawListener = ViewTreeObserver.OnPreDrawListener {
        getBackgroundAndDrawBehind()
        true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init(measuredWidth, measuredHeight)
    }

    private fun init(measuredWidth: Int, measuredHeight: Int) {
        bitmap = Bitmap.createBitmap(
            measuredWidth,
            measuredHeight,
            Bitmap.Config.ARGB_8888
        )

        canvas = Canvas(bitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.restore()
    }

    private fun getBackgroundAndDrawBehind() {
        val rootLocation = IntArray(2)
        val viewLocation = IntArray(2)

        parent.getLocationOnScreen(rootLocation)
        this.getLocationOnScreen(viewLocation)

        val left: Int = viewLocation[0] - rootLocation[0]
        val top: Int = viewLocation[1] - rootLocation[1]

        canvas?.save()
        canvas?.translate(-left.toFloat(), -top.toFloat())
        canvas?.let { parent.draw(it) }
        canvas?.restore()
        if (::bitmap.isInitialized) {
            blurWithRenderScript()
        }
    }

    /** Android Api 31 까지 */
    private fun blurWithRenderScript() {
        val renderScript = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))

        val inAllocation = Allocation.createFromBitmap(renderScript, bitmap)
        val outAllocation = Allocation.createTyped(renderScript, inAllocation.type)
        blurScript.setRadius(20f)
        blurScript.setInput(inAllocation)

        blurScript.forEach(outAllocation)
        outAllocation.copyTo(bitmap)

        inAllocation.destroy()
    }

//    private fun blurWithRenderEffect(){
//        this.rootView.setRenderEffect()
//    }

    fun setParent(parent: ViewGroup){
        this.parent = parent
        this.parent.viewTreeObserver.removeOnPreDrawListener(drawListener)
        this.parent.viewTreeObserver.addOnPreDrawListener(drawListener)
    }
}
package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

   // private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    //}

    private  var textBtn = ""
    fun setTextBtn (text : String){
        textBtn = text
        invalidate()
    }

    private  var sweepAngle = 0f
    fun setSweepAngle (angle : Float){
        sweepAngle = angle
        invalidate()
    }

    private  var rightBackLoading = 0f
    fun setRightBackLoading (right : Float){
        rightBackLoading = right
        invalidate()
    }

    private  var textPosition = 340f
    fun setTextPosition (x : Float){
        textPosition = x
        invalidate()
    }


    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.LoadingButton, 0, 0).apply {
            try {
                textBtn = getString(R.styleable.LoadingButton_textBtn)!!
            }

            finally {
                recycle()
            }
        }
    }

    private var paint = Paint().apply {
        color = Color.YELLOW
    }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 75f
    }
    private val backPaint = Paint().apply {
        color = Color.DKGRAY
        textSize = 75f
    }

    private var loadingbackPaint = Paint().apply {
        color = Color.GRAY
        textSize = 75f
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f,0f,975f,170f,backPaint)

        //textBtn = "Download"
        //canvas?.drawText(textBtn,340f,90f,textPaint)
        //textBtn = "We are loading"
        //canvas?.drawText(textBtn,220f,90f,textPaint)


        canvas?.drawRect(0f,0f,rightBackLoading,170f,loadingbackPaint)
        canvas?.drawArc(
            790f, 20f, 890f, 120f,
            0f, sweepAngle, true, paint
        )

        canvas?.drawText(textBtn,textPosition,90f,textPaint)


    }

    fun animateBtn(){
        loadingbackPaint = Paint().apply {
            color = Color.GRAY
            textSize = 75f
        }
        paint = Paint().apply {
            color = Color.YELLOW
        }
        val animatorSweep = ObjectAnimator.ofFloat(this, "sweepAngle", 0f, 360f).apply {
            duration = 350
        }
        val animatorBackLoad = ObjectAnimator.ofFloat(this, "rightBackLoading", 0f, 975f).apply {
            duration = 350
        }
        animatorSweep.start()
        animatorBackLoad.start()

        setTextPosition(220f)
        setTextBtn("We are loading")
    }

    fun resetBtn(){
        loadingbackPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 75f
        }
        paint = Paint().apply {
            color = Color.DKGRAY
        }
        setSweepAngle(0f)
        setRightBackLoading(0f)
        setTextPosition(340f)
        setTextBtn("Download")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}
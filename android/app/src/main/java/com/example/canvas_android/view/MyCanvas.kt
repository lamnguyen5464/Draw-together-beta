package com.example.canvas_android.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.canvas_android.R
import com.example.canvas_android.model.Painting
import com.example.canvas_android.model.Point
import com.example.canvas_android.model.WS
import io.socket.emitter.Emitter
import org.json.JSONArray


class MyCanvas(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var myPainting = Painting()
    var yourPainting = Painting()

    private val SCREEN_WIDTH: Int
    private val SCREEN_HEIGHT: Int

    var savedBitmap: Bitmap? = null
    var savedCanvas: Canvas? = null

    init {
        val metrics: DisplayMetrics? = context?.getResources()?.getDisplayMetrics()
        SCREEN_WIDTH = metrics?.widthPixels ?: 0
        SCREEN_HEIGHT = metrics?.heightPixels ?: 0

        WS.getIntance().setEventListener("server_data", Emitter.Listener {
//            Log.d("@@@", "receive data from server" + it[0].toString())
            yourPainting.addStroke(JSONArray(it[0].toString()))
            postInvalidate()

        })
    }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (savedBitmap === null) {
            //void re-draw
            savedBitmap = Bitmap.createBitmap(SCREEN_WIDTH, SCREEN_HEIGHT, Bitmap.Config.ARGB_8888)
            savedCanvas = Canvas(savedBitmap!!)
            savedCanvas?.drawColor(
                    Color.TRANSPARENT,
                    PorterDuff.Mode.CLEAR);
        }

        var paintStyle = Paint();
        paintStyle.strokeWidth = 15F
        paintStyle.color = R.color.BLACK_20
        paintStyle.strokeCap = Paint.Cap.ROUND
        paintStyle.alpha = 1000

        var prePoint: Point? = null
        yourPainting?.scanLatestStoke { point: Point ->
            var curPoint = point
            prePoint = if (prePoint === null) point else prePoint
            savedCanvas?.drawLine(prePoint!!.x * SCREEN_WIDTH, prePoint!!.y * SCREEN_HEIGHT, curPoint.x * SCREEN_WIDTH, curPoint.y * SCREEN_HEIGHT, paintStyle)
            prePoint = curPoint
        }

        canvas?.drawBitmap(savedBitmap!!, 0f, 0f, Paint())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                myPainting.addStroke(arrayListOf(Point(event.x / SCREEN_WIDTH, event.y / SCREEN_HEIGHT)))
            }

            MotionEvent.ACTION_MOVE -> {
                myPainting.addPointToLastStroke(Point(event.x / SCREEN_WIDTH, event.y / SCREEN_HEIGHT))
//                postInvalidate()
                WS.getIntance().socket.emit("device_data", myPainting.lastStrokeToJSONArray())
            }
        }

        return true
    }
}
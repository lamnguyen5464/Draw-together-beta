package com.example.canvas_android.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.canvas_android.R
import com.example.canvas_android.model.Painting
import com.example.canvas_android.model.Point
import com.example.canvas_android.model.WS
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray


class MyCanvas(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var myPainting = Painting()
    var yourPainting = Painting()

    val SCREEN_WIDTH: Int
    val SCREEN_HEIGHT: Int

    init {
        val metrics: DisplayMetrics? = context?.getResources()?.getDisplayMetrics()
        SCREEN_WIDTH = metrics?.widthPixels ?: 0
        SCREEN_HEIGHT = metrics?.heightPixels ?: 0

        WS.getIntance().doConnect()

        WS.getIntance().socket.emit("request_join", "1")

        WS.getIntance().setEventListener(Socket.EVENT_CONNECT, Emitter.Listener {
            Log.d("@@@ EVENT_CONNECT", it.contentToString());
        })

        WS.getIntance().setEventListener(Socket.EVENT_DISCONNECT, Emitter.Listener {
            Log.d("@@@ EVENT_DISCONNECT", it.contentToString());
        })

        WS.getIntance().setEventListener("server_data", Emitter.Listener {
            Log.d("@@@", "receive data from server" + it[0].toString())
            yourPainting.addStroke(JSONArray(it[0].toString()))

            postInvalidate()

        })
    }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var paintStyle = Paint();
        paintStyle.strokeWidth = 15F
        paintStyle.color = R.color.BLACK_20
        paintStyle.strokeCap = Paint.Cap.ROUND
        paintStyle.alpha = 1000

        var prePoint: Point? = null
        yourPainting?.scanAllPoints { point: Point, isFirstPointOfStroke: Boolean ->
            var curPoint = point
            prePoint = if (isFirstPointOfStroke) point else prePoint
            canvas?.drawLine(prePoint!!.x * SCREEN_WIDTH, prePoint!!.y * SCREEN_HEIGHT, curPoint.x * SCREEN_WIDTH, curPoint.y * SCREEN_HEIGHT, paintStyle)
            prePoint = curPoint
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                myPainting.addStroke(ArrayList<Point>())
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
package com.example.canvas_android.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.canvas_android.R
import com.example.canvas_android.model.Painting
import com.example.canvas_android.model.Point
import com.example.canvas_android.model.WS
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.jetbrains.anko.runOnUiThread
import org.json.JSONArray
import kotlin.collections.ArrayList

class MyCanvas(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var myPainting = Painting()
    var yourPainting = Painting()

    init {

        WS.getIntance().doConnect()

        WS.getIntance().socket.emit("request_join", "1")

        WS.getIntance().setEventListener("back", Emitter.Listener {
            Log.d("@@@ back", it.contentToString() + " " + WS.getIntance().id)
        })

        WS.getIntance().setEventListener(Socket.EVENT_CONNECT, Emitter.Listener {
            Log.d("@@@ EVENT_CONNECT", it.contentToString());
        })

        WS.getIntance().setEventListener(Socket.EVENT_DISCONNECT, Emitter.Listener {
            Log.d("@@@ EVENT_DISCONNECT", it.contentToString());
        })

        WS.getIntance().setEventListener("server_data", Emitter.Listener {
            Log.d("@@@", "receive data from server" + it[0].toString())
            yourPainting = Painting(JSONArray(it[0].toString()))

            this.context.runOnUiThread {
                postInvalidate()

            }
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
            canvas?.drawLine(prePoint!!.x, prePoint!!.y, curPoint.x, curPoint.y, paintStyle)
            prePoint = curPoint
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                myPainting.addStroke(ArrayList<Point>())
            }

            MotionEvent.ACTION_MOVE -> {
                myPainting.addPointToLastStroke(Point(event.x, event.y))
//                postInvalidate()
                WS.getIntance().socket.emit("device_data", myPainting.toJSONArray())
            }
        }

        return true
    }
}
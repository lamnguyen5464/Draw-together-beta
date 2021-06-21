package com.example.canvas_android.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.canvas_android.R
import com.example.canvas_android.model.Point

class MyCanvas(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var lines = ArrayList<ArrayList<Point>>()

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var paintStyle = Paint();
        paintStyle.strokeWidth = 15F
        paintStyle.color = R.color.BLACK_05
        paintStyle.strokeCap = Paint.Cap.ROUND

        if (lines === null) {
            return
        }

        for (line in lines!!) {
            for (i in 1..line.size - 1) {
                var prePoint = line[i - 1]
                var curPoint = line[i]

                canvas?.drawLine(prePoint.x, prePoint.y, curPoint.x, curPoint.y, paintStyle)
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lines?.add(ArrayList<Point>())
            }

            MotionEvent.ACTION_MOVE -> {
                lines?.lastOrNull {
                    it?.add(Point(event.x, event.y))
                }
                postInvalidate()
            }
        }

        return true
    }
}
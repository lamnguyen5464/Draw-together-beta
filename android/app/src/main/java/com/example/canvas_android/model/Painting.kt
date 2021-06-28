package com.example.canvas_android.model

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.sqrt

class Painting {
    var listStrokes = ArrayList<ArrayList<Point>>()

    constructor() {
    }

    constructor(jsonArray: JSONArray) {
        for (index in 0..jsonArray.length() - 1) {
            val jsonStroke: JSONArray = jsonArray.getJSONArray(index)
            addStroke((jsonStroke))
        }
    }

    fun addStroke(jsonStroke: JSONArray) {
        val stroke = ArrayList<Point>()
        for (j in 0..jsonStroke.length() - 1) {
            val jsonPoint = jsonStroke.getJSONObject(j)
            val point = Point(jsonPoint.getDouble("x").toFloat(), jsonPoint.getDouble("y").toFloat())
            stroke.add(point)
        }
        listStrokes.add(stroke)
    }

    fun addStroke(stroke: ArrayList<Point>) {
        listStrokes.add(stroke)
    }

    fun addPointToLastStroke(point: Point) {
        listStrokes.last {
            //optimize point
            val lastPoint = it[it.size - 1]
            val dis: Double = sqrt(Math.pow(((lastPoint.x - point.x).toDouble()), 2.0) + Math.pow(((lastPoint.y - point.y).toDouble()), 2.0))
            if (dis < 0.03) return
//            Log.d("@@@", point.x.toString() + " " + point.y.toString() + "      " + dis.toString() + "       " + it.size.toString())
            it.add(point)
        }
    }

    fun scanLatestStoke(resolve: (Point) -> Unit){
        if (listStrokes.size > 0){
            val latestStroke = listStrokes[listStrokes.size - 1]
            latestStroke.forEach {
                resolve(it)
            }
        }
    }

    fun scanAllPoints(resolve: (Point, Boolean) -> Unit) {
        for (index in 0..listStrokes.size - 1) {
            val stroke = listStrokes[index]
            for ((jndex, currentPoint) in stroke.iterator().withIndex()) {
                resolve(currentPoint, jndex === 0)
            }
        }
    }

    fun lastStrokeToJSONArray(): JSONArray {
        var stroke = JSONArray()

        if (listStrokes.size !== 0) {
            var lastStroke = listStrokes[listStrokes.size - 1]
            for (point in lastStroke) {
                var obj = JSONObject()
                obj.put("x", point.x)
                obj.put("y", point.y)
                stroke.put(obj)
            }
        }
        return stroke;
    }

    fun toJSONArray(): JSONArray {
        var res = JSONArray()

        var stroke = JSONArray()

        scanAllPoints { point: Point, isFirstPointOfNewStroke: Boolean ->
            var obj = JSONObject()
            obj.put("x", point.x)
            obj.put("y", point.y)
            if (isFirstPointOfNewStroke) {
                if (stroke.length() !== 0) {
                    res.put(stroke)
                }
                stroke = JSONArray()
            }
            stroke.put(obj)
        }
        res.put(stroke)
        return res
    }
}
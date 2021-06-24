package com.example.canvas_android.model

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class Painting {
    var listStrokes = ArrayList<ArrayList<Point>>()

    constructor() {
    }

    constructor(jsonArray: JSONArray) {
        for (index in 0..jsonArray.length() - 1) {
            val jsonStroke: JSONArray = jsonArray.getJSONArray(index)
            val stroke = ArrayList<Point>()
            for (j in 0..jsonStroke.length() - 1) {
                val jsonPoint = jsonStroke.getJSONObject(j)
                val point = Point(jsonPoint.getDouble("x").toFloat(), jsonPoint.getDouble("y").toFloat())
                stroke.add(point)
            }
            listStrokes.add(stroke)
        }
    }

    fun addStroke(stroke: ArrayList<Point>) {
        listStrokes.add(stroke)
    }

    fun addPointToLastStroke(point: Point) {
        listStrokes.last {
            it.add(point)
        }
    }

    fun scanAllPoints(resolve: (Point, Boolean) -> Unit) {
        for (stroke in listStrokes) {
            for ((index, currentPoint) in stroke.iterator().withIndex()) {
                resolve(currentPoint, index === 0)
            }
        }
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
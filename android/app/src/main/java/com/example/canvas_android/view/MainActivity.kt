package com.example.canvas_android.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.canvas_android.R
import com.example.canvas_android.model.WS
import io.socket.client.Socket
import io.socket.emitter.Emitter

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
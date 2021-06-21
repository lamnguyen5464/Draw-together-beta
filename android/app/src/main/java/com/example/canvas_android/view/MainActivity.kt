package com.example.canvas_android.view

import android.app.Activity
import android.os.Bundle
import com.example.canvas_android.R
import com.example.canvas_android.model.WS
import java.net.URI

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WS.getIntance().uri = URI("http://192.168.1.6:3000/")
        WS.getIntance().doConnect()
    }
}
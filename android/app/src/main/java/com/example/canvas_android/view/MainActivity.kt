package com.example.canvas_android.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.canvas_android.R
import com.example.canvas_android.model.WS
import com.example.canvas_android.utils.ConfigHelper
import io.socket.client.Socket
import io.socket.emitter.Emitter

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()

        initSocket()

        setContentView(R.layout.activity_main)
    }

    override fun onRestart() {
        super.onRestart()
        initSocket()
    }

    override fun onResume() {
        super.onResume()
        initSocket()
    }

    private fun initSocket() {
        WS.getIntance().doConnect(ConfigHelper.getConfigValue(this, "socket_url"))

        WS.getIntance().socket.emit("request_join", "1")

        WS.getIntance().setEventListener(Socket.EVENT_CONNECT, Emitter.Listener {
            Log.d("@@@ EVENT_CONNECT", it.contentToString());
        })

        WS.getIntance().setEventListener(Socket.EVENT_DISCONNECT, Emitter.Listener {
            Log.d("@@@ EVENT_DISCONNECT", it.contentToString());
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        WS.getIntance().forceDisconnect()
    }
}
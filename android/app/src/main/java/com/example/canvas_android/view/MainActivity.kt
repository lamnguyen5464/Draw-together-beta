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

        WS.getIntance().doConnect()

        WS.getIntance().socket.emit("REQUEST_JOIN", "1")

        WS.getIntance().setEventListener("back", Emitter.Listener {
            Log.d("@@@ back", it.contentToString() + " " + WS.getIntance().id)
        })

        WS.getIntance().setEventListener(Socket.EVENT_CONNECT, Emitter.Listener {
            Log.d("@@@ EVENT_CONNECT", it.contentToString());
        })

        WS.getIntance().setEventListener(Socket.EVENT_DISCONNECT, Emitter.Listener {
            Log.d("@@@ EVENT_DISCONNECT", it.contentToString());
        })

    }
}
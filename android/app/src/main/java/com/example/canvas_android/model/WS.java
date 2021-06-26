package com.example.canvas_android.model;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WS {
    static private WS ws = null;
    public Socket socket = null;

    public static WS getIntance() {
        if (ws == null) {
            ws = new WS();
        }
        return ws;
    }

    public boolean isActive() {
        return this.socket.isActive();
    }

    public boolean isStillThere() {
        return (this.socket != null && this.socket.isActive() && this.socket.connected());
    }

    public String getID() {
        return isStillThere() ? this.socket.id() : "";
    }

    public void doConnect(String url) {
        if (this.socket == null) {
            try {
                this.socket = IO.socket(url);
                this.socket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.d("@@@ error", e.getMessage());
            }
        }
    }

    public void forceDisconnect() {
        Log.d("@@@", "force disconnect");
        this.socket.disconnect();
    }

    public void setEventListener(String eventName, Emitter.Listener emitter) {
        this.socket.on(eventName, emitter);
    }

}

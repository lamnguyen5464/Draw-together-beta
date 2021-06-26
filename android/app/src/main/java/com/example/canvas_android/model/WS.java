package com.example.canvas_android.model;

import android.util.Log;

import com.example.canvas_android.utils.Configs;

import java.net.URI;
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

    public boolean isStillThere(){
        return (this.socket != null && this.socket.isActive() && this.socket.connected());
    }

    public String getID() {
        return isStillThere() ? this.socket.id() : "";
    }

    public void doConnect() {
        if (this.socket == null) {
            try {
                this.socket = IO.socket(Configs.SOCKET_URI);
                this.socket.disconnect();
                this.socket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.d("@@@ error", e.getMessage());
            }
        }

    }

    public void setEventListener(String eventName, Emitter.Listener emitter) {
        this.socket.on(eventName, emitter);
    }

}

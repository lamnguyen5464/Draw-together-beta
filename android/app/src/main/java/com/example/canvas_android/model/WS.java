package com.example.canvas_android.model;

import android.util.Log;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;

public class WS {
    static private WS ws = null;
    private URI uri = null;
    public Socket socket = null;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;

        Log.d("@@@ uri", uri.getHost());

    }


    public static WS getIntance() {
        if (ws == null) {
            ws = new WS();
        }
        return ws;
    }

    public void doConnect() {
        if (this.socket == null) {
            Log.d("@@2", "in doConnect " + this.uri.getPath());
            this.socket = IO.socket(this.uri);
            this.socket.connect();
        }

    }

}

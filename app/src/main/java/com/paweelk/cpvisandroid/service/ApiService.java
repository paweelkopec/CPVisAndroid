package com.paweelk.cpvisandroid.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.paweelk.cpvisandroid.model.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 02.03.17.
 */

public class ApiService {

    private Server server;

    public ApiService(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public ApiService setServer(Server server) {
        this.server = server;
        return this;
    }

    public Bitmap getBitmapStream() throws IOException {
        //Date
        String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        //URL & open connection
        URL url = new URL(this.server.getUrl() + "/GetBitmapStream/" + date.toString());
        Log.i(getClass().toString(), "Request sending: "+ url.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("X-Requested-With", "Curl");
        //Authorization
        String authorization = this.server.getUsername() + ":" + this.server.getPassword();
        con.setRequestProperty("Authorization", authorization);
        //Send request and get Input
        InputStream in = con.getInputStream();
        Log.i(getClass().toString(), "HTTP Code: "+con.getResponseCode());
        if (con.getResponseCode() != 200) {
            throw new IOException("Server returned HTTP response code: " + con.getResponseCode());
        }else if(in ==null){
            throw new IOException("Server returned empty response ");
        }
        return BitmapFactory.decodeStream(in);
    }

    public void touched(int x, int y) throws IOException, JSONException {
        //JSON
        JSONObject request = new JSONObject();
        request.put("x", x).put("y", y);
        //URL & open connection
        URL url = new URL(this.server.getUrl() + "/Touched");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        //Authorization
        String auth = this.server.getUsername() + ":" + this.server.getPassword();
        con.setRequestProperty("Authorization", auth);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("charset", "utf-8");
        //Send request
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(request.toString());
        wr.flush();
        wr.close();
        //Get response code
        if (con.getResponseCode() != 200) {
            throw new IOException("Server returned HTTP response code: " + con.getResponseCode());
        }
    }
}


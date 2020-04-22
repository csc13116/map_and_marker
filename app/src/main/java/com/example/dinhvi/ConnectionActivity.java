package com.example.dinhvi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class ConnectionActivity extends AppCompatActivity {

    private Socket mSocket;
    TextView tvCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_code);

        tvCode = (TextView) findViewById(R.id.tv_secretCode);

        try {
            mSocket = IO.socket("https://socketio-temp.herokuapp.com/connect");
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.emit("parent wait", "5e932814d26d1d1d9c5cd034"); //Fixed ID for testing: 5e932814d26d1d1d9c5cd034, otherwise, change to userId
        mSocket.on("wait connect", onWaitConnect);

        mSocket.on("child connect", onChildConnect);
    }

    private Emitter.Listener onWaitConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String code = object.getString("connectionString");
                        tvCode.setText(code);

                        //-------------- This is for testing, delete when done
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                connectToMap(); //Change to confirmChildScreen() for testing without child-side
                            }
                        }, 10000);   //10 seconds

                        //------------------
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onChildConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    mSocket.emit("both connect", "connectionString");
                    //connectToMap(); //Enable once child-side is completed
                }
            });
        }
    };

    public void confirmChildScreen() {
        Intent intent = new Intent(this, ConfirmChild.class);
        startActivity(intent);
    }

    public void connectToMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("Disconnect from Socket Server!");
    }
}


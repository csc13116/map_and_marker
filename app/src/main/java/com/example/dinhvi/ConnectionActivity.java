package com.example.dinhvi;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

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
            mSocket = IO.socket("https://dacnpm-backend.herokuapp.com/connect");
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

                        /*
                        //-------------- This is for testing, delete when done
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                confirmChildScreen(); //Change to confirmChildScreen() for testing without child-side
                            }
                        }, 7000);   //x seconds

                        //------------------
                         */
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
                    try {
                        String childID = object.getString("connect");
                        confirmChildScreen(); //Enable once child-side is completed
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void confirmChildScreen() {
        Intent intent = new Intent(this, ConfirmChild.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("Disconnect from Socket Server!");
    }
}


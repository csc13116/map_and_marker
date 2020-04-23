package com.example.dinhvi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class ChildConnectionActivity extends AppCompatActivity {

    private Socket mSocket;
    EditText connectionCode;
    TextView wrongCodeAnnounce;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_connect);

        connectionCode = findViewById(R.id.ed_secretCode);
        wrongCodeAnnounce = findViewById(R.id.tv_announceWrongCode);

        try {
            mSocket = IO.socket("https://socketio-temp.herokuapp.com/connect");
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.emit("child wait", "connectionString");
        mSocket.on("found", onFoundConnect);
    }

    private Emitter.Listener onFoundConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String secretCode = object.getString("connectionString");
                        String inputCode = connectionCode.getText().toString();
                        if (secretCode == inputCode)
                        {
                            onBothPartiesConnect();
                        }
                        else
                        {
                            wrongCodeAnnounce.setText("Mật mã sai, xin hãy nhập lại mật mã");
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void onBothPartiesConnect() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}

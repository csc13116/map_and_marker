package com.example.dinhvi;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmChild extends AppCompatActivity {

    EditText etNameChild;
    Button btnChildConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_child);

        etNameChild =  findViewById(R.id.et_childName);
        btnChildConfirm = findViewById(R.id.btn_childConfirm);


        btnChildConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String childName = etNameChild.getText().toString();
                Intent intent = new Intent(ConfirmChild.this, MapsActivity.class);
                intent.putExtra("CHILD_NAME", childName);
                startActivity(intent);
            }
        });

    }
}

package com.example.datatelekommapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText eName;
    private EditText ePass;
    private Button eLogin;
    private TextView eLoginInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eName = findViewById(R.id.etUsername);
        ePass = findViewById(R.id.etPassword);
        eLogin = findViewById(R.id.btnLogin);
        eLoginInfo = findViewById(R.id.btnLogin);

        eLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String inputUser = eName.getText().toString();
                String inputPass = ePass.getText().toString();

                // Todo: Create more thorough password checking criteria (no spaces etc)
                if(inputUser.isEmpty() || inputPass.isEmpty()){
                    Toast.makeText(this, "Please enter a ");
                }

            }
        });
    }
}
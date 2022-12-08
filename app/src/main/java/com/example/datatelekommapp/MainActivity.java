package com.example.datatelekommapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity {

    private EditText eChannel;
    private EditText eWriteKey;
    private Button eLogin;
    private TextView eLoginInfo;
    private Connection myCon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Prepares on-screen elements for use

        eChannel = findViewById(R.id.etChannelNr);
        eWriteKey = findViewById(R.id.etChannelWriteKey);
        eLogin = findViewById(R.id.btnLogin);
        eLoginInfo = findViewById(R.id.btnLogin);
        myCon = new Connection();


        eLogin.setOnClickListener(view -> {

            // Gets the values from the on screen fields
            myCon.setChannel(eChannel.getText().toString());
            myCon.setWriteKey(eWriteKey.getText().toString());

            // Sends a request for the auth field in Thingspeak and sets the result for later use
            try {
                myCon.request('4');
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (myCon.getChannel().isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a valid user/password", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Sets the status to a dummy variable which is impossible
                    myCon.setStatus(4, 'x');
                    myCon.auth();

                    // Prevent instant error message
                    while (myCon.getStatus(4) == 'x') {
                        System.out.println("Waiting...");
                    }

                    // If unexpected response from channel
                    if (!myCon.verify('8', 4)) {

                        Toast.makeText(MainActivity.this, "Incorrect channel number", Toast.LENGTH_SHORT).show();

                    }
                    //Code is correct and the ESP will send a signal out to unlock
                    else {
                        eLoginInfo.setText("Login successful");
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        //Code to go to new activity and bring the input with
                        Intent intent = new Intent(MainActivity.this, Homepageactivity.class);
                        intent.putExtra("writeKey", myCon.getWriteKey());
                        intent.putExtra("channel", eChannel.getText().toString());
                        startActivity(intent);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });
    }


}
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

        eChannel = findViewById(R.id.etChannelNr);
        eWriteKey = findViewById(R.id.etChannelWriteKey);
        eLogin = findViewById(R.id.btnLogin);
        eLoginInfo = findViewById(R.id.btnLogin);
        myCon = new Connection();




        eLogin.setOnClickListener(view -> {

            myCon.setChannel(eChannel.getText().toString());
            myCon.setWriteKey(eWriteKey.getText().toString());

            try {
                myCon.request('4');
            } catch (Exception e) {
                e.printStackTrace();
            }


            if(myCon.getChannel().isEmpty()){
                Toast.makeText(MainActivity.this, "Please enter a valid user/password", Toast.LENGTH_SHORT).show();
            }

            else{
                try {
                    myCon.setStatus(4,'x');
                    myCon.auth();

                    while(myCon.getStatus(4) == 'x'){
                        System.out.println("Waiting...");
                    }

                    if(!myCon.verify('8',4)){

                        Toast.makeText(MainActivity.this, "Incorrect channel number", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        eLoginInfo.setText("Login successful");
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // code to go to new activity
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
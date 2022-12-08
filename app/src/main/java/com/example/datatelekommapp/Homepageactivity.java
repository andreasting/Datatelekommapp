package com.example.datatelekommapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Homepageactivity extends AppCompatActivity {
    private TextView eRFID;
    private TextView eAuth;
    private Connection myCon;
    private Button eRefreshButton;
    private Button eAuthButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepageactivity);
        myCon = new Connection();

        //Prepares on screen elements

        eRFID = findViewById(R.id.textRFID);
        eAuth = findViewById(R.id.textAuthreq);
        eRefreshButton = findViewById(R.id.btnRefresh);
        eAuthButton = findViewById((R.id.btnAuth));

        //Fetches values from last screen for use here
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
        myCon.setWriteKey(bundle.getString("writeKey"));
        myCon.setChannel(bundle.getString("channel"));
        }

       refreshValues();

        //Fetches new values and refreshes
        eRefreshButton.setOnClickListener(view ->{
            refreshValues();
            refreshUI();

        });

        eAuthButton.setOnClickListener(view ->{
            // Dummy code so the app doesn't immediately say not successful
            myCon.setLastCode("-100");
            myCon.auth();

            while(myCon.getLastCode().equals("-100")){
                System.out.println("Waiting...");
            }
            System.out.println("Last code:" + myCon.getLastCode());

            Toast.makeText(Homepageactivity.this, "Authentication code: " + myCon.getLastCode(), Toast.LENGTH_SHORT).show();


            });
        };


    // Changes the text of a selected textvew
    protected void fillText(char status, String module,TextView text){
        text.setText(getMessage(status, module));
    }

    // Based on server response - prepares a message to display
    protected String getMessage(char status, String module){
        String toSend = module + " status: ";
        System.out.println("Module status " + module + " : " + status);
        if(status == '0'){
            toSend += " inactive ";
        }
        else if(status == '1'){
            toSend += " active";
        }
        else{
            toSend = "ERROR: COULDN'T FETCH " + module + " STATUS";
        }
        return toSend;
    }

    //Refreshes v alues to be on screen
    protected void refreshValues(){

        for(int i = 2; i<4; i++){
            try {
                char field = Character.forDigit(i, 10);
                myCon.request(field);
                System.out.println("Testing function");
                System.out.println(field);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Refreshes the on-screen UI
    protected void refreshUI(){
        fillText(myCon.getStatus(2),"RFID",eRFID);
        fillText(myCon.getStatus(3),"Authenticator", eAuth);
        System.out.println(myCon.getStatus(3));
        System.out.println("Refresh status: " + myCon.getStatus(2));
        System.out.println("Refresh status: " + myCon.getStatus(3));


    }
}


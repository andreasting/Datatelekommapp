package com.example.datatelekommapp;

import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class Connection {
    private final OkHttpClient client = new OkHttpClient();
    private char[] status = new char[5];
    private String channel;
    private String writeKey;
    private String code = "0";


    public void request(char field) throws Exception {

// Prepares and builds a OkHttp3 request
        Request request = new Request.Builder()
                .url("https://api.thingspeak.com/channels/" + getChannel() + "/fields/" + field + ".csv?api_key=" + getWriteKey() + "&results=8000")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    // Processes the CSV response from the server
                    String body = responseBody.string() + " end";
                    System.out.println("Body is: " + body);
                    char gotStatus = getValue(body);

                    // Prints and sets the last status received for the appropriate field
                    System.out.println("Status: " + gotStatus);
                    setStatus(Integer.parseInt(String.valueOf(field)), gotStatus);
                    getStatus(Integer.parseInt(String.valueOf(field)));
                    System.out.println("Field = " + String.valueOf(field));

                    // Shows the status of each field, for debugging
                    for (char c : status) {
                        System.out.println(c);
                    }


                }
            }
        });
    }

    //Attempts to authenticate and sets code, if the code is 8, authentication succeeded
    public void auth() {

        // Builds a url to request for authentication. In the thingspeak API, updates can be done
        // with HTTP fetch, which is what is used here
        Request request = new Request.Builder()
                .url("https://api.thingspeak.com/update?api_key=" + writeKey + "&field3=1")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Gets and sets the last response code
                try (ResponseBody ResponseBody = response.body()) {
                    code = String.valueOf(response.code());
                    setLastCode(code);

                }


            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Code = " + code);
    }


    // Method to get the current value of a field. Processes the CSV response in string format
    public char getValue(String toSearch) {

        char status = '!';
        for (int i = 0; i < toSearch.length(); i++) {
            char c = toSearch.charAt(i);
            char c2 = '!';
            if (i < toSearch.length() - 2) {
                c2 = toSearch.charAt(i + 1);
            }

            // Data follows the format 2022-12-02 10:23:35 UTC,102,8. First the timezone comes, then the
            // entry number, and then the data. If a C precedes a comma it is not the status field
            if (c == ',' && toSearch.charAt(i - 1) != 'C') {

                // The only possible statuses
                if (c2 == '1' || c2 == '0' || c2 == '8') {
                    status = toSearch.charAt(i + 1);
                }


            }

        }
        return status;


    }


    public boolean verify(char compareTo, int i) {
        return (getStatus(i) == compareTo);
    }

    public String getLastCode() {
        return code;
    }

    public void setLastCode(String code) {
        this.code = code;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getWriteKey() {
        return this.writeKey;
    }

    public void setWriteKey(String writeKey) {
        this.writeKey = writeKey;
    }

    public char getStatus(int index) {
        return status[index];
    }

    public void setStatus(int index, char status) {
        this.status[index] = status;
    }


}

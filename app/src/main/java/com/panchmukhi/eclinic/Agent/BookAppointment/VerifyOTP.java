package com.panchmukhi.eclinic.Agent.BookAppointment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.panchmukhi.eclinic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VerifyOTP extends AppCompatActivity {

    Button verifyotp;

    RequestQueue requestQueue;
    int randonnumber;
    String phonenumber="9955431261";


    private static final String TAG = "VerifyOTP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        verifyotp=(Button)findViewById(R.id.verifyotp);

        requestQueue = Volley.newRequestQueue(VerifyOTP.this);

        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    // Construct data
                    String apiKey = "apikey=" + "NmU0ODY3NGE1NTVhN2E3YTRiNjM0ODU3NTY0MTc0MzM=";
                    Random random = new Random();
                    randonnumber = random.nextInt(99999);
                    String message = "&message=" + "Hey, Your OTP is " + randonnumber;
                    String sender = "&sender=" + "TXTLCL";
                    String numbers = "&numbers=" + phonenumber;
                    // Send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
                    String data = apiKey + numbers + message + sender;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        Toast.makeText(VerifyOTP.this, line.toString(), Toast.LENGTH_SHORT).show();
//                        stringBuffer.append(line);
                    }
                    rd.close();
                } catch (Exception e) {
                    System.out.println("Error SMS " + e);
                }


            }
        });

        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }




    void setData() {


        // Takes the response from the JSON request
        // Handles errors that occur due to Volley
        JsonArrayRequest arrayreq = new JsonArrayRequest("http://sms.shubhsandesh.in/http-key-api.php?apikey=QaKAXuLXWisBkCb3&senderid=SUKRCA&number=9955431261&message=Dear Student, your admission is granted and user id 9955431261 and password 123 .(Sukrishna Commerce) Thanks.",
                response -> {

                    Log.d(TAG, "onResponse: ");

                    try {

                        for (int i = 0; i < response.length(); i++) {
                            //gets each JSON object within the JSON array
                            JSONObject jsonObject = response.getJSONObject(i);

                            // Retrieves the string labeled "colorName" and "hexValue",
                            // and converts them into javascript objects
//                                String email = jsonObject.getString("user");

//                                if (email.equals(SessionManager.getInstance(HomePage.this).getUserEmail())) {
//
//                                    jsonObject1 = jsonObject;
//                                }

                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Log.e("Volley", "Error")
        );
        requestQueue.add(arrayreq);
    }
}
package com.panchmukhi.eclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.panchmukhi.eclinic.Registration.Login.Login;
import com.panchmukhi.eclinic.Registration.Login.LoginDashBoard;

public class SplashScreen extends AppCompatActivity {

    ImageView img;
    TextView hTextView,hTextView1,txt3,txt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img = findViewById(R.id.img);

        rotate_Clockwise(img);


        hTextView =  findViewById(R.id.txt);
        hTextView.setText("\"अब हर गांव में डॉक्टर ");

        hTextView1 = findViewById(R.id.txt1);
        hTextView1.setText("हर मोबाइल में डॉक्टर \"");

        txt3 = findViewById(R.id.txt3);
        txt3.setText("न यात्रा, ना इंतजार\"");

        txt4 = findViewById(R.id.txt4);
        txt4.setText("तुरंत डॉक्टर से मुलाकात \"");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginDashBoard.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    public void rotate_Clockwise(View view) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 180f, 0f);
//       rotate.setRepeatCount(10);
        rotate.setDuration(3000);
        rotate.start();
    }
}

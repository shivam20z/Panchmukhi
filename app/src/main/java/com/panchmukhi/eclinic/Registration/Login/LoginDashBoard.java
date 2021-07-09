package com.panchmukhi.eclinic.Registration.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.panchmukhi.eclinic.Agent.Homepage.Home;
import com.panchmukhi.eclinic.R;

public class LoginDashBoard extends AppCompatActivity {

    RelativeLayout layoutAgent,layoutPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dash_board);

        layoutAgent=findViewById(R.id.layoutAgent);
        layoutPatient=findViewById(R.id.layoutPatient);


        layoutPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginDashBoard.this, Home.class);
                startActivity(intent);
            }
        });

        layoutAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginDashBoard.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
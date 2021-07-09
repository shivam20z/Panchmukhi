package com.panchmukhi.eclinic.Agent.BookAppointment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panchmukhi.eclinic.R;
import com.panchmukhi.eclinic.Utils;

import es.dmoral.toasty.Toasty;

public class OtpVerification extends AppCompatActivity {

    TextView tvTime, tvReSend;
    Button btnOtp;
    EditText etPhoneNumber, etOtpNo;
    int verificationCounter = 0,otpNumber;
    RelativeLayout layoutTimmer;
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        tvTime = findViewById(R.id.tvTime);
        tvReSend = findViewById(R.id.tvReSend);
        btnOtp = findViewById(R.id.btnOtp);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        layoutTimmer = findViewById(R.id.layoutTimmer);
        etOtpNo = findViewById(R.id.etOtpNo);
        ibBack = findViewById(R.id.ibBack);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificationCounter==0){

                if (etPhoneNumber.getText().toString().trim().length() == 10) {
                    if (Utils.isNetworkConnectionAvailable(OtpVerification.this)) {
                        sendOtp();
                        layoutTimmer.setVisibility(View.VISIBLE);
                        btnOtp.setText("Verify");
                        verificationCounter++;
                        startTimer();
                        etPhoneNumber.setVisibility(View.GONE);
                        etOtpNo.setVisibility(View.VISIBLE);
                        etOtpNo.setVisibility(View.VISIBLE);


                    } else {
                        Utils.checkNetworkConnection(OtpVerification.this);
                    }

                } else {

                    Toasty.error(OtpVerification.this, "Please Enter Phone Number Properly.", Toast.LENGTH_SHORT, true).show();
                }
                }else {

                    if (Integer.parseInt(etOtpNo.getText().toString().trim())==otpNumber){

                    }else {
                        Toasty.error(OtpVerification.this, "Wrong OTP", Toast.LENGTH_SHORT, true).show();

                    }


                }


            }
        });


        tvReSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendOtp();
                tvReSend.setVisibility(View.GONE);
                startTimer();
            }
        });


    }

    private void sendOtp() {
    }

    private void startTimer() {

        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTime.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvTime.setText("00:00");
                tvReSend.setVisibility(View.VISIBLE);
            }

        }.start();
    }


}

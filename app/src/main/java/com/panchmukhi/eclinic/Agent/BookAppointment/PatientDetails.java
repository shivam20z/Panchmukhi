package com.panchmukhi.eclinic.Agent.BookAppointment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;

import com.panchmukhi.eclinic.R;

public class PatientDetails extends AppCompatActivity {

    Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
    }
}
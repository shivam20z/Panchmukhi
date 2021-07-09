package com.panchmukhi.eclinic.Doctor.ExploreAppointment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.ncorti.slidetoact.SlideToActView;
import com.panchmukhi.eclinic.R;

public class ExploreAppointmentDetails extends AppCompatActivity {

    TextView orderNumber,date,time,name,age,gender,locality,description;
    SlideToActView callPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_appointment_details);

        orderNumber=findViewById(R.id.orderNumber);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        name=findViewById(R.id.name);
        age=findViewById(R.id.age);
        gender=findViewById(R.id.gender);
        locality=findViewById(R.id.locality);
        description=findViewById(R.id.description);
        callPatient=findViewById(R.id.callPatient);


        orderNumber.setText(getIntent().getStringExtra("doctorOrderNumber"));
        date.setText(getIntent().getStringExtra("appointmentDate"));
        time.setText(getIntent().getStringExtra("appointmentTime"));
        name.setText(getIntent().getStringExtra("patientName"));
        age.setText(getIntent().getStringExtra("patientAge"));
        gender.setText(getIntent().getStringExtra("patientGender"));
        locality.setText(getIntent().getStringExtra("Locality"));
        description.setText(getIntent().getStringExtra("Description"));


    }
}
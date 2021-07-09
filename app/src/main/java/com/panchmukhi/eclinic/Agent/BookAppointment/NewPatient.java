package com.panchmukhi.eclinic.Agent.BookAppointment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.panchmukhi.eclinic.Agent.AllAppointment.AppointmentList;
import com.panchmukhi.eclinic.Notification.FcmNotificationsSender;
import com.panchmukhi.eclinic.R;
import com.panchmukhi.eclinic.Registration.SignUp.DoctorRegister;
import com.panchmukhi.eclinic.Registration.SignUp.MainSpinnerAdapter;
import com.panchmukhi.eclinic.Registration.SignUp.SpinnerParentItemModel;
import com.panchmukhi.eclinic.Test;
import com.panchmukhi.eclinic.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class NewPatient extends Fragment {

    final Calendar myCalendar = Calendar.getInstance();
    StringBuffer result = new StringBuffer();
    Spinner genderSpinner;
    List<SpinnerParentItemModel> genderList;
    private MainSpinnerAdapter genderAdapter;
    String strGender;
    Dialog loadingDialog;
    int doctorOrderNo, agentOrderNo;
    Button btDate, btnBookAppointment;
    EditText etName, etAge, etMobile, etAddress, etLocality, etLandmark, etCity, etState, etCountry, etPincode, etDescription;
    AlertDialog.Builder builder;
    Date getDate,getTime;
    String appointmentDate,appointmentTime;

    public NewPatient() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_patient, container, false);


        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getContext().getDrawable(R.drawable.rounded_border));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        genderList = addGender();
        genderSpinner = view.findViewById(R.id.genderSpinner);
        btDate = view.findViewById(R.id.btDate);
        etMobile = view.findViewById(R.id.etMobile);
        btnBookAppointment = view.findViewById(R.id.btnBookAppointment);

        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etAddress = view.findViewById(R.id.etAddress);
        etLocality = view.findViewById(R.id.etLocality);
        etLandmark = view.findViewById(R.id.etLandmark);
        etCity = view.findViewById(R.id.etCity);
        etState = view.findViewById(R.id.etState);
        etCountry = view.findViewById(R.id.etCountry);
        etPincode = view.findViewById(R.id.etPincode);
        etDescription = view.findViewById(R.id.etDescription);

        genderAdapter = new MainSpinnerAdapter(getContext(), R.layout.custom_spinner_main_item, genderList);

        genderSpinner.setAdapter(genderAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    strGender = "nothing";
                } else if (position == 1) {
                    strGender = "Male";
                } else if (position == 2) {
                    strGender = "Female";
                } else if (position == 3) {
                    strGender = "Other";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                try {
                    btDate.setText(null);
                    result.setLength(0);
                    updateLabel();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Book Appointment");
        builder.setMessage("If you collected the 120 rupee from patient then click \"Yes\" to book this appointment.");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Utils.isNetworkConnectionAvailable(getContext())) {
                    loadingDialog.show();
                    addAppointment();
                } else {
                    Utils.checkNetworkConnection(getContext());
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
         getDate = new Date();
//        Toast.makeText(getContext(), formatter.format(appointmentDate), Toast.LENGTH_SHORT).show();
        appointmentDate=String.valueOf(formatter.format(getDate));


        SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
         getTime = new Date();
//        Toast.makeText(getContext(), formatter1.format(appointmentTime), Toast.LENGTH_SHORT).show();
        appointmentTime=String.valueOf(formatter1.format(getTime));


        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (etName.getText().toString().isEmpty()) {
//                    etName.setError("Fill This.");
//                } else if (etAge.getText().toString().isEmpty()) {
//                    etAge.setError("Fill This.");
//                } else if (etAddress.getText().toString().isEmpty()) {
//                    etAddress.setError("Fill This.");
//                } else if (etMobile.getText().toString().isEmpty()) {
//                    etMobile.setError("Fill This.");
//                } else if (etLocality.getText().toString().isEmpty()) {
//                    etLocality.setError("Fill This.");
//                } else if (etLandmark.getText().toString().isEmpty()) {
//                    etLandmark.setError("Fill This.");
//                } else if (etCity.getText().toString().isEmpty()) {
//                    etCity.setError("Fill This.");
//                } else if (etState.getText().toString().isEmpty()) {
//                    etState.setError("Fill This.");
//                } else if (etCountry.getText().toString().isEmpty()) {
//                    etCountry.setError("Fill This.");
//                } else if (etPincode.getText().toString().isEmpty()) {
//                    etPincode.setError("Fill This.");
//                } else if (btDate.getText().toString().equals("Set Date Of Birth")) {
//                    Toasty.error(getContext(), "Select Date Of Birth.", Toast.LENGTH_SHORT, true).show();
//                } else if (strGender.equals("elect Gender")) {
//                    Toasty.error(getContext(), "Select Gender", Toast.LENGTH_SHORT, true).show();
//                } else {
//
//                    builder.show();
//                }


            }
        });


        return view;
    }


    private void updateLabel() throws ParseException {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        result.append(sdf.format(myCalendar.getTime()));

        btDate.setText(result.toString());
    }

    private List<SpinnerParentItemModel> addGender() {

        List<SpinnerParentItemModel> list = new ArrayList<>();
        list.add(new SpinnerParentItemModel(R.drawable.gender, "Select Gender"));
        list.add(new SpinnerParentItemModel(R.drawable.gender, "Male"));
        list.add(new SpinnerParentItemModel(R.drawable.gender, "Female"));
        list.add(new SpinnerParentItemModel(R.drawable.gender, "Other"));

        return list;
    }


    void addAppointment() {

        String uniqueID = UUID.randomUUID().toString();


        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", etName.getText().toString());
        map.put("Age", etAge.getText().toString());
        map.put("Number", etMobile.getText().toString());
        map.put("Address", etAddress.getText().toString());
        map.put("Locality", etLocality.getText().toString());
        map.put("Landmark", etLandmark.getText().toString());
        map.put("City", etCity.getText().toString());
        map.put("State", etState.getText().toString());
        map.put("Country", etCountry.getText().toString());
        map.put("Pincode", etPincode.getText().toString());
        map.put("Gender", strGender);
        map.put("DOB", btDate.getText().toString());
        map.put("PatientUid", uniqueID + etMobile.getText().toString().trim());
        map.put("UserUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("DoctorUid", getActivity().getIntent().getStringExtra("doctorUid"));


        FirebaseDatabase.getInstance().getReference().child("PatientDetails").child(uniqueID + etMobile.getText().toString().trim()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isComplete()) {

                    if (etDescription.getText().toString().isEmpty()) {
                        map.put("Description", "$");
                    } else {
                        map.put("Description", etDescription.getText().toString());
                    }
                    map.put("UniqueCallingId", UUID.randomUUID().toString());
                    FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Doctor")
                            .child(getActivity().getIntent().getStringExtra("doctorUid")).child("DoctorAppointmentCounter").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            doctorOrderNo = Integer.parseInt(snapshot.getValue().toString());
                            map.put("DoctorOrderNumber", doctorOrderNo);


                            FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Agent").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("AgentAppointmentCounter").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    agentOrderNo = Integer.parseInt(snapshot.getValue().toString());
                                    map.put("AgentOrderNumber", agentOrderNo);
                                    map.put("AppointmentDate", appointmentDate);
                                    map.put("AppointmentTime", appointmentTime);

                                    //add order in doctor
                                    FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Doctor")
                                            .child(getActivity().getIntent().getStringExtra("doctorUid")).child("Appointment")
                                            .child(uniqueID + etMobile.getText().toString().trim()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            //add order in agent
                                            FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Agent")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Appointment")
                                                    .child(uniqueID + etMobile.getText().toString().trim()).setValue(map);

                                            FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Doctor").child(getActivity().getIntent().getStringExtra("doctorUid")).child("DoctorAppointmentCounter").setValue(doctorOrderNo + 1);
                                            FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Agent").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("AgentAppointmentCounter").setValue(agentOrderNo + 1);

                                            FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(getActivity().getIntent().getStringExtra("userNotificationToken"),"Alert","You got a new appointment.",getContext(), getActivity());
                                            fcmNotificationsSender.SendNotifications();


                                            loadingDialog.dismiss();
                                            Intent intent = new Intent(getContext(), AppointmentList.class);
                                            startActivity(intent);
                                            Toasty.success(getContext(), "Appointment Booked", Toasty.LENGTH_SHORT, true).show();

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    loadingDialog.dismiss();
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            loadingDialog.dismiss();
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {

                    Toast.makeText(getContext(), "Something Went Wrong !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
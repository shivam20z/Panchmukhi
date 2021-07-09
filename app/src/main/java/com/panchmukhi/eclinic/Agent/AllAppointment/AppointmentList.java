package com.panchmukhi.eclinic.Agent.AllAppointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.panchmukhi.eclinic.Agent.DoctorList.DoctorListAdapter;
import com.panchmukhi.eclinic.Agent.DoctorList.DoctorListModel;
import com.panchmukhi.eclinic.Agent.DoctorList.DoctorListRv;
import com.panchmukhi.eclinic.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppointmentList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<AppointmentListModel> list;
    AppointmentListAdapter adapter;
    ImageButton back;
    Dialog loadingDialog;
    LinearLayout noData;
    SwipeRefreshLayout swipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        recyclerView=findViewById(R.id.rvAppointment);
        back = findViewById(R.id.back);
        noData = findViewById(R.id.noData);
        list = new ArrayList<>();

//        swipLayout = findViewById(R.id.swipe_layout);
//        swipLayout.setOnRefreshListener(AppointmentList.this);
//
//        swipLayout.setColorSchemeResources(R.color.black,
///                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.rounded_border));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        adapter = new AppointmentListAdapter(list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        loadingDialog.show();
        setData();

    }

    @Override
    public void onRefresh() {
        //your refresh code here
        setData();
    }


    void setData() {


        FirebaseDatabase.getInstance().getReference().child("AllUserDetails").
                child("Agent").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Appointment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    list.clear();
                    noData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
//                    swipLayout.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        list.add(new AppointmentListModel(
                                Objects.requireNonNull(dataSnapshot1.child("Address").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("City").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Country").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("DOB").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Gender").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Landmark").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Locality").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Name").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Number").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Pincode").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("State").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("PatientUid").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("DoctorUid").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("UserUid").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("UniqueCallingId").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Age").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("AppointmentDate").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("AppointmentTime").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("DoctorOrderNumber").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("AgentOrderNumber").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Description").getValue()).toString(),
                                Integer.parseInt(dataSnapshot1.child("AgentOrderNumber").getValue().toString())
                        ));
                        adapter.notifyDataSetChanged();

                    }
                } else {
//                    swipLayout.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
//                swipLayout.setRefreshing(false);
                loadingDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                loadingDialog.show();
                Toast.makeText(AppointmentList.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
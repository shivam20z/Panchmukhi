package com.panchmukhi.eclinic.Doctor.DoctorDashBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.panchmukhi.eclinic.Agent.AllAppointment.AppointmentListAdapter;
import com.panchmukhi.eclinic.Agent.AllAppointment.AppointmentListModel;
import com.panchmukhi.eclinic.R;
import com.panchmukhi.eclinic.Registration.Login.Login;
import com.panchmukhi.eclinic.SessionManager;
import com.panchmukhi.eclinic.Utils;

import android.app.Dialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class DoctorHomepage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ImageView navUserPhoto;
    SwitchCompat swtOnline;
    NavigationView navigationView;
    com.airbnb.lottie.LottieAnimationView fab;
    LinearLayout settings;
    TextView navUsername, textViewEmail;
    RecyclerView recyclerView;
    List<AppointmentListModel> list;
    AppointmentListDoctorAdapter adapter;
    RelativeLayout noData;
    SwipeRefreshLayout swipLayout;
    Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_homepage);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.rounded_border));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        swtOnline = findViewById(R.id.swtOnline);
        settings = findViewById(R.id.settings);
        recyclerView = findViewById(R.id.recyclerviewDelivery);
        noData = findViewById(R.id.noData);
        list = new ArrayList<>();

        updateNavHeader();



        swipLayout = findViewById(R.id.swipe_layout);
        swipLayout.setOnRefreshListener(this);

        swipLayout.setColorSchemeResources(R.color.black,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        adapter = new AppointmentListDoctorAdapter(list, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


        swtOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (Utils.isNetworkConnectionAvailable(DoctorHomepage.this)) {
//                    swtOnline.setClickable(true);
                    if (isChecked) {
                        changeOnlineStatus(1);
                    } else {
                        changeOnlineStatus(0);
                    }
                } else {
//                    swtOnline.setClickable(false);
                    Utils.checkNetworkConnection(DoctorHomepage.this);
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnectionAvailable(DoctorHomepage.this)) {
                    if (SessionManager.getInstance(DoctorHomepage.this).getIsLogin()) {
                        SessionManager.getInstance(DoctorHomepage.this).setIsLogin(false);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(DoctorHomepage.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DoctorHomepage.this, "Login Please !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Utils.checkNetworkConnection(DoctorHomepage.this);
                }

            }
        });

    }

    @Override
    public void onRefresh() {
        //your refresh code here
        setRecyclerViewData();
    }


    public void setRecyclerViewData() {
       loadingDialog.show();
        FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Doctor").
                child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).
                child("Appointment").orderByChild("DoctorOrderNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    swipLayout.setVisibility(View.VISIBLE);
                    list.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
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
                    swipLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
                swipLayout.setRefreshing(false);
                loadingDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.dismiss();


                Toast.makeText(DoctorHomepage.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    void changeOnlineStatus(int value) {
        Map<String, Object> map = new HashMap<>();
        map.put("OnlineStatus", value);

        FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    if (value == 1) {
                        Toasty.success(DoctorHomepage.this, "Online!", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(DoctorHomepage.this, "Offline.", Toast.LENGTH_SHORT, true).show();

                    }
                }

            }
        });
    }

    void checkPreviousData() {

        loadingDialog.show();
        FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("OnlineStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    int value = Integer.parseInt(snapshot.getValue().toString());
                    if (value == 1) {
                        Toasty.success(DoctorHomepage.this, "Online!", Toast.LENGTH_SHORT, true).show();
                        swtOnline.setChecked(true);
                        loadingDialog.dismiss();
                    } else {
                        swtOnline.setChecked(false);
                        loadingDialog.dismiss();

                    }

                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(DoctorHomepage.this, "Something Went Wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.dismiss();
                Toast.makeText(DoctorHomepage.this, "Something Went Wrong !", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        navUsername = headerView.findViewById(R.id.textViewName);
        textViewEmail = headerView.findViewById(R.id.textViewEmail);
        navUserPhoto = headerView.findViewById(R.id.imageView);


        navUsername.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        textViewEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        Glide.with(getApplicationContext())
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.user1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .into(navUserPhoto);

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPreviousData();

        setRecyclerViewData();
    }
}
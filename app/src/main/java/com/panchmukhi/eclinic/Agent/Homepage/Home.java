package com.panchmukhi.eclinic.Agent.Homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.panchmukhi.eclinic.Agent.AboutUs.AboutUsPage;
import com.panchmukhi.eclinic.Agent.AllAppointment.AppointmentList;
import com.panchmukhi.eclinic.Agent.DoctorList.DoctorListRv;
import com.panchmukhi.eclinic.Doctor.DoctorDashBoard.DoctorHomepage;
import com.panchmukhi.eclinic.R;
import com.google.android.material.navigation.NavigationView;
import com.panchmukhi.eclinic.Registration.Login.Login;
import com.panchmukhi.eclinic.SessionManager;
import com.panchmukhi.eclinic.Utils;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Home extends AppCompatActivity {

    List<SliderItem> sliderItems;
    NavigationView navigationView;
    RecyclerView rvCommonSpecialist, rvCommonSymptoms;
    List<CommonSpecialistModel> commonSpecialistModels;
    CommonSpecialistAdapter commonSpecialistAdapter;
    CommonSymptomsAdapter commonSymptomsAdapter;
    List<CommonSymptomsModel> commonSymptomsModels;
    LinearLayout layoutAboutUs, appointmentPage,searchDoctor,settings;
    SliderAdapterExample sliderAdapter;
    Dialog loadingDialog;


    com.airbnb.lottie.LottieAnimationView fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sliderItems = new ArrayList<>();
        commonSpecialistModels = new ArrayList<>();
        commonSymptomsModels = new ArrayList<>();

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.rounded_border));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderAdapter = new SliderAdapterExample(this, sliderItems);
        sliderView.setSliderAdapter(sliderAdapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();
        sliderView.setScrollTimeInMillis(3000);

        rvCommonSpecialist = findViewById(R.id.rvCommonSpecialist);
        rvCommonSymptoms = findViewById(R.id.rvCommonSymptoms);
        layoutAboutUs = findViewById(R.id.layoutAboutUs);
        appointmentPage = findViewById(R.id.appointmentPage);
        searchDoctor = findViewById(R.id.searchDoctor);
        settings = findViewById(R.id.settings);

        rvCommonSpecialist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvCommonSpecialist);
        // pager indicator
        rvCommonSpecialist.addItemDecoration(new LinePagerIndicatorDecoration(Home.this));

        commonSpecialistModels.add(new CommonSpecialistModel(R.drawable.cardiologist, "Cardiologist","???????????? ????????? ????????????????????????"));
        commonSpecialistModels.add(new CommonSpecialistModel(R.drawable.dentist, "Dentist","????????? ????????????????????????"));
        commonSpecialistModels.add(new CommonSpecialistModel(R.drawable.dermatologist, "Dermatologist","??????????????? ????????????????????????"));
        commonSpecialistModels.add(new CommonSpecialistModel(R.drawable.otolaryngologist, "Otolaryngologist","???????????? ??? ??????????????????????????? ????????????????????????"));
        commonSpecialistModels.add(new CommonSpecialistModel(R.drawable.neurology, "neurologist","??????????????????-????????????????????????"));
        commonSpecialistModels.add(new CommonSpecialistModel(R.drawable.gastroenterologist, "Gastroenterologist","???????????????????????? ????????????????????????"));
        commonSpecialistModels.add(new CommonSpecialistModel(R.drawable.sexologist, "Sexologist","??????????????? ????????????????????????"));
        commonSpecialistModels.add(new CommonSpecialistModel(R.drawable.orthopedist, "Orthopedist","??????????????? ????????? ????????????????????????"));
        commonSpecialistModels.add(new CommonSpecialistModel(R.drawable.gynecologist, "Gynecologist","?????????????????? ????????? ????????????????????????"));

        commonSpecialistAdapter = new CommonSpecialistAdapter(Home.this, commonSpecialistModels);
        rvCommonSpecialist.setAdapter(commonSpecialistAdapter);


//        rvCommonSymptoms.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        GridLayoutManager layoutManager =
                new GridLayoutManager(Home.this, 2, GridLayoutManager.HORIZONTAL, false);
        rvCommonSymptoms.setLayoutManager(layoutManager);

//        PagerSnapHelper snapHelper1 = new PagerSnapHelper();
//        snapHelper1.attachToRecyclerView(rvCommonSymptoms);
//        // pager indicator
//        rvCommonSymptoms.addItemDecoration(new LinePagerIndicatorDecoration(Home.this));

        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.fever, "Fever","???????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.constipation, "Constipation","???????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.back_pain, "Back Pain","????????? ????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.pimples, "Pimples","???????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.urinary_tract_infection, "UTI","??????????????? ??????????????? ????????? ?????????????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.diabetes, "Diabetes","??????????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.acidity, "Acidity","????????? ?????? ?????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.sexual_problem, "Sexual Problem","????????? ??????????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.period_issue, "Period Issue","???????????? ?????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.thyroid, "Thyroid","?????????????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.dry_skin, "Dry Skin","??????????????? ???????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.cough, "Cough","???????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.cold, "Cold / Runny Nose","???????????? / ???????????? ?????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.weight_loss, "Weight Loss","????????? ????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.stress, "Stress","????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.depression, "Depression","????????????????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.headache, "Headache","??????????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.erectile_dysfunction, "Erectile Dysfunction","????????????????????????"));
        commonSymptomsModels.add(new CommonSymptomsModel(R.drawable.hair_fall, "Hair Fall","????????? ???????????????"));

        commonSymptomsAdapter = new CommonSymptomsAdapter(Home.this, commonSymptomsModels);
        rvCommonSymptoms.setAdapter(commonSymptomsAdapter);


        layoutAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, AboutUsPage.class);
                startActivity(intent);
            }
        });

        appointmentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, AppointmentList.class);
                startActivity(intent);
            }
        });

        searchDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Home.this, DoctorListRv.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isNetworkConnectionAvailable(Home.this)) {
                    if (SessionManager.getInstance(Home.this).getIsLogin()) {
                        SessionManager.getInstance(Home.this).setIsLogin(false);
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Home.this, "Login Please !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Utils.checkNetworkConnection(Home.this);
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

        headerDoctorList();

    }

    void headerDoctorList() {
        loadingDialog.show();
        FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Doctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    sliderItems.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        sliderItems.add(new SliderItem(
                                Objects.requireNonNull(dataSnapshot1.child("DisplayName").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Specialist").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("UserImageUrl").getValue()).toString()

                        ));

                        sliderAdapter.notifyDataSetChanged();
                    }

                } else {

                    Toast.makeText(Home.this, "No Data ", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Home.this, "Something Went Wrong !", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();

            }
        });
    }
}
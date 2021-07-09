package com.panchmukhi.eclinic.Agent.DoctorList;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.panchmukhi.eclinic.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DoctorListRv extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {


    RecyclerView recyclerView;
    ArrayList<DoctorListModel> list;
    DoctorListAdapter adapter;
    EditText search;
    ImageButton back;
    Dialog loadingDialog;
    LinearLayout noData;
    SwipeRefreshLayout swipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list_rv);

        recyclerView = findViewById(R.id.rateListRv);
        back = findViewById(R.id.back);
//        search = findViewById(R.id.search);
        noData = findViewById(R.id.noData);

        swipLayout = findViewById(R.id.swipe_layout);
        swipLayout.setOnRefreshListener(this);

        swipLayout.setColorSchemeResources(R.color.black,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.rounded_border));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        list = new ArrayList<>();

        adapter = new DoctorListAdapter(list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                filter(s.toString());
//            }
//        });
    }

    @Override
    public void onRefresh() {
        //your refresh code here
        setData();
//        adapter.notifyDataSetChanged();
    }

//    private void filter(String text) {
//
//        ArrayList<DoctorListModel> filterList = new ArrayList<>();
//
//        for (DoctorListModel items : list) {
//            if (items.getName().toLowerCase().contains(text.toLowerCase())) {
//                filterList.add(items);
//            }
//        }
//
//        adapter.filterList(filterList);
//    }

    void setData() {
        loadingDialog.show();

        FirebaseDatabase.getInstance().getReference().child("AllUserDetails").
                child("Doctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    noData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    swipLayout.setVisibility(View.VISIBLE);
                    list.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        list.add(new DoctorListModel(
                                Objects.requireNonNull(dataSnapshot1.child("Uid").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("UserImageUrl").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("DisplayName").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Specialist").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot1.child("Degree").getValue()).toString(),
                                Integer.parseInt(dataSnapshot1.child("OnlineStatus").getValue().toString()),
                                Objects.requireNonNull(dataSnapshot1.child("UserNotificationToken").getValue()).toString()

                                ));
                        adapter.notifyDataSetChanged();

                    }
                } else {
                    swipLayout.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    loadingDialog.dismiss();

                }
                swipLayout.setRefreshing(false);
                loadingDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                loadingDialog.dismiss();

                Toast.makeText(DoctorListRv.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setData();
    }
}
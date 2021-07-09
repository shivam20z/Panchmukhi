package com.panchmukhi.eclinic.Registration.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.panchmukhi.eclinic.Agent.BookAppointment.HttpParse;
import com.panchmukhi.eclinic.Agent.Homepage.Home;
import com.panchmukhi.eclinic.Doctor.DoctorDashBoard.DoctorHomepage;
import com.panchmukhi.eclinic.R;
import com.panchmukhi.eclinic.Registration.SignUp.AgentRegister;
import com.panchmukhi.eclinic.Registration.SignUp.DoctorRegister;
import com.panchmukhi.eclinic.Registration.SignUp.UserRegister;
import com.panchmukhi.eclinic.SessionManager;
import com.panchmukhi.eclinic.Utils;

import java.util.HashMap;
import java.util.Objects;

public class Login extends AppCompatActivity {

    //    LinearLayout layoutUser, layoutAgent;
    Button btnLogin;
    EditText etEmail, etPassword;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String verifiedValue = "0";
    //    Dialog loadingDialog;
    String loginUrl1 = "https://gograbit.app/doc/submit.php";
    HashMap<String, String> hashMap = new HashMap<>();
    String finalResult;
    HttpParse httpParse = new HttpParse();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

//
//        loadingDialog = new Dialog(Login.this);
//        loadingDialog.setContentView(R.layout.loading);
//        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.rounded_border));
//        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        loadingDialog.setCancelable(false);

//        layoutUser = findViewById(R.id.layoutUser);
//        layoutAgent = findViewById(R.id.layoutAgent);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.usernameLogin);
        etPassword = findViewById(R.id.passwordLogin);


//        layoutUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Login.this, DoctorRegister.class);
//                intent.putExtra("setDataParent", "Doctor");
//                intent.putExtra("setPhotoPath", "DoctorImage");
//                intent.putExtra("setAccountValue", 1);
//                startActivity(intent);
//            }
//        });
//
//        layoutAgent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Login.this, AgentRegister.class);
//                intent.putExtra("setDataParent", "Agent");
//                intent.putExtra("setPhotoPath", "AgentImage");
//                intent.putExtra("setAccountValue", 0);
//                startActivity(intent);
//            }
//        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();


                if (email.isEmpty() || password.isEmpty()) {

                    if (email.isEmpty()) {
                        etEmail.setError("Enter Your Email");
                    }

                    if (password.isEmpty()) {
                        etPassword.setError("Enter Your Password");
                    }
                } else {
                    if (!isValidEmail(email)) {
                        etEmail.setError("Invalid Email Address !");
                    } else {

                        if (Utils.isNetworkConnectionAvailable(Login.this)) {
                            Utils.setDialogWaveShow(Login.this);
                            logIn(email, password);
                        } else {
                            Utils.checkNetworkConnection(Login.this);
                        }


                    }
                }

            }
        });
    }

    private void logIn(String mail, String password) {

        class UserLoginClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);

                Utils.setDialogWaveDismiss();

                if (httpResponseMsg.equalsIgnoreCase("1  ")) {

                    SessionManager.getInstance(Login.this).setIsLogin(true);
                    Toast.makeText(Login.this, "Successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Login.this, Home.class);
                    intent.putExtra("UserMail", mail);
                    startActivity(intent);
                    finish();

                } else {

                    etEmail.setText(httpResponseMsg.toString());
                    Toast.makeText(Login.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email", params[0]);

                hashMap.put("password", params[1]);

                finalResult = httpParse.postRequest(hashMap, loginUrl1);
                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(mail, password);
    }


    private void signIn(String email, String password) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if (task.isSuccessful()) {
                        firebaseUser = mAuth.getCurrentUser();
                        getVerificationValue();
                    }

                }).addOnFailureListener(e -> {
            Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
//            loadingDialog.dismiss();
        });

    }

    void getVerificationValue() {

        FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Agent").child(firebaseUser.getUid()).child("Account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    verifiedValue = Objects.requireNonNull(snapshot.getValue()).toString();
                    Toast.makeText(Login.this, "Agent", Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {

                    FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Doctor")
                            .child(firebaseUser.getUid()).child("Account").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            verifiedValue = Objects.requireNonNull(snapshot.getValue()).toString();
                            Toast.makeText(Login.this, "Doctor", Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toast.makeText(Login.this, "Wrong Email Address !", Toast.LENGTH_SHORT).show();
//                            loadingDialog.dismiss();
                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
//                loadingDialog.dismiss();


            }
        });
    }


    private void updateUI() {

        if (verifiedValue.equals("0")) {
//            loadingDialog.dismiss();

//            Toast.makeText(this, "User", Toast.LENGTH_SHORT).show();
            SessionManager.getInstance(Login.this).clearPreference();
            SessionManager.getInstance(this).setIsLogin(true);
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
//            finish();


        } else if (verifiedValue.equals("1")) {
//            loadingDialog.dismiss();

            SessionManager.getInstance(Login.this).clearPreference();
            SessionManager.getInstance(this).setIsLogin(true);
            Intent intent = new Intent(Login.this, DoctorHomepage.class);
            startActivity(intent);
//            finish();


        } else if (verifiedValue.equals("2")) {
//            loadingDialog.dismiss();

            Toast.makeText(this, "Your Account is rejected from our service.", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isValidEmail(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Utils.isNetworkConnectionAvailable(Login.this)) {
            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null) {
//                loadingDialog.show();
//                getVerificationValue();
            }
        } else {
            Utils.checkNetworkConnection(Login.this);
        }
    }
}
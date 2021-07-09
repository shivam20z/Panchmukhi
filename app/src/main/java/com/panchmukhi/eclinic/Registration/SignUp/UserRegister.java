package com.panchmukhi.eclinic.Registration.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.panchmukhi.eclinic.R;
import com.panchmukhi.eclinic.Registration.Login.Login;
import com.panchmukhi.eclinic.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegister extends AppCompatActivity {

    private static final int REQUEST_CODE_FOR_FINE_LOCATION = 1234;
    static int REQUESCODE = 101;
    Uri pickedImgUri;
    Bitmap mBitmap;
    byte[] fileInBytes;
    private Dialog loadingDialog;
    ImageView goBack1;
    CircleImageView regUserPhoto,edit_background;
    com.airbnb.lottie.LottieAnimationView userImageAnimation;

    EditText et_username, et_email, et_password, etMobile;
    Button btSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);


        goBack1 = findViewById(R.id.goBack);
        regUserPhoto = findViewById(R.id.profile_image);
        edit_background = findViewById(R.id.edit_background);
        et_username = findViewById(R.id.etName);
        et_email = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        et_password = findViewById(R.id.etPassword);
        btSignin = findViewById(R.id.registerBtn);
        userImageAnimation = findViewById(R.id.userImageAnimation);

        userImageAnimation.setVisibility(View.VISIBLE);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.rounded_border));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        edit_background.setOnClickListener(view -> openGallery());

        goBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btSignin.setOnClickListener(view -> {

            String name = et_username.getText().toString();
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();


            if (name.isEmpty()) {
                et_username.requestFocus();
                et_username.setError("Enter Your UserName");
            } else if (email.isEmpty()) {
                et_email.requestFocus();
                et_email.setError("Enter Your Email");
            } else if (password.isEmpty()) {
                et_password.requestFocus();
                et_password.setError("Enter Your Password");
            } else if (etMobile.getText().toString().isEmpty()) {
                etMobile.requestFocus();
                etMobile.setError("Enter Your Mobile Number");
            } else if (pickedImgUri == null) {
                Toast.makeText(UserRegister.this, "Please Select Profile Picture !", Toast.LENGTH_SHORT).show();
            } else {

                if (!isValidEmail(email)) {
                    et_email.setError("Invalid Email Address !");
                } else {

                    if (Utils.isNetworkConnectionAvailable(UserRegister.this)) {
                        createUserAccount(name, email, password);
                    } else {
                        Utils.checkNetworkConnection(UserRegister.this);
                    }
                }
            }
        });

        if (ContextCompat.checkSelfPermission(UserRegister.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            checkPermission();
        }
    }


    private void createUserAccount(String name, String email, String password) {

        updateUI();
        Toast.makeText(this, "Finish !", Toast.LENGTH_SHORT).show();

    }


    private void updateUI() {
        Intent intent = new Intent(UserRegister.this, Login.class);
        startActivity(intent);
        finish();
    }

    public void openGallery() {

//        Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && requestCode == REQUESCODE && data != null) {
            userImageAnimation.setVisibility(View.GONE);
            pickedImgUri = data.getData();
            regUserPhoto.setImageURI(pickedImgUri);

            LongOperation longOperation1 = new LongOperation();
            longOperation1.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_FOR_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {

                if (!ActivityCompat.shouldShowRequestPermissionRationale(UserRegister.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(UserRegister.this)
                            .setMessage("We have permanently denied this permission, go to settings to enable this permission")
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    gotoApplicationSettings();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .setCancelable(false)
                            .show();
                }

            }
        }
    }


    private boolean isValidEmail(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(UserRegister.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UserRegister.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(UserRegister.this)
                        .setMessage("We Need Permission For Picture Upload")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialogInterface, i) ->
                                ActivityCompat.requestPermissions(UserRegister.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_FOR_FINE_LOCATION))
                        .show();
            } else {
                ActivityCompat.requestPermissions(UserRegister.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_FOR_FINE_LOCATION);
            }
        }
    }

    private void gotoApplicationSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public class LongOperation extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(UserRegister.this.getContentResolver(), pickedImgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
            fileInBytes = bytes.toByteArray();

            return "Image Picked";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(UserRegister.this, s, Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
        }
    }
}
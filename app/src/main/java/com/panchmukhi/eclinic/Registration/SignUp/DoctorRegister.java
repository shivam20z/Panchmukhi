package com.panchmukhi.eclinic.Registration.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.panchmukhi.eclinic.R;
import com.panchmukhi.eclinic.Registration.Login.Login;
import com.panchmukhi.eclinic.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class DoctorRegister extends AppCompatActivity {

    StateProgressBar stateProgressBar;
    String[] descriptionData = {"Personal \n Details", "Business \n Address", "Bank \n Details", "Complete"};
    LinearLayout agentLayout1, agentLayout2, agentLayout4;
    Button registerBtn;
    int layoutPageCounter = 1;
    static int REQUESCODE = 101;
    final Calendar myCalendar = Calendar.getInstance();
    StringBuffer result = new StringBuffer();
    com.airbnb.lottie.LottieAnimationView userImageAnimation;
    String parentData, photoPath;
    int accountValue;
    //layout 1
    CircleImageView profile_image, addImage;
    Uri pickedImgUri;
    Bitmap mBitmap;
    byte[] fileInBytes;
    EditText etName, etDisplayName, etEmail, etMobile, etAlternateMobile, etPassword;
    Spinner genderSpinner;
    private MainSpinnerAdapter genderAdapter;
    List<SpinnerParentItemModel>  genderList;
    String strGender,userToken;
    Button btDate;
    //layout2
    EditText etAddressLine1, etLocality, etLandMark, addressText, etDegree, etSpecialist;
    TextView tvCountry, tvState, tvPinCode;
    LinearLayout layoutCountry, layoutState, layoutPinCode;

    //layout 4
    EditText etBankIfscCode, etBankName, etBankBranchName, etBankAccountName, etBankAccountNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));

        userImageAnimation = findViewById(R.id.userImageAnimation);

        userImageAnimation.setVisibility(View.VISIBLE);

        agentLayout1 = findViewById(R.id.agentLayout1);
        agentLayout2 = findViewById(R.id.agentLayout2);
        agentLayout4 = findViewById(R.id.agentLayout4);
        registerBtn = findViewById(R.id.registerBtn);

        stateProgressBar = findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateDescriptionTypeface("fonts/montserrat_bold.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/montserrat_bold.ttf");
        stateProgressBar.setAllStatesCompleted(false);

        registerBtn.setText("Next");

        genderList = addGender();

        genderSpinner = findViewById(R.id.genderSpinner);

        addImage = findViewById(R.id.addImage);
        profile_image = findViewById(R.id.profile_image);
        etName = findViewById(R.id.etName);
        etDisplayName = findViewById(R.id.etDisplayName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etAlternateMobile = findViewById(R.id.etAlternateMobile);
        etPassword = findViewById(R.id.etPassword);
        btDate = findViewById(R.id.btDate);
        etDegree = findViewById(R.id.etDegree);
        etSpecialist = findViewById(R.id.etSpecialist);

        //layout 2
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etLocality = findViewById(R.id.etLocality);
        etLandMark = findViewById(R.id.etLandMark);
        addressText = findViewById(R.id.addressText);
        tvCountry = findViewById(R.id.tvCountry);
        tvState = findViewById(R.id.tvState);
        tvPinCode = findViewById(R.id.tvPinCode);
        layoutCountry = findViewById(R.id.layoutCountry);
        layoutState = findViewById(R.id.layoutState);
        layoutPinCode = findViewById(R.id.layoutPinCode);


        addressText.setFocusable(false);


        //layout 4
        etBankIfscCode = findViewById(R.id.etBankIfscCode);
        etBankName = findViewById(R.id.etBankName);
        etBankBranchName = findViewById(R.id.etBankBranchName);
        etBankAccountNumber = findViewById(R.id.etBankAccountNumber);
        etBankAccountName = findViewById(R.id.etBankAccountName);

        parentData = getIntent().getStringExtra("setDataParent");
        photoPath = getIntent().getStringExtra("setPhotoPath");
        accountValue = getIntent().getIntExtra("setAccountValue", 3);

        stateProgressBar.setAllStatesCompleted(false);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);


        //gender
        genderAdapter = new MainSpinnerAdapter(DoctorRegister.this, R.layout.custom_spinner_main_item, genderList);

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

                new DatePickerDialog(DoctorRegister.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList).build(Home.this);
//                startActivityForResult(intent, 100);

                Intent intent1 = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList)
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setTypeFilter(TypeFilter.CITIES)
                        .build(DoctorRegister.this);
                startActivityForResult(intent1, 100);
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layoutPageCounter == 1) {

                    layoutOneMethod();

                } else if (layoutPageCounter == 2) {

                    layoutTwoMethod();

                } else if (layoutPageCounter == 3) {

                    layoutFourMethod();

                }

            }
        });

    }

    void layoutOneMethod() {

        if (etName.getText().toString().isEmpty()) {
            etName.setError("Fill This.");
        } else if (etDisplayName.getText().toString().isEmpty()) {
            etDisplayName.setError("Fill This.");
        } else if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("Fill This.");
        } else if (etMobile.getText().toString().isEmpty()) {
            etMobile.setError("Fill This.");
        } else if (etAlternateMobile.getText().toString().isEmpty()) {
            etAlternateMobile.setError("Fill This.");
        } else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Fill This.");
        } else if (etDegree.getText().toString().isEmpty()) {
            etDegree.setError("Fill This.");
        } else if (etSpecialist.getText().toString().isEmpty()) {
            etSpecialist.setError("Fill This.");
        } else if (!isValidEmail(etEmail.getText().toString())) {
            etEmail.setError("Write Valid Email Address");
        } else if (btDate.getText().toString().equals("Set Date Of Birth")) {
            Toasty.error(DoctorRegister.this, "Select Date Of Birth.", Toast.LENGTH_SHORT, true).show();
        } else if (strGender.equals("nothing")) {
            Toasty.error(DoctorRegister.this, "Select Gender Group.", Toast.LENGTH_SHORT, true).show();
        } else if (pickedImgUri == null) {
            Toasty.error(DoctorRegister.this, "Add Your Image.", Toast.LENGTH_SHORT, true).show();
        } else {
            layoutPageCounter++;
            agentLayout1.setVisibility(View.GONE);
            agentLayout2.setVisibility(View.VISIBLE);
            agentLayout4.setVisibility(View.GONE);
            registerBtn.setText("Next");
            stateProgressBar.setAnimationDuration(3000);
            stateProgressBar.setAllStatesCompleted(false);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);

        }

    }

    void layoutTwoMethod() {

        if (etAddressLine1.getText().toString().isEmpty()) {
            etAddressLine1.setError("Fill This.");
        } else if (etLocality.getText().toString().isEmpty()) {
            etLocality.setError("Fill This.");
        } else if (etLandMark.getText().toString().isEmpty()) {
            etLandMark.setError("Fill This.");
        } else if (addressText.getText().toString().isEmpty()) {
            addressText.setError("Fill This.");
        } else if (tvPinCode.getText().toString().isEmpty() || tvCountry.getText().toString().isEmpty() || tvState.getText().toString().isEmpty()) {
            Toasty.error(DoctorRegister.this, "Please Select Proper Location.", Toast.LENGTH_SHORT, true).show();
        } else {
            layoutPageCounter++;
            agentLayout1.setVisibility(View.GONE);
            agentLayout2.setVisibility(View.GONE);
            agentLayout4.setVisibility(View.VISIBLE);
            registerBtn.setText("Next");
            stateProgressBar.setAllStatesCompleted(false);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        }

    }

    void layoutFourMethod() {


        if (etBankAccountName.getText().toString().isEmpty()) {
            etBankAccountName.setError("Fill This.");
        } else if (etBankAccountNumber.getText().toString().isEmpty()) {
            etBankAccountNumber.setError("Fill This.");
        } else if (etBankIfscCode.getText().toString().isEmpty()) {
            etBankIfscCode.setError("Fill This.");
        }
//        else if (etBankMircCode.getText().toString().isEmpty()) {
//            etBankMircCode.setError("Fill This.");
//        }
        else if (etBankName.getText().toString().isEmpty()) {
            etBankName.setError("Fill This.");
        } else if (etBankBranchName.getText().toString().isEmpty()) {
            etBankBranchName.setError("Fill This.");
        } else {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
            stateProgressBar.setAllStatesCompleted(true);
            layoutPageCounter++;

            createUserAccount(etDisplayName.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString());
            Toast.makeText(this, "Finish !", Toast.LENGTH_SHORT).show();

        }

    }


    private List<SpinnerParentItemModel> addGender() {

        List<SpinnerParentItemModel> list = new ArrayList<>();
        list.add(new SpinnerParentItemModel(R.drawable.gender, "Select Gender"));
        list.add(new SpinnerParentItemModel(R.drawable.gender, "Male"));
        list.add(new SpinnerParentItemModel(R.drawable.gender, "Female"));
        list.add(new SpinnerParentItemModel(R.drawable.gender, "Other"));

        return list;
    }

    private boolean isValidEmail(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void updateLabel() throws ParseException {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        result.append(sdf.format(myCalendar.getTime()));

        btDate.setText(result.toString());
    }

    public void openGallery() {

//        Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void createUserAccount(String name, String email, String password) {

        Utils.loadingDialogShow(DoctorRegister.this);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setDataInDataBase(photoPath, name, email, password);

                } else {
                    Toast.makeText(DoctorRegister.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    Utils.loadingDialogDismiss();
                }
            }
        });

    }

    void setDataInDataBase(String photoPath, String name, String email, String password) {

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(photoPath).child(email + "+" + name);

        mStorage.putBytes(fileInBytes).addOnSuccessListener(taskSnapshot ->
                mStorage.getDownloadUrl().addOnSuccessListener(uri -> {

                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .setPhotoUri(uri)
                            .build();

                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (task.isComplete()) {
                                userToken = task.getResult();
                            }
                        }
                    });


                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(profileUpdate).addOnCompleteListener(task -> {

                        HashMap<String, Object> map = new HashMap<>();

                        map.put("Account", 1);
                        map.put("Name", etName.getText().toString());
                        map.put("DisplayName", name);
                        map.put("Email", email);
                        map.put("Password", password);
                        map.put("UserImageUrl", uri.toString());
                        map.put("PhoneNumber", etMobile.getText().toString().trim());
                        map.put("AlternatePhoneNumber", etAlternateMobile.getText().toString().trim());
                        map.put("Gender", strGender);
                        map.put("DOB", btDate.getText().toString());
                        map.put("Address", etAddressLine1.getText().toString());
                        map.put("Locality", etLocality.getText().toString());
                        map.put("Landmark", etLandMark.getText().toString());
                        map.put("City", addressText.getText().toString());
                        map.put("Country", tvCountry.getText().toString());
                        map.put("State", tvState.getText().toString());
                        map.put("PinCode", tvPinCode.getText().toString());
                        map.put("BankAccountNo", etBankAccountNumber.getText().toString().trim());
                        map.put("BankAccountName", etBankAccountName.getText().toString());
                        map.put("BankAccountIfscCode", etBankIfscCode.getText().toString());
                        map.put("BankName", etBankName.getText().toString());
                        map.put("BankBranchName", etBankBranchName.getText().toString());
                        map.put("Specialist", etSpecialist.getText().toString());
                        map.put("Degree", etDegree.getText().toString());
                        map.put("Uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        map.put("OnlineStatus",0);
                        map.put("DoctorAppointmentCounter",0);
                        map.put("UserNotificationToken", userToken);



                        FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(DoctorRegister.this, "Account Created !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DoctorRegister.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                    Utils.loadingDialogDismiss();


                                } else {
                                    Utils.loadingDialogDismiss();


                                    Toast.makeText(DoctorRegister.this, "Something Went wrong !!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    });
                }));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);

            layoutPinCode.setVisibility(View.VISIBLE);
            layoutState.setVisibility(View.VISIBLE);
            layoutCountry.setVisibility(View.VISIBLE);
            addressText.setText(place.getName());

            try {
                getCityNameByCoordinates(place.getLatLng().latitude, place.getLatLng().longitude);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Toast.makeText(this, String.valueOf(place.getLatLng()), Toast.LENGTH_SHORT).show();


        } else if (requestCode == 100 && requestCode == AutocompleteActivity.RESULT_ERROR) {

            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();

        }


        if (requestCode == 101 && requestCode == REQUESCODE && data != null) {
            userImageAnimation.setVisibility(View.GONE);

            pickedImgUri = data.getData();
            profile_image.setImageURI(pickedImgUri);

            LongOperation longOperation1 = new LongOperation();
            longOperation1.execute();
        }


    }


    private void getCityNameByCoordinates(double lat, double lon) throws IOException {
        Geocoder mGeocoder = new Geocoder(DoctorRegister.this, Locale.getDefault());

        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {

            tvState.setText(String.valueOf(addresses.get(0).getAdminArea()));
            tvPinCode.setText(String.valueOf(addresses.get(0).getPostalCode()));
            tvCountry.setText(String.valueOf(addresses.get(0).getCountryName()));

        }
    }


    private class LongOperation extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.loadingDialogShow(DoctorRegister.this);
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(DoctorRegister.this.getContentResolver(), pickedImgUri);
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
            Toast.makeText(DoctorRegister.this, s, Toast.LENGTH_SHORT).show();
            Utils.loadingDialogDismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (layoutPageCounter == 1) {
            Intent intent = new Intent(DoctorRegister.this, Login.class);
            startActivity(intent);
        } else if (layoutPageCounter == 2) {
            registerBtn.setText("Next");
            agentLayout1.setVisibility(View.VISIBLE);
            agentLayout2.setVisibility(View.GONE);
            agentLayout4.setVisibility(View.GONE);
            stateProgressBar.setAnimationDuration(3000);
            stateProgressBar.setAllStatesCompleted(false);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

        } else if (layoutPageCounter == 3) {
            registerBtn.setText("Next");
            agentLayout1.setVisibility(View.GONE);
            agentLayout2.setVisibility(View.VISIBLE);
            agentLayout4.setVisibility(View.GONE);
            stateProgressBar.setAnimationDuration(3000);
            stateProgressBar.setAllStatesCompleted(false);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        }
        layoutPageCounter--;

    }
}
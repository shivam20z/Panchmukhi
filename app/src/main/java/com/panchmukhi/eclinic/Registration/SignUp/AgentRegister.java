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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.panchmukhi.eclinic.R;
import com.panchmukhi.eclinic.Registration.Login.Login;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.kofigyan.stateprogressbar.StateProgressBar;

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

public class AgentRegister extends AppCompatActivity {

    StateProgressBar stateProgressBar;
    String[] descriptionData = {"Personal \n Details", "Business \n Address", "Bank \n Details", "Complete"};
    LinearLayout agentLayout1, agentLayout2, agentLayout3, agentLayout4;
    Button registerBtn;
    int layoutPageCounter = 1;
    private Dialog loadingDialog;
    static int REQUESCODE = 101;
    final Calendar myCalendar = Calendar.getInstance();
    StringBuffer result = new StringBuffer();
    com.airbnb.lottie.LottieAnimationView userImageAnimation;
    String parentData, photoPath,userToken;
    int accountValue;
    //layout 1
    CircleImageView profile_image, addImage;
    Uri pickedImgUri;
    Bitmap mBitmap;
    byte[] fileInBytes;
    EditText etName, etDisplayName, etEmail, etMobile, etAlternateMobile,
            etFatherName, etMotherName, etPanNo, etAadhaarNo, etNationality, etPassword;
    Spinner bloodGroupSpinner, religionSpinner, categorySpinner, genderSpinner;
    private MainSpinnerAdapter bloodAdapter, religionAdapter, categoryAdapter, genderAdapter;
    List<SpinnerParentItemModel> bloodGroupList, religionList, categoryList, genderList;
    String strBloodGroup, strReligion, strCategory, strGender;
    Button btDate;
    //layout2
    EditText etAddressLine1, etLocality, etLandMark, addressText;
    TextView tvCountry, tvState, tvPinCode;
    LinearLayout layoutCountry, layoutState, layoutPinCode;


    //layout 3
    CheckBox cbAddress;
    EditText etAddressLine3, etAddressLine4, addressText1;
    TextView tvCountry1, tvState1, tvPinCode1;
    LinearLayout layoutCountry1, layoutState1, layoutPinCode1;

    //layout 4
    EditText etBankIfscCode, etBankName, etBankBranchName, etBankAccountName, etBankAccountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_register);


        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.rounded_border));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));

        userImageAnimation = findViewById(R.id.userImageAnimation);

        userImageAnimation.setVisibility(View.VISIBLE);

        agentLayout1 = findViewById(R.id.agentLayout1);
        agentLayout2 = findViewById(R.id.agentLayout2);
        agentLayout3 = findViewById(R.id.agentLayout3);
        agentLayout4 = findViewById(R.id.agentLayout4);
        registerBtn = findViewById(R.id.registerBtn);

        stateProgressBar = findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateDescriptionTypeface("fonts/montserrat_bold.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/montserrat_bold.ttf");
        stateProgressBar.setAllStatesCompleted(false);

        registerBtn.setText("Next");

        //layout1
        bloodGroupList = addBloodGroup();
        religionList = addReligion();
        categoryList = addCategory();
        genderList = addGender();

//        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
//        religionSpinner = findViewById(R.id.religionSpinner);
//        categorySpinner = findViewById(R.id.categorySpinner);
        genderSpinner = findViewById(R.id.genderSpinner);

        addImage = findViewById(R.id.addImage);
        profile_image = findViewById(R.id.profile_image);
        etName = findViewById(R.id.etName);
        etDisplayName = findViewById(R.id.etDisplayName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etAlternateMobile = findViewById(R.id.etAlternateMobile);
//        etFatherName = findViewById(R.id.etFatherName);
//        etMotherName = findViewById(R.id.etMotherName);
//        etPanNo = findViewById(R.id.etPanNo);
//        etAadhaarNo = findViewById(R.id.etAadhaarNo);
//        etNationality = findViewById(R.id.etNationality);
        etPassword = findViewById(R.id.etPassword);
        btDate = findViewById(R.id.btDate);
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

        //layout 3
        cbAddress = findViewById(R.id.cbAddress);
        etAddressLine3 = findViewById(R.id.etAddressLine3);
        etAddressLine4 = findViewById(R.id.etAddressLine4);
        addressText1 = findViewById(R.id.addressText1);
        tvCountry1 = findViewById(R.id.tvCountry1);
        tvState1 = findViewById(R.id.tvState1);
        tvPinCode1 = findViewById(R.id.tvPinCode1);
        layoutCountry1 = findViewById(R.id.layoutCountry1);
        layoutState1 = findViewById(R.id.layoutState1);
        layoutPinCode1 = findViewById(R.id.layoutPinCode1);

        addressText1.setFocusable(false);


        //layout 4
        etBankIfscCode = findViewById(R.id.etBankIfscCode);
//        etBankMircCode = findViewById(R.id.etBankMircCode);
        etBankName = findViewById(R.id.etBankName);
        etBankBranchName = findViewById(R.id.etBankBranchName);
        etBankAccountNumber = findViewById(R.id.etBankAccountNumber);
        etBankAccountName = findViewById(R.id.etBankAccountName);

        parentData = getIntent().getStringExtra("setDataParent");
        photoPath = getIntent().getStringExtra("setPhotoPath");
        accountValue = getIntent().getIntExtra("setAccountValue", 3);

        stateProgressBar.setAllStatesCompleted(false);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        //blood group
//        bloodAdapter = new MainSpinnerAdapter(AgentRegister.this, R.layout.custom_spinner_main_item, bloodGroupList);
//
//        bloodGroupSpinner.setAdapter(bloodAdapter);
//
//        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    strBloodGroup = "nothing";
//                } else if (position == 1) {
//                    strBloodGroup = "A+";
//                } else if (position == 2) {
//                    strBloodGroup = "A-";
//                } else if (position == 3) {
//                    strBloodGroup = "B+";
//                } else if (position == 4) {
//                    strBloodGroup = "B-";
//                } else if (position == 5) {
//                    strBloodGroup = "AB+";
//                } else if (position == 6) {
//                    strBloodGroup = "AB-";
//                } else if (position == 7) {
//                    strBloodGroup = "O+";
//                } else if (position == 8) {
//                    strBloodGroup = "O-";
//                } else if (position == 9) {
//                    strBloodGroup = "Unknown";
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        //religion
//        religionAdapter = new MainSpinnerAdapter(AgentRegister.this, R.layout.custom_spinner_main_item, religionList);
//
//        religionSpinner.setAdapter(religionAdapter);
//
//        religionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    strReligion = "nothing";
//                } else if (position == 1) {
//                    strReligion = "Hindu";
//                } else if (position == 2) {
//                    strReligion = "Islam";
//                } else if (position == 3) {
//                    strReligion = "Christian";
//                } else if (position == 4) {
//                    strReligion = "Buddhism";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        //category
//        categoryAdapter = new MainSpinnerAdapter(AgentRegister.this, R.layout.custom_spinner_main_item, categoryList);
//
//        categorySpinner.setAdapter(categoryAdapter);
//
//        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    strCategory = "nothing";
//                } else if (position == 1) {
//                    strCategory = "General (Unreserved)";
//                } else if (position == 2) {
//                    strCategory = "Extremely Backward Class (BC-1)";
//                } else if (position == 3) {
//                    strCategory = "Backward Class (BC-2)";
//                } else if (position == 4) {
//                    strCategory = "Scheduled Caste (SC)";
//                } else if (position == 5) {
//                    strCategory = "Scheduled Tribes (ST)";
//                } else if (position == 6) {
//                    strCategory = "Economical Weaker Section (EWS)";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        //gender
        genderAdapter = new MainSpinnerAdapter(AgentRegister.this, R.layout.custom_spinner_main_item, genderList);

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

                new DatePickerDialog(AgentRegister.this, date, myCalendar
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
                        .build(AgentRegister.this);
                startActivityForResult(intent1, 100);
            }
        });


        addressText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList).build(Home.this);
//                startActivityForResult(intent, 100);

                Intent intent1 = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList)
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setTypeFilter(TypeFilter.CITIES)
                        .build(AgentRegister.this);
                startActivityForResult(intent1, 102);
            }
        });


        cbAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CompoundButton) v).isChecked()) {

                    etAddressLine3.setText(etAddressLine1.getText().toString());
                    etAddressLine4.setText(etLocality.getText().toString());
                    addressText1.setText(addressText.getText().toString());
                    tvCountry1.setText(tvCountry.getText().toString());
                    tvState1.setText(tvState.getText().toString());
                    tvPinCode1.setText(tvPinCode.getText().toString());


                    layoutCountry1.setVisibility(View.VISIBLE);
                    layoutPinCode1.setVisibility(View.VISIBLE);
                    layoutState1.setVisibility(View.VISIBLE);


                } else {

                    etAddressLine3.setText("");
                    etAddressLine4.setText("");
                    addressText1.setText("");
                    tvCountry1.setText("");
                    tvState1.setText("");
                    tvPinCode1.setText("");


                    layoutCountry1.setVisibility(View.GONE);
                    layoutPinCode1.setVisibility(View.GONE);
                    layoutState1.setVisibility(View.GONE);
                }
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layoutPageCounter == 1) {

                    layoutOneMethod();

                } else if (layoutPageCounter == 2) {

                    layoutTwoMethod();

                }

//                else if (layoutPageCounter == 3) {
//
//                    layoutThreeMethod();
//
//                }
                else if (layoutPageCounter == 3) {

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
        }
//        else if (etFatherName.getText().toString().isEmpty()) {
//            etFatherName.setError("Fill This.");
//        } else if (etMotherName.getText().toString().isEmpty()) {
//            etMotherName.setError("Fill This.");
//        }
//
//        else if (etPanNo.getText().toString().isEmpty()) {
//            etPanNo.setError("Fill This.");
//        } else if (etAadhaarNo.getText().toString().isEmpty()) {
//            etAadhaarNo.setError("Fill This.");
//        }
//        else if (etNationality.getText().toString().isEmpty()) {
//            etNationality.setError("Fill This.");
//        }

        else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Fill This.");
        } else if (!isValidEmail(etEmail.getText().toString())) {
            etEmail.setError("Write Valid Email Address");
        } else if (btDate.getText().toString().equals("Set Date Of Birth")) {
            Toasty.error(AgentRegister.this, "Select Date Of Birth.", Toast.LENGTH_SHORT, true).show();
        }
//        else if (strBloodGroup.equals("nothing")) {
//            Toasty.error(AgentRegister.this, "Select Blood Group.", Toast.LENGTH_SHORT, true).show();
//        } else if (strReligion.equals("nothing")) {
//            Toasty.error(AgentRegister.this, "Select Religion Group.", Toast.LENGTH_SHORT, true).show();
//        } else if (categoryList.equals("nothing")) {
//            Toasty.error(AgentRegister.this, "Select Category Group.", Toast.LENGTH_SHORT, true).show();
//        }
        else if (strGender.equals("nothing")) {
            Toasty.error(AgentRegister.this, "Select Gender Group.", Toast.LENGTH_SHORT, true).show();
        } else if (pickedImgUri == null) {
            Toasty.error(AgentRegister.this, "Add Your Image.", Toast.LENGTH_SHORT, true).show();
        } else {
            layoutPageCounter++;
            agentLayout1.setVisibility(View.GONE);
            agentLayout2.setVisibility(View.VISIBLE);
            agentLayout3.setVisibility(View.GONE);
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
            Toasty.error(AgentRegister.this, "Please Select Proper Location.", Toast.LENGTH_SHORT, true).show();
        } else {
            layoutPageCounter++;
            agentLayout1.setVisibility(View.GONE);
            agentLayout2.setVisibility(View.GONE);
//            agentLayout3.setVisibility(View.VISIBLE);
            agentLayout4.setVisibility(View.VISIBLE);
            registerBtn.setText("Next");
            stateProgressBar.setAllStatesCompleted(false);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        }

    }

    void layoutThreeMethod() {

        if (etAddressLine3.getText().toString().isEmpty()) {
            etAddressLine3.setError("Fill This.");
        } else if (etAddressLine4.getText().toString().isEmpty()) {
            etAddressLine4.setError("Fill This.");
        } else if (addressText1.getText().toString().isEmpty()) {
            addressText1.setError("Fill This.");
        } else if (tvPinCode1.getText().toString().isEmpty() || tvCountry1.getText().toString().isEmpty() || tvState1.getText().toString().isEmpty()) {
            Toasty.error(AgentRegister.this, "Please Select Proper Location.", Toast.LENGTH_SHORT, true).show();
        } else {
            stateProgressBar.setAllStatesCompleted(false);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
            layoutPageCounter++;
            agentLayout1.setVisibility(View.GONE);
            agentLayout2.setVisibility(View.GONE);
            agentLayout3.setVisibility(View.GONE);
            agentLayout4.setVisibility(View.VISIBLE);
            registerBtn.setText("Finish");

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


    private List<SpinnerParentItemModel> addBloodGroup() {

        List<SpinnerParentItemModel> list = new ArrayList<>();
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "Select Blood Group"));
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "A+"));
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "A-"));
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "B+"));
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "B-"));
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "AB+"));
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "AB-"));
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "O+"));
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "O-"));
        list.add(new SpinnerParentItemModel(R.drawable.blood_group, "Unknown"));

        return list;
    }

    private List<SpinnerParentItemModel> addReligion() {

        List<SpinnerParentItemModel> list = new ArrayList<>();
        list.add(new SpinnerParentItemModel(R.drawable.religion, "Select Religion"));
        list.add(new SpinnerParentItemModel(R.drawable.religion, "Hindu"));
        list.add(new SpinnerParentItemModel(R.drawable.religion, "Islam"));
        list.add(new SpinnerParentItemModel(R.drawable.religion, "Christian"));
        list.add(new SpinnerParentItemModel(R.drawable.religion, "Buddhism"));

        return list;
    }

    private List<SpinnerParentItemModel> addCategory() {

        List<SpinnerParentItemModel> list = new ArrayList<>();
        list.add(new SpinnerParentItemModel(R.drawable.category, "Select Category"));
        list.add(new SpinnerParentItemModel(R.drawable.category, "General (Unreserved)"));
        list.add(new SpinnerParentItemModel(R.drawable.category, "Extremely Backward Class (BC-1)"));
        list.add(new SpinnerParentItemModel(R.drawable.category, "Backward Class (BC-2)"));
        list.add(new SpinnerParentItemModel(R.drawable.category, "Scheduled Caste (SC)"));
        list.add(new SpinnerParentItemModel(R.drawable.category, "Scheduled Tribes (ST)"));
        list.add(new SpinnerParentItemModel(R.drawable.category, "Economical Weaker Section (EWS)"));

        return list;
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

        loadingDialog.show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setDataInDataBase(photoPath, name, email, password);

                } else {
                    Toast.makeText(AgentRegister.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
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

                        map.put("Account", 0);
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
                        map.put("Uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        map.put("AgentAppointmentCounter", 0);
                        map.put("UserNotificationToken", userToken);


                        FirebaseDatabase.getInstance().getReference().child("AllUserDetails").child("Agent").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(AgentRegister.this, "Account Created !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AgentRegister.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                    loadingDialog.dismiss();

                                } else {
                                    loadingDialog.dismiss();

                                    Toast.makeText(AgentRegister.this, "Something Went wrong !!!", Toast.LENGTH_SHORT).show();
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


        if (requestCode == 102 && resultCode == RESULT_OK) {

            Place place = Autocomplete.getPlaceFromIntent(data);

            layoutPinCode1.setVisibility(View.VISIBLE);
            layoutState1.setVisibility(View.VISIBLE);
            layoutCountry1.setVisibility(View.VISIBLE);
            addressText1.setText(place.getName());

            try {
                getCityNameByCoordinates1(place.getLatLng().latitude, place.getLatLng().longitude);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Toast.makeText(this, String.valueOf(place.getLatLng()), Toast.LENGTH_SHORT).show();


        } else if (requestCode == 102 && requestCode == AutocompleteActivity.RESULT_ERROR) {

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
        Geocoder mGeocoder = new Geocoder(AgentRegister.this, Locale.getDefault());

        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {

            tvState.setText(String.valueOf(addresses.get(0).getAdminArea()));
            tvPinCode.setText(String.valueOf(addresses.get(0).getPostalCode()));
            tvCountry.setText(String.valueOf(addresses.get(0).getCountryName()));

        }
    }

    private void getCityNameByCoordinates1(double lat, double lon) throws IOException {
        Geocoder mGeocoder = new Geocoder(AgentRegister.this, Locale.getDefault());

        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {

            tvState1.setText(String.valueOf(addresses.get(0).getAdminArea()));
            tvPinCode1.setText(String.valueOf(addresses.get(0).getPostalCode()));
            tvCountry1.setText(String.valueOf(addresses.get(0).getCountryName()));

        }
    }


    private class LongOperation extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(AgentRegister.this.getContentResolver(), pickedImgUri);
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
            Toast.makeText(AgentRegister.this, s, Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (layoutPageCounter == 1) {
            Intent intent = new Intent(AgentRegister.this, Login.class);
            startActivity(intent);
        } else if (layoutPageCounter == 2) {
            registerBtn.setText("Next");
            agentLayout1.setVisibility(View.VISIBLE);
            agentLayout2.setVisibility(View.GONE);
            agentLayout3.setVisibility(View.GONE);
            agentLayout4.setVisibility(View.GONE);
            stateProgressBar.setAnimationDuration(3000);
            stateProgressBar.setAllStatesCompleted(false);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

        }

//        else if (layoutPageCounter == 3) {
//            registerBtn.setText("Next");
//            agentLayout1.setVisibility(View.GONE);
//            agentLayout2.setVisibility(View.VISIBLE);
//            agentLayout3.setVisibility(View.GONE);
//            agentLayout4.setVisibility(View.GONE);
//            stateProgressBar.setAnimationDuration(3000);
//            stateProgressBar.setAllStatesCompleted(false);
//            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
//        }

        else if (layoutPageCounter == 3) {
            registerBtn.setText("Next");
            agentLayout1.setVisibility(View.GONE);
            agentLayout2.setVisibility(View.VISIBLE);
//            agentLayout3.setVisibility(View.VISIBLE);
            agentLayout4.setVisibility(View.GONE);
            stateProgressBar.setAnimationDuration(3000);
            stateProgressBar.setAllStatesCompleted(false);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        }
        layoutPageCounter--;

    }
}
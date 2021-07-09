package com.panchmukhi.eclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.panchmukhi.eclinic.Notification.FcmNotificationsSender;

import java.util.Objects;

public class Test extends AppCompatActivity {

    EditText etTitle,etMessage,etUserToken;
    Button btnSendToAll,btnSendToUser;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        etTitle=findViewById(R.id.etTitle);
        etMessage=findViewById(R.id.etMessage);
        etUserToken=findViewById(R.id.etUserToken);
        btnSendToAll=findViewById(R.id.btnSendToAll);
        btnSendToUser=findViewById(R.id.btnSendToUser);

        FirebaseMessaging.getInstance().subscribeToTopic("all");

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (task.isSuccessful()) {
//                            String  token = Objects.requireNonNull(task.getResult()).getToken();
//
//                        }
//
//                    }
//                });

        returnMeFCMtoken();

        btnSendToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etTitle.getText().toString().isEmpty() && !etMessage.getText().toString().isEmpty()){

                    FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender("/topics/all",etTitle.getText().toString(),etMessage.getText().toString(),getApplicationContext(),Test.this);
                    fcmNotificationsSender.SendNotifications();
                }else {
                    Toast.makeText(Test.this, "Fill the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnSendToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etTitle.getText().toString().isEmpty() && !etMessage.getText().toString().isEmpty() && !etUserToken.getText().toString().isEmpty()){

                    FcmNotificationsSender fcmNotificationsSender=new FcmNotificationsSender(token,etTitle.getText().toString(),etMessage.getText().toString(),getApplicationContext(),Test.this);
                    fcmNotificationsSender.SendNotifications();

                }else {

                    Toast.makeText(Test.this, "Fill All Fields !", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public  void returnMeFCMtoken() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isComplete()){
                    token = task.getResult();
                }
            }
        });
    }
}
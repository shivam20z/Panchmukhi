package com.panchmukhi.eclinic.Mail;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.panchmukhi.eclinic.LinedEditText;
import com.panchmukhi.eclinic.R;
import com.panchmukhi.eclinic.Utils;

import java.util.Objects;

public class SendMail extends AppCompatActivity {

    Button sendMail;
    LinedEditText textMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Mail Box");
        Utils.changeToolbarFont(toolbar, SendMail.this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        textMail = findViewById(R.id.textMail);
        sendMail = findViewById(R.id.sendMail);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkConnectionAvailable(SendMail.this)) {
                    if (textMail.getText().toString().isEmpty()){
                        Toast.makeText(SendMail.this, "Please write something in message pad.", Toast.LENGTH_SHORT).show();
                    }else {
                        sendMail(getIntent().getStringExtra("mailToSend"),
                                textMail.getText().toString());
                    }

                } else {
                    Utils.checkNetworkConnection(SendMail.this);
                }

            }
        });
    }

    private void sendMail(String mail, String message) {

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mail, "Mail From GoGrabit", message);

        javaMailAPI.execute();

    }
}
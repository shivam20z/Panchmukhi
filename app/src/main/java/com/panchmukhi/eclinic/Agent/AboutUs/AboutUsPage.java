package com.panchmukhi.eclinic.Agent.AboutUs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.panchmukhi.eclinic.Mail.SendMail;
import com.panchmukhi.eclinic.R;
import com.panchmukhi.eclinic.Utils;

import java.util.Objects;

public class AboutUsPage extends AppCompatActivity {

    PDFView pdfView;
    Button btnSendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_page);

        Toolbar toolbar = findViewById(R.id.toolBar);
        btnSendMail=findViewById(R.id.btnSendMail);

        toolbar.setTitle("About Us");
        Utils.changeToolbarFont(toolbar, this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        pdfView=findViewById(R.id.pdfView);

        pdfView.fromAsset("pdfp.pdf")

                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .spacing(0)
                .load();


        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AboutUsPage.this, SendMail.class);
                startActivity(intent);
            }
        });
    }
}
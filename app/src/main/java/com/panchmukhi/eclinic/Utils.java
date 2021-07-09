package com.panchmukhi.eclinic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;

import java.util.Objects;

public class Utils {

    public static final String EMAIL = "worldtravellingteam@gmail.com";
    //This is your from email password
    public static final String PASSWORD = "Qwwe@1234";
    public static Dialog loadingDialog;
    public static Dialog customDialog;


    public static void checkNetworkConnection(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection, And open Again !");

        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static boolean isNetworkConnectionAvailable(Context context) {
        boolean isConnected;

        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));


        assert connectivityManager != null;
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {

            Log.d("Network", "Connected");
            return true;
        } else {
//            checkNetworkConnection(context);
            Log.d("Network", "Not Connected");
            return false;
        }
    }


    public static void changeToolbarFont(Toolbar toolbar, Context context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setTextSize(16);
//                tv.setText("");
                if (tv.getText().equals(toolbar.getTitle())) {
                    try {
                        tv.setTextColor(context.getResources().getColor(R.color.white));
                    } catch (Exception e) {
                        // handle exceptions
                    }
                    Utils.setFontBubblyRegular(tv);
                    break;
                }
            }
        }
    }


    public static void setDialogWaveShow(Context context) {
        customDialog = new Dialog(context);
        customDialog.setContentView(R.layout.custom_loading);
        customDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(context.getDrawable(R.drawable.square_border));
        customDialog.setCancelable(false);
        ProgressBar progressBar = customDialog.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new WanderingCubes();
        progressBar.setIndeterminateDrawable(doubleBounce);
        customDialog.show();
    }

    public static void setDialogWaveDismiss() {

        customDialog.dismiss();
    }


    public static void setDialogBounceShow(Context context) {
        customDialog = new Dialog(context);
        customDialog.setContentView(R.layout.custom_loading2);
        customDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(context.getDrawable(R.drawable.rounded_border));

        customDialog.setCancelable(false);
        ProgressBar progressBar = customDialog.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        customDialog.show();
    }

    public static void setDialogBounceDismiss() {

        customDialog.dismiss();
    }

    public static void loadingDialogShow(Context context){
        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(context.getDrawable(R.drawable.rounded_border));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public static void loadingDialogDismiss(){
        loadingDialog.dismiss();
    }

    public static void setFontBubblyRegular(TextView textView) {
        Typeface tf = Typeface.createFromAsset(textView.getContext()
                .getAssets(), "fonts/montserrat_bold.ttf");
        textView.setTypeface(tf);
    }

}



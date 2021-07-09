package com.panchmukhi.eclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentPage extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "PaymentPage";
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        btnPay=findViewById(R.id.btnPay);

        Checkout.preload(getApplicationContext());

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPayment();
            }
        });
    }

    public void startPayment() {

//
//        key_id,key_secret
//        rzp_test_dTgnOQetMPZ0JD,6DhjQJuPK5PqBm60qF2guF7T

//        Log.e("orderid", orderId);
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

//        checkout.setKeyID("rzp_test_dTgnOQetMPZ0JD");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo2_end);

        /**
         * Reference to current activity
         */
        final Activity activity = PaymentPage.this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Panchmukhi E-Clinic");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", orderId);//from response of step 3.
            options.put("theme.color", R.color.blue2);
            options.put("currency", "INR");
            options.put("amount", "500000");//pass amount in currency subunits
            options.put("prefill.email", "guptabittu77@gmail.com");
            options.put("prefill.contact","9955431261");

            options.put("send_sms_hash", true);

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);

            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(this, "Payment SuccessFul !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, s.toString(), Toast.LENGTH_SHORT).show();

    }
}
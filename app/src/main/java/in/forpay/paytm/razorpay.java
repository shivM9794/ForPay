package in.forpay.paytm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import in.forpay.R;
import in.forpay.util.Utility;

public class razorpay extends AppCompatActivity implements PaymentResultListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        startPayment();
    }
    @Override
    public void onPaymentSuccess(String s) {

        Utility.showToast(this,"Success "+s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Utility.showToast(this,"Failed "+s+" i is "+i);

    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "ForPay");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Test order final");
            options.put("order_id", "order_E3fkl7wBr4NkKI");
            options.put("currency", "INR");
            options.put("prefill.email","rajdeep74@gmail.com");


            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", "10000");

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("Razorpay", "Error in starting Razorpay Checkout", e);
        }
    }
}

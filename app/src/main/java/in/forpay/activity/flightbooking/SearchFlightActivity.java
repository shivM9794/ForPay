package in.forpay.activity.flightbooking;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import in.forpay.R;
import in.forpay.databinding.ActivitySearchFlightBinding;
import in.forpay.util.ProgressHelper;

public class SearchFlightActivity extends AppCompatActivity {

    private ActivitySearchFlightBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_flight);
        activity = this;
        progressHelper = new ProgressHelper(activity);

        basicMethods();

    }

    private void basicMethods() {

        binding.backArrow.setOnClickListener(v -> finish());

        binding.btnFilterFlight.setOnClickListener(v -> onClickFilter());
    }

    public void onClickFilter() {
        filterDialog();
    }

    private void filterDialog() {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_filter_flight);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }
}
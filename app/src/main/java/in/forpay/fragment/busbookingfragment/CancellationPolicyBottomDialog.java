package in.forpay.fragment.busbookingfragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.forpay.R;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class CancellationPolicyBottomDialog extends BottomSheetDialogFragment {

    View view;
    private Bundle bundle = new Bundle();
    private String cancellationPolicy;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        public void onSlide(@NonNull View view, float f) {
        }

        public void onStateChanged(@NonNull View view, int i) {
            if (i == 5) {
                CancellationPolicyBottomDialog.this.dismiss();
            }
        }
    };

    @SuppressLint({"RestrictedApi"})
    @Override
    public void setupDialog(Dialog dialog, int i) {
        super.setupDialog(dialog, i);
        view = View.inflate(getContext(), R.layout.bottom_dialog_bus_cancellation_policy, null);
        dialog.setContentView(view);
        ((CoordinatorLayout.LayoutParams) ((View) this.view.getParent()).getLayoutParams()).getBehavior();
        view.findViewById(R.id.iv_cut).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CancellationPolicyBottomDialog.this.dismiss();
            }
        });
        this.bundle = getArguments();
        this.cancellationPolicy = this.bundle.getString(Constant.KEY_CANCELLATION_POLICY);
        Utility.getCancellation(getContext(), this.cancellationPolicy, R.layout.item_cancellation_policy, (LinearLayout) view.findViewById(R.id.ll_cancellation));
    }

    @Override
    public void onActivityCreated(Bundle bundle2) {
        super.onActivityCreated(bundle2);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getDialog().getWindow().setGravity(80);
        Window window = getDialog().getWindow();
        double d = (double) displayMetrics.heightPixels;
        Double.isNaN(d);
        window.setLayout(-1, (int) (d * 0.5d));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

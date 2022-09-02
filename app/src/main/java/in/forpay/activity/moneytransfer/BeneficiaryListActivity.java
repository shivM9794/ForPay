package in.forpay.activity.moneytransfer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.BeneficiaryAdapter;
import in.forpay.databinding.ActivityBeneficiaryListBinding;
import in.forpay.model.response.GetCustomerHistoryResponse;

public class BeneficiaryListActivity extends AppCompatActivity {

    private ActivityBeneficiaryListBinding mBinding;
    private String mMobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_beneficiary_list);
        mBinding.setActivity(this);

        init();
    }

    private void init() {
        getBundle();
        setToolbar();
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Get data from bundle
     */
    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            mMobile = bundle.getString("mobile");

            ArrayList<GetCustomerHistoryResponse.Beneficiary> list = bundle.getParcelableArrayList("list");
            if (list != null && list.size() > 0) {
                setAdapter(list);
            }
        }
    }

    /**
     * Set toolbar
     */
    private void setToolbar() {
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        mBinding.beneficiaryTitle.setText("Beneficiary List");

    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<GetCustomerHistoryResponse.Beneficiary> list) {
        mBinding.recyclerView.setAdapter(new BeneficiaryAdapter(this, list,mMobile));
    }
}

package in.forpay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.SplashActivity;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentLogoutBinding;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.PreferenceConnector;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class LogoutFragment extends Fragment {
    private FragmentLogoutBinding mBinding;
    private DatabaseHelper databaseHelper;
    private ProgressHelper progressHelper;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    public LogoutFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_logout, container, false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void init() {
        mBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                logout("");

                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        mBinding.btnLogoutAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout("all");
            }
        });
    }

    private void logout(String type) {
        String imei = PreferenceConnector.readString(getContext(), PreferenceConnector.IMEI, "");
        String token = Utility.getToken(getActivity());
        String activeKey = PreferenceConnector.readString(getContext(),
                PreferenceConnector.ACTIVE_KEY, "");
        String mobile = Utility.getUsername(getActivity());
        PreferenceConnector.clear(getContext());
        PreferenceConnector.writeString(getContext(), PreferenceConnector.IMEI, imei);
        PreferenceConnector.writeString(getContext(), PreferenceConnector.ACTIVE_KEY, activeKey);
        PreferenceConnector.writeString(getContext(), PreferenceConnector.USER_NAME, mobile);
        //Log.d("Logout","mobile set to "+mobile);


        // Delete db table
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        databaseHelper.deleteServiceTypeTable(); // Delete service type table
        //databaseHelper.deleteOrderIdTable(); // Delete order id table
        databaseHelper.deleteRechargeHistoryTable(); // Delete recharge history table
        OxyMePref oxyMePref = new OxyMePref(getActivity());
        oxyMePref.clear();

        ArrayList<String> list = new ArrayList<>();
        if (getActivity() != null) {

            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(getActivity())); // key
            map1.put("imei", Utility.getImei(getActivity())); // imei
            map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
            map1.put("os", Utility.getOs(getActivity()));

            map1.put("type", type);

            String request = Utility.mapWrapper(getActivity(), map1);


            if (Utility.isNetworkConnected(getActivity())) {
                progressHelper = new ProgressHelper(getActivity());
                progressHelper.show();

                QueryManager.getInstance().postRequest(getActivity(),
                        Constant.METHOD_LOGOUT, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {
                                progressHelper.dismiss();

                                //Log.d("TEST","request - "+result);
                            }
                        });
            }
        }


        Intent intent3 = new Intent(getContext(), SplashActivity.class);
        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent3);



    }

}

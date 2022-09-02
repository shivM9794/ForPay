package in.forpay.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import in.forpay.R;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.TransactionCommissionModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class TransactionCommissionFragment extends Fragment {

    View rootView;
    private Activity activity;
    private ProgressHelper progressHelper;
    private RelativeLayout mainLay;
    private LinearLayout mainLinear;

    private RechargeNowListener mListener;

    @SuppressLint("ValidFragment")

    public TransactionCommissionFragment(RechargeNowListener listener) {
        this.mListener = listener;
    }

    public TransactionCommissionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_transaction_commission, container, false);

        activity = getActivity();
        progressHelper = new ProgressHelper(activity);
        mainLay = rootView.findViewById(R.id.mainLay);
//        mainLinear = rootView.findViewById(R.id.mainLinear);

        postAepsResponseBeforeCapture("commision");

        return rootView;
    }

    private void postAepsResponseBeforeCapture(String postData) {

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("responseFor", postData); // startDate

        String request = Utility.mapWrapper(activity, map1);

        if (in.forpay.util.Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_AEPS_DETAILS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            Log.e("res", result);


                            try {
                                JSONObject jsonObject = new JSONObject(result);

                                if (jsonObject.optString("resCode").equals(Constant.CODE_200)) {
                                    JSONArray commissionArray = jsonObject.getJSONArray("commissionArray");
                                    initUI(commissionArray);

                                }

                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }

                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(activity, getString(R.string.internet_connect), "");
        }


    }

    private void initUI(JSONArray commissionArray) throws JSONException {

        ArrayList<TransactionCommissionModel> commissionModels = new ArrayList<>();
        ArrayList<String> headings = new ArrayList<>();

        ScrollView scrollView = new ScrollView(activity);


        mainLinear = new LinearLayout(activity);

        mainLinear.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams mainLiner = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        scrollView.addView(mainLinear, mainLiner);

        scrollView.setVerticalScrollBarEnabled(true);
        mainLay.addView(scrollView);

        for (int i = 0; i < commissionArray.length(); i++) {

            if (i == 0) {
                JSONObject jsonObject = commissionArray.getJSONObject(0);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    headings.add(key);
                }
            }

            try {
                TransactionCommissionModel commissionModel = new Gson().fromJson(commissionArray.getString(i).trim(), TransactionCommissionModel.class);
                commissionModels.add(commissionModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


//        mainLinear.removeAllViews();

        TextView tabletitle = new TextView(activity);

        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(8, 8, 8, 2);
        titleParams.addRule(RelativeLayout.BELOW, R.id.mainLinear);
        int titleId = View.generateViewId();
        tabletitle.setId(titleId);
        tabletitle.setText("Commission: ");
        tabletitle.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins_bold));
        TableLayout tableLayout = new TableLayout(activity);

        tableLayout.setPadding(8, 8, 8, 8);

        TableRow head = new TableRow(activity);


        for (int i = 0; i < commissionModels.size(); i++) {

            Log.e(commissionModels.get(i).getType(), commissionModels.get(i).getAmount() + commissionModels.get(i).getCommission());


            TableRow row = new TableRow(activity);

            for (int k = 0; k < headings.size(); k++) {

                if (i == 0) {

                    TextView heading = rowData(headings.get(k).toUpperCase());
                    heading.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins_bold));
                    head.addView(heading);
                }

                TextView tv = rowData(commissionModels.get(i).getValueByKey(headings.get(k)).toUpperCase());
                tv.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins_semibold));
                row.addView(tv);

            }

            if (i == 0) {
                tableLayout.addView(head);
            }


            tableLayout.addView(row);


        }

        RelativeLayout.LayoutParams tableParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.BELOW, titleId);
        relativeParams.setMargins(8, 8, 8, 8);
//        ScrollView scrollView= new ScrollView(activity);
//        scrollView.setVerticalScrollBarEnabled(true);

        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(activity);
        horizontalScrollView.addView(tableLayout, tableParams);
        LinearLayout.LayoutParams hparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        horizontalScrollView.setHorizontalScrollBarEnabled(true);
        horizontalScrollView.setLayoutParams(hparams);
//        scrollView.addView(horizontalScrollView);

        mainLinear.addView(tabletitle, titleParams);

        mainLinear.addView(horizontalScrollView, hparams);


    }


    private TextView rowData(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(20, 20, 20, 20);
        //textView.setBackgroundResource(R.drawable.table_border);
        return textView;
    }


}
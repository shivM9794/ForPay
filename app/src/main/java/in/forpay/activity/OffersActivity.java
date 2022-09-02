package in.forpay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.balance.MobileActivity;
import in.forpay.database.DataPlansModel;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityOffersBinding;
import in.forpay.fragment.recharge.DTHFragment;
import in.forpay.fragment.recharge.MobileFragment;
import in.forpay.fragment.recharge.OffersFragment;
import in.forpay.listener.OfferListener;
import in.forpay.model.request.OfferModel;
import in.forpay.model.response.GetLoginValidateResponse;
import in.forpay.model.response.GetOfferResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class OffersActivity extends AppCompatActivity {

    private ActivityOffersBinding mBinding;
    private int circleSelect = 0;
    private String circleId = "";
    private ProgressHelper progressHelper;
    private OffersFragment splFragment;// SPL
    private OffersFragment dataFragment;// DATA
    private OffersFragment fttFragment;// FTT
    private OffersFragment tupFragment;// TUP
    private OffersFragment rmgFragment;// RMG
    private OffersFragment otrFragment;// OTR
    private String operatorId = "";
    private String from = "";
    final private String searchType = "local";
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_offers);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(OffersActivity.this, mBinding.appbarLayout);
    }

    /**
     * Click on choose circle
     */
    public void onClickCircle() {
        chooseCircle();
    }

    private void init() {
        getBundle();
        progressHelper = new ProgressHelper(this);
        setToolbar();
        mBinding.viewPager.setOffscreenPageLimit(6);
        setupViewPager(mBinding.viewPager);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        String msg = getApplicationContext().getResources().getString(R.string.disclaimer_description);
        SpannableString ss=new SpannableString(msg);
        StyleSpan boldSpan=new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan,0,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.wesupport.setText(ss);


        if (from.equals("dth")) {
            if(!Constant.OFFER_DATA_PLAN2.equals("")){
                getOfferNew(Constant.OFFER_DATA_PLAN2);
            }
            if (DTHFragment.selectedcircle!=null)
                getOffer(DTHFragment.selectedcircle);
            else
                getOffer(MobileActivity.selectedcircle);
        } else {
            if(!Constant.OFFER_DATA_PLAN1.equals("")){
                getOfferNew(Constant.OFFER_DATA_PLAN1);
            }
            if (MobileFragment.selectedcircle!=null){
                getOffer(MobileFragment.selectedcircle);
            }else {
                getOffer(MobileActivity.selectedcircle);
            }
            //getMobileDataOffers();


        }


/*

        databaseHelper = new DatabaseHelper(OffersActivity.this);

        final ArrayList<DataPlansModel> arrayList = databaseHelper.getDataPlans("0");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NewmakeList(arrayList);

            }
        },1000);
*/


    }

    private void getOfferNew(final String result) {

        databaseHelper = new DatabaseHelper(OffersActivity.this);
        databaseHelper.deleteDataPlansTable();


        try {


            JSONObject jsonObject = new JSONObject(result);
            JSONArray dataarray = jsonObject.getJSONArray("data");
            Log.e("DataInst", dataarray.length() + "");

            for (int j = 0; j < dataarray.length(); j++) {

                JSONObject jsonObject1 = dataarray.getJSONObject(j);

                String type = jsonObject1.getString("type");
                String amount = jsonObject1.getString("amount");
                String detail = jsonObject1.getString("detail");
                String validity = jsonObject1.getString("validity");
                String talktime = jsonObject1.getString("talktime");
                String extraOffer = jsonObject1.getInt("extraOffer") + "";
                String commission = jsonObject1.getString("commission");
                String data = jsonObject1.getString("data");

                try {
                    databaseHelper.insertDataPlans(new DataPlansModel(type, amount, detail, validity, talktime, extraOffer, commission, data));
                } catch (Exception e3) {

                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    private void getMobileDataOffers() {


        //Log.e("Responnse", MobileFragment.mobileoffersresponse);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //parseResponse(MobileFragment.mobileoffersresponse, "");
            }
        }, 1000);


//        parseResponse(MobileFragment.mobileoffersresponse, "");


    }


    /**
     * Get data from bundle
     */
    private void getBundle() {
            operatorId = getIntent().getStringExtra("id");
            from = getIntent().getStringExtra("from");
            Log.e("FROM ", from);

    }

    /**
     * Set toolbar
     */
    private void setToolbar() {
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        mBinding.offerTitle.setText("Offer Plan");
    }

    /**
     * Show choose circle selection dialog
     */

    private void chooseCircle() {
        final ArrayList<GetLoginValidateResponse.Circle> list = Utility.getCircleList(this);
        if (list != null && list.size() > 0) {
            ArrayList<String> listTemp = new ArrayList<>();
            for (GetLoginValidateResponse.Circle circle : list) {
                listTemp.add(circle.getCircle());
            }
            final String[] listArray = listTemp.toArray(new String[listTemp.size()]);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("Choose an circle");
            mBuilder.setSingleChoiceItems(listArray, circleSelect, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    circleSelect = i;
                    circleId = list.get(i).getId();
                    mBinding.textViewCircle.setText(listArray[i]);
                    dialogInterface.dismiss();


                    Map<String,String> map1 = new HashMap<>();

                    map1.put("token",Utility.getToken(OffersActivity.this)); // key
                    map1.put("imei",Utility.getImei(OffersActivity.this)); // imei
                    map1.put("versionCode",Utility.getVersionCode(OffersActivity.this)); // version code
                    map1.put("os", Utility.getOs(OffersActivity.this));
                    map1.put("operatorId" , operatorId); // operatorId
                    map1.put("circleId" , circleId); // circleId

                    String request = Utility.mapWrapper(OffersActivity.this,map1);



                    getOffer(request);
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } else {
            Utility.showToast(this, getString(R.string.something_went_wrong), "");
        }
    }

    /**
     * Make other list
     */
    private void makeList(ArrayList<GetOfferResponse.Data> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        ArrayList<OfferModel> splList = new ArrayList<>();
        ArrayList<OfferModel> dataList = new ArrayList<>();
        ArrayList<OfferModel> fttList = new ArrayList<>();
        ArrayList<OfferModel> tupList = new ArrayList<>();
        ArrayList<OfferModel> rmgList = new ArrayList<>();
        ArrayList<OfferModel> otrList = new ArrayList<>();

        for (GetOfferResponse.Data data : list) {
            if (!TextUtils.isEmpty(data.getType())) {
                if (data.getType().equalsIgnoreCase("SPL")) {
                    splList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("DATA")) {
                    dataList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("FTT")) {
                    fttList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("TUP")) {
                    tupList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("RMG")) {
                    rmgList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("OTR")) {
                    otrList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                }
            }
        }

        splFragment.setAdapter(splList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        dataFragment.setAdapter(dataList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);

                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        fttFragment.setAdapter(fttList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tupFragment.setAdapter(tupList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rmgFragment.setAdapter(rmgList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        otrFragment.setAdapter(otrList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * Refresh ui
     */


    private void NewmakeList(ArrayList<DataPlansModel> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }

        ArrayList<OfferModel> splList = new ArrayList<>();
        ArrayList<OfferModel> dataList = new ArrayList<>();
        ArrayList<OfferModel> fttList = new ArrayList<>();
        ArrayList<OfferModel> tupList = new ArrayList<>();
        ArrayList<OfferModel> rmgList = new ArrayList<>();
        ArrayList<OfferModel> otrList = new ArrayList<>();

        for (DataPlansModel data : arrayList) {
            if (!TextUtils.isEmpty(data.getType())) {
                if (data.getType().equalsIgnoreCase("SPL")) {
                    splList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("DATA")) {
                    dataList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("FTT")) {
                    fttList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("TUP")) {
                    tupList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("RMG")) {
                    rmgList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                } else if (data.getType().equalsIgnoreCase("OTR")) {
                    otrList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                            , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
                }
            }
        }

        splFragment.setAdapter(splList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        dataFragment.setAdapter(dataList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);

                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        fttFragment.setAdapter(fttList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tupFragment.setAdapter(tupList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rmgFragment.setAdapter(rmgList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        otrFragment.setAdapter(otrList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    private void refreshUI() {
        ArrayList<OfferModel> splList = new ArrayList<>();
        ArrayList<OfferModel> dataList = new ArrayList<>();
        ArrayList<OfferModel> fttList = new ArrayList<>();
        ArrayList<OfferModel> tupList = new ArrayList<>();
        ArrayList<OfferModel> rmgList = new ArrayList<>();
        ArrayList<OfferModel> otrList = new ArrayList<>();
        splFragment.setAdapter(splList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        dataFragment.setAdapter(dataList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        fttFragment.setAdapter(fttList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tupFragment.setAdapter(tupList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rmgFragment.setAdapter(rmgList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        otrFragment.setAdapter(otrList, new OfferListener() {
            @Override
            public void onSelect(String amount, String details, String extraoffer, String commission) {
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                intent.putExtra("details", details);
                intent.putExtra("extraoffer", extraoffer);
                intent.putExtra("commission", commission);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * Set view pager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(splFragment = new OffersFragment(), "SPECIAL");
        adapter.addFragment(rmgFragment = new OffersFragment(), "ROAMING");
        adapter.addFragment(tupFragment = new OffersFragment(), "TOP UP");
        adapter.addFragment(dataFragment = new OffersFragment(), "DATA");
        adapter.addFragment(otrFragment = new OffersFragment(), "OTHER");
        adapter.addFragment(fttFragment = new OffersFragment(), "FULL TALKTIME");


        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /**
     * Get offer after select circle
     */
    private void getOffer(final String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            if (searchType.equals("local")) {

                Log.e("LOG ", "showing from local prev");
                QueryManager.getInstance().postRequest(this,
                        Constant.METHOD_PING, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {



                                databaseHelper = new DatabaseHelper(OffersActivity.this);

                                ArrayList arrayList = databaseHelper.getDataPlans("0");

                                parseResponse(result, arrayList);


                            }
                        });

            } else {

                QueryManager.getInstance().postRequest(this,
                        Constant.METHOD_GET_OFFER, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {

                                databaseHelper = new DatabaseHelper(OffersActivity.this);

                                ArrayList arrayList = databaseHelper.getDataPlans("0");

                                parseResponse("", arrayList);


                                Gson gson = new GsonBuilder().create();
                                JsonArray myCustomArray = gson.toJsonTree(arrayList).getAsJsonArray();

                                Log.e("Offer json ", " data " + myCustomArray);

                                //HashMap sc = new HashMap();
                                //sc.put("data", "value");

                                ArrayList<HashMap<String, ArrayList>> arrayList2 = new ArrayList<HashMap<String, ArrayList>>();
                                HashMap<String, ArrayList> h2 = new HashMap<String, ArrayList>();
                                h2.put("data", arrayList);

                                arrayList2.add(h2);


                                JsonArray myCustomArray2 = gson.toJsonTree(arrayList2).getAsJsonArray();

                                Log.e("Offer json 2", " data " + myCustomArray2);


                            }
                        });
            }

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result, ArrayList arrayList) {
        Log.e("Error ", "showing  1" + searchType + "result -" + result);
        if (!isDestroyed()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                Log.e("Error ", "showing " + searchType);
                if (searchType.equals("local")) {

                    Log.e("LOG ", "showing from local");
                    //GetOfferResponse response = new Gson().fromJson(result, GetOfferResponse.class);


                    NewmakeList(arrayList);


                } else {
                    Log.e("Error ", "showing live");
                    GetOfferResponse response = new Gson().fromJson(result, GetOfferResponse.class);
                    if (response.getResCode().equals(Constant.CODE_200)) {
                        makeList(response.getOfferList());


                    } else {
                        refreshUI();
                        Utility.showToast(this, response.getResText(), response.getResCode());
                    }
                }
            } else {
                //Utility.showToast(this, getString(R.string.server_not_responding));
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }
        }
    }
}

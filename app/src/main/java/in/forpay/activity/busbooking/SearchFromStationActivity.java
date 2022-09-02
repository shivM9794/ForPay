package in.forpay.activity.busbooking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.busbookingadapter.SearchCityAdapter;
import in.forpay.databinding.ActivitySearchFromStationBinding;
import in.forpay.model.CityList;
import in.forpay.network.CallbackListener;
import in.forpay.network.ItemClickListeners;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class SearchFromStationActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListeners {

    private static Handler handler;
    ActivitySearchFromStationBinding searchStationBinding;
    SearchCityAdapter searchCityAdapter;
    ArrayList<CityList.DataBean.BusStopsBean> cityList;

    TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (i2 == 0) {

                if (searchCityAdapter != null) {
                    searchCityAdapter.clearListByDefault(new ArrayList<>());

                    searchStationBinding.tvClear.setVisibility(View.GONE);
                } else {
                    searchStationBinding.tvClear.setVisibility(View.VISIBLE);
                }

            } else {
                if (searchCityAdapter != null)
                    searchCityAdapter.getFilter().filter(charSequence.toString());

                searchStationBinding.tvClear.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {


        }
    };
    private CityList.DataBean.BusStopsBean busStopsBean;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_from_station);

        oxyMePref = new OxyMePref(this);

        searchStationBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_from_station);
        searchStationBinding.setSearchStationActivity(this);

        progressHelper = new ProgressHelper(this);

        String isSourceOrDestination = getIntent().getStringExtra(Constant.SOURCE_DESTINATION);
        Log.d("BusActivity"," isSourceOrDestination "+isSourceOrDestination);

        if (Objects.equals(isSourceOrDestination, Constant.SOURCE)) {

            searchStationBinding.editSearchStation.setHint(getString(R.string.enter_city));

            getSourceCity();

        } else if (Objects.equals(isSourceOrDestination, Constant.DESTINATION)) {
            busStopsBean = getIntent().getParcelableExtra(Constant.SOURCE_CITY);
            searchStationBinding.editSearchStation.setHint(getString(R.string.enter_destination));
            getDestinationCity();
        }


        setViewClickListeners();


        searchStationBinding.editSearchStation.addTextChangedListener(editTextWatcher);

        searchStationBinding.editSearchStation.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(searchStationBinding.editSearchStation, InputMethodManager.SHOW_IMPLICIT);

        ItemClickListeners itemClickListeners = this;

        handler = new Handler() {

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                if (msg.what == 101) {
                    searchCityAdapter = new SearchCityAdapter(cityList, itemClickListeners);
                    searchStationBinding.recSearchStation.setAdapter(searchCityAdapter);
                }

            }
        };

    }

    private void getSourceCity() {

        searchStationBinding.recSearchStation.setLayoutManager(new LinearLayoutManager(this));

        if (!oxyMePref.getBoolean(Constant.CALL_BUS_STOP_API)){


            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(this)); // key
            map1.put("imei",Utility.getImei(this)); // imei
            map1.put("versionCode",Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));

            String request = Utility.mapWrapper(this,map1);


            Log.d("BusActivity"," request called "+request);
            getBusStops(request);

        }else{

            String city = oxyMePref.getString(Constant.BUS_STOP_LIST);
            Log.d("BusActivity"," city "+city+"jhh");
            //Log.e("city",city+"jhh");

            CityList apiCityList = new Gson().fromJson(city, CityList.class);

            if (busStopsBean != null){

                for (CityList.DataBean.BusStopsBean busStopSource : apiCityList.getData().getBusStops()) {

                    if (busStopSource.getSourceId().equals(busStopsBean.getSourceId())){
                        apiCityList.getData().getBusStops().remove(busStopSource);
                        break;
                    }
                }

            }

            cityList = new ArrayList<>();
            if (apiCityList!=null)
                cityList.addAll(apiCityList.getData().getBusStops());

            searchCityAdapter = new SearchCityAdapter(cityList, this);
            searchStationBinding.recSearchStation.setAdapter(searchCityAdapter);
        }





    }

    private void getBusStops(String request) {

        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.BUS_STOPS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }


    }

    private void parseResponse(String result) {

        if (!isFinishing()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {

                Log.e("SEARCH BUS ::", result);

                oxyMePref.putString(Constant.BUS_STOP_LIST,result);

                CityList apiCityList = new Gson().fromJson(result, CityList.class);

                if (busStopsBean != null){

                    for (CityList.DataBean.BusStopsBean busStopSource : apiCityList.getData().getBusStops()) {

                        if (busStopSource.getSourceId().equals(busStopsBean.getSourceId())){
                            apiCityList.getData().getBusStops().remove(busStopSource);
                            break;
                        }
                    }

                }

                cityList = new ArrayList<>();
                cityList.addAll(apiCityList.getData().getBusStops());

                searchCityAdapter = new SearchCityAdapter(cityList, this);
                searchStationBinding.recSearchStation.setAdapter(searchCityAdapter);



            } else {
                //Utility.showToast(this, getString(R.string.server_not_responding), "");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }
        }


    }

    private void getDestinationCity() {

        getSourceCity();
    }

    private void setViewClickListeners() {
        searchStationBinding.imgBack.setOnClickListener(this);
        searchStationBinding.tvClear.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.tvClear:
                searchStationBinding.editSearchStation.setText(null);
                break;
        }


    }

    @Override
    public void onItemClick(int pos) {

        Intent intent = new Intent();
        intent.putExtra(Constant.SOURCE, cityList.get(pos));
        setResult(RESULT_OK, intent);
        finish();

    }


}

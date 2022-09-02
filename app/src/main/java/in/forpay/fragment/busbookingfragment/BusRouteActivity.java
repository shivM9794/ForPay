package in.forpay.fragment.busbookingfragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.busbookingadapter.BusRouteAdapter;
import in.forpay.databinding.ActivityBusRouteBinding;
import in.forpay.model.busbookingModel.BusRoutModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BusRouteActivity extends AppCompatActivity {

    private ActivityBusRouteBinding binding;
    private Activity activity;
    private String ticket_id;
    private ProgressHelper progressHelper;
    private ArrayList<Object> arrayList_boarding = new ArrayList<>();
    private ArrayList<Object> arrayList_dropping = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusRouteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;


        binding.backBtn.setOnClickListener(view -> onBackPressed());

        ticket_id = getIntent().getStringExtra(Constant.TICKET_ID);


        getRoute(ticket_id);

    }


    private void getRoute(String ticket_id) {

        progressHelper = new ProgressHelper(activity);


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode" , Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));


        map1.put("ticketId" , ticket_id);


        String request = Utility.mapWrapper(activity,map1);

        getBookSeatDetailResponse(request);
    }

    private void getBookSeatDetailResponse(String request) {

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.BUS_ROUTE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void parseResponse(String result) {
        progressHelper.dismiss();

        BusRoutModel model = new Gson().fromJson(result, BusRoutModel.class);

        if (model.getResCode().equals("200")) {

            for (int i = 0; i < model.getData().getRouteList().size(); i++) {
                if (model.getData().getRouteList().get(i).getType().equals("boardingPoint")) {

                    arrayList_boarding.add(model.getData().getRouteList().get(i));
                } else {
                    arrayList_dropping.add(model.getData().getRouteList().get(i));
                }
            }

            binding.recBoarding.setLayoutManager(new LinearLayoutManager(activity));
            BusRouteAdapter adapter = new BusRouteAdapter(activity, arrayList_boarding);
            binding.recBoarding.setAdapter(adapter);

            binding.recDropping.setLayoutManager(new LinearLayoutManager(activity));
            BusRouteAdapter adapter2 = new BusRouteAdapter(activity, arrayList_dropping);
            binding.recDropping.setAdapter(adapter2);

            binding.travelsName.setText(model.getBusName());
            binding.travelsType.setText(model.getBusType());
        }
        else{
            Utility.showToast(activity, model.getResText(), "ERROR");
        }


    }
}
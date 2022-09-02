package in.forpay.fragment.shop;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.shop.MyRatingAdapter;
import in.forpay.databinding.AdapterNoDataFoundBinding;
import in.forpay.databinding.FragmentShopRatingBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.GetMyRatingModel;
import in.forpay.model.shop.GetUserRating;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ShopRatingFragment extends Fragment implements ItemClickListener {

    private FragmentShopRatingBinding binding;
    private Activity activity;
    private MyRatingAdapter adapter;
    private ArrayList<Object> arrayList;
    private ProgressHelper progressHelper;

    public ShopRatingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentShopRatingBinding.inflate(inflater, container, false);
        activity=getActivity();
        progressHelper=new ProgressHelper(activity);

        init();

        return binding.getRoot();
    }

    private void init() {
        setMyRateAdapter();
        getMyRating();
    }

    private void setMyRateAdapter() {
        arrayList=new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new MyRatingAdapter(activity,arrayList,true,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, String tag) {

    }

    private void getMyRating() {
        if (Utility.isNetworkConnected(activity)) {

            ArrayList<String> list = new ArrayList<>();
            list.add("token=" + Utility.getToken(activity));
            list.add("imei=" + Utility.getImei(activity));
            list.add("appVersion=" + Utility.getVersionCode(activity));
            String request = Utility.dataWrapper(list);
            getMyRatingRequest(request);

        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void getMyRatingRequest(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_GET_MY_RATING, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGetMyRatingResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void parseGetMyRatingResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("GetMyRatingResponse","response "+response);

            GetMyRatingModel model= new Gson().fromJson(response, GetMyRatingModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                if (model.getData().getShopRating()!=null){
                    arrayList.addAll(model.getData().getShopRating());
                    adapter.notifyDataSetChanged();
                }

                if (arrayList.size()==0){
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Review Found");
                }
            }else
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Review Found");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
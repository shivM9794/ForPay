package in.forpay.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.shop.ShopChatAdapter;
import in.forpay.databinding.ActivityShopChatBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ChatHistoryModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ShopChatActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityShopChatBinding binding;
    private AppCompatActivity activity;
    private ProgressHelper progressHelper;
    private String shopId,productId;
    private ArrayList<ChatHistoryModel.DataBean> arrayList;
    private ShopChatAdapter adapter;
    private File directory;
    private String staticId="5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShopChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        productId=getIntent().getStringExtra(Constant.PRODUCT_ID);

        init();
    }

    private void init() {
        setShopChatAdapter();
        getChatHistory();
        binding.backPress.setOnClickListener(v->onBackPressed());
    }

    private void setShopChatAdapter() {
        arrayList = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShopChatAdapter(activity, arrayList,this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void getChatHistory() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code

            String request = Utility.mapWrapper(this, map1);


            getChatRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void getChatRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_GET_CHAT_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGetShopChatResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseGetShopChatResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("GetShopChatResponse","response "+response);

            ChatHistoryModel model= new Gson().fromJson(response, ChatHistoryModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                arrayList.addAll(model.getData());
                adapter.notifyDataSetChanged();

                if(arrayList.size()==0){
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Chat Data Found");
                }
            }
            else {
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Chat Data Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position, String tag) {
        startActivity(new Intent(activity,ShopListChatActivity.class)
                .putExtra(Constant.SHOP_ID,arrayList.get(position).getShopId())
                .putExtra(Constant.TO_USER_ID,arrayList.get(position).getToUserId()));
    }
}
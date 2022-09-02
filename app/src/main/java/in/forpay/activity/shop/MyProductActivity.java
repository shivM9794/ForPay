package in.forpay.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.shop.MyProductAdapter;
import in.forpay.databinding.ActivityMyProductBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ProductSuggestionModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.PaginationListener;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

import static in.forpay.util.PaginationListener.PAGE_START;

public class MyProductActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityMyProductBinding binding;
    private AppCompatActivity activity;
    private MyProductAdapter adapter;
    private ProgressHelper progressHelper;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    //private ArrayList<MyProductModel.DataBean.ProductSuggestionBean> arrayListTray;
    private ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayList;
    private ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayListTemp=new ArrayList<>();
    private ArrayList<ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean>> arrayListTray=new ArrayList<>();
    private ArrayList<ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean>> tempArrayListTray;

    private int index = 0;
    private String searchTxt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        init();
    }

    private void init() {
        setAdapter();
        onScrollListener();
        getMyAvailableProduct(true);

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //getMyAvailableProduct(false,true);
                //currentPage = 1;
                //adapter.removeData();
                searchTxt=editable.toString().toLowerCase().trim();
                ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayListFilter=new ArrayList();
                try{
                    if (editable.toString().isEmpty()) {
                        arrayListFilter = arrayList;
                    } else {
                        for (ProductSuggestionModel.DataBean.ProductSuggestionBean items : arrayList){
                            if (items.getBrand().toLowerCase().trim().contains(searchTxt)||items.getName().toLowerCase().trim().contains(searchTxt)){
                                arrayListFilter.add(items);
                            }
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                ArrayList<ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean>> trayList=createImageListTray(arrayListFilter);
                arrayListTray=new ArrayList<>();
                arrayListTray.addAll(trayList);
                adapter.removeData();
                adapter.addItems(trayList);

                if (trayList.size()==0){
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Data Found");
                    binding.dotsIndicator.setVisibility(View.GONE);
                }else {
                    binding.inflateLayout.removeAllViews();
                    binding.inflateLayout.addView(binding.recyclerView);
                    binding.dotsIndicator.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.backPress.setOnClickListener(view -> onBackPressed());
    }

    private void setAdapter() {
        arrayList=new ArrayList<>();
        tempArrayListTray=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new MyProductAdapter(activity,tempArrayListTray,this);
        binding.recyclerView.setAdapter(adapter);
        new PagerSnapHelper().attachToRecyclerView(binding.recyclerView);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                binding.dotsIndicator.setSelected(linearLayoutManager.findFirstVisibleItemPosition());
                binding.dotsIndicator.setCount(arrayListTray.size());
            }
        });
    }

    @Override
    public void onItemClick(int position, String tag) {
        try{
            int pagePosition=Integer.parseInt(tag);
            startActivityForResult(new Intent(activity,ProductSuggestionActivity.class)
            .putExtra(Constant.MODEL,arrayListTray.get(pagePosition).get(position))
            .putExtra(Constant.IS_FROM_MY_PRODUCT_ACTIVITY,true),7);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==7 && resultCode==RESULT_OK){
            ProductSuggestionModel.DataBean.ProductSuggestionBean model=data.getParcelableExtra(Constant.MODEL);
            arrayList.remove(model.getPosition());

            ArrayList<ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean>> trayList=createImageListTray(arrayList);
            arrayListTray=new ArrayList<>();
            arrayListTray.addAll(trayList);
            adapter.removeData();
            adapter.addItems(trayList);

            if (trayList.size()==0){
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Data Found");
                binding.dotsIndicator.setVisibility(View.GONE);
            }
            binding.edtSearch.setText("");
        }
    }

    private ArrayList<ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean>> createImageListTray(ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayList) {
        tempArrayListTray=new ArrayList<>();
        index=0;
        ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayListDivide=new ArrayList<>();
        ArrayList<ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean>> tempArrayListTray=new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            ProductSuggestionModel.DataBean.ProductSuggestionBean modelAddImg= arrayList.get(i);
            modelAddImg.setImageUrl(arrayList.get(i).getImageUrl());
            modelAddImg.setShopId(arrayList.get(i).getShopId());
            modelAddImg.setPosition(i);
            arrayListDivide.add(modelAddImg);

            if (arrayListDivide.size()%4==0){
                tempArrayListTray.add(arrayListDivide);
                arrayListDivide=new ArrayList<>();
                index++;
            }else if (arrayList.size()/4==index && arrayListDivide.size()== arrayList.size()%4){
                tempArrayListTray.add(arrayListDivide);
                arrayListDivide=new ArrayList<>();
                index++;
            }
        }
        return tempArrayListTray;
    }


    private void onScrollListener() {
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        binding.recyclerView.addOnScrollListener(new PaginationListener(5,linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                getMyAvailableProduct(false);
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void getMyAvailableProduct(boolean showDialog) {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("keyword",binding.edtSearch.getText().toString());
            map1.put("pageNumber", String.valueOf(currentPage));
            String request = Utility.mapWrapper(this, map1);

            getMyAvailableProductRequest(request,showDialog);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void getMyAvailableProductRequest(String request, boolean showDialog) {
        if (Utility.isNetworkConnected(this)) {
            if (showDialog)
                progressHelper.show();

            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_MY_AVAILABLE_PRODUCT, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseMyAvailableProductResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseMyAvailableProductResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            ProductSuggestionModel model=new Gson().fromJson(response, ProductSuggestionModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                Log.d("GetMyAvailableProduct","response "+response);

                arrayList.addAll(model.getData().getProductSuggestion());
                arrayListTemp.addAll(model.getData().getProductSuggestion());
                tempArrayListTray=new ArrayList<>();

                index=0;
                ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> newArray=new ArrayList<>(model.getData().getProductSuggestion());
                ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayListDivide=new ArrayList<>();
                for (int i = 0; i < newArray.size(); i++) {
                    ProductSuggestionModel.DataBean.ProductSuggestionBean modelAddImg=newArray.get(i);
                    modelAddImg.setImageUrl(model.getData().getImgUrl());
                    modelAddImg.setShopId(model.getShopId());
                    modelAddImg.setPosition(i);
                    arrayListDivide.add(modelAddImg);

                    if (arrayListDivide.size()%4==0){
                        tempArrayListTray.add(arrayListDivide);
                        arrayListDivide=new ArrayList<>();
                        index++;
                    }else if (newArray.size()/4==index && arrayListDivide.size()==newArray.size()%4){
                        tempArrayListTray.add(arrayListDivide);
                        arrayListDivide=new ArrayList<>();
                        index++;
                    }
                }

                arrayListTray.addAll(tempArrayListTray);
                /**
                 * manage progress view
                 */
                if (currentPage != PAGE_START)
                    adapter.removeLoading();
                adapter.addItems(tempArrayListTray);
                // check weather is last page or not
                if (model.getData().getProductSuggestion().size()>=20){
                    adapter.addLoading();
                }else {
                    isLastPage = true;
                }
                isLoading = false;

                if (arrayList.size()==0){
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Data Found");
                    binding.dotsIndicator.setVisibility(View.GONE);
                }else {
                    binding.inflateLayout.removeAllViews();
                    binding.inflateLayout.addView(binding.recyclerView);
                    binding.dotsIndicator.setVisibility(View.VISIBLE);
                }

            } else {
                Utility.showToast(activity, model.getResText(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
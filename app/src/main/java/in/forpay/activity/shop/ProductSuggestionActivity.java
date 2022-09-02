package in.forpay.activity.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import in.forpay.R;
import in.forpay.adapter.shop.ProductSuggestionAdapter;
import in.forpay.databinding.ActivityProductSuggestionBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.InfoModel;
import in.forpay.model.shop.ProductSuggestionModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.PaginationCardStackListener;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

import static in.forpay.util.PaginationCardStackListener.PAGE_START;

public class ProductSuggestionActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityProductSuggestionBinding binding;
    private AppCompatActivity activity;
    private ProductSuggestionAdapter adapter;
    private ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayList;
    private ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> tempArrayList;
    private ProgressHelper progressHelper;
    private CardStackLayoutManager cardStackLayoutManager;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    public final static String YES="yes";
    public final static String NO="no";
    private String shopId="";
    private String TAG="ProductSuggestionActivity";
    private int currentPosition=-1;
    private boolean isFromMyProduct;
    private ProductSuggestionModel.DataBean.ProductSuggestionBean model;

    private Timer timer=new Timer();
    private final long DELAY = 500; // milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityProductSuggestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        isFromMyProduct=getIntent().getBooleanExtra(Constant.IS_FROM_MY_PRODUCT_ACTIVITY,false);
        model=getIntent().getParcelableExtra(Constant.MODEL);

        init();
    }

    private void init() {
        setAdapter();
        onScrollListener();
        if (isFromMyProduct){
            shopId=model.getShopId();
            arrayList.add(model);
            tempArrayList.add(model);
            adapter.notifyDataSetChanged();
            //Utility.imageLoader(activity,model.getImageUrl(),binding.image);
            binding.searchLayout.setVisibility(View.GONE);
        }else
            getProductSuggestion(true);
        recyclerViewScrollListener();

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    // TODO: do what you need here (refresh list)
                                    // you will probably need to use runOnUiThread(Runnable action) for some specific actions (e.g. manipulating views)
                                    currentPage=1;
                                    adapter.removeData();
                                    arrayList=new ArrayList<>();
                                    tempArrayList=new ArrayList<>();
                                    getProductSuggestion(false);
                                }
                            },
                            DELAY
                    );

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        binding.backPress.setOnClickListener(view -> onBackPressed());
    }

    private void recyclerViewScrollListener() {
        /*binding.cardStackView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int currentFirstVisible = cardStackLayoutManager.findFirstVisibleItemPosition();
                if (currentPosition!=currentFirstVisible){
                    if (currentPosition>currentFirstVisible){
                        Log.e(TAG,currentFirstVisible +" -----> "+"Scrolling up");
                        callUpdateAvailableProduct(arrayList.get(currentFirstVisible).getId(),NO);
                    }else {
                        Log.e(TAG,currentFirstVisible +" -----> "+"Scrolling down");
                        callUpdateAvailableProduct(arrayList.get(currentFirstVisible).getId(),YES);
                    }
                    currentPosition=currentFirstVisible;
                    Log.e(TAG,currentPosition +" -----> "+"Current Position");
                }
            }
        });*/
    }

    private void setAdapter() {
        arrayList=new ArrayList<>();
        tempArrayList=new ArrayList<>();
        cardStackLayoutManager=new CardStackLayoutManager(activity,cardStackListener);
        cardStackLayoutManager.setStackFrom(StackFrom.None);
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setTranslationInterval(8.0f);
        cardStackLayoutManager.setScaleInterval(0.95f);
        cardStackLayoutManager.setSwipeThreshold(0.3f);
        cardStackLayoutManager.setMaxDegree(0.0f);
        cardStackLayoutManager.setDirections(Direction.VERTICAL);
        cardStackLayoutManager.setCanScrollHorizontal(true);
        cardStackLayoutManager.setCanScrollVertical(true);
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        cardStackLayoutManager.setOverlayInterpolator(new LinearInterpolator());

        binding.cardStackView.setLayoutManager(cardStackLayoutManager);
        adapter=new ProductSuggestionAdapter(activity,tempArrayList,this);
        binding.cardStackView.setAdapter(adapter);
        /*SnapHelper snapHelper=new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerView);*/
    }

    @Override
    public void onItemClick(int position, String tag) {
        switch (tag){
            case YES:
                clickOnButton(Direction.Top);
                break;
            case NO:
                clickOnButton(Direction.Bottom);
                break;
        }
    }

    private void clickOnButton(Direction direction) {
    //CardStackListener call
        SwipeAnimationSetting setting1 = new SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(Duration.Slow.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        cardStackLayoutManager.setSwipeAnimationSetting(setting1);
        binding.cardStackView.swipe();
    }

    private void onScrollListener() {
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        binding.cardStackView.addOnScrollListener(new PaginationCardStackListener(10,cardStackLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                getProductSuggestion(false);
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

    private void getProductSuggestion(boolean showDialog) {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("keyword",binding.edtSearch.getText().toString());
            map1.put("pageNumber", String.valueOf(currentPage));

            String request = Utility.mapWrapper(this, map1);


            getProductSuggestionRequest(request,showDialog);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void getProductSuggestionRequest(String request,boolean showDialog) {
        if (Utility.isNetworkConnected(this)) {
            if (showDialog)
                progressHelper.show();

            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_PRODUCTION_SUGGESTION, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseProductSuggestionResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    @SuppressLint("LongLogTag")
    private void parseProductSuggestionResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            ProductSuggestionModel model=new Gson().fromJson(response, ProductSuggestionModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                Log.d("ProductSuggestionResponse","response "+response);
                arrayList.addAll(model.getData().getProductSuggestion());
                tempArrayList=new ArrayList<>();
                tempArrayList.addAll(model.getData().getProductSuggestion());

                shopId=model.getShopId();
                //Utility.imageLoader(activity,model.getData().getImgUrl(),binding.image);
                /**
                 * manage progress view
                 */
                if (currentPage != PAGE_START)
                    adapter.removeLoading();
                adapter.addItems(tempArrayList);
                // check weather is last page or not
                if (model.getData().getProductSuggestion().size()>=10){
                    adapter.addLoading();
                }else {
                    isLastPage = true;
                }
                isLoading = false;

                if (arrayList.size()==0){
                    binding.noDataFound.setVisibility(View.VISIBLE);
                }else {
                    if (binding.noDataFound.getVisibility()==View.VISIBLE)
                        binding.noDataFound.setVisibility(View.GONE);
                }

            } else {
                Utility.showToast(activity, model.getResText(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callUpdateAvailableProduct(String productId,String available) {
        if (Utility.isNetworkConnected(activity)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("shopId",shopId);
            map1.put("productId",productId);
            map1.put("available",available);

            String request = Utility.mapWrapper(this, map1);

            callUpdateAvailableProductRequest(request,productId,available);

        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    private void callUpdateAvailableProductRequest(String request,String productId,String available) {
        if (Utility.isNetworkConnected(activity)) {
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_UPDATE_AVAILABLE_PRODUCT, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseUpdateAvailableProductResponse(result, responseManager,productId,available);
                        }
                    });
        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    private void parseUpdateAvailableProductResponse(String response, ResponseManager responseManager,String productId,String available) {
        try {
            InfoModel model=new Gson().fromJson(response, InfoModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                Log.d("UpdateAvailableProduct","response "+response);

                if (isFromMyProduct && available.equals(NO)){
                    Intent intent=new Intent();
                    intent.putExtra(Constant.MODEL,arrayList.get(cardStackLayoutManager.getTopPosition()-1));
                    setResult(RESULT_OK,intent);
                    onBackPressed();
                }else if (isFromMyProduct)
                    onBackPressed();

            } else {
                Utility.showToast(activity, model.getResText(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //CardStackListener
    CardStackListener cardStackListener=new CardStackListener() {
        @Override
        public void onCardDragging(Direction direction, float ratio) {
            Log.d(TAG, "onCardDragging: direction = "+direction.name()+ "ratio = "+ratio);
        }

        @Override
        public void onCardSwiped(Direction direction) {
            Log.d(TAG, "onCardSwiped: position = "+cardStackLayoutManager.getTopPosition()+ "direction = "+direction);
            try {
                /*if (binding.noDataFound.getVisibility()==View.VISIBLE) {
                    binding.noDataFound.setVisibility(View.GONE);
                }*/
                if (direction == Direction.Top) {
                    callUpdateAvailableProduct(arrayList.get(cardStackLayoutManager.getTopPosition()-1).getId(), YES);
                } else if (direction == Direction.Bottom) {
                    callUpdateAvailableProduct(arrayList.get(cardStackLayoutManager.getTopPosition()-1).getId(), NO);
                }
            }catch (Exception e){
                //binding.noDataFound.setVisibility(View.VISIBLE);
                onBackPressed();
            }
        }

        @Override
        public void onCardRewound() {
            Log.d(TAG, "onCardRewound: "+cardStackLayoutManager.getTopPosition());
        }

        @Override
        public void onCardCanceled() {
            Log.d(TAG, "onCardCanceled: "+cardStackLayoutManager.getTopPosition());
        }

        @Override
        public void onCardAppeared(View view, int position) {
            Log.d(TAG, "onCardAppeared:"+ position+"View "+view);
        }

        @Override
        public void onCardDisappeared(View view, int position) {
            Log.d(TAG, "onCardDisappeared:"+ position+"View "+view);
        }
    };

}
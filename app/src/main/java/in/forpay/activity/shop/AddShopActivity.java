package in.forpay.activity.shop;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.shop.SelectDeliveryRageAdapter;
import in.forpay.databinding.ActivityAddShopBinding;
import in.forpay.model.shop.AddShopModel;
import in.forpay.model.shop.GetShopModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class AddShopActivity extends AppCompatActivity {

    private ActivityAddShopBinding binding;
    private AppCompatActivity activity;
    private SelectDeliveryRageAdapter adapter;
    /*private ArrayList<ShopCategoryModel.DataBean.SubCategoryBean> arrayList=new ArrayList<>();*/
    private ArrayList<String> list = new ArrayList<>(Arrays.asList("Select Delivery Range", "1KM", "2KM", "5KM", "10KM", "20KM", "30KM"));
    private String cityName = "", address = "", latitude = "", longitude = "";
    private ProgressHelper progressHelper;
    private String shopImage, shopName, contactNo, deliveryRange, productId, categoryId, shopCatImg,minimumOrderAmount;
    private boolean isFromEditYourShop, isImageChange;
    private File directory;
    private String dirPath;
    private String imageCat = "";
    private String ShopCatID="";
    private String imageCatUrl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        progressHelper = new ProgressHelper(activity);

        // arrayList=getIntent().getParcelableArrayListExtra(Constant.SHOP_SUB_CATEGORY_LIST);
        productId = getIntent().getStringExtra(Constant.PRODUCT_ID);
        ShopCatID =getIntent().getStringExtra("ShopCatID");
        imageCatUrl=getIntent().getStringExtra("imageCatUrl");
        if(imageCatUrl!=null){
            if(!imageCatUrl.isEmpty()){
                Utility.imageLoader(activity,imageCatUrl,binding.shopCatImg);
            }
        }
        isFromEditYourShop = getIntent().getBooleanExtra(Constant.IS_FROM_EDIT_YOUR_SHOP, false);

        dirPath = Environment.getExternalStorageDirectory() + File.separator
                + activity.getResources().getString(R.string.app_name) + File.separator + Constant.SHOP_IMAGE_DIRECTORY_NAME;
        directory = new File(dirPath);

        init();
    }

    private void init() {
        setDeliveryRangeAdapter();

        binding.backPress.setOnClickListener(v -> onBackPressed());
        binding.selectLocation.setOnClickListener(v -> {
            if (binding.selectLocation.getText().toString().isEmpty())
                openAddLocation();
        });
        binding.locationImg.setOnClickListener(v -> openAddLocation());
        binding.image.setOnClickListener(v -> Utility.imagePickerIntent(activity));
        binding.editShop.setOnClickListener(v -> Utility.imagePickerIntent(activity));

        binding.continueBtn.setOnClickListener(v -> {
            if (checkValidation()) {
                addShop();
            }
        });

        if (productId == null)
            productId = "";
        if (isFromEditYourShop) {
            binding.image.setEnabled(false);
            binding.addLayout.setVisibility(View.GONE);
            binding.image.setBackgroundResource(R.drawable.shop_loading);
            getShop();
        }
    }

    private void openAddLocation() {
        startActivityForResult(new Intent(activity, LocationAddActivity.class), Constant.ACTIVITY_RESULT_ADD_LOCATION);
    }

    private void setDeliveryRangeAdapter() {
        adapter = new SelectDeliveryRageAdapter(activity, list);
        binding.rangeSpinner.setAdapter(adapter);

        binding.rangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.range.setText(adapterView.getItemAtPosition(i).toString());

                if (i == 0) {
                   /* list.remove("Select Delivery Range");
                    adapter.notifyDataSetChanged();*/
                    binding.range.setTextColor(ContextCompat.getColor(activity, R.color.gray_shop));
                } else
                    binding.range.setTextColor(ContextCompat.getColor(activity, R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean checkValidation() {
        shopName = binding.shopName.getText().toString();
        contactNo = binding.contactNumber.getText().toString();
        shopName = binding.shopName.getText().toString();
        deliveryRange = binding.range.getText().toString();
        minimumOrderAmount=binding.minimumAmount.getText().toString();
        if (shopName.isEmpty()) {
            binding.shopName.setError("Please Enter Shop Name");
            return false;
        } else if (contactNo.isEmpty()) {
            binding.contactNumber.setError("Please Enter Contact No");
            return false;
        } else if (deliveryRange.equals("Select Delivery Range")) {
            Utility.showToast(activity, "Please Select Delivery Range");
            return false;
        } else if (latitude == null || latitude.equals("")) {
            Utility.showToast(activity, "Please Select Location");
            return false;
        } else if (shopImage == null && shopImage.isEmpty()) {
            Utility.showToast(activity, "Please Select Shop Image");
            return false;
        } else return isValidMobile(contactNo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.ACTIVITY_RESULT_IMAGE_SHOP) {
                try {
                    if (data != null) {
                        Uri uri = data.getData();
                        shopImage = Utility.getRealPathFromURIPath(uri, activity);

                        //Log.d("sdgfs", "add " + imageCat);
                        imageCat = data.getStringExtra("imageCat");


                        if (shopImage != null) {
                            binding.addLayout.setVisibility(View.GONE);
                            binding.image.setBackground(null);
                            isImageChange = true;
                            Utility.imageLoader(activity, shopImage, binding.image);
                            //Utility.imageLoader(activity,shopCatImg,binding.shopCatImg);

                        }
                    }

                } catch (Exception e) {
                    Utility.showToast(activity, getString(R.string.something_went_wrong), "");
                }
            }
            if (requestCode == Constant.ACTIVITY_RESULT_ADD_LOCATION) {

                if (data != null) {
                    //String city = data.getStringExtra(Constant.LOCATION_CITY);
                    //String subLocality = data.getStringExtra(Constant.LOCATION_SUB_LOCALITY);
                    address = data.getStringExtra(Constant.LOCATION_ADDRESS);
                    latitude = data.getStringExtra(Constant.LOCATION_LATITUDE);
                    longitude = data.getStringExtra(Constant.LOCATION_LONGITUDE);
                    binding.selectLocation.setText(address);
                    binding.selectLocation.setTextColor(ContextCompat.getColor(activity, R.color.black));
                }
            }

//            if (requestCode == RESULT_OK) {
//                if (resultCode == RESULT_OK) {
//                    try {
//                        if (data != null) {
//
//                            category_image = data.getStringExtra("category_image");
//                            Log.d("sdgfs", "add " + category_image);
//
//                        }
//
//                    } catch (Exception e) {
//                        // Utility.showToast(AddShopActivity.this, getString(R.string.something_went_wrong), "");
//                    }
//                }
//
//            }
        }
    }

        private boolean isValidMobile (String phone){
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }

        private void addShop () {
            if (Utility.isNetworkConnected(this)) {

                Map<String, String> map1 = new HashMap<>();
                map1.put("token", Utility.getToken(this)); // key
                map1.put("imei", Utility.getImei(this)); // imei
                map1.put("appVersion", Utility.getVersionCode(this)); // version code
                map1.put("os", Utility.getOs(this));
                map1.put("shopName", shopName);
                map1.put("contactNumber",contactNo);
                map1.put("deliveryRange", deliveryRange);
                map1.put("minimumOrderAmount",minimumOrderAmount);
                map1.put("latitude",latitude);
                map1.put("longitude",longitude);
                map1.put("catId",ShopCatID);

                String request = Utility.mapWrapper(this, map1);
                addShopRequest(request);

            }
            else {
                Utility.showToast(this, getString(R.string.internet_connect), "");
            }
        }

        private void addShopRequest (String request){
            if (Utility.isNetworkConnected(this)) {
                progressHelper.show();

                if (isImageChange) {
                    if (!shopImage.isEmpty()) {
                        QueryManager.getInstance().filePath = shopImage;
                        QueryManager.getInstance().fileType = "shopImage";
                    }
                }

                QueryManager.getInstance().postRequest(this,
                        Constant.METHOD_ADD_SHOP, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {
                                parseAddShopResponse(result, responseManager);
                            }
                        });
            }
            else {
                Utility.showToast(this, getString(R.string.internet_connect), "");
            }
        }

        private void parseAddShopResponse (String response, ResponseManager responseManager){
            progressHelper.dismiss();
            try {
                AddShopModel addShopModel = new Gson().fromJson(response, AddShopModel.class);
                if (addShopModel.getResCode().equals(Constant.CODE_200)) {
                    Log.d("AddShopResponse", "response " + response);
                    Intent intent = new Intent(activity, ShopCategoryActivity.class);
                    intent.putExtra(Constant.SHOP_IMAGE, shopImage);
                    intent.putExtra(Constant.SHOP_NAME, shopName);
                    intent.putExtra(Constant.SHOP_CONTACT_NO, contactNo);
                    intent.putExtra(Constant.SHOP_DELIVERY_RANGE, deliveryRange);
                    intent.putExtra(Constant.LOCATION_LATITUDE, latitude);
                    intent.putExtra(Constant.LOCATION_LONGITUDE, longitude);
                    intent.putExtra(Constant.PRODUCT_ID, productId);
                    intent.putExtra(Constant.SHOP_CATEGORY_ID, categoryId);
                    intent.putExtra(Constant.SHOP_ID, addShopModel.getShopId());
                    intent.putExtra(Constant.IS_FROM_EDIT_YOUR_SHOP, isFromEditYourShop);
                    startActivity(intent);

                } else {
                    Utility.showToast(activity, addShopModel.getResText(), "");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void getShop () {
            if (Utility.isNetworkConnected(this)) {

                Map<String, String> map1 = new HashMap<>();
                map1.put("token", Utility.getToken(this)); // key
                map1.put("imei", Utility.getImei(this)); // imei
                map1.put("appVersion", Utility.getVersionCode(this)); // version code

                String request = Utility.dataWrapper(list);
                getShopRequest(request);

            } else {
                Utility.showToast(this, getString(R.string.internet_connect), "");
            }
        }

        private void getShopRequest (String request){
            if (Utility.isNetworkConnected(this)) {
                progressHelper.show();

                QueryManager.getInstance().postRequest(this,
                        Constant.METHOD_SHOP_LIST, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {
                                parseGetShopResponse(result, responseManager);
                            }
                        });
            } else {
                Utility.showToast(this, getString(R.string.internet_connect), "");
            }
        }

        private void parseGetShopResponse (String response, ResponseManager responseManager){
            progressHelper.dismiss();
            try {
                GetShopModel model = new Gson().fromJson(response, GetShopModel.class);
                if (model.getResCode().equals(Constant.CODE_200)) {
                    Log.d("GetShopResponse", "response " + response);

                    shopName = model.getData().getShopName();
                    contactNo = model.getData().getContactNumber();
                    deliveryRange = model.getData().getDeliveryRange() + "KM";
                    latitude = model.getData().getLatitude();
                    longitude = model.getData().getLongitude();
                    shopImage = model.getData().getImgUrl();
                    /* categoryId=model.getData().getCatIds();*/

                    if (!shopImage.isEmpty()) {
                        binding.editShop.setVisibility(View.VISIBLE);
                        binding.image.setBackground(null);
                        Utility.imageLoader(activity, shopImage, binding.image);
                    }

                    productId = model.getData().getProductIds();
                    categoryId = model.getData().getCatIds();
                    binding.shopName.setText(shopName);
                    binding.contactNumber.setText(contactNo);
                    binding.range.setText(deliveryRange);
                    binding.range.setTextColor(ContextCompat.getColor(activity, R.color.black));
                    fillAddress(binding.selectLocation);

                } else {
                    Utility.showToast(activity, model.getResText(), "");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void fillAddress ( final TextView textView){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                        final List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!addresses.isEmpty()) {
                                    if (addresses.size() > 0) {
                                        String address = addresses.get(0).getAddressLine(0);
                                        textView.setText(address);
                                    }
                                } else {
                                    textView.setText("Not Available");
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
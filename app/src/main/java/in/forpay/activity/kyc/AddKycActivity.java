package in.forpay.activity.kyc;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.forpay.R;
import in.forpay.activity.VideoViewActivity;
import in.forpay.databinding.ActivityAddKycBinding;
import in.forpay.model.KycData;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

import static in.forpay.util.Utility.askForPermission;

public class AddKycActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddKycActivity";
    private ActivityAddKycBinding binding;
    private final int RECORD_VIDEO_CODE = 1002;
    private static final Integer CAMERA = 5005;
    private static final Integer STORAGE=6005;
    private File directory;
    private ProgressHelper progressHelper;
    private Uri fileUri;
    private String videoFileName;
    ArrayList<String> arealist = new ArrayList<>();
    ArrayAdapter<String> dataAdapter;
    String videoDuration="2";
    Integer durationVideo = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_kyc);

        //directory = new File(Environment.getExternalStorageDirectory() + File.separatorgetResources().getString(R.string.app_name) + File.separator + "Kyc");
        directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ForPay");

        progressHelper = new ProgressHelper(this);

        binding.backBtn.setOnClickListener(this);
        binding.btnUploadVideo.setOnClickListener(this);

        makeDir();
        getKycUpdate();


        binding.btnSampleVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddKycActivity.this, VideoViewActivity.class);
                intent.putExtra("url", "https://forpay.in/sampleVideo.php");
                intent.putExtra("webName", "sampleKyc");
                startActivity(intent);

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.btn_upload_video:

                if (ContextCompat.checkSelfPermission(AddKycActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(AddKycActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        openCameraIntent();
                    }
                    else{
                        askForPermission(AddKycActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE);
                    }

                }else {
                    askForPermission(AddKycActivity.this,Manifest.permission.CAMERA,CAMERA);
                }
                break;
        }
    }

    private void openCameraIntent(){

        if(getVideoStoreFileUri()!=null) {


            try {
                durationVideo = Integer.parseInt(videoDuration);
            } catch (Exception nfe) {

            }


            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("android.intent.extra.durationLimit", durationVideo);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            //intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 5491520L); // 5MB
            try {
                intent.putExtra(MediaStore.Video.Thumbnails.HEIGHT, 420);
                intent.putExtra(MediaStore.Video.Thumbnails.WIDTH, 320);
            } catch (Exception e) {

            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getVideoStoreFileUri());
            startActivityForResult(intent, RECORD_VIDEO_CODE);
        }else{
            Utility.showToastLatest(this,"Storage or Camera permission denied","ERROR");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RECORD_VIDEO_CODE) {
            try {
                if (data != null) {
                    Uri uri = data.getData();
                    String videoPath = videoFileName;
                    Log.d(TAG, "onActivityResult: " + videoPath);
                    assert videoPath != null;
                    File file = new File(videoPath);
                    Log.d(TAG, "onActivityResult: " + file.length());
                    checkVideoFileDuration(uri, videoPath);
                }

            } catch (Exception e) {
                Utility.showToastLatest(this,"Video Record Failed "+e.toString(),"ERROR");
            }
        } else {
            Log.d(TAG, "onActivityResult Cancel Video Capture : ");
            Utility.showToastLatest(this,"Video Record Failed , Error code "+resultCode,"ERROR");

        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //openCameraIntent();
            } else {
                Toasty.error(AddKycActivity.this, "Please provide Permission to record video.").show();
            }
        }
    }

    private void checkVideoFileDuration(Uri uri, String videoFilepath) {
        try {
            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
            metadataRetriever.setDataSource(this, uri);
            String time = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMillisec = Long.parseLong(time);
            Log.d(TAG, "checkVideoFileDuration: " + timeInMillisec);
            int second = (int) (timeInMillisec / 1000);
            Log.d(TAG, "checkVideoFileDuration: " + second);
            if (second > durationVideo+7) {
                Utility.showToastLatest(this, "Max video duration allowed is "+durationVideo+" Second , Your video duration is - " + second + " Second", "ERROR");
                metadataRetriever.release();
                return;
            }
            metadataRetriever.release();
            uploadVideoConformation(videoFilepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri getVideoStoreFileUri() {
        try {
            if (!directory.exists()) {
                //directory.mkdirs();
            }

            if (!directory.exists()) {
                try {
                    if(directory.mkdirs()){
                        Log.i("FileOperation", "Directory created" + directory.getAbsolutePath());

                    }
                    else{
                        Log.i("FileOperation", "Directory not created" + directory.getAbsolutePath());
                        Utility.showToastLatest(this,"Directory not created "+directory.getAbsolutePath(),"ERROR");
                    }
                }
                catch (Exception e){
                    Log.i("FileOperation", "Directory not created" + e.toString());
                    Utility.showToastLatest(this,"Directory not created Error "+e.toString(),"ERROR");

                }

            }

            if(directory.exists()) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
                videoFileName = directory + File.separator + "KYC_VID_" +
                        timeStamp + ".mp4";
                File file = new File(videoFileName);
                fileUri = FileProvider.getUriForFile(this, getPackageName() + ".com.aumfibrofil.aum.provider", file);
                return fileUri;
            }
            else{
                Utility.showToastLatest(this,"Unable to create directory at "+directory.getAbsolutePath(),"ERROR");

            }
        } catch (Exception e) {
            Utility.showToastLatest(this,"Directory creation error "+e.toString(),"ERROR");
            e.printStackTrace();
        }
        return null;
    }

    private void makeDir() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }

    private void getKycUpdate() {
        progressHelper.show();

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this,map1);


        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_KYC, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    parseKycResponseData(result);
                }
            });
        }
    }

    private void parseKycResponseData(String response) {
        Log.e("response",response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            int resCode = jsonObject.getInt("resCode");
            String resMessage = jsonObject.getString("resText");
            videoDuration=jsonObject.getString("videoDuration");
            String kycStep=jsonObject.getString("kycStep");
            binding.kycStep.setText(kycStep);
//            showdialog(jsonObject.getJSONArray("kycData"));
            if (resCode == 200) {
                binding.linKycApproved.setVisibility(View.VISIBLE);
                binding.linKycInstruction.setVisibility(View.GONE);
                binding.kycReject.setVisibility(View.GONE);
                binding.kycPending.setVisibility(View.GONE);

            } else if (resCode == 201) {

                binding.linKycApproved.setVisibility(View.GONE);
                binding.linKycInstruction.setVisibility(View.VISIBLE);
                binding.kycReject.setVisibility(View.GONE);
                binding.kycPending.setVisibility(View.GONE);


            } else if (resCode == 203) {
                binding.linKycApproved.setVisibility(View.GONE);
                binding.linKycInstruction.setVisibility(View.VISIBLE);
                binding.kycReject.setVisibility(View.VISIBLE);
                binding.kycPending.setVisibility(View.GONE);

            } else if (resCode == 202){

                binding.linKycApproved.setVisibility(View.GONE);
                binding.linKycInstruction.setVisibility(View.GONE);
                binding.kycReject.setVisibility(View.GONE);
                binding.kycPending.setVisibility(View.VISIBLE);
                binding.kycPending.setText(resMessage);

            }
            Utility.showToastLatest(this, resMessage,resCode+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadKYCVideo(String filePath) {
        Log.e("filep",filePath);

        //Utility.showToast(AddKycActivity.this, "Uploading file "+filePath,"");
        Toast.makeText(AddKycActivity.this,"Uploading file "+filePath,Toast.LENGTH_LONG).show();
        if (Utility.isNetworkConnected(this)) {

            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(this)); // key
            map1.put("imei",Utility.getImei(this)); // imei
            map1.put("versionCode",Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));

            String request = Utility.mapWrapper(this,map1);
/*

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(getApplicationContext(),Constant.BASE_URL+Constant.METHOD_KEY_UPLOAD);

            try {
                multipartUploadRequest.setMethod("POST");
                multipartUploadRequest.addFileToUpload(filePath,"kyc");

                multipartUploadRequest.addParameter("data",request);
                multipartUploadRequest.startUpload();

                multipartUploadRequest.subscribe(getApplicationContext(), this, new RequestObserverDelegate() {
                    @Override
                    public void onProgress(Context context, UploadInfo uploadInfo) {

                        Toasty.info(context,"Video Uploading").show();

                    }

                    @Override
                    public void onSuccess(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                        Toasty.success(context,"Video Successfully uploaded ").show();

                    }

                    @Override
                    public void onError(Context context, UploadInfo uploadInfo, Throwable throwable) {

                        Toasty.error(context,throwable.getMessage()).show();
                    }

                    @Override
                    public void onCompleted(Context context, UploadInfo uploadInfo) {

                        Toasty.success(context,"Video Successfully Uploaded 2").show();

                    }

                    @Override
                    public void onCompletedWhileNotObserving() {

                    }
                });

            } catch (Exception e) {
                Toasty.error(AddKycActivity.this,e.toString()).show();
            }
*/


            QueryManager.getInstance(AddKycActivity.this).filePath = filePath;
            QueryManager.getInstance(AddKycActivity.this).fileType = "Video";
            QueryManager.getInstance(AddKycActivity.this).postRequest(AddKycActivity.this, Constant.METHOD_KEY_UPLOAD, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    Log.d(TAG, "onResult: " + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String resCode = jsonObject.getString("resCode");
                        String resMessage = jsonObject.getString("resText");

                            Utility.showToastLatest(AddKycActivity.this, resMessage,resCode);

                    } catch (Exception e1) {
                        Toasty.error(AddKycActivity.this,"Error - Path "+filePath+e1.toString()).show();
                        Log.d(TAG, "onResult: Error " + e1.toString());
                    }
                }
            });


        }
    }

    private void uploadVideoConformation(String filePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name) + " KYC");
        builder.setMessage(getResources().getString(R.string.kyc_upload_conform_msg));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                uploadKYCVideo(filePath);
            }
        }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    AlertDialog dialog;

    private void showdialog(JSONArray kycdata){

        ArrayList<KycData> kycDataArrayList = new ArrayList<>();




        AlertDialog.Builder kycdatadialog = new AlertDialog.Builder(AddKycActivity.this);
        //kycdatadialog.setTitle("KYC Details");
        kycdatadialog.setCancelable(true);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.titlebar, null);



        kycdatadialog.setCustomTitle(view);
        // kycdatadialog.setMessage("Kyc Details");


        LinearLayout mainLay = new LinearLayout(AddKycActivity.this);

        mainLay.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mainLay.setPadding(32,32,32,32);

        mainLay.setOrientation(LinearLayout.VERTICAL);

        View lineView = new View(AddKycActivity.this);
        lineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen._1sdp)));
        lineView.setBackgroundColor(getResources().getColor(R.color.gray_new));


        for(int i=0;i<kycdata.length();i++){



            LinearLayout secondaryLay = new LinearLayout(AddKycActivity.this);

            secondaryLay.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            secondaryLay.setOrientation(LinearLayout.HORIZONTAL);
            secondaryLay.setPadding(0,(int) getResources().getDimension(R.dimen._5sdp),0,(int) getResources().getDimension(R.dimen._5sdp));

            try {


                KycData kycData = new Gson().fromJson(String.valueOf(kycdata.getJSONObject(i)), KycData.class);
                kycDataArrayList.add(kycData);



                if(kycData.getInputType().equals("numeric") || kycData.getInputType().equals("alpha") || kycData.getInputType().equals("alphanumeric")) {

                    View base = LayoutInflater.from(this).inflate(R.layout.text_field_material, null);


                    TextInputLayout textInputLayout = base.findViewById(R.id.TextInputLayout);//new TextInputLayout(this, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
                    textInputLayout.setGravity(Gravity.CENTER);
                    textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                    textInputLayout.setBoxCornerRadii(5, 5, 5, 5);
                    TextInputEditText editText = base.findViewById(R.id.TextInputEditText);//new TextInputEditText(emailTextInputLayout.getContext());

                    editText.setTag(kycData.getName());

                    if(Integer.parseInt(kycData.getMinLength())>0){
                        textInputLayout.setHint(kycData.getPlaceHolder()+"*");
                    }else
                        textInputLayout.setHint(kycData.getPlaceHolder());

                    if(!kycData.getValue().isEmpty() || !kycData.getValue().toString().equals("")){
                        editText.setText(kycData.getValue());
                        editText.setEnabled(false);
                    }



                    if (kycData.getInputType().equals("numeric")) {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else if (kycData.getInputType().equals("alpha")){
                        editText.setFilters(new InputFilter[] {Utility.getEditTextFilter(),Utility.filterMaxLenght(kycData.getMaxLength())});
                    }


                    if (!kycData.getInputType().equals("alpha"))
                        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(kycData.getMaxLength()))});
                    if (!kycData.getLine().equals("singleLine")) {
                        editText.setSingleLine(false);
                        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                    } else
                        editText.setSingleLine(true);

                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mainLay.addView(textInputLayout);
                }
                else if (kycData.getInputType().equals("radio")){

                    RadioGroup radioGroup = new RadioGroup(this);
                    radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                    radioGroup.setTag(kycData.getName());
                    radioGroup.setGravity(Gravity.CENTER);


                    for (int pos=0;pos< kycData.getValueArray().length;pos++) {

                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setText(kycData.getValueArray()[pos]);
                        switch (pos) {
                            case 0:
                                radioButton.setId(R.id.male);
                                break;
                            case 1:
                                radioButton.setId(R.id.female);
                                break;
                            case 2:
                                radioButton.setId(R.id.other);
                                break;
                        }
                        if(!kycData.getValue().isEmpty() || !kycData.getValue().equals("")) {
                            if(radioButton.getText().toString().equals(kycData.getValue()))
                                radioButton.setChecked(true);

                        }
                        radioGroup.addView(radioButton);
                    }




                    LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0,30,0,30);
                    radioGroup.setLayoutParams(layoutParams);


                    mainLay.addView(radioGroup);

                }
                else if (kycData.getInputType().equals("date"))
                {

                    View base = LayoutInflater.from(this).inflate(R.layout.text_field_material, null);


                    TextInputLayout TextInputLayout = base.findViewById(R.id.TextInputLayout);//new TextInputLayout(this, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);

                    TextInputLayout.setGravity(Gravity.CENTER);
                    TextInputLayout.setHint(kycData.getPlaceHolder());
                    TextInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                    TextInputLayout.setBoxCornerRadii(5, 5, 5, 5);
                    TextInputEditText edittext = base.findViewById(R.id.TextInputEditText);//new TextInputEditText(emailTextInputLayout.getContext());

                    edittext.setTag(kycData.getName());

                    edittext.setFocusable(false);
                    edittext.setClickable(true);

                    final Calendar myCalendar = Calendar.getInstance();


                    if(Integer.parseInt(kycData.getMinLength())>0){
                        TextInputLayout.setHint(kycData.getPlaceHolder()+"*");
                    }else
                        TextInputLayout.setHint(kycData.getPlaceHolder());

                    if(!kycData.getValue().isEmpty() || !kycData.getValue().equals("")){
                        edittext.setText(kycData.getValue());
                        edittext.setEnabled(false);
                    }
                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            if(kycData.getValue().isEmpty() || kycData.getValue().equals("")){
                                String myFormat = "yyyy/MM/dd"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                                edittext.setText(sdf.format(myCalendar.getTime()));
                            }else {
                                edittext.setText(kycData.getValue());
                            }
                        }

                    };

                    edittext.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            new DatePickerDialog(AddKycActivity.this, date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    mainLay.addView(TextInputLayout);

                }
                else if(kycData.getInputType().equals("select")){



                    Spinner spinner = new Spinner(this);

                    spinner.setPrompt(kycData.getPlaceHolder());

                    spinner.setTag(kycData.getName());


                    if (!kycData.getValue().isEmpty() || !kycData.getValue().toString().equals("")){
                        arealist.add(kycData.getValue());
                    }else
                    arealist.add("Area Based on Pin");

                    dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arealist);

                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(dataAdapter);

                    LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout.LayoutParams layoutParamsSpinner =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    layoutParams.setMargins(20,20,20,20);

                    spinner.setLayoutParams(layoutParamsSpinner);

//                    spinner.setPadding(10,10,10,10);

                    LinearLayout linearLayout = new LinearLayout(this);

                    linearLayout.setLayoutParams(layoutParams);

                    linearLayout.setPadding(0,20,0,20);

                    linearLayout.setBackground(getResources().getDrawable(R.drawable.bg_spinner));

                    linearLayout.addView(spinner);

                    linearLayout.setGravity(Gravity.CENTER);
                    mainLay.addView(linearLayout);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ScrollView scrollView = new ScrollView(this);

        scrollView.addView(mainLay);

        kycdatadialog.setView(scrollView);


        TextInputEditText pincode = mainLay.findViewWithTag("pincode");

        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(pincode.getText().toString().length()==6){
                    getAreaFromPin(pincode.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });



        
        kycdatadialog.setPositiveButton("Confirm",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog = kycdatadialog.create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Map<String,String> map1 = new HashMap<>();

                map1.put("token",Utility.getToken(AddKycActivity.this)); // key
                map1.put("imei",Utility.getImei(AddKycActivity.this)); // imei
                map1.put("versionCode",Utility.getVersionCode(AddKycActivity.this)); // version code
                map1.put("os", Utility.getOs(AddKycActivity.this));

                for (KycData kycData: kycDataArrayList){
                    View addedView =mainLay.findViewWithTag(kycData.getName());

                    if(addedView instanceof TextInputEditText){
                        Log.e("value of textfield minl",((TextInputEditText) addedView).getText().toString()+" "+kycData.getMinLength());

                        if(((TextInputEditText) addedView).getText().toString().length()<Integer.parseInt(kycData.getMinLength())) {
                            Toasty.error(AddKycActivity.this, "Please input valid " + kycData.getPlaceHolder()).show();
                            return;

                        }else

                        map1.put(kycData.getName(), ((TextInputEditText) addedView).getText().toString());

                    }
                    else if(addedView instanceof RadioGroup){

                        String gender = "";

                        if(((RadioGroup) addedView).getCheckedRadioButtonId()==R.id.male)
                            gender="Male";
                        else if (((RadioGroup) addedView).getCheckedRadioButtonId()==R.id.female)
                            gender="Female";
                        else if (((RadioGroup) addedView).getCheckedRadioButtonId()==R.id.other)
                            gender="Other";


                        map1.put(kycData.getName(), gender);
                    }
                    else if (addedView instanceof Spinner){

                        String areaa = ((Spinner) addedView).getSelectedItem().toString();

                        if(areaa.equals("Area Based on Pin")) {
                            return;
                        }
                        else

                        map1.put(kycData.getName(), areaa);

                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //list.forEach(pram -> Log.e("param",pram));
                }
                updateKyc(map1);


            }
        });

    }

    private void getAreaFromPin(String pincode) {
        progressHelper.show();
        arealist.clear();

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(AddKycActivity.this)); // key
        map1.put("imei",Utility.getImei(AddKycActivity.this)); // imei
        map1.put("versionCode",Utility.getVersionCode(AddKycActivity.this)); // version code
        map1.put("os", Utility.getOs(AddKycActivity.this));
        map1.put("pincode",pincode);
        String request = Utility.mapWrapper(this,map1);
        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_PINCODE, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("pinArray");

                        for (int i=0;i<jsonArray.length();i++){

                            arealist.add(jsonArray.getJSONObject(i).optString("name").trim());

                        }
                        dataAdapter.notifyDataSetChanged();

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            });
        }
    }

    private void updateKyc(Map<String,String> map1) {
        progressHelper.show();


        String request = Utility.mapWrapper(AddKycActivity.this,map1);

        Log.e("request",request);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_KYC_UPDATE, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    Log.e("res",result);
                    if(result!=null) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.getString("resCode").equals(Constant.CODE_200)) {

                                if(dialog!=null && dialog.isShowing())
                                    dialog.cancel();

                            }
                            else{
                                Utility.showToastLatest(AddKycActivity.this,obj.getString("resText"),"ERROR");
                            }
                        }
                        catch (Exception e1){
                            Utility.showToastLatest(AddKycActivity.this,"Wrong response "+e1.toString(),"ERROR");
                        }

                    }

                }
            });
        }
    }


}
//{"resCode":"202","resText":"Your Kyc is still in pending , Please wait for approval"}

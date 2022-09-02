package in.forpay.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentProfileProfile2Binding;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.response.GetProfileResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ProfileProfileFragment extends Fragment {

    private RechargeNowListener mListener;
    private ProgressHelper progressHelper;
    private FragmentProfileProfile2Binding mBinding;
    private final int IMG_RESULT = 1009;
    private Uri uri;
    private Activity activity;
    private DatabaseHelper databaseHelper;
    private String realPath;

    public ProfileProfileFragment() {

    }

    @SuppressLint("ValidFragment")

    public ProfileProfileFragment(RechargeNowListener listener) {
        this.mListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_profile2, container, false);
        mBinding.setFragment(this);
        databaseHelper = new DatabaseHelper(activity);
        mBinding.profileImg.setOnClickListener(v -> selectProfileImage());
        try {
            Bitmap link = databaseHelper.getProfileImage();
            Glide.with(activity).load(link).error(R.drawable.profile_placeholder).into(mBinding.profileImg);

        } catch (Exception e) {

        }

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void selectProfileImage() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMG_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == IMG_RESULT && resultCode == RESULT_OK && null != data) {
                uri = data.getData();
                realPath = Utility.getRealPathFromURIPath(uri, activity);
                Glide.with(activity).load(uri).error(R.drawable.profile_placeholder).into(mBinding.profileImg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        progressHelper = new ProgressHelper(getActivity());
        mBinding.editTextMobile.setText(Utility.getUsername(getActivity()));
        mBinding.editTextName.setText(Utility.getCustomerName(getActivity()));
        mBinding.editTextEmail.setText(Utility.getCustomerEmail(getActivity()));

        mBinding.submitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> map1 = new HashMap<>();

                map1.put("token", Utility.getToken(getActivity())); // key
                map1.put("imei", Utility.getImei(getActivity())); // imei
                map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
                map1.put("os", Utility.getOs(getActivity()));

                map1.put("profileEmail", mBinding.editTextEmail.getText().toString().trim()); // email
                map1.put("profileName", mBinding.editTextName.getText().toString().trim());

                String request = Utility.mapWrapper(getActivity(), map1);

                if (validation()) {
                    updateProfile(request);
                }
            }
        });


    }


    private static InputFilter getEditTextFilter() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9,.\\- ]+$");
                Matcher ms = ps.matcher(String.valueOf(c));
                return ms.matches();
            }
        };
    }

    private void updateProfile(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_UPDATE_PROFILE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseUpdateProfileResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect));
        }
    }

    /**
     * Parse response
     */
    private void parseUpdateProfileResponse(String result) {
        if (isVisible()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetProfileResponse response = new Gson().fromJson(result, GetProfileResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToastLatest(getActivity(), response.getResText(), "SUCCESS");
                    Utility.setCustomerEmail(getActivity(), response.getData().getEmail());
                    Utility.setCustomerName(getActivity(), response.getData().getName());
                    try {
                        databaseHelper.insertImage(realPath);
                        Constant.PROFILE_ADDED = true;
                    } catch (Exception e) {

                    }
                } else {
                    Utility.showToast(getActivity(), response.getResText());
                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding));
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }


    private boolean validation() {

        String email = mBinding.editTextEmail.getText().toString().trim();
        String name = mBinding.editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Utility.showToast(getActivity(), "Please enter email", "");
            return false;
        } else if (TextUtils.isEmpty(name)) {
            Utility.showToast(getActivity(), "Please enter name", "");
            return false;
        }

        return true;

    }
}

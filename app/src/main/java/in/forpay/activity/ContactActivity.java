package in.forpay.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.adapter.ContactListAdapter;
import in.forpay.databinding.ActivityContactBinding;
import in.forpay.model.ContactList;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;
    private Activity activity;
    private ContactListAdapter adapter;
    private ArrayList<ContactList> list = new ArrayList<ContactList>();

    String[] permissions = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ContactActivity.this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_contact);
        Log.d("ContactActivity", "checking 1");
        boolean isFromNewRecharge = getIntent().getBooleanExtra(Constant.IS_FROM_NEW_RECHARGE, false);
        if (isFromNewRecharge) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
                getWindow().getDecorView().setSystemUiVisibility(flags);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gray_bg_shop)); // optional
            }

            binding.title.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.appbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            binding.backBtn.setColorFilter(ContextCompat.getColor(activity, R.color.black), PorterDuff.Mode.MULTIPLY);
        }


        binding.btnClear.setOnClickListener(v -> binding.edtSearch.setText(""));
        binding.backBtn.setOnClickListener(v -> onBackPressed());
        list = Utility.getContactList(activity);

        //Log.d("ContactActivity", "size 1 " + list.size());
        if (list == null || list.size() == 0) {
            getContactList();
            Utility.getContactListFromContact(activity);
            Utility.showToast(activity, "No contact found", "");
            return;
        }
        else {
            getContactList();
        }

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void filterData(String text) {
        try {
            ArrayList<ContactList> filteredList2 = new ArrayList<>();

            for (ContactList item : list) {
                if (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getPhoneNumber().toLowerCase().contains(text.toLowerCase())) {
                    filteredList2.add(item);
                }
            }

            if (adapter != null)
                adapter.filterList(filteredList2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity, binding.appbarLayout);
    }

    private void getContactList() {

        Log.d("ContactActivity","checking 0");
        if (!checkAllPermissionsGranted()) {
            openWhyNeedPermissionDialog();

            Log.d("ContactActivity","checking 0-1");
        }

//        Log.d("ContactActivityAdapter", "size 2 " +
        if(list!=null) {
            if (list.size() > 0) {
                binding.contactList.setLayoutManager(new LinearLayoutManager(activity));
                adapter = new ContactListAdapter(activity, list);
                binding.contactList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void openWhyNeedPermissionDialog() {

        checkPermission();

    }

    /**
     * Check location permission
     */
    private void checkPermission() {

        Log.d("ContactActivity","checking");
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                //Log.d("ContactActivity","checking granted");
                //adapter.notifyDataSetChanged();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                //Utility.showToast(PersonalDetailActivity.this, "Location permission is necessary");
            }
        };


        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(permissions)
                .check();
    }

    private boolean checkAllPermissionsGranted() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissions != null) {
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }

            if (!listPermissionsNeeded.isEmpty()) {

                Log.d("ContactActivity","checking 1");
                return false;
            }
        }

        Log.d("ContactActivity","checking 2");
        return true;
    }

}

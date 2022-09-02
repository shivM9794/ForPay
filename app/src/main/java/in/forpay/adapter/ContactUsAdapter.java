package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.forpay.R;
import in.forpay.databinding.LayoutContactUsBinding;
import in.forpay.model.ContactUsModel;

public class ContactUsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<ContactUsModel> list;

    public ContactUsAdapter(Activity activity, ArrayList<ContactUsModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_contact_us, parent, false);
        return new ContactUsHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ContactUsHolder){
            ContactUsModel usModel = (ContactUsModel)list.get(position);
            ContactUsHolder contactUsHolder = (ContactUsHolder)holder;
            contactUsHolder.adContainerViewBinding.txtMobile.setText(usModel.getMobile());
            contactUsHolder.adContainerViewBinding.txtName.setText(usModel.getName());

            String str = usModel.getDepartment();
            List<String> departmentList = Arrays.asList(str.split(","));

            contactUsHolder.adContainerViewBinding.departmentChips.removeAllViews();
            for (String s:departmentList){
                Chip mChip = (Chip) activity.getLayoutInflater().inflate(R.layout.department_chips, null, false);
                mChip.setText(s);
                int paddingDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, activity.getResources().getDisplayMetrics());
                mChip.setPadding(paddingDp, 5, paddingDp, 5);
                contactUsHolder.adContainerViewBinding.departmentChips.addView(mChip);
            }

            //contactUsHolder.adContainerViewBinding.txtDepartment.setText(usModel.getDepartment());
            contactUsHolder.adContainerViewBinding.txtLanguage.setText("Language : "+usModel.getLanguage());
            if(usModel.getStatus().equals("Available")){
                contactUsHolder.adContainerViewBinding.btnCall.setBackgroundColor(activity.getResources().getColor(R.color.success));
            }
        }
    }

    public void filterList(ArrayList<ContactUsModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ContactUsHolder extends RecyclerView.ViewHolder {
        LayoutContactUsBinding adContainerViewBinding;

        public ContactUsHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = LayoutContactUsBinding.bind(itemView);

            adContainerViewBinding.btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCall(activity,adContainerViewBinding.txtMobile.getText().toString());
                }
            });
        }
    }

    public static void openCall(Activity activity,String phone){
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+phone));
            activity.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

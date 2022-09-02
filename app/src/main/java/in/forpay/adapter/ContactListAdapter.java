package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.LayoutContactListBinding;
import in.forpay.model.ContactList;
import in.forpay.util.Constant;

public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<ContactList> arrayList;


    public ContactListAdapter(Activity activity, ArrayList<ContactList> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;

        //Log.d("ContactActivityAdapter", "size " + arrayList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_contact_list, parent, false);
        return new ContactHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ContactHolder) {

            ContactList contactList = (ContactList) arrayList.get(position);
            ContactHolder holder1 = (ContactHolder) holder;
            holder1.adContainerViewBinding.contactName.setText(contactList.getName());
            holder1.adContainerViewBinding.contactNumber.setText(contactList.getPhoneNumber());
            char first = contactList.getName().charAt(0);
            holder1.adContainerViewBinding.contactIcon.setText(String.valueOf(first).toUpperCase());
        }


    }

    public void filterList(ArrayList<ContactList> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        LayoutContactListBinding adContainerViewBinding;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = LayoutContactListBinding.bind(itemView);

            itemView.setOnClickListener(v -> {
                ContactList contactList = (ContactList) arrayList.get(getAdapterPosition());
                Constant.CONTACT_NUMBER = contactList.getPhoneNumber();

                Intent intent = new Intent();

                String phoneNumber = contactList.getPhoneNumber().replace(" ", "");
                phoneNumber = phoneNumber.replace("+91", "");
                phoneNumber = phoneNumber.replace("+ 91", "");
                phoneNumber = phoneNumber.replaceAll("\\s+", "");
                phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
                intent.putExtra("mobile", phoneNumber);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            });
        }
    }

}

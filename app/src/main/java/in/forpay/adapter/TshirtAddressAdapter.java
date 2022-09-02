package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.BuyTShirtActivity;
import in.forpay.model.response.GetAddressResponse;

public class TshirtAddressAdapter extends RecyclerView.Adapter<TshirtAddressAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetAddressResponse.Data> mList = new ArrayList<>();

    public TshirtAddressAdapter(Activity mActivity, ArrayList<GetAddressResponse.Data> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_tshirt,parent,false);
        return new TshirtAddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.user_address.setText(mList.get(position).getAddress());
        holder.name.setText(mList.get(position).getName());
        holder.mobile.setText(mList.get(position).getMobile());

        holder.ll_tshirtAddress.setOnClickListener(v -> {
            Intent intent = new Intent();
            //Log.d("Tshirt","fererfef "+mList.get(position).getAddressId());
            intent.putExtra("addressId",mList.get(position).getAddressId());
            mActivity.setResult(Activity.RESULT_OK, intent);
            mActivity.finish();

        });

    }

    @Override
    public int getItemCount() {

//        Log.d("sfdsfsdfs","dfdfdsf"+mList.size());
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView user_address,name,mobile;
        LinearLayout ll_tshirtAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_tshirtAddress = itemView.findViewById(R.id.ll_tshirtAddress);
            user_address = itemView.findViewById(R.id.user_address);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
        }
    }
}

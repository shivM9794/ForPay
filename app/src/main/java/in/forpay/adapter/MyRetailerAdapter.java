package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.shop.AddShopActivity;
import in.forpay.model.response.GetMyRetailerResponse;

public class MyRetailerAdapter extends RecyclerView.Adapter<MyRetailerAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetMyRetailerResponse.DataList> mList = new ArrayList<>();

    public MyRetailerAdapter(Activity mActivity, ArrayList<GetMyRetailerResponse.DataList> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_retailer,parent,false);
        return new MyRetailerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textRetailerName.setText(mList.get(position).getName());
        holder.textRetailerMobile.setText(mList.get(position).getMobile());
        holder.textRetailerBal.setText(mList.get(position).getBal());
        holder.textRetailerType.setText(mList.get(position).getUserType());



    }

    @Override
    public int getItemCount() {
//        Log.d("dfedfdfdfe","afafef"+mList.size());
        return mList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textRetailerName,textRetailerMobile,textRetailerBal,textRetailerType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textRetailerName = itemView.findViewById(R.id.textRetailerName);
            textRetailerMobile = itemView.findViewById(R.id.textRetailerMobile);
            textRetailerBal = itemView.findViewById(R.id.textRetailerBal);
            textRetailerType = itemView.findViewById(R.id.textRetailerType);
        }
    }
}

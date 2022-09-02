package in.forpay.adapter;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.response.GetSmsRechargeResponse;

public class SMSRechargeAdapter extends RecyclerView.Adapter<SMSRechargeAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<GetSmsRechargeResponse.Data> mList = new ArrayList<>();

    public SMSRechargeAdapter(Activity activity, ArrayList<GetSmsRechargeResponse.Data> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public SMSRechargeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recharge_sms,parent,false);
        return new SMSRechargeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SMSRechargeAdapter.ViewHolder holder, int position) {

        holder.operator1.setText(mList.get(position).getOperator());
        holder.code1.setText(mList.get(position).getCode());

    }

    @Override
    public int getItemCount() {
        //Log.d("zcvvcx","ffdsf"+mList.size());
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView recharge_msg;
        TextView operator1,code1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            operator1 = itemView.findViewById(R.id.operator1);
            code1 = itemView.findViewById(R.id.code1);
        }
    }
}

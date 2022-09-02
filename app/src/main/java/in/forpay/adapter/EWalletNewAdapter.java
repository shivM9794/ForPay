package in.forpay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.forpay.R;
import in.forpay.model.response.GetEWalletListResponse;
import in.forpay.util.Utility;

public class EWalletNewAdapter extends RecyclerView.Adapter<EWalletNewAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetEWalletListResponse.BrandList> mList = new ArrayList<>();

    public EWalletNewAdapter(Activity mActivity, ArrayList<GetEWalletListResponse.BrandList> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ewallet_new_adapter,parent,false);
        return new EWalletNewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.brand_name.setText(mList.get(position).getBrandName());
        holder.ewallet_id.setText(mList.get(position).getEwalletId());
        Utility.imageLoader(mActivity,mList.get(position).getImage(),holder.brand_icon);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView brand_icon;
        private TextView ewallet_id, brand_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            brand_icon = itemView.findViewById(R.id.brand_icon);
            ewallet_id = itemView.findViewById(R.id.ewallet_id);
            brand_name = itemView.findViewById(R.id.brand_name);
        }
    }
}

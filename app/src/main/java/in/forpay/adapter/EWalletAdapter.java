package in.forpay.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.EwalletAdapterBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.response.GetEWalletListResponse;
import in.forpay.util.Utility;

public class EWalletAdapter extends RecyclerView.Adapter<EWalletAdapter.ViewHolder> {

    private Activity mActivity;
    private ItemClickListener listener;
    String selectedId = "";
    private ArrayList<GetEWalletListResponse.BrandList> mList = new ArrayList<>();


    public EWalletAdapter(Activity mActivity, ArrayList<GetEWalletListResponse.BrandList> mList, ItemClickListener listener) {
        this.mActivity = mActivity;
        this.mList = mList;
        this.listener = listener;

        Log.d("EWallet", "sizeEwallet" + mList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ewallet_adapter, parent, false);
//        Log.d("EWallet", "sizeEwallet 1" + mList.size());
        return new EWalletAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Log.d("EWallet", "sizeEwallet 2" + mList.size());

        holder.brand_name.setText(mList.get(position).getBrandName());
//        holder.ewallet_id.setText(mList.get(position).getEwalletId());
        Utility.imageLoader(mActivity, mList.get(position).getImage(), holder.brand_icon);

        if (selectedId.equals(mList.get(position).getEwalletId())) {
            holder.binding.select.setVisibility(View.VISIBLE);
        } else {
            holder.binding.select.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
//        Log.d("EWallet", "sizeEwallet is" + mList.size());
        return mList.size();

    }

    public void setItems(String selectedId, ArrayList<GetEWalletListResponse.BrandList> tempArrayList) {

        this.selectedId = selectedId;
        this.mList = tempArrayList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EwalletAdapterBinding binding;
        RelativeLayout relative_layout;
        private ImageView brand_icon;
        private TextView brand_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = EwalletAdapterBinding.bind(itemView);

//            Log.d("EWallet", "sizeEwallet 3" + mList.size());

            brand_icon = itemView.findViewById(R.id.brand_icon);
//            ewallet_id = itemView.findViewById(R.id.ewallet_id);
            brand_name = itemView.findViewById(R.id.brand_name);
            relative_layout = itemView.findViewById(R.id.relative_layout);

            binding.getRoot().setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), mList.get(getAdapterPosition()).getEwalletId()));


        }
    }
}

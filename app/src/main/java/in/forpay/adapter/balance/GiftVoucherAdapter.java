package in.forpay.adapter.balance;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterGiftVoucherBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.response.GiftVoucherResponse;
import in.forpay.util.Utility;

public class GiftVoucherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private ArrayList<GiftVoucherResponse.ServiceBean> arrayList;

    public GiftVoucherAdapter(Context context, ArrayList<GiftVoucherResponse.ServiceBean> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_gift_voucher, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            try {
                MyViewHolder viewHolder = (MyViewHolder) holder;
                GiftVoucherResponse.ServiceBean model = (GiftVoucherResponse.ServiceBean) arrayList.get(position);
                viewHolder.binding.service.setText(model.getBrandName());
                //viewHolder.binding.off.setText("");
                Utility.imageLoader((Activity) context, model.getImage(), viewHolder.binding.image);
                viewHolder.binding.off.setText(model.getDiscount()+" % Off");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filterList(ArrayList<GiftVoucherResponse.ServiceBean> filteredList2) {
        this.arrayList=filteredList2;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterGiftVoucherBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterGiftVoucherBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), ""));
        }
    }
}

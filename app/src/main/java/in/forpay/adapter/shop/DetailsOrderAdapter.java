package in.forpay.adapter.shop;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.databinding.AdapterDetailsOrderBinding;
import in.forpay.model.shop.ShopOrderModel;
import in.forpay.model.shop.SoldOrderDetailModel;

public class DetailsOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int quantity;
    ArrayList<ShopOrderModel.DataBean.OrderHistoryBean.ItemsBean> arrayList;
    ArrayList<SoldOrderDetailModel.DataBean.OrderHistoryBean.ItemsBean> orderDetailArrayList;
    boolean isFromOrderDetail;

    public DetailsOrderAdapter(Context context, ArrayList<ShopOrderModel.DataBean.OrderHistoryBean.ItemsBean> arrayList, boolean isFromOrderDetail) {
        this.context = context;
        this.arrayList = arrayList;
        this.isFromOrderDetail = isFromOrderDetail;
    }

    public DetailsOrderAdapter(Activity context, ArrayList<SoldOrderDetailModel.DataBean.OrderHistoryBean.ItemsBean> orderDetailArrayList, boolean isFromOrderDetail) {
        this.context = context;
        this.orderDetailArrayList = orderDetailArrayList;
        this.isFromOrderDetail = isFromOrderDetail;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AdapterDetailsOrderBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;

            if (isFromOrderDetail){
                viewHolder.binding.itemName.setText(orderDetailArrayList.get(position).getItemName());
                viewHolder.binding.price.setText(orderDetailArrayList.get(position).getAmount());
            }else {
                viewHolder.binding.itemName.setText(arrayList.get(position).getItemName());
                viewHolder.binding.price.setText(arrayList.get(position).getAmount());
            }
        }
    }

    @Override
    public int getItemCount() {
        return isFromOrderDetail ? orderDetailArrayList.size() :arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterDetailsOrderBinding binding;

        MyViewHolder(@NonNull AdapterDetailsOrderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

package in.forpay.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.forpay.databinding.AdapterShopOrderBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ShopOrderModel;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class ShopOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<Object> arrayList;
    private ArrayList<Object> arrayListItems;

    public ShopOrderAdapter(Context context, List<Object> arrayList, ArrayList<Object> arrayListItems, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListItems = arrayListItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AdapterShopOrderBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;

            ShopOrderModel.DataBean model=(ShopOrderModel.DataBean) arrayList.get(0);
            ShopOrderModel.DataBean.OrderHistoryBean orderHistory=((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory().get(position);
            ShopOrderModel.DataBean.OrderHistoryBean.ItemsBean items= ((ShopOrderModel.DataBean.OrderHistoryBean) arrayListItems.get(position)).getItems().get(0);

            viewHolder.binding.description.setText(model.getDiscription());
            viewHolder.binding.orderId.setText(orderHistory.getOrderId());
            viewHolder.binding.status.setText(orderHistory.getStatus());
            viewHolder.binding.rating.setText(orderHistory.getRating());
            viewHolder.binding.address.setText(orderHistory.getAddress());
            viewHolder.binding.date.setText(Utility.convertDate(orderHistory.getOrderTime()));

            viewHolder.binding.item.setText(items.getItemName());
            viewHolder.binding.amount.setText("Rs. "+items.getAmount());
        }
    }

    @Override
    public int getItemCount() {
        return arrayListItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterShopOrderBinding binding;

        MyViewHolder(@NonNull AdapterShopOrderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.viewOrder.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.VIEW_ORDER));

            binding.ratingLayout.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.RATING));
        }
    }
}

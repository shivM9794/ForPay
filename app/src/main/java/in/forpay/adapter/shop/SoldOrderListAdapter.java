package in.forpay.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.databinding.AdapterSoldOrderListBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.MySoldOrderHistoryModel;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class SoldOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<Object> arrayList;
    private List<Object> arrayListItems;

    public SoldOrderListAdapter(Context context, List<Object> arrayList,List<Object> arrayListItems, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListItems = arrayListItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_sold_order_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            MySoldOrderHistoryModel.DataBean model=(MySoldOrderHistoryModel.DataBean) arrayList.get(0);
            MySoldOrderHistoryModel.DataBean.OrderHistoryBean orderHistory=((MySoldOrderHistoryModel.DataBean) arrayList.get(0)).getOrderHistory();
            MySoldOrderHistoryModel.DataBean.OrderHistoryBean.ItemsBean items= (MySoldOrderHistoryModel.DataBean.OrderHistoryBean.ItemsBean) arrayListItems.get(position);

            viewHolder.binding.orderId.setText(orderHistory.getOrderId());
            viewHolder.binding.status.setText(orderHistory.getStatus());
            viewHolder.binding.rating.setText(orderHistory.getRating());
            viewHolder.binding.address.setText(orderHistory.getAddress());
            viewHolder.binding.statusText.setText(orderHistory.getStatusText());
            viewHolder.binding.date.setText(Utility.convertDate(orderHistory.getOrderTime()));

            viewHolder.binding.item.setText(items.getItemName());
            viewHolder.binding.amount.setText("Rs. "+items.getAmount());
        }
    }

    @Override
    public int getItemCount() {
        return arrayListItems.size();
    }

    public void filter(ArrayList<Object> arrayListFilter) {
        arrayListItems=arrayListFilter;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterSoldOrderListBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterSoldOrderListBinding.bind(itemView);

            binding.viewOrder.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.VIEW_ORDER));
            binding.ratingLayout.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.RATING));
            binding.statusLayout.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.STATUS));
        }
    }
}
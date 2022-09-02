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
import in.forpay.databinding.AdapterMyPurchaseOrderBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.MyPurchaseOrderModel;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class MyPurchaseOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<Object> arrayList;
    private List<Object> arrayListItems;

    public MyPurchaseOrderAdapter(Context context, List<Object> arrayList,List<Object> arrayListItems, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListItems = arrayListItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_my_purchase_order, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            MyPurchaseOrderModel.DataBean model=(MyPurchaseOrderModel.DataBean) arrayList.get(0);
            MyPurchaseOrderModel.DataBean.OrderHistoryBean orderHistory=((MyPurchaseOrderModel.DataBean) arrayList.get(0)).getOrderHistory();
            MyPurchaseOrderModel.DataBean.OrderHistoryBean.ItemsBean items= (MyPurchaseOrderModel.DataBean.OrderHistoryBean.ItemsBean) arrayListItems.get(position);

            viewHolder.binding.orderId.setText(orderHistory.getOrderId());
            viewHolder.binding.status.setText(orderHistory.getStatus());
            viewHolder.binding.rating.setText(orderHistory.getRating()+" "+"Rating");
            viewHolder.binding.address.setText(orderHistory.getAddress());
            //viewHolder.binding.statusText.setText(orderHistory.getStatusText());
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
        AdapterMyPurchaseOrderBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterMyPurchaseOrderBinding.bind(itemView);

            binding.viewOrder.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.VIEW_ORDER));
            binding.ratingLayout.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.RATING));
            binding.status.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.STATUS));
        }
    }
}

package in.forpay.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.forpay.databinding.AdapterShopExtraBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ShopModelExtra;
import in.forpay.util.Constant;

public class ShopExtraAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<ShopModelExtra> arrayList;

    public ShopExtraAdapter(Context context, List<ShopModelExtra> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AdapterShopExtraBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;

            viewHolder.binding.title.setText(arrayList.get(position).getTitle());
            viewHolder.binding.image.setImageResource(arrayList.get(position).getImage());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterShopExtraBinding binding;

        MyViewHolder(@NonNull AdapterShopExtraBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.getRoot().setOnClickListener(v->{
                switch (getAdapterPosition()){
                    case 0:listener.onItemClick(getAdapterPosition(), Constant.ADD_NEW_SHOP);
                        break;
                    case 1:listener.onItemClick(getAdapterPosition(), Constant.EDIT_YOUR_SHOP);
                        break;
                    case 2:listener.onItemClick(getAdapterPosition(), Constant.SHOP_LOCAL);
                        break;
                    case 3:listener.onItemClick(getAdapterPosition(), Constant.MY_PURCHASE_ORDERS);
                        break;
                    case 4:listener.onItemClick(getAdapterPosition(), Constant.MY_SOLD_ORDERS);
                        break;
                    case 5:listener.onItemClick(getAdapterPosition(), Constant.GENERATE_NEW_ORDER);
                        break;
                    case 6:listener.onItemClick(getAdapterPosition(), Constant.MY_RATING);
                        break;
                    case 7:listener.onItemClick(getAdapterPosition(), Constant.MY_CHAT);
                        break;
                }
            });
        }
    }
}

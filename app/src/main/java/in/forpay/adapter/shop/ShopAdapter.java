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
import in.forpay.databinding.AdapterShopBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ShopModel;
import in.forpay.util.Constant;

public class ShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<Object> arrayList;

    public ShopAdapter(Context context, List<Object> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_shop, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            ShopModel.DataBean model= (ShopModel.DataBean) arrayList.get(position);
            viewHolder.binding.shopName.setText(model.getShopName());
            if (model.getProducts().size()>0)
                viewHolder.binding.category.setText(model.getProducts().get(0).getProductName());
            viewHolder.binding.distance.setText(model.getDistance().substring(0, model.getDistance().indexOf(".")));
            viewHolder.binding.deliveryTime.setText(model.getAvgDeliveryTime());
            viewHolder.binding.rating.setText(model.getAvgRating());
            viewHolder.binding.description.setText(model.getDiscription());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filter(ArrayList<Object> arrayListFilter) {
        arrayList=arrayListFilter;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterShopBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterShopBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.SHOP_LIST));
            binding.ratingLayout.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.SHOP_RATING));
            binding.distanceLayout.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.SHOP_DISTANCE));
        }
    }
}

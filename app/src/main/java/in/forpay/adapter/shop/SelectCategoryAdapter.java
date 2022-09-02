package in.forpay.adapter.shop;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterShopCategoryBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ShopCategoryModel;
import in.forpay.util.Utility;

public class SelectCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private ArrayList<ShopCategoryModel.DataBean> arrayList;
    private ArrayList<String> selectedIdList;

    public SelectCategoryAdapter(Context context, ArrayList<ShopCategoryModel.DataBean> arrayList, ArrayList<String> selectedIdList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.selectedIdList = selectedIdList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_shop_category, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;

            viewHolder.binding.title.setText(arrayList.get(position).getName());
            Utility.imageLoader((Activity) context,arrayList.get(position).getImgUrl(),viewHolder.binding.image);

            if (selectedIdList.contains(arrayList.get(position).getId())){
                viewHolder.binding.select.setVisibility(View.VISIBLE);
            }else {
                viewHolder.binding.select.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterShopCategoryBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterShopCategoryBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->{
                listener.onItemClick(getAdapterPosition(),"");
            });
        }
    }
}

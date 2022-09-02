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
import in.forpay.databinding.AdapterShopSubcategoryListBinding;
import in.forpay.listener.SelectedIdListener;
import in.forpay.model.shop.ShopCategoryModel;
import in.forpay.util.Utility;

public class ShopSubCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private SelectedIdListener listener;
    private ArrayList<ShopCategoryModel.DataBean.SubCategoryBean> arrayList;
    private ArrayList<String> selectedIdList;
    private int adapterPosition;

    public ShopSubCategoryListAdapter(Context context, ArrayList<ShopCategoryModel.DataBean.SubCategoryBean> arrayList, ArrayList<String> selectedIdList,int adapterPosition, SelectedIdListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.selectedIdList=selectedIdList;
        this.adapterPosition=adapterPosition;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new MyViewHolder(AdapterShopCategoryBinding.inflate(LayoutInflater.from(parent.getContext())));
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_shop_subcategory_list, parent, false);
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
        AdapterShopSubcategoryListBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterShopSubcategoryListBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->{

                if (selectedIdList.contains(arrayList.get(getAdapterPosition()).getId()))
                    selectedIdList.remove(arrayList.get(getAdapterPosition()).getId());
                else
                    selectedIdList.add(arrayList.get(getAdapterPosition()).getId());
                notifyItemChanged(getAdapterPosition());

                listener.onItemClick(selectedIdList);
            });
            //binding.select.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.SHOP_CATEGORY_SELECT));
            //binding.info.setOnClickListener(v->listener.onItemClick(adapterPosition,getAdapterPosition(), Constant.SHOP_CATEGORY_INFO));
        }
    }
}

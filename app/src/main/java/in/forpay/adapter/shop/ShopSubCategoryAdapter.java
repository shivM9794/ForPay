package in.forpay.adapter.shop;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.databinding.AdapterShopSubcategoryBinding;
import in.forpay.listener.SelectedIdListener;
import in.forpay.model.shop.ShopCategoryModel;
import in.forpay.util.Utility;

public class ShopSubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private SelectedIdListener listener;
    private ArrayList<List<ShopCategoryModel.DataBean.SubCategoryBean>> arrayList;
    private ArrayList<String> selectedIdList;
    private ArrayList<String> categoryList;
    private ShopSubCategoryListAdapter adapter;


    public ShopSubCategoryAdapter(Context context, ArrayList<List<ShopCategoryModel.DataBean.SubCategoryBean>> arrayList, ArrayList<String> selectedIdList, ArrayList<String> categoryList, SelectedIdListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.selectedIdList=selectedIdList;
        this.categoryList=categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new MyViewHolder(AdapterShopCategoryBinding.inflate(LayoutInflater.from(parent.getContext())));
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_shop_subcategory, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;

            viewHolder.binding.category.setText(categoryList.get(position));

            if (arrayList.get(position).size()==0)
                Utility.inflateNoDataFoundLayout((Activity) context,viewHolder.binding.inflateLayout,"No Sub Category Found");
            else
                setShopCategoryListAdapter(viewHolder,position);
        }
    }

    private void setShopCategoryListAdapter(MyViewHolder viewHolder, int position) {
        viewHolder.binding.recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        adapter=new ShopSubCategoryListAdapter(context, (ArrayList<ShopCategoryModel.DataBean.SubCategoryBean>) arrayList.get(position),selectedIdList,position,listener);
        viewHolder.binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterShopSubcategoryBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterShopSubcategoryBinding.bind(itemView);

           /* binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.SHOP_CATEGORY_SELECT));
            //binding.select.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.SHOP_CATEGORY_SELECT));
            binding.info.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.SHOP_CATEGORY_INFO));*/
        }
    }
}

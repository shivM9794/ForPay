package in.forpay.adapter.shop;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.databinding.AdapterMyProductCategoryBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ProductSuggestionModel;
import in.forpay.util.Utility;

public class MyProductCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayList;
    private int pagePosition;

    public MyProductCategoryAdapter(Context context, List<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayList, int pagePosition, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.pagePosition = pagePosition;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_my_product_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            ProductSuggestionModel.DataBean.ProductSuggestionBean model= arrayList.get(position);
            viewHolder.binding.title.setText(model.getName());
            if (model.getImages().size()>0)
                Utility.imageLoader((Activity) context,model.getImages().get(0).getImageUrl(),viewHolder.binding.image);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void updateList(ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> temp) {
        this.arrayList=temp;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterMyProductCategoryBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterMyProductCategoryBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), String.valueOf(pagePosition)));
        }
    }
}

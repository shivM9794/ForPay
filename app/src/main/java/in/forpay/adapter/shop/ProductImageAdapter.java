package in.forpay.adapter.shop;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.forpay.R;
import in.forpay.databinding.AdapterProductImageBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ProductSuggestionModel;
import in.forpay.util.Utility;

public class ProductImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<ProductSuggestionModel.DataBean.ProductSuggestionBean.ImagesBean> arrayList;
    private int position;
    private int selectedPosition;

    public ProductImageAdapter(Context context, List<ProductSuggestionModel.DataBean.ProductSuggestionBean.ImagesBean> arrayList, int position, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.position=position;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_product_image, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            if (selectedPosition==position)
                viewHolder.binding.mainLayout.setBackgroundResource(R.drawable.purple_shadow_bg);
            else
                viewHolder.binding.mainLayout.setBackgroundResource(R.drawable.purple_image_border_bg);
            Utility.imageLoader((Activity) context,arrayList.get(position).getImageUrl(),viewHolder.binding.image);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterProductImageBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterProductImageBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->{
                selectedPosition=getAdapterPosition();
                listener.onItemClick(getAdapterPosition(), String.valueOf(position));
                notifyDataSetChanged();
            });
        }
    }
}

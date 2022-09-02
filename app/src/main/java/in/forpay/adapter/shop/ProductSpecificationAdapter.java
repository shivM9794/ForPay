package in.forpay.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterProductSpecificationBinding;
import in.forpay.listener.ItemClickListener;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private ArrayList<String> arrayList;

    public ProductSpecificationAdapter(Context context, ArrayList<String> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_product_specification, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.binding.productSpecification.setText(arrayList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterProductSpecificationBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterProductSpecificationBinding.bind(itemView);
        }
    }
}

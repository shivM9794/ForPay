package in.forpay.adapter.shop;

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
import in.forpay.databinding.AdapterLoadingVerticalBinding;
import in.forpay.databinding.AdapterMyProductBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ProductSuggestionModel;

public class MyProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean>> arrayList;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private MyProductCategoryAdapter adapter;
    private int index = 0;

    public MyProductAdapter(Context context, List<ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean>> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_my_product, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(LayoutInflater.from(context).inflate(R.layout.adapter_loading_vertical, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            setAdapter(viewHolder.binding.recyclerView,position);

        }
    }

    private void setAdapter(RecyclerView recyclerView, int position) {
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        adapter=new MyProductCategoryAdapter(context,arrayList.get(position),position,listener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == arrayList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItems(ArrayList<ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean>> temp) {
        this.arrayList.addAll(temp);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        arrayList.add(null);
        notifyItemInserted(arrayList.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = arrayList.size() - 1;
        if (arrayList.get(position) == null) {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeData() {
        this.arrayList=new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterMyProductBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterMyProductBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), ""));
        }
    }

    public class ProgressHolder extends RecyclerView.ViewHolder{
        private AdapterLoadingVerticalBinding binding;

        public ProgressHolder(@NonNull View itemView) {
            super(itemView);
            binding=AdapterLoadingVerticalBinding.bind(itemView);
        }
    }
}

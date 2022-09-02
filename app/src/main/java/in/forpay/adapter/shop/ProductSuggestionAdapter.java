package in.forpay.adapter.shop;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.activity.shop.ProductSuggestionActivity;
import in.forpay.databinding.AdapterProductLoadingBinding;
import in.forpay.databinding.AdapterProductSuggestionBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ProductSuggestionModel;
import in.forpay.util.Utility;

public class ProductSuggestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener{

    private Context context;
    private ItemClickListener listener;
    private ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayList;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private ProductImageAdapter adapter;
    private ProductSpecificationAdapter adapterSpecification;
    private MyViewHolder viewHolder;

    public ProductSuggestionAdapter(Context context, ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_product_suggestion, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(LayoutInflater.from(context).inflate(R.layout.adapter_product_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        try {
            ProductSuggestionModel.DataBean.ProductSuggestionBean model = arrayList.get(position);
            if (model.getImages() != null && !model.getImages().isEmpty()) {

                if (payloads.isEmpty()) {
                    setAdapter(viewHolder.binding.recyclerView, model.getImages(), position);
                    String spec=model.getImages().get(0).getSpecs();
                    setAdapterSpecification(viewHolder.binding.recyclerViewSpecification, Utility.convertJSONIntoString(spec));
                    Utility.imageLoader((Activity) context, model.getImages().get(0).getImageUrl(), viewHolder.binding.image);

                } else {
                    int imagePosition = Integer.parseInt(String.valueOf(payloads.get(0)));
                    String spec=arrayList.get(position).getImages().get(imagePosition).getSpecs();
                    setAdapterSpecification(viewHolder.binding.recyclerViewSpecification, Utility.convertJSONIntoString(spec));
                    Utility.imageLoader((Activity) context, arrayList.get(position).getImages().get(imagePosition).getImageUrl(), viewHolder.binding.image);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof MyViewHolder) {
                viewHolder = (MyViewHolder) holder;
                ProductSuggestionModel.DataBean.ProductSuggestionBean model = arrayList.get(position);
                viewHolder.binding.productName.setText(model.getName());
                viewHolder.binding.productTagName.setText(model.getBrand());

                /*viewHolder.binding.productDetail1.setText(Utility.convertJSONIntoString(model.getSpecs1()));
                viewHolder.binding.productDetail2.setText(Utility.convertJSONIntoString(model.getSpecs2()));
                viewHolder.binding.productDetail3.setText(model.getWarranty());*/
                setItemTouchListener(viewHolder.binding.recyclerView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAdapter(RecyclerView recyclerView, List<ProductSuggestionModel.DataBean.ProductSuggestionBean.ImagesBean> images, int position) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter=new ProductImageAdapter(context,images,position,this);
        recyclerView.setAdapter(adapter);
    }

    private void setAdapterSpecification(RecyclerView recyclerView, ArrayList<String> specificationList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapterSpecification=new ProductSpecificationAdapter(context,specificationList,this);
        recyclerView.setAdapter(adapterSpecification);
    }

    private void setItemTouchListener(RecyclerView recyclerView) {
        RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };

        recyclerView.addOnItemTouchListener(mScrollTouchListener);
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

    public void addItems(ArrayList<ProductSuggestionModel.DataBean.ProductSuggestionBean> temp) {
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

    //Slide Image Click
    @Override
    public void onItemClick(int position, String tag) {
        if (viewHolder!=null && tag!=null && !tag.isEmpty()) {
            int viewPosition=Integer.parseInt(tag);
            //Call onBindViewHolder payloads
            notifyItemChanged(viewPosition,position);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterProductSuggestionBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterProductSuggestionBinding.bind(itemView);

            binding.yes.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), ProductSuggestionActivity.YES));
            binding.no.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), ProductSuggestionActivity.NO));
        }
    }

    public class ProgressHolder extends RecyclerView.ViewHolder{
        private AdapterProductLoadingBinding binding;

        public ProgressHolder(@NonNull View itemView) {
            super(itemView);
            binding=AdapterProductLoadingBinding.bind(itemView);
        }
    }
}

package in.forpay.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterMyRatingBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.GetMyRatingModel;

public class MyRatingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private ArrayList<Object> arrayList;
    private boolean isFromShopRating;

    public MyRatingAdapter(Context context, ArrayList<Object> arrayList,boolean isFromShopRating, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.isFromShopRating=isFromShopRating;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_my_rating, null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;

            if (isFromShopRating){
                GetMyRatingModel.DataBean.ShopRatingBean model=(GetMyRatingModel.DataBean.ShopRatingBean)arrayList.get(position);
                viewHolder.binding.ratingBy.setText(model.getRateBy());
                viewHolder.binding.ratingText.setText(model.getRatingText());
                if (!model.getRating().isEmpty())
                    viewHolder.binding.ratingBar.setRating(Float.parseFloat(model.getRating()));
            }else {
                GetMyRatingModel.DataBean.MyRatingBean model=(GetMyRatingModel.DataBean.MyRatingBean)arrayList.get(position);
                viewHolder.binding.ratingBy.setText(model.getRateBy());
                viewHolder.binding.ratingText.setText(model.getRatingText());
                if (!model.getRating().isEmpty())
                    viewHolder.binding.ratingBar.setRating(Float.parseFloat(model.getRating()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterMyRatingBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), ""));
        }
    }
}

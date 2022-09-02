package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.shop.AddShopActivity;
import in.forpay.databinding.ShopCategoryAdapterBinding;
import in.forpay.model.response.GetShopCategoryResponse;
import in.forpay.util.Utility;

public class ShopCategoryAdapter extends RecyclerView.Adapter<ShopCategoryAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetShopCategoryResponse.Data> shopCategoryResponses = new ArrayList<>();

    public ShopCategoryAdapter(Activity mActivity, ArrayList<GetShopCategoryResponse.Data> shopCategoryResponses) {
        this.mActivity = mActivity;
        this.shopCategoryResponses = shopCategoryResponses;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_category_adapter, parent, false);
//        Log.d("ShopCategoryAdapter", "sizeShop 1" + shopCategoryResponses.size());
        return new ShopCategoryAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Log.d("ShopCategoryAdapter", "sizeShop 2" + shopCategoryResponses.size());

        holder.brand_name.setText(shopCategoryResponses.get(position).getName());
       // Log.d("sdsdgdv", "erfsfsd" + shopCategoryResponses.get(position).getImgUrl());
        Utility.imageLoader(mActivity, shopCategoryResponses.get(position).getImgUrl(), holder.brand_icon);

        holder.brand_icon.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, AddShopActivity.class);

            //  Log.d("safsfsf","wefere"+shopCategoryResponses.get(position).getId());
            intent.putExtra("ShopCatID", shopCategoryResponses.get(position).getId());

           //Log.d("cvbnhng","poiuyyg"+shopCategoryResponses.get(position).getImgUrl());
            intent.putExtra("imageCatUrl", shopCategoryResponses.get(position).getImgUrl());
            mActivity.startActivity(intent);
            mActivity.finish();
        });
    }

    @Override
    public int getItemCount() {
//        Log.d("dsdsdsd", "qwdded" + shopCategoryResponses.size());
        return shopCategoryResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ShopCategoryAdapterBinding binding;
        RelativeLayout relative_layout;
        private ImageView brand_icon;
        private TextView brand_name;
        private TextView ewallet_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            Log.d("ShopCategoryAdapter", "sizeShop 3" + shopCategoryResponses.size());

            binding = ShopCategoryAdapterBinding.bind(itemView);
            brand_icon = itemView.findViewById(R.id.brand_icon);
            brand_name = itemView.findViewById(R.id.brand_name);
            ewallet_id = itemView.findViewById(R.id.ewallet_id);
            relative_layout = itemView.findViewById(R.id.relative_layout);

//            binding.getRoot().setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), shopCategoryResponses.get(getAdapterPosition()).getId()));

        }
    }
}

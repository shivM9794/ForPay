package in.forpay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.response.GetCategoryShopResponse;
import in.forpay.util.Utility;

public class CategoryShop extends RecyclerView.Adapter<CategoryShop.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetCategoryShopResponse.Data> mList = new ArrayList<>();

    public CategoryShop(Activity mActivity, ArrayList<GetCategoryShopResponse.Data> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_shop,parent,false);
        return new CategoryShop.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.brand_name.setText(mList.get(position).getName());
        holder.ewallet_id.setText(mList.get(position).getId());
        Utility.imageLoader(mActivity,mList.get(position).getImgUrl(),holder.brand_icon);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView brand_icon;
        TextView ewallet_id,brand_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            brand_icon = itemView.findViewById(R.id.brand_icon);
            ewallet_id = itemView.findViewById(R.id.ewallet_id);
            brand_name = itemView.findViewById(R.id.brand_name);


        }
    }
}

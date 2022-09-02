package in.forpay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rd.PageIndicatorView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.util.Utility;


public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.ItemViewHolder>  {

    private Activity activity;
    private ArrayList<MainRechargeModel.HomeBannerListBean> arrayList;
    private ItemClickListener listener;
    PageIndicatorView dotsIndicator;

    public HomeBannerAdapter(final Activity activity,ArrayList<MainRechargeModel.HomeBannerListBean> arrayList, PageIndicatorView dotsIndicator, ItemClickListener listener) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.dotsIndicator = dotsIndicator;
        this.listener=listener;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_home_banner,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Utility.imageLoader(activity,arrayList.get(position).getUrl(),holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
                listener.onItemClick(position,"");
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);

        }
    }

}

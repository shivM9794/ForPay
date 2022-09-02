package in.forpay.adapter.balance;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterHomeBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ItemClickListener listener;
    private ArrayList<MainRechargeModel.MainMenuBean> arrayList;
    private final String from;

    public HomeAdapter(Context context, ArrayList<MainRechargeModel.MainMenuBean> arrayList, ItemClickListener listener, String from) {
        this.context = context;
        this.listener = listener;
        this.from = from;

        if (arrayList!=null) {
            if (!from.equals("homeCategory"))
                this.arrayList = arrayList;
            else {
                ArrayList<MainRechargeModel.MainMenuBean> quickMenuBeanArrayList = new ArrayList<>();
                for (MainRechargeModel.MainMenuBean menuBean : arrayList) {
                    if (menuBean.getIsBbps().equals("1"))
                        quickMenuBeanArrayList.add(menuBean);
                }
                this.arrayList = quickMenuBeanArrayList;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_home, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            try {
                MyViewHolder viewHolder = (MyViewHolder) holder;
                MainRechargeModel.MainMenuBean model =arrayList.get(position);

                viewHolder.binding.title.setText(model.getName());
                //Log.d("Home Image "," - "+model.getImgurl());
                Utility.imageLoader((Activity) context, model.getImgurl(), viewHolder.binding.image);

                if (model.getIsNew().equals("yes")) {

                    Glide.with(context)
                            .load(R.drawable.new_n)
                            .into(viewHolder.binding.newLogo);



                    Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
                    animation.setDuration(1000); //1 second duration for each animation cycle
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
                    animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
                    viewHolder.binding.image.startAnimation(animation); //to start animation
                }



            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setData(ArrayList<MainRechargeModel.MainMenuBean> filterArrayList) {
//        arrayList=filterArrayList;
        if (filterArrayList!=null) {
            if (!from.equals("homeCategory"))
                this.arrayList = filterArrayList;
            else {
                ArrayList<MainRechargeModel.MainMenuBean> quickMenuBeanArrayList = new ArrayList<>();
                for (MainRechargeModel.MainMenuBean menuBean : filterArrayList) {
                    if (menuBean.getIsBbps().equals("1")) quickMenuBeanArrayList.add(menuBean);
                }
                this.arrayList = quickMenuBeanArrayList;
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterHomeBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterHomeBinding.bind(itemView);

            Log.d("HomeAdapterFinal","Clicked "+getAbsoluteAdapterPosition());

            binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.SHOP_LIST));
        }
    }
}

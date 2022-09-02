package in.forpay.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.forpay.R;
import in.forpay.databinding.RawHomeCategoryBinding;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class HomeDataCategoryAdapter extends RecyclerView.Adapter<HomeDataCategoryAdapter.MyViewHolder> {

    private List<Integer> mListDataIcon;
    private List<String> mListDataLable;
    private List<String> mListDataImgurl;
    private List<String> mListDataActivityName;
    private List<String> mListDataIsNew;
    private Activity activity;
    private ProgressHelper progressHelper;

    public HomeDataCategoryAdapter(Activity con, List<Integer> mListDataIcon, List<String> mListDataLable,List<String> mListDataImgurl,List<String> mListDataActivityName, List<String> mListDataIsNew, ProgressHelper progressHelper) {
        this.activity = con;
        this.mListDataLable = mListDataLable;
        this.mListDataIcon = mListDataIcon;
        this.progressHelper = progressHelper;
        this.mListDataImgurl=mListDataImgurl;
        this.mListDataActivityName=mListDataActivityName;
        this.mListDataIsNew=mListDataIsNew;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.raw_home_category,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Utility.imageLoader(activity, mListDataImgurl.get(position), holder.binding.imgCategory);


        holder.binding.txtTitle.setText(mListDataLable.get(position));

        Log.d("HomeDataCategory","Clicked 2 "+mListDataIsNew.get(position));
        //Log.e("isNew",mListDataIsNew.get(position)+"df");

        if(mListDataIsNew.get(position).equals("yes")) {
            Glide.with(activity)
                    .load(R.drawable.new_n)
                    .into(holder.binding.newLogo);
        }

        if(holder.binding.txtTitle.getText()==null || holder.binding.txtTitle.getText()==""){
            //holder.itemView.setVisibility(View.GONE);
            holder.binding.quickIcon.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mListDataLable == null ? 0 : mListDataLable.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RawHomeCategoryBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickFromHome(getAdapterPosition(),mListDataActivityName.get(getAdapterPosition()));
                    //Log.d("Id clicked2 ","clicked "+getAdapterPosition());
                    Log.d("HomeDataCategory","Clicked "+getAdapterPosition());
                }
            });


        }
    }

    public void onClickFromHome(int position,String value) {
        Log.d("HomeDataCategory","Clicked "+value);

        Bundle bundle=new Bundle();
        bundle.putString("from","homeCategory");
        Utility.openActivity(activity , value, bundle);
    }
}

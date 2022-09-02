package in.forpay.adapter.balance;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterMobileBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.util.Utility;

public class MobileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private ArrayList<MainRechargeModel.ServiceBean> arrayList;
    String selectedId="";

    public MobileAdapter(Context context, ArrayList<MainRechargeModel.ServiceBean> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_mobile, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder && position<arrayList.size()) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            MainRechargeModel.ServiceBean model= (MainRechargeModel.ServiceBean) arrayList.get(position);
            viewHolder.binding.title.setText(model.getService());
                       Utility.imageLoader((Activity) context, model.getImgUrl(), viewHolder.binding.image);
            if (selectedId.equals(arrayList.get(position).getId())){
                viewHolder.binding.select.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.select.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setItems(String selectedId, ArrayList<MainRechargeModel.ServiceBean> tempArrayList){
        this.selectedId=selectedId;
        this.arrayList=tempArrayList;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<MainRechargeModel.ServiceBean> filterArrayList) {
        arrayList=filterArrayList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterMobileBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterMobileBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), arrayList.get(getAdapterPosition()).getId()));
        }
    }
}

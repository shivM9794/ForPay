package in.forpay.adapter.balance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.forpay.R;
import in.forpay.databinding.AdapterCouponOfferBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.MainRechargeModel;

public class CouponOfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<MainRechargeModel.CouponOfferBean> arrayList;
    String selectedId="";

    public CouponOfferAdapter(Context context, List<MainRechargeModel.CouponOfferBean> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(context).inflate(R.layout.adapter_coupon_offer, parent, false);
        return new MyViewHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            try {
                MyViewHolder viewHolder = (MyViewHolder) holder;
                MainRechargeModel.CouponOfferBean model = arrayList.get(position);
                viewHolder.binding.title.setText(model.getText());
                //viewHolder.binding.price.setText("");
                viewHolder.binding.coupon.setText(model.getCoupon());

                if (!model.getExpireDate().isEmpty())
                    viewHolder.binding.couponExpire.setText("#Expire: "+model.getExpireDate());

                if (selectedId.equals(arrayList.get(position).getId())){
                    viewHolder.binding.check.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.binding.check.setVisibility(View.INVISIBLE);
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

    public void setItems(String selectedId) {
        this.selectedId=selectedId;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCouponOfferBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterCouponOfferBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), ""));
        }
    }
}

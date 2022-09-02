package in.forpay.adapter.busbookingadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterBoardingDroppingBinding;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.network.ItemClickListeners;
import in.forpay.util.Constant;

public class DroppingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListeners itemClickListeners;
    private int oldItemSelectedPos = -1;
    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean> dropingBeans;



    public DroppingAdapter(Context context, ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean> dropingBeans, ItemClickListeners itemClickListeners) {
        this.context =context;
        this.itemClickListeners = itemClickListeners;
        this.dropingBeans = dropingBeans;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_boarding_dropping,parent,false);
        return new DroppingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DroppingHolder){

            ((DroppingHolder) holder).bindData(holder.getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return dropingBeans.size();
    }

    private class DroppingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterBoardingDroppingBinding binding;

        public DroppingHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);


            binding.linRootItem.setOnClickListener(this);
            binding.radioBtn.setOnClickListener(this);
        }


        public void bindData(int adapterPosition) {

            binding.txtPlaceName.setText(dropingBeans.get(adapterPosition).getDpName());
            if (!dropingBeans.get(adapterPosition).getDpAddress().isEmpty()){
                binding.txtPlaceSubTitle.setVisibility(View.VISIBLE);
                binding.txtPlaceSubTitle.setText(dropingBeans.get(adapterPosition).getDpAddress());
            }else {
                binding.txtPlaceSubTitle.setVisibility(View.GONE);

            }
            if (!dropingBeans.get(adapterPosition).getContactNumber().isEmpty()){
                binding.txtNumber.setVisibility(View.VISIBLE);
                binding.txtNumber.setText("Contact :- "+dropingBeans.get(adapterPosition).getContactNumber());
            }else {
                binding.txtNumber.setVisibility(View.GONE);

            }

            binding.txtBusTime.setText(Constant.getDateFormat("hh:mm a",dropingBeans.get(adapterPosition).getDpTime()));

            if (oldItemSelectedPos == adapterPosition){
                binding.radioBtn.setChecked(true);
            }else{
                binding.radioBtn.setChecked(false);
            }

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.radioBtn:
                case R.id.linRootItem:
                    if (dropingBeans.get(getAdapterPosition()).isItemSelected()){
                        dropingBeans.get(getAdapterPosition()).setItemSelected(false);
                        binding.radioBtn.setChecked(false);
                    }else{
                        dropingBeans.get(getAdapterPosition()).setItemSelected(true);
                        oldItemSelectedPos= getAdapterPosition();
                        binding.radioBtn.setChecked(true);
                    }

                    itemClickListeners.onItemClick(getAdapterPosition());

                    break;

            }
        }
    }
}

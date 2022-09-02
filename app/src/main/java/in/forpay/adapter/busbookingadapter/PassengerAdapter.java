package in.forpay.adapter.busbookingadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterPassengerBinding;
import in.forpay.model.busbookingModel.PassengerInfo;
import in.forpay.network.ItemClickListeners;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder> {

    private Context context;
    private ArrayList<PassengerInfo> passengerInfos;
    private ItemClickListeners itemClickListeners;

    public PassengerAdapter(Context context, ArrayList<PassengerInfo> passengerInfos, ItemClickListeners itemClickListeners) {
        this.context =context;
        this.itemClickListeners = itemClickListeners;
        this.passengerInfos = passengerInfos;

    }

    @NonNull
    @Override
    public PassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_passenger,parent,false);
        return new PassengerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerViewHolder holder, int position) {
        holder.bindData(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return passengerInfos.size();
    }

    public class PassengerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterPassengerBinding binding;

        public PassengerViewHolder(@NonNull ViewDataBinding itemView) {
            super(itemView.getRoot());

            binding =DataBindingUtil.bind(itemView.getRoot());
        }

        public void bindData(int pos) {

            if (passengerInfos.get(pos).getFirstName() == null || passengerInfos.get(pos).getFirstName().isEmpty()){
                binding.linPassengerDetailFillView.setVisibility(View.GONE);
                binding.linAddTravellersView.setVisibility(View.VISIBLE);
            }else{
                binding.linPassengerDetailFillView.setVisibility(View.VISIBLE);
                binding.linAddTravellersView.setVisibility(View.GONE);
                binding.txtChange.setVisibility(View.GONE);

                binding.txtPassengerName.setText(passengerInfos.get(pos).getFirstName());
                binding.txtSeatNumber.setText("Seat "+ passengerInfos.get(pos).getSeatNo());

            }


            binding.txtAddTravellers.setOnClickListener(this);
            binding.txtChange.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.txtAddTravellers:
                    itemClickListeners.onItemClick(getAdapterPosition());
                    break;
                case R.id.txtChange:
                    break;
            }
        }
    }
}

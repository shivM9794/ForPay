package in.forpay.adapter.busbookingadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.busbooking.BusBookingDetails;
import in.forpay.databinding.AdapterPassengerDetailBinding;
import in.forpay.fragment.busbookingfragment.ShowBookingDetailsFragment;
import in.forpay.model.busbookingModel.BusBookDetail;

public class AdapterPassengerDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ShowBookingDetailsFragment.getCancelTicketItemList itemClickListeners;
    private Context context;
    private ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetailsBeans;
    private int oldItemSelectedPos = -1;

    public AdapterPassengerDetail(Context context, ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetailsBeans, ShowBookingDetailsFragment.getCancelTicketItemList itemClickListeners) {
        this.itemClickListeners = itemClickListeners;
        this.context = context;
        this.passengerDetailsBeans = passengerDetailsBeans;

    }

    public AdapterPassengerDetail(Activity activity, ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetailsBeans, BusBookingDetails.getCancelTicketItemList getCancelTicketItemList) {
        this.itemClickListeners = itemClickListeners;
        this.context = context;
        this.passengerDetailsBeans = passengerDetailsBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_passenger_detail, parent, false);
        return new PassengerDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PassengerDetailHolder) {
            ((PassengerDetailHolder) holder).bindData(holder.getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return passengerDetailsBeans.size();
    }

    private class PassengerDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterPassengerDetailBinding binding;


        public PassengerDetailHolder(View view) {
            super(view.getRootView());

            binding = DataBindingUtil.bind(view);

            binding.linSeatItem.setOnClickListener(this);
            binding.checkBoxSelectItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.checkBoxSelectItem:
                case R.id.linSeatItem:
                    if (passengerDetailsBeans.get(getAdapterPosition()).getSelected()) {
                        passengerDetailsBeans.get(getAdapterPosition()).setSelected(false);
                        binding.checkBoxSelectItem.setChecked(false);
                        itemClickListeners.onItemClick(false, passengerDetailsBeans.get(getAdapterPosition()), getAdapterPosition());
                    } else {
                        passengerDetailsBeans.get(getAdapterPosition()).setSelected(true);
                        oldItemSelectedPos = getAdapterPosition();
                        binding.checkBoxSelectItem.setChecked(true);
                        itemClickListeners.onItemClick(true, passengerDetailsBeans.get(getAdapterPosition()), getAdapterPosition());
                    }

                    notifyDataSetChanged();

                    break;
            }
        }

        public void bindData(int adapterPosition) {

            if (binding.checkBoxSelectItem.isChecked()) {
                binding.checkBoxSelectItem.setChecked(true);
            } else {
                binding.checkBoxSelectItem.setChecked(false);
            }

            binding.txtPassengerName.setText(passengerDetailsBeans.get(adapterPosition).getName());
            binding.txtPassengerAge.setText(passengerDetailsBeans.get(adapterPosition).getAge());
            binding.txtPassengerGender.setText(passengerDetailsBeans.get(adapterPosition).getGender());
            binding.txtPassengerSeat.setText(passengerDetailsBeans.get(adapterPosition).getSeatNumber());
            binding.txtAmount.setText(passengerDetailsBeans.get(adapterPosition).getSeatAmount());

            if (passengerDetailsBeans.get(adapterPosition).getStatus().equalsIgnoreCase("CANCELLED")) {
                binding.imgCancel.setVisibility(View.VISIBLE);
                binding.llCancel.setVisibility(View.GONE);
                binding.lineCancel.setVisibility(View.GONE);
            } else {
                binding.imgCancel.setVisibility(View.GONE);
                binding.llCancel.setVisibility(View.GONE);
                binding.lineCancel.setVisibility(View.GONE);
            }
        }
    }
}

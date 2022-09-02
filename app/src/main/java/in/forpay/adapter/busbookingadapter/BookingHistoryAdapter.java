package in.forpay.adapter.busbookingadapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.TransactionDetails.PrepaidRechargeDetails;
import in.forpay.activity.busbooking.BusBookingDetails;
import in.forpay.databinding.AdapterBusBookingHistoryBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.busbookingModel.BookingHistoryModel;

public class BookingHistoryAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private ArrayList<Object> arrayList;
    private ItemClickListener listener;

    public BookingHistoryAdapter(Activity activity, ArrayList<Object> arrayList, ItemClickListener listener) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.listener = listener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bus_booking_history, parent, false);
        return new BookingHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BookingHistoryViewHolder) {

            BookingHistoryViewHolder historyViewHolder = (BookingHistoryViewHolder) holder;
            BookingHistoryModel.BookingListBean model = (BookingHistoryModel.BookingListBean) arrayList.get(position);

            historyViewHolder.binding.name.setText(model.getName());
            historyViewHolder.binding.bookingAmount.setText("Rs." + model.getAmount());
            historyViewHolder.binding.txtSource.setText(model.getSource());
            historyViewHolder.binding.txtDestination.setText(model.getDestination());
            historyViewHolder.binding.bookingDate.setText(model.getDate());
            String status = model.getStatus();
            historyViewHolder.binding.bookingStatus.setText(status);
            if (status.equalsIgnoreCase("PENDING")) {
                historyViewHolder.binding.bookingStatus.setTextColor(activity.getResources().getColor(R.color.orange_new));
            }
            else if (status.equalsIgnoreCase("CANCELLED")) {
                historyViewHolder.binding.bookingStatus.setTextColor(activity.getResources().getColor(R.color.red_mat));
            }else {
                historyViewHolder.binding.bookingStatus.setTextColor(activity.getResources().getColor(R.color.green_new));
            }


            historyViewHolder.binding.mainLayout.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putString("orderId",model.getId());
                //listener.onItemClick(position, model.getId(), bundle);

                Intent intent = new Intent(activity, BusBookingDetails.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
            });


        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private class BookingHistoryViewHolder extends RecyclerView.ViewHolder {
        private AdapterBusBookingHistoryBinding binding;

        public BookingHistoryViewHolder(View view) {
            super(view);
            binding = AdapterBusBookingHistoryBinding.bind(view);
        }
    }
}

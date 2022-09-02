package in.forpay.adapter.busbookingadapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.BusesAvailableOnJourneyAdapter1Binding;
import in.forpay.fragment.busbookingfragment.AvailableBusesFragment;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.network.ItemClickListeners;
import in.forpay.network.PolicyClick;
import in.forpay.util.Constant;
import in.forpay.util.PreferenceConnector;

public class BusesAvailableOnJourneyAdapter extends RecyclerView.Adapter<BusesAvailableOnJourneyAdapter.BusesAvailableHolder> {


    private Context context;
    private ItemClickListeners itemClickListeners;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> busStopsBeans;
    AvailableBusesFragment.shareInterface shareInterface;
    private PolicyClick policyClick;

    public BusesAvailableOnJourneyAdapter(Context context,
                                          ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> busStopsBeans,
                                          ItemClickListeners itemClickListeners, AvailableBusesFragment.shareInterface shareInterface, PolicyClick policyClick) {
        this.context = context;
        this.itemClickListeners = itemClickListeners;
        this.busStopsBeans = busStopsBeans;
        this.shareInterface = shareInterface;
        this.policyClick = policyClick;


    }

    @NonNull
    @Override
    public BusesAvailableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.buses_available_on_journey_adapter1, parent, false);

        return new BusesAvailableHolder((BusesAvailableOnJourneyAdapter1Binding) binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BusesAvailableHolder holder, int position) {

        holder.bindData(busStopsBeans);

    }


    @Override
    public int getItemCount() {
        return busStopsBeans.size();
    }

    public void clearList(ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> busStopsBeans) {
        this.busStopsBeans.clear();
        this.busStopsBeans.addAll(busStopsBeans);
        notifyDataSetChanged();

    }

    class BusesAvailableHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        BusesAvailableOnJourneyAdapter1Binding binding;

        public BusesAvailableHolder(@NonNull BusesAvailableOnJourneyAdapter1Binding itemView) {
            super(itemView.getRoot());

            binding = itemView;

            binding.checkBoxOfTravellersItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    binding.checkBoxOfTravellersItem.setChecked(b);

                }
            });

            binding.linBusRootItem.setOnClickListener(this);
            binding.imgWhatsApp.setOnClickListener(this);
        }

        public void bindData(ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> busStopsBeans) {


            String startTime = Constant.getDateFormat("hh:mm a", busStopsBeans.get(getAdapterPosition()).getDepartureTime());
            String endTime = Constant.getDateFormat("hh:mm a", busStopsBeans.get(getAdapterPosition()).getArrivalTime());
            binding.busDepartureTime.setText(startTime);
            binding.busArrivalTime.setText(endTime);
            String busDepartureName = PreferenceConnector.readString(context, PreferenceConnector.BUS_SOURCE, "");
            String busArrivalName = PreferenceConnector.readString(context, PreferenceConnector.BUS_DESTINATION, "");
            binding.busDepartureName.setText(busDepartureName);
            binding.busArrivalName.setText(busArrivalName);
            //binding.txtTimeOfTravellers.setText(startTime + " - " + endTime);

            binding.txtDurationOfTravellers.setText(Constant.getHour("hh", busStopsBeans.get(getAdapterPosition()).getTravelDuration()) +
                    "h " + Constant.getMins("mm", busStopsBeans.get(getAdapterPosition()).getTravelDuration()) +
                    "m");

            String travel_duration = Constant.getHour("hh", busStopsBeans.get(getAdapterPosition()).getTravelDuration()) +
                    "h " + Constant.getMins("mm", busStopsBeans.get(getAdapterPosition()).getTravelDuration()) +
                    "m";
            String bus_amount = String.valueOf((Double.parseDouble(busStopsBeans.get(getAdapterPosition()).getFares().get(0)) - (Double.parseDouble(busStopsBeans.get(getAdapterPosition()).getCommission()))));
            String busAlertTxt=busStopsBeans.get(getAdapterPosition()).getBusAlert();

            PreferenceConnector.writeString(context, PreferenceConnector.TRAVEL_DURATION, travel_duration);
            PreferenceConnector.writeString(context, PreferenceConnector.BUS_NAME, busStopsBeans.get(getAdapterPosition()).getBusName());
            PreferenceConnector.writeString(context, PreferenceConnector.BUS_TYPE, busStopsBeans.get(getAdapterPosition()).getBusType());
            PreferenceConnector.writeString(context, PreferenceConnector.BUS_AMOUNT, bus_amount);

            binding.txtSeatAvailableOfTravellers.setText(busStopsBeans.get(getAdapterPosition()).getAvlSeats() + " Seat(s) Left");
            binding.txtNameOfTravellers.setText(busStopsBeans.get(getAdapterPosition()).getBusName());
            binding.txtTypeOfTravellers.setText(busStopsBeans.get(getAdapterPosition()).getBusType());

            binding.txtActualPriceOfTravellers.setText("Rs. " + busStopsBeans.get(getAdapterPosition()).getFares().get(0));
            binding.txtSavePriceOfTravellers.setText(busStopsBeans.get(getAdapterPosition()).getCommission());
            if(busAlertTxt!=null) {
                binding.busAlertTxt.setText(busAlertTxt);
            }
            binding.txtOfferPriceOfTravellers.setText("Rs. " + (Double.parseDouble(busStopsBeans.get(getAdapterPosition()).getFares().get(0)) - (Double.parseDouble(busStopsBeans.get(getAdapterPosition()).getCommission()))));

            binding.policy.setOnClickListener(view -> {

                policyClick.onItemClick(busStopsBeans.get(getAdapterPosition()).getCancellationPolicy());


            });
           /* binding.btnPay.setOnClickListener(view -> {
                itemClickListeners.onItemClick(getAdapterPosition());
            });*/

            binding.mainLayout.setOnClickListener(view -> {
                itemClickListeners.onItemClick(getAdapterPosition());
            });

        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.linBusRootItem:
                    // itemClickListeners.onItemClick(getAdapterPosition());
                    break;
                case R.id.imgWhatsApp:
                    shareInterface.onWhatsAppShare(getAdapterPosition());
                    //Utility.showToast(context,"This Feature is not implemented yet", "ERROR");
                    break;
            }
        }
    }
}

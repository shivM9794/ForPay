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
import in.forpay.util.PreferenceConnector;

public class BoardingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListeners itemClickListeners;
    private int oldItemSelectedPos = -1;

    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean> boardingPointsBeans;

    public BoardingAdapter(Context context, ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean> boardingPointsBeans, ItemClickListeners itemClickListeners) {
        this.context = context;
        this.itemClickListeners = itemClickListeners;
        this.boardingPointsBeans = boardingPointsBeans;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_boarding_dropping, parent, false);
        return new BoardingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BoardingHolder) {

            ((BoardingHolder) holder).bindData(holder.getAdapterPosition());
        }

    }

    @Override
    public int getItemCount() {
        return boardingPointsBeans.size();
    }


    private class BoardingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterBoardingDroppingBinding binding;

        public BoardingHolder(View view) {
            super(view);

            binding = DataBindingUtil.bind(view);

            binding.linRootItem.setOnClickListener(this);
            binding.radioBtn.setOnClickListener(this);
        }

        public void bindData(int adapterPosition) {

            binding.txtPlaceName.setText(boardingPointsBeans.get(adapterPosition).getBpName());
            if (!boardingPointsBeans.get(adapterPosition).getBpAddress().isEmpty()) {
                binding.txtPlaceSubTitle.setVisibility(View.VISIBLE);
                binding.txtPlaceSubTitle.setText(boardingPointsBeans.get(adapterPosition).getBpAddress());
            } else {
                binding.txtPlaceSubTitle.setVisibility(View.GONE);
            }

            if (!boardingPointsBeans.get(adapterPosition).getContactNumber().isEmpty()) {
                binding.txtNumber.setText("Contact :- " + boardingPointsBeans.get(adapterPosition).getContactNumber());
                PreferenceConnector.writeString(context, PreferenceConnector.BUS_CONTACT, "Contact :- " + boardingPointsBeans.get(adapterPosition).getContactNumber());
            } else {
                binding.txtNumber.setVisibility(View.VISIBLE);
                binding.txtNumber.setVisibility(View.GONE);
            }

            binding.txtBusTime.setText(Constant.getDateFormat("hh:mm a", boardingPointsBeans.get(adapterPosition).getBpTime()));

            if (oldItemSelectedPos == adapterPosition) {
                binding.radioBtn.setChecked(true);
            } else {
                binding.radioBtn.setChecked(false);
            }

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.radioBtn:
                case R.id.linRootItem:
                    if (boardingPointsBeans.get(getAdapterPosition()).isItemSelected()) {
                        boardingPointsBeans.get(getAdapterPosition()).setItemSelected(false);
                        binding.radioBtn.setChecked(false);
                    } else {
                        boardingPointsBeans.get(getAdapterPosition()).setItemSelected(true);
                        oldItemSelectedPos = getAdapterPosition();
                        binding.radioBtn.setChecked(true);
                    }

                    itemClickListeners.onItemClick(getAdapterPosition());
                    break;

            }
        }
    }
}

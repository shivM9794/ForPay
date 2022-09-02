package in.forpay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.listener.OfferListener;
import in.forpay.model.request.OfferModel;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<OfferModel> mList = new ArrayList<>();
    private OfferListener mListener;

    public OffersAdapter(Activity mActivity, ArrayList<OfferModel> list, OfferListener listener) {
        this.mActivity = mActivity;
        this.mList = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.offers_item, viewGroup, false);
        return new ViewHolder(view);
    }

    public void filterList(ArrayList<OfferModel> filteredList) {
        mList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        /*if (position % 2 == 0) {
            viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(mActivity,
                    R.color.light_gray));
        } else {
            viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(mActivity,
                    R.color.white));
        }*/
        if(!mList.get(position).getData().equals("NA")) {
            viewHolder.textData.setText("Data : " + mList.get(position).getData());
        }else {
            viewHolder.textData.setVisibility(View.GONE);
        }
        String amount =mList.get(position).getAmount() +" "+  mActivity.getString(R.string.rupees);
        viewHolder.textViewAmount.setText(amount);
        String details = mList.get(position).getDetail();
        viewHolder.textViewDetail.setText(details);
        String validity = mList.get(position).getValidity();
        viewHolder.textViewValidity.setText("Validity " + validity);
        if(mList.get(position).getExtraoffer().equals("0")){
            viewHolder.commissiontv.setText(mList.get(position).getCommission());
        }
        else {
            viewHolder.commissiontv.setText(mList.get(position).getCommission() + " + " + mList.get(position).getExtraoffer());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout cardView;
        private TextView textViewAmount;
        private TextView textViewDetail;
        private TextView textViewValidity;
        private TextView textData;
        TextView commissiontv;

        ViewHolder(@NonNull View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            textViewAmount = view.findViewById(R.id.textViewAmount);
            textViewDetail = view.findViewById(R.id.textViewDetail);
            textViewValidity = view.findViewById(R.id.textViewValidity);
            commissiontv=view.findViewById(R.id.commissiontv);
            textData = view.findViewById(R.id.textData);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onSelect(mList.get(getAdapterPosition()).getAmount(),mList.get(getAdapterPosition()).getDetail(),
                            mList.get(getAdapterPosition()).getExtraoffer(),mList.get(getAdapterPosition()).getCommission());
                }
            });
        }
    }
}

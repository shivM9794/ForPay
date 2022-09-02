package in.forpay.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.ActivityAddRechargeAdapterBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.response.GetAddRechargeFavContactResponse;
import in.forpay.util.Utility;

public class AddRechargeAdapter extends RecyclerView.Adapter<AddRechargeAdapter.ViewHolder> {

    private Activity mActivity;
    private ItemClickListener listener;
    String selectedId = "";
    private ArrayList<GetAddRechargeFavContactResponse.OperatorList> mList = new ArrayList<>();

    public AddRechargeAdapter(Activity mActivity, ItemClickListener listener, ArrayList<GetAddRechargeFavContactResponse.OperatorList> mList) {
        this.mActivity = mActivity;
        this.listener = listener;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_add_recharge_adapter, parent, false);
        //Log.d("Recharge", "sizeEwallet1" + mList.size());
        return new AddRechargeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Log.d("Recharge", "sizeEwallet2" + mList.size());

        holder.brand_name.setText(mList.get(position).getName());
//        holder.editText.setHint(mList.get(position).getPlaceHolder());
//        holder.label.setHint(mList.get(position).getLabel());
        Utility.imageLoader(mActivity, mList.get(position).getIconUrl(), holder.brand_icon);

        if (selectedId.equals(mList.get(position).getOperatorId())) {
            holder.binding.select.setVisibility(View.VISIBLE);
        } else {
            holder.binding.select.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        //Log.d("Recharge", "sizeEwalletis" + mList.size());
        return mList.size();
    }

    public void setItems(String selectedId, ArrayList<GetAddRechargeFavContactResponse.OperatorList> tempArrayList) {

        this.selectedId = selectedId;
        this.mList = tempArrayList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ActivityAddRechargeAdapterBinding binding;
        RelativeLayout relative_layout;
        private ImageView brand_icon;
        private TextView brand_name, label;
        private EditText editText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ActivityAddRechargeAdapterBinding.bind(itemView);
            brand_icon = itemView.findViewById(R.id.brand_icon);
            brand_name = itemView.findViewById(R.id.brand_name);
            editText = itemView.findViewById(R.id.editText);
            label = itemView.findViewById(R.id.label);
            relative_layout = itemView.findViewById(R.id.relative_layout);

            binding.getRoot().setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), mList.get(getAdapterPosition()).getOperatorId()));


        }
    }
}

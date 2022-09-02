package in.forpay.adapter.busbookingadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterBusRouteBinding;
import in.forpay.model.busbookingModel.BusRoutModel;

public class BusRouteAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private ArrayList<Object> arrayList;


    public BusRouteAdapter(Activity activity, ArrayList<Object> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bus_route, parent, false);
        return new BusRouteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BusRouteHolder) {
            BusRouteHolder busRouteHolder = (BusRouteHolder) holder;

            BusRoutModel.DataBean.RouteListBean model = (BusRoutModel.DataBean.RouteListBean) arrayList.get(position);

            busRouteHolder.binding.stopName.setText(model.getStopName());
            busRouteHolder.binding.date.setText(model.getDate());

            if (model.getIsSelected().equalsIgnoreCase("yes")) {
                if (model.getType().equalsIgnoreCase("boardingPoint")) {
                    busRouteHolder.binding.linItem.setBackgroundColor(activity.getResources().getColor(R.color.green_new_t));
                    Glide.with(activity)
                            .load(activity.getResources().getDrawable(R.drawable.route_green))
                            .error(activity.getResources().getDrawable(R.drawable.route_green))
                            .into(busRouteHolder.binding.imgSelect);
                } else {
                    busRouteHolder.binding.linItem.setBackgroundColor(activity.getResources().getColor(R.color.orange_new_t));
                    Glide.with(activity)
                            .load(activity.getResources().getDrawable(R.drawable.route_orange))
                            .error(activity.getResources().getDrawable(R.drawable.route_orange))
                            .into(busRouteHolder.binding.imgSelect);
                }
            }


        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private class BusRouteHolder extends RecyclerView.ViewHolder {

        AdapterBusRouteBinding binding;


        public BusRouteHolder(View view) {
            super(view.getRootView());
            binding = DataBindingUtil.bind(view);

        }

    }


}

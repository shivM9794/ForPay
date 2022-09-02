package in.forpay.adapter.busbookingadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.AdapterSearchBusBinding;
import in.forpay.model.CityList;
import in.forpay.network.ItemClickListeners;

public class SearchCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private ArrayList<CityList.DataBean.BusStopsBean> cityList;
    private ArrayList<CityList.DataBean.BusStopsBean> myTempList;
    private ItemClickListeners itemClickListeners;

    public SearchCityAdapter(ArrayList<CityList.DataBean.BusStopsBean> cityList, ItemClickListeners itemClickListeners) {

        this.cityList = cityList;
        this.myTempList = new ArrayList<>();
        this.myTempList.addAll(cityList);
        this.itemClickListeners = itemClickListeners;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_search_bus, parent, false);
        return new SearchBusHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SearchBusHolder){

            ((SearchBusHolder) holder).bindData(holder.getAdapterPosition());
        }

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void clearListByDefault(ArrayList<CityList.DataBean.BusStopsBean> strings) {

        cityList.clear();
        cityList.addAll(myTempList);
        notifyDataSetChanged();

    }

    @Override
    public Filter getFilter() {
        return cityFilter;
    }

    private Filter cityFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                if (charSequence.length() > 2) {
                    cityList.clear();

                    if (myTempList.size() != 0) {
                        for (CityList.DataBean.BusStopsBean  cityName : myTempList) {
                            if (cityName.getStopName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                cityList.add(cityName);
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = cityList;
                    filterResults.count = cityList.size();
                    return filterResults;
                }
            }
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {


            if (filterResults.count == 0) {
                cityList.clear();
            }
            notifyDataSetChanged();


        }
    };


    class SearchBusHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterSearchBusBinding searchBusBinding;

        SearchBusHolder(@NonNull ViewDataBinding searchBusBinding) {
            super(searchBusBinding.getRoot());
            this.searchBusBinding = (AdapterSearchBusBinding) searchBusBinding;

            this.searchBusBinding.txtSearchCityItem.setOnClickListener(this);

        }

        public void bindData(int adapterPosition) {
            this.searchBusBinding.txtSearchCityItem.setText(cityList.get(adapterPosition).getStopName());
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.txtSearchCityItem:
                    itemClickListeners.onItemClick(getAdapterPosition());
                    break;
            }
        }
    }
}

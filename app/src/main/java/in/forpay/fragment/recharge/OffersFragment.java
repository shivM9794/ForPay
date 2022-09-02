package in.forpay.fragment.recharge;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.OffersAdapter;
import in.forpay.database.DataPlansModel;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentOffersBinding;
import in.forpay.listener.OfferListener;
import in.forpay.model.request.OfferModel;

public class OffersFragment extends Fragment {

    private FragmentOffersBinding mBinding;
    private ArrayList<OfferModel> list = new ArrayList<>();
    private OffersAdapter offersAdapter;
    private DatabaseHelper databaseHelper;
    ArrayList<OfferModel> allDataArrayList = new ArrayList<>();
    public OffersFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        databaseHelper = new DatabaseHelper(getActivity());
        getAllDataFromDataBase();
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBinding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    filterData(s.toString());
                }else {
                    if (offersAdapter != null)
                        offersAdapter.filterList(list);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void filterData(String text) {
        try {
            ArrayList<OfferModel> filteredList2 = new ArrayList<>();


            for (OfferModel item : allDataArrayList) {
                if (item.getDetail().toLowerCase().contains(text.toLowerCase()) || item.getType().toLowerCase().contains(text.toLowerCase()) || item.getValidity().toLowerCase().contains(text.toLowerCase()) || item.getData().toLowerCase().contains(text.toLowerCase()) || item.getAmount().toLowerCase().contains(text.toLowerCase())) {
                    filteredList2.add(item);
                    Log.d("Offer data ",""+item);
                }
            }

            if (offersAdapter != null)
                offersAdapter.filterList(filteredList2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Get AllData
     */
    private void getAllDataFromDataBase() {
        ArrayList<DataPlansModel> arrayList = databaseHelper.getDataPlans("0");

        for (DataPlansModel data : arrayList) {
            allDataArrayList.add(new OfferModel(data.getType(), data.getAmount(), data.getDetail()
                    , data.getValidity(), data.getTalktime(), data.getExtraOffer(), data.getCommission(), data.getData()));
        }
    }

    /**
     * Set adapter
     */
    public void setAdapter(ArrayList<OfferModel> list, OfferListener listener) {
        this.list.clear();
        this.list = list;
        offersAdapter = new OffersAdapter(getActivity(),list,listener);
        mBinding.recyclerView.setAdapter(offersAdapter);
    }
}

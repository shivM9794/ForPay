package in.forpay.fragment.busbookingfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.busbookingadapter.PassengerAdapter;
import in.forpay.databinding.FragmentAddTravellersBinding;
import in.forpay.model.busbookingModel.PassengerInfo;
import in.forpay.model.busbookingModel.SeatLayoutModel;
import in.forpay.network.ItemClickListeners;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTravellersFragment extends Fragment implements ItemClickListeners {

    FragmentAddTravellersBinding binding;
    ArrayList<PassengerInfo> passengerInfos;
    private PassengerAdapter adapter;
    private String noOfSeats;


    private ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> mSelectedSeat;


    public AddTravellersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_travellers, container, false);
    binding.setAddTravellers(this);
    return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();
        setRecyclerView();
    }

    private void getBundleData() {

        noOfSeats = getArguments().getString(Constant.NO_OF_SEATS);
    }

    private void setRecyclerView() {

        binding.recTravellers.setLayoutManager(new LinearLayoutManager(getContext()));
        passengerInfos = new ArrayList<>();

        if (!noOfSeats.isEmpty()){

            for (int i = 0; i < Integer.parseInt(noOfSeats) ; i++) {
                passengerInfos.add(new PassengerInfo());
            }
        }
        binding.txtTotalTravellers.setText("Travellers ("+0+"/"+noOfSeats+")" );
        adapter = new PassengerAdapter(getActivity(), passengerInfos,this);
        binding.recTravellers.setAdapter(adapter);
    }

    public void updateData(int noOfSeats, ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> mSelectedSeat, boolean isAdded) {

        this.mSelectedSeat = mSelectedSeat;


       /* if (!isAdded){

            for (int i = 0; i < passengerInfos.size(); i++) {
                boolean isDataContain = true;


                for (int j = 0; j < mSelectedSeat.size() ; j++) {

                    if (passengerInfos.get(i).getSeatNo() != null
                            && !passengerInfos.get(i).getSeatNo().toLowerCase().trim().equals(mSelectedSeat.get(j).getName().toLowerCase().toString())){

                        isDataContain = false;
                    }
                }
                if (!isDataContain){
                    passengerInfos.set(i,new PassengerInfo());
                }
            }

        }else{


            passengerInfos.clear();

            for (int i = 0; i < noOfSeats ; i++) {
                passengerInfos.add(new PassengerInfo());
            }

        }*/

        passengerInfos.clear();

        for (int i = 0; i < noOfSeats ; i++) {
            passengerInfos.add(new PassengerInfo());
        }


        adapter.notifyDataSetChanged();


        SelectSeatAndTravellers selectSeatAndTravellers = (SelectSeatAndTravellers) getActivity().getSupportFragmentManager().findFragmentByTag(SelectSeatAndTravellers.class.getSimpleName());
        selectSeatAndTravellers.makeViewClickable(true, passengerInfos);

        binding.txtTotalTravellers.setText("Travellers ("+0+"/"+noOfSeats+")" );
    }

    @Override
    public void onItemClick(int pos) {

        if(mSelectedSeat != null &&  mSelectedSeat.get(pos).getName() != null){

            AddAndUpdatePassengerDetail addAndUpdatePassengerDetail = new AddAndUpdatePassengerDetail();
            Bundle bundle = new Bundle();
            bundle.putString("SeatNo",mSelectedSeat.get(pos).getName());
            bundle.putInt("pos",pos);
            bundle.putParcelableArrayList(Constant.PASSENGER_INFO, passengerInfos);
            addAndUpdatePassengerDetail.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameContainerBusBooking,addAndUpdatePassengerDetail,AddAndUpdatePassengerDetail.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }else{
            Utility.showToast(getContext(),"Please Select Seat First");
        }




    }

    public void addDetailToList(PassengerInfo list, int pos) {

        passengerInfos.set(pos,list);
        adapter.notifyItemChanged(pos);
        int  size= 0;

        for (int i = 0; i < passengerInfos.size() ; i++) {

            if (passengerInfos.get(i).getSeatNo() != null && !passengerInfos.get(i).getSeatNo().isEmpty()){
                size = i+1;
            }
        }


        binding.txtTotalTravellers.setText("Travellers ("+size+"/"+ passengerInfos.size()+")" );

        if (size == passengerInfos.size()){

            SelectSeatAndTravellers selectSeatAndTravellers = (SelectSeatAndTravellers) getActivity().getSupportFragmentManager().findFragmentByTag(SelectSeatAndTravellers.class.getSimpleName());
           selectSeatAndTravellers.makeViewClickable(true, passengerInfos);

        }else{
            SelectSeatAndTravellers selectSeatAndTravellers = (SelectSeatAndTravellers) getActivity().getSupportFragmentManager().findFragmentByTag(SelectSeatAndTravellers.class.getSimpleName());
            selectSeatAndTravellers.makeViewClickable(false, passengerInfos);
        }
    }
}

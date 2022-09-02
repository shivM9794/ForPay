package in.forpay.fragment.busbookingfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import in.forpay.R;
import in.forpay.databinding.FragmentAddAndUpdatePassengerDetailBinding;
import in.forpay.model.busbookingModel.PassengerInfo;


public class AddAndUpdatePassengerDetail extends Fragment implements View.OnClickListener {


    FragmentAddAndUpdatePassengerDetailBinding binding;
    private  String seatNo;
    private int pos;

    public AddAndUpdatePassengerDetail() {
        // Required empty public constructor
    }

    public static AddAndUpdatePassengerDetail newInstance(String param1, String param2) {
        AddAndUpdatePassengerDetail fragment = new AddAndUpdatePassengerDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_and_update_passenger_detail, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();
        setViewClickListeners();
    }

    private void getBundleData() {

         seatNo = getArguments().getString("SeatNo");
         pos  = getArguments().getInt("pos");

    }

    private void setViewClickListeners() {


        binding.btAddPassenger.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_add_passenger:
                if (checkFieldValidation()){

                    addUserDetailToPassengerModel();
                }

                break;

            case R.id.imgBack:
                getActivity().onBackPressed();
                break;
        }

    }

    private void addUserDetailToPassengerModel() {


        SelectSeatAndTravellers selectSeatAndTravellers = (SelectSeatAndTravellers) getActivity().getSupportFragmentManager().findFragmentByTag(SelectSeatAndTravellers.class.getSimpleName());
        AddTravellersFragment addTravellersFragment = (AddTravellersFragment) selectSeatAndTravellers.adapter.getFragment(1);

        PassengerInfo passengerInfo = new PassengerInfo();

        passengerInfo.setFirstName(binding.etPassengerFirstName.getText().toString().trim());
        passengerInfo.setLastName(binding.etPassengerLastName.getText().toString().trim());

        passengerInfo.setSeatNo(seatNo);
        passengerInfo.setAge(binding.etAge.getText().toString().trim());

        passengerInfo.setMobileNumber(binding.etMobileNo.getText().toString());



        String gender = "";
       if (binding.rbMale.isChecked()){
           gender = "M";
       }
       if (binding.rbFemale.isChecked()){
           gender = "F";
       }
        passengerInfo.setGender(gender);

        String title = "";
        if (binding.rbMr.isChecked()){
            title = "Mr";
        }
        if (binding.rbMrs.isChecked()){
            title = "Mrs";
        }

        passengerInfo.setTitle(title);

        addTravellersFragment.addDetailToList(passengerInfo,pos);

        getActivity().getSupportFragmentManager().popBackStack();
    }

    private boolean checkFieldValidation() {

        if (binding.etPassengerFirstName.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.etPassengerLastName.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please Enter Last Name", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.etAge.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please Enter Your Age", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.etMobileNo.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }
}

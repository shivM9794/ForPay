package in.forpay.fragment.busbookingfragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.busbookingadapter.BookingHistoryAdapter;
import in.forpay.databinding.FragmentBookingHistoryBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.busbookingModel.BookingHistoryModel;
import in.forpay.model.busbookingModel.BusBookDetail;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class BookingHistoryFragment extends Fragment {

    private FragmentBookingHistoryBinding binding;
    private Activity activity;
    ProgressHelper progressHelper;
    private BookingHistoryAdapter adapter;
    private ArrayList<Object> arrayList = new ArrayList<>();


    public BookingHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        progressHelper = new ProgressHelper(activity);
        getBookedHistory();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_history, container, false);
        binding.setFragmentBusBookHistory(this);

        return binding.getRoot();

    }

    private void getBookedHistory() {
        progressHelper.show();


        Map<String,String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei

        map1.put("os", Utility.getOs(getActivity()));

        map1.put("versionCode" , Utility.getVersionCode(getActivity())); // version code


        String request = Utility.mapWrapper(getActivity(),map1);



        if (in.forpay.util.Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.BUS_TICKET_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            progressHelper.dismiss();


                            try {
                                BookingHistoryModel model = new Gson().fromJson(result, BookingHistoryModel.class);

                                if (model.getResCode().equals("200")) {

                                    arrayList.clear();
                                    arrayList.addAll(model.getBookingList());
                                    binding.recBookingHistory.setLayoutManager(new LinearLayoutManager(activity));
                                    adapter = new BookingHistoryAdapter(activity, arrayList, new ItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, String tag) {

                                            ShowBookingDetailsFragment showBookingDetailsFragment = new ShowBookingDetailsFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString(Constant.BUS_ID, tag);
                                            bundle.putString(Constant.BOOKED_ID, tag);
                                            bundle.putString(Constant.BOOKED_STATUS, model.getBookingList().get(position).getStatus());
                                            bundle.putString(Constant.TICKET_ID, model.getBookingList().get(position).getId());
                                            bundle.putString("viewType","rc");
                                            showBookingDetailsFragment.setArguments(bundle);

                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.frameContainerBusBooking, showBookingDetailsFragment, ShowBookingDetailsFragment.class.getSimpleName())
                                                    .commit();


                                        }
                                    });
                                    binding.recBookingHistory.setAdapter(adapter);


                                } else {
                                    in.forpay.util.Utility.showToast(activity, model.getResText(), "");
                                }


                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(activity, getString(R.string.internet_connect), "ERROR");
        }


    }


}
package in.forpay.fragment.busbookingfragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.FragmentSeatBookingBinding;
import in.forpay.model.busbookingModel.SeatLayoutModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeatBookingFragment extends Fragment implements View.OnClickListener {


    private FragmentSeatBookingBinding binding;
    private String busId;
    private int noOfSeats;
    private ProgressHelper progressHelper;

    private int seatGaping = 10;
    private int seatSize = 100;
    private int STATUS_AVAILABLE = 1;
    private int STATUS_BOOKED = 2;
    private int STATUS_RESERVED = 3;
    private List<TextView> seatViewList = new ArrayList<>();

    private int rowPos = -1;
    private int columnPos = -1;
    private LinearLayout layout = null;

    private String selectedIds = "";


    public ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> mSelectedSeat = new ArrayList<>();

    public SeatBookingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_seat_booking, container, false);
        binding.setSeatBookingFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();
        intiHelperClass();
        getSeatLayoutApi();
        setViewClickListeners();


    }

    private void setViewClickListeners() {

        binding.radioGrpOfBirth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.btnLowerBirth:
                        binding.llLower.setVisibility(View.VISIBLE);
                        binding.llUpperLayout.setVisibility(View.GONE);
                        binding.imgDriver.setVisibility(View.VISIBLE);
                        binding.btnLowerBirth.setBackground(getActivity().getDrawable(R.drawable.green_round));
                        binding.btnUpperBirth.setBackground(getActivity().getDrawable(R.drawable.green_round_border));
                        binding.btnLowerBirth.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        binding.btnUpperBirth.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        break;
                    case R.id.btnUpperBirth:
                        binding.llLower.setVisibility(View.GONE);
                        binding.imgDriver.setVisibility(View.INVISIBLE);
                        binding.llUpperLayout.setVisibility(View.VISIBLE);
                        binding.btnLowerBirth.setBackground(getActivity().getDrawable(R.drawable.green_round_border));
                        binding.btnUpperBirth.setBackground(getActivity().getDrawable(R.drawable.green_round));
                        binding.btnLowerBirth.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        binding.btnUpperBirth.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        break;
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void intiHelperClass() {

        progressHelper = new ProgressHelper(getActivity());

    }

    private void getBundleData() {

        busId = Objects.requireNonNull(getArguments()).getString(Constant.BUS_ID);

        noOfSeats = Integer.parseInt(Objects.requireNonNull(getArguments()).getString(Constant.NO_OF_SEATS));

        //Bundle b = getArguments();
        //String busId2 = b.getString(Constant.DATE_OF_JOURNEY);

        //Log.d("Bus clicked"," bus id from seatbooking "+busId + "busid 2"+busId2+" seats "+noOfSeats);

    }

    private void getSeatLayoutApi() {


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei
        map1.put("versionCode" , Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));

        map1.put("busId" , busId); // version code

        String request = Utility.mapWrapper(getActivity(),map1);

        getAvailableBusesOnRoute(request);

    }

    private void getAvailableBusesOnRoute(String request) {

        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.BUS_SEAT_LAYOUT, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect), "");
        }
    }

    private void parseResponse(String result) {

        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {

            SeatLayoutModel seatLayoutModel = new Gson().fromJson(result, SeatLayoutModel.class);

            if (!seatLayoutModel.getResCode().equals("200")) {
                Utility.showToast(getActivity(), seatLayoutModel.getResText(), seatLayoutModel.getResCode());
            } else {
                // filterSeatLayoutResponse(seatLayoutModel.getData());

                setDataToSeatLayout(seatLayoutModel.getData());
            }


        } else {
            //Utility.showToast(getActivity(), getString(R.string.server_not_responding), "");
            startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
        }
    }

    private void setDataToSeatLayout(SeatLayoutModel.DataBean data) {


        int zIndex = 0;
        int columns = 0;
        int rows = 0;

        for (int i = 0; i < data.getSeatLayout().size(); i++) {

            if (columns < Integer.parseInt(data.getSeatLayout().get(i).getColums())) {
                columns = Integer.parseInt(data.getSeatLayout().get(i).getColums());
            }
            if (rows < Integer.parseInt(data.getSeatLayout().get(i).getRow())) {
                rows = Integer.parseInt(data.getSeatLayout().get(i).getRow());
            }
            if (Integer.parseInt(data.getSeatLayout().get(i).getZIndex()) > 0) {
                zIndex = Integer.parseInt(data.getSeatLayout().get(i).getZIndex());
            }
        }


        if (zIndex == 0) {

            ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> doSelectionRowSort = doSelectionRowSort(data.getSeatLayout());
            ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList = new ArrayList<>();
            for (int i = 0; i < rows; i++) {
                ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList2 = new ArrayList<>();
                for (int j = 0; j < doSelectionRowSort.size(); j++) {
                    if (i == Integer.parseInt((doSelectionRowSort.get(j)).getRow())) {
                        arrayList2.add(doSelectionRowSort.get(j));
                    }
                }
                arrayList.addAll(arrayList.size(), doSelectionColumnSort(arrayList2));
            }
            binding.llSeatsLower.addView(createTableLayout(rows, columns, doSelectionRowSort));
            return;
        }


        binding.radioGrpOfBirth.setVisibility(View.VISIBLE);

        ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> lowerBirth = new ArrayList<>();
        ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> upperBirth = new ArrayList<>();

        for (int i = 0; i < data.getSeatLayout().size(); i++) {
            if (Integer.parseInt(data.getSeatLayout().get(i).getZIndex()) == 0) {
                lowerBirth.add(data.getSeatLayout().get(i));
            } else {
                upperBirth.add(data.getSeatLayout().get(i));
            }
        }

        ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> tempListLowerBirth = new ArrayList<>();

        for (int i = 0; i < columns; i++) {
            ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList5 = new ArrayList<>();
            for (int j = 0; j < lowerBirth.size(); j++) {
                if (i == Integer.parseInt(lowerBirth.get(j).getColums())) {
                    arrayList5.add(lowerBirth.get(j));
                }
            }

            tempListLowerBirth.addAll(tempListLowerBirth.size(), doSelectionColumnSort(arrayList5));
        }

        ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> tempListUpperBirth = new ArrayList<>();


        for (int i = 0; i < columns; i++) {
            ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList6 = new ArrayList<>();
            for (int j = 0; j < upperBirth.size(); j++) {
                if (i == Integer.parseInt(upperBirth.get(j).getRow())) {
                    arrayList6.add(upperBirth.get(j));
                }
            }
            tempListUpperBirth.addAll(tempListUpperBirth.size(), doSelectionColumnSort(arrayList6));
        }
        binding.llSeatsLower.addView(createTableLayoutLower(rows, columns, tempListLowerBirth));
        binding.llSeatsUpper.addView(createTableLayoutUpper(rows, columns, tempListUpperBirth));

    }

    public static ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> doSelectionRowSort(List<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList) {

        for (int i = 0; i < arrayList.size(); i++) {
            int i2 = i;
            int i3 = i2;
            while (i2 < arrayList.size()) {
                if (Integer.parseInt((arrayList.get(i2)).getRow()) < Integer.parseInt((arrayList.get(i3)).getRow())) {
                    i3 = i2;
                }
                i2++;
            }
            SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails = arrayList.get(i3);
            arrayList.set(i3, arrayList.get(i));
            arrayList.set(i, busSeatsDetails);
        }
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            String sb = "ROW: " +
                    (arrayList.get(i4)).getRow() +
                    "::" +
                    (arrayList.get(i4)).getColums();
            Log.e("ROW ::: ", sb);
        }
        return (ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean>) arrayList;
    }


    public static ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> doSelectionColumnSort(ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            int i2 = i;
            int i3 = i2;
            while (i2 < arrayList.size()) {
                if (Integer.parseInt(arrayList.get(i2).getColums()) < Integer.parseInt(arrayList.get(i3).getColums())) {
                    i3 = i2;
                }
                i2++;
            }
            SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails = arrayList.get(i3);
            arrayList.set(i3, arrayList.get(i));
            arrayList.set(i, busSeatsDetails);
            new Matrix();
        }
        return arrayList;
    }

    private TableLayout createTableLayout(int i, int i2, ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList) {
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(getActivity());
        TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams();
        //   layoutParams2.setMargins(1, 1, 1, 1);
        // layoutParams2.weight = 1.0f;
        for (int i3 = 0; i3 <= i2; i3++) {
            TableRow tableRow = new TableRow(getActivity());
            for (int i4 = i; i4 >= 0; i4--) {
                tableRow.addView(getViewSeat(getBusSeatObj(arrayList, i4, i3), 0), layoutParams2);
            }
            tableLayout.addView(tableRow, layoutParams);
        }
        return tableLayout;
    }

    private SeatLayoutModel.DataBean.SeatLayoutBean getBusSeatObj(ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList, int rows, int column) {
        SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails = new SeatLayoutModel.DataBean.SeatLayoutBean();
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            if (Integer.parseInt(arrayList.get(i3).getRow()) == rows && Integer.parseInt(arrayList.get(i3).getColums()) == column) {
                busSeatsDetails = arrayList.get(i3);
            }
        }
        return busSeatsDetails;

    }

    private TableLayout createTableLayoutLower(int rows, int column, ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList) {
        TableLayout tableLayout = new TableLayout(getContext());
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // layoutParams2.setMargins(1, 1, 1, 1);
        // layoutParams2.weight = 1.0f;
        for (int i3 = 0; i3 <= column; i3++) {
            TableRow tableRow = new TableRow(getActivity());
            for (int i4 = rows; i4 >= 0; i4--) {
                tableRow.addView(getViewSeat(getBusSeatObj(arrayList, i4, i3), column), layoutParams2);
            }
            tableLayout.addView(tableRow, layoutParams);
        }
        return tableLayout;
    }

    private TableLayout createTableLayoutUpper(int row, int column, ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> arrayList) {
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableLayout tableLayout = new TableLayout(getActivity());
        TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //  layoutParams2.setMargins(1, 1, 1, 1);
        //layoutParams2.weight = 1.0f;
        for (int i3 = 0; i3 <= column; i3++) {
            TableRow tableRow = new TableRow(getActivity());
            for (int i4 = row; i4 >= 0; i4--) {
                tableRow.addView(getViewSeat(getBusSeatObj(arrayList, i4, i3), column), layoutParams2);
            }
            tableLayout.addView(tableRow, layoutParams);
        }
        return tableLayout;
    }


    private View getViewSeat(final SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails, int column) {
        View inflate = View.inflate(getActivity(), R.layout.item_bus_seats, null);
        inflate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        TextView textView = inflate.findViewById(R.id.tv_seats);
        TextView txtPrice = inflate.findViewById(R.id.tv_price);
        ImageView imageView = inflate.findViewById(R.id.iv_seat);


        if (!(busSeatsDetails == null || busSeatsDetails.getName() == null)) {

            if (Integer.parseInt(busSeatsDetails.getLength()) == 1 && Integer.parseInt(busSeatsDetails.getWidth()) == 1) {
                imageView.setImageResource(getImageSeat(busSeatsDetails));
            } else if (Integer.parseInt(busSeatsDetails.getLength()) == 2 && Integer.parseInt(busSeatsDetails.getWidth()) == 1) {
                imageView.setImageResource(getImageSleeper(busSeatsDetails));
            } else if (Integer.parseInt(busSeatsDetails.getLength()) == 1 && Integer.parseInt(busSeatsDetails.getWidth()) == 2) {
                imageView.setImageResource(getImageSleeperHoz(busSeatsDetails));
            }
            textView.setText(busSeatsDetails.getName());
            txtPrice.setText(getActivity().getString(R.string.rupee) + " " + busSeatsDetails.getFare());

            inflate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (isSelector(busSeatsDetails)) {
                        deleteSelectSeat(busSeatsDetails);
                        setSeatImage(busSeatsDetails, imageView);
                    } else if (busSeatsDetails.getIsAvailable().equals("true") && mSelectedSeat.size() < noOfSeats) {
                        mSelectedSeat.add(busSeatsDetails);
                        setSeatImage(busSeatsDetails, imageView);


                        SelectSeatAndTravellers selectSeatAndTravellers = (SelectSeatAndTravellers) getActivity().getSupportFragmentManager().findFragmentByTag(SelectSeatAndTravellers.class.getSimpleName());

                        if (mSelectedSeat.size() == noOfSeats) {

                            // AddTravellersFragment addTravellersFragment = (AddTravellersFragment) getActivity().getSupportFragmentManager().findFragmentByTag(AddTravellersFragment.class.getSimpleName());


                            AddTravellersFragment addTravellersFragment = (AddTravellersFragment) selectSeatAndTravellers.adapter.getFragment(1);
                            addTravellersFragment.updateData(noOfSeats, mSelectedSeat, true);

                            selectSeatAndTravellers.binding.viewPager.setCurrentItem(1);
                        }


                        selectSeatAndTravellers.showTicketDetail(mSelectedSeat, noOfSeats);

                    }
                }
            });
        }
        return inflate;
    }

    private int getImageSeat(SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails) {
        if (!busSeatsDetails.getIsAvailable().equals("true")) {
            return R.drawable.ic_seat_booked;
        }
        if (isSelector(busSeatsDetails)) {
            return R.drawable.ic_seat_selected;
        }
        return busSeatsDetails.getLadiesSeat().equals("true") ? R.drawable.ic_seat_leadies : R.drawable.ic_seat;
    }

    private int getImageSleeper(SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails) {
        if (!busSeatsDetails.getIsAvailable().equals("true")) {
            return R.drawable.ic_sleeper_hoz_booked;
        }
        if (isSelector(busSeatsDetails)) {
            return R.drawable.ic_sleeper_hoz_selected;
        }
        return busSeatsDetails.getLadiesSeat().equals("true") ? R.drawable.ic_sleeper_hoz_ladies : R.drawable.ic_sleeper_hoz;
    }

    private int getImageSleeperHoz(SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails) {
        if (!busSeatsDetails.getIsAvailable().equals("true")) {
            return R.drawable.ic_sleeper_booked;
        }
        if (isSelector(busSeatsDetails)) {
            return R.drawable.ic_sleeper_selected;
        }
        return busSeatsDetails.getLadiesSeat().equals("true") ? R.drawable.ic_sleeper_ladies : R.drawable.ic_sleeper;
    }

    public boolean isSelector(SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails) {
        for (int i = 0; i < this.mSelectedSeat.size(); i++) {
            if (busSeatsDetails.getName().equals(this.mSelectedSeat.get(i).getName())) {
                return true;
            }
        }
        return false;
    }


    public void deleteSelectSeat(SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails) {
        for (int i = 0; i < this.mSelectedSeat.size(); i++) {
            if (busSeatsDetails.getName().equals(this.mSelectedSeat.get(i).getName())) {
                this.mSelectedSeat.remove(i);
            }
        }

        SelectSeatAndTravellers selectSeatAndTravellers = (SelectSeatAndTravellers) getActivity().getSupportFragmentManager().findFragmentByTag(SelectSeatAndTravellers.class.getSimpleName());
        selectSeatAndTravellers.showTicketDetail(mSelectedSeat, noOfSeats);

        AddTravellersFragment addTravellersFragment = (AddTravellersFragment) selectSeatAndTravellers.adapter.getFragment(1);
        addTravellersFragment.updateData(noOfSeats, mSelectedSeat, false);

    }

    public void setSeatImage(SeatLayoutModel.DataBean.SeatLayoutBean busSeatsDetails, ImageView imageView) {
        if (Integer.parseInt(busSeatsDetails.getLength()) == 1 && Integer.parseInt(busSeatsDetails.getWidth()) == 1) {
            imageView.setImageResource(getImageSeat(busSeatsDetails));
        } else if (Integer.parseInt(busSeatsDetails.getLength()) == 2 && Integer.parseInt(busSeatsDetails.getWidth()) == 1) {
            imageView.setImageResource(getImageSleeper(busSeatsDetails));
        } else if (Integer.parseInt(busSeatsDetails.getLength()) == 1 && Integer.parseInt(busSeatsDetails.getWidth()) == 2) {
            imageView.setImageResource(getImageSleeperHoz(busSeatsDetails));
        }
    }



    /*private void filterSeatLayoutResponse(SeatLayoutModel.DataBean data) {


        ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> layoutList = new ArrayList<>(data.getSeatLayout());

        Collections.sort(layoutList, new Comparator<SeatLayoutModel.DataBean.SeatLayoutBean>() {
            @Override
            public int compare(SeatLayoutModel.DataBean.SeatLayoutBean seatLayoutBean, SeatLayoutModel.DataBean.SeatLayoutBean t1) {
                if (Integer.parseInt(seatLayoutBean.getColums()) == Integer.parseInt(t1.getColums())) {
                    return 0;
                } else if (Integer.parseInt(seatLayoutBean.getColums()) < Integer.parseInt(t1.getColums())) {
                    return -1;
                }
                return 1;
            }
        });


        Collections.sort(layoutList, new Comparator<SeatLayoutModel.DataBean.SeatLayoutBean>() {
            @Override
            public int compare(SeatLayoutModel.DataBean.SeatLayoutBean seatLayoutBean, SeatLayoutModel.DataBean.SeatLayoutBean t1) {
                if (Integer.parseInt(seatLayoutBean.getRow()) == Integer.parseInt(t1.getRow())) {
                    return 0;
                } else if (Integer.parseInt(seatLayoutBean.getRow()) > Integer.parseInt(t1.getRow())) {
                    return -1;
                }
                return 1;
            }
        });

        Log.e("response ", layoutList.size() + "");


        ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> lowerBirth = new ArrayList<>();
        ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> upperBirth = new ArrayList<>();
        for (int i = 0; i < layoutList.size(); i++) {


            if (layoutList.get(i).getZIndex().equals("0")) {
                lowerBirth.add(layoutList.get(i));
            } else {
                upperBirth.add(layoutList.get(i));
            }
        }


        setLowerBirthData(lowerBirth);
        setUpperBirthData(upperBirth);



    }

    private void setUpperBirthData(ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> upperBirth) {

        binding.frameUpperBirth.removeAllViews();


        LinearLayout layoutSeat = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.HORIZONTAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(20 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        binding.frameUpperBirth.addView(layoutSeat);


        for (int i = 0; i < upperBirth.size(); i++) {


            if (Integer.parseInt(upperBirth.get(i).getRow()) != rowPos) {

                if (rowPos == -1) {

                    layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layoutSeat.addView(layout);
                } else {
                    int originalGap;
                    if (rowPos == 3)
                        originalGap =  130 + seatGaping;
                    else
                        originalGap = 50;

                    layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(originalGap, seatGaping, seatGaping, seatGaping);
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.line));
                    layout.setLayoutParams(layoutParams);
                    layoutSeat.addView(layout);


                    layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layoutSeat.addView(layout);

                }

                rowPos = Integer.parseInt(upperBirth.get(i).getRow());
                columnPos = Integer.parseInt(upperBirth.get(i).getColums());


            } else {

                if (Integer.parseInt(upperBirth.get(i).getColums()) != columnPos && rowPos == Integer.parseInt(upperBirth.get(i).getRow())) {
                    columnPos = Integer.parseInt(upperBirth.get(i).getColums());
                }

            }

            TextView view = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize + 100);



            layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
            view.setLayoutParams(layoutParams);
            view.setPadding(seatGaping, 0, 0, 2 * seatGaping);
            view.setId(i);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(Color.WHITE);
            if (upperBirth.get(i).getIsAvailable().equals("true")) {
                view.setTag(STATUS_AVAILABLE);
                view.setBackgroundResource(R.color.green);
            } else {
                view.setTag(STATUS_BOOKED);
                view.setBackgroundResource(R.color.red_light);
            }

            view.setText(upperBirth.get(i).getName());
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
            layout.addView(view);

            view.setOnClickListener(this);

            seatViewList.add(view);
        }
    }

    private void setLowerBirthData(ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> lowerBirth) {

        binding.frameLowerBirth.removeAllViews();


        LinearLayout layoutSeat = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.HORIZONTAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(20 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        binding.frameLowerBirth.addView(layoutSeat);


        for (int i = 0; i < lowerBirth.size(); i++) {


            if (Integer.parseInt(lowerBirth.get(i).getRow()) != rowPos) {

                if (rowPos == -1) {

                    layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layoutSeat.addView(layout);
                } else {
                    int originalGap;
                    if (rowPos == 3)
                        originalGap =  130 + seatGaping;
                    else
                        originalGap = 50;

                    layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(originalGap, seatGaping, seatGaping, seatGaping);
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.line));
                    layout.setLayoutParams(layoutParams);
                    layoutSeat.addView(layout);


                    layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layoutSeat.addView(layout);

                }

                rowPos = Integer.parseInt(lowerBirth.get(i).getRow());
                columnPos = Integer.parseInt(lowerBirth.get(i).getColums());


            } else {

                if (Integer.parseInt(lowerBirth.get(i).getColums()) != columnPos && rowPos == Integer.parseInt(lowerBirth.get(i).getRow())) {
                    columnPos = Integer.parseInt(lowerBirth.get(i).getColums());
                }

            }

            TextView view = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize + 100);



            layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
            view.setLayoutParams(layoutParams);
            view.setPadding(seatGaping, 0, 0, 2 * seatGaping);
            view.setId(i);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(Color.WHITE);
            if (lowerBirth.get(i).getIsAvailable().equals("true")) {
                view.setTag(STATUS_AVAILABLE);
                view.setBackgroundResource(R.color.green);
            } else {
                view.setTag(STATUS_BOOKED);
                view.setBackgroundResource(R.color.red_light);
            }

            view.setText(lowerBirth.get(i).getName());
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
            layout.addView(view);

            view.setOnClickListener(this);

            seatViewList.add(view);
        }
    }*/


    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("onAttach", "onAttach: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("onDetach", "onDetach: ");
    }
*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onDetach", "onDetach: ");
    }

    @Override
    public void onClick(View view) {

        if ((int) view.getTag() == STATUS_AVAILABLE) {
            if (selectedIds.contains(view.getId() + ",")) {
                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                view.setBackgroundResource(R.color.green);
            } else {
                selectedIds = selectedIds + view.getId() + ",";
                view.setBackgroundResource(R.color.red_light);

                //  SelectSeatAndTravellers selectSeatAndTravellers = (SelectSeatAndTravellers) getActivity().getSupportFragmentManager().findFragmentByTag(SelectSeatAndTravellers.class.getSimpleName());
                //  selectSeatAndTravellers.binding.viewPager.setCurrentItem(1);
            }
        } else if ((int) view.getTag() == STATUS_BOOKED) {
            Toast.makeText(getContext(), "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();
        } else if ((int) view.getTag() == STATUS_RESERVED) {
            Toast.makeText(getContext(), "Seat " + view.getId() + " is Reserved", Toast.LENGTH_SHORT).show();
        }
    }
}

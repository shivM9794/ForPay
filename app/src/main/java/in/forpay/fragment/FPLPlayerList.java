package in.forpay.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.forpaypremierleague.PaginationListener;
import in.forpay.databinding.FragmentFPLPlayerlistBinding;
import in.forpay.databinding.FragmentFPLResultBinding;
import in.forpay.databinding.MatchPlayerlistLayoutBinding;
import in.forpay.databinding.MatchResultLayoutBinding;
import in.forpay.model.response.GetFPLPlayerlist;
import in.forpay.model.response.GetFPLResult;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class FPLPlayerList extends BottomSheetDialogFragment {
    FragmentFPLPlayerlistBinding binding;

    ProgressHelper progressHelper;
    Activity activity;
    private String groupId;
    private int PAGE = 1;
    private ArrayList<GetFPLPlayerlist.Datum> fplResultList = new ArrayList<>();
    private PlayerListAdapter adapter;
    private boolean isLoadMore = false;
    private int PAGINATION = 50;

    public FPLPlayerList(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_f_p_l_playerlist, container, false);
        activity = getActivity();
        progressHelper = new ProgressHelper(activity);

        init();

        return binding.getRoot();


    }

    private void init() {
        initAdapter();
        callPlayerListApi(true);
    }

    private void callPlayerListApi(Boolean showProgressbar) {

        if (PAGE == 1) {
            fplResultList.clear();
        }
        if (showProgressbar) {
            progressHelper.show();
        }

        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("latitude", Utility.getLatitude(activity));
        map1.put("longitude", Utility.getLongitude(activity));
        map1.put("pageNumber", String.valueOf(PAGE));
        map1.put("activityName", "");
        map1.put("groupId", groupId);

        String request = Utility.mapWrapper(activity, map1);

        if (Utility.isNetworkConnected(activity)) {
            QueryManager.getInstance().postRequest(activity, Constant.METHOD_FPL_PLAYERS, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {

                    if (showProgressbar) {
                        progressHelper.dismiss();
                    }
                    parseCashbackResponseData(result);
                }
            });
        }
    }

    private void parseCashbackResponseData(String result) {
        Log.d("..........", "parseCashbackResponseData: " + result);
        GetFPLPlayerlist response = new Gson().fromJson(result, GetFPLPlayerlist.class);
        if (response.getResCode().equals(Constant.CODE_200)) {
            fplResultList.addAll(response.getData());
            adapter.UpdateData(response.getData());

           /* ArrayList<GetFPLPlayerlist.Datum> data = response.getData();
            PlayerListAdapter adapter = new PlayerListAdapter(activity, data);
            binding.rcvplayerlist.setLayoutManager(new LinearLayoutManager(activity));
            binding.rcvplayerlist.setAdapter(adapter);*/
        }

    }

    private void initAdapter() {
        adapter = new PlayerListAdapter(activity, new ArrayList<>(), new PaginationListener() {
            @Override
            public void onPage() {
                isLoadMore = true;
                PAGE = 1;
                PAGE = (fplResultList.size() / PAGINATION) + 1;
                callPlayerListApi(true);
            }
        });

        binding.rcvplayerlist.setLayoutManager(new LinearLayoutManager(activity));
        binding.rcvplayerlist.setAdapter(adapter);
    }


    private class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> {
        private Activity activity;
        private final ArrayList<GetFPLPlayerlist.Datum> list;
        private PaginationListener listener;

        public PlayerListAdapter(Activity activity, ArrayList<GetFPLPlayerlist.Datum> data, PaginationListener listener) {
            this.activity = activity;
            this.list = data;
            this.listener = listener;
        }

        public void UpdateData(ArrayList<GetFPLPlayerlist.Datum> data) {
            int startPosition = 0;
            if (list.size() > 0) {
                startPosition = Math.max((list.size() - 2), 0);
            }
            this.list.addAll(data);
            notifyItemRangeChanged(startPosition, this.list.size());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.match_playerlist_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.mobile.setText(list.get(position).getMobile());
            holder.binding.name.setText(list.get(position).getName());
            holder.binding.rank.setText(list.get(position).getRank());

            if (position == list.size() - 1) {
                if (((position + 1) % PAGINATION) == 0) {
                    listener.onPage();
                }
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            MatchPlayerlistLayoutBinding binding;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                binding = MatchPlayerlistLayoutBinding.bind(itemView);
            }
        }
    }
}
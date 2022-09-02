package in.forpay.fragment;

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
import in.forpay.adapter.LanguageListAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.DialogFragmentChooseLanguageBinding;
import in.forpay.model.request.LanguageList;

public class LanguageChangeFragment extends Fragment {

    private DialogFragmentChooseLanguageBinding mBinding;
    private DatabaseHelper databaseHelper;

    public static LanguageChangeFragment newInstance(){
        return new LanguageChangeFragment();
    }
    public LanguageChangeFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_choose_language, container, false);


        return mBinding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }


    private void init(){
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseHelper = new DatabaseHelper(getActivity());
        ArrayList<LanguageList> list = databaseHelper.getLanguageList();

        //Log.d("TAG",""+list.get(0).getLanguage());

        setAdapter(list);
    }

    private void setAdapter(ArrayList<LanguageList> list) {
        if (list == null || list.size() == 0) {
            return;
        }



        mBinding.recyclerView.setAdapter(new LanguageListAdapter(getActivity(), list,false));
    }

}

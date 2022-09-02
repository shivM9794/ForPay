package in.forpay.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.forpay.R;
import in.forpay.databinding.FragmentMakeTeamsBinding;
import in.forpay.util.ProgressHelper;


public class MakeTeams extends BottomSheetDialogFragment {

    private FragmentMakeTeamsBinding binding;
    ProgressHelper progressHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_make_teams,container,false);
        progressHelper = new ProgressHelper(getActivity());
        return binding.getRoot();
    }
}
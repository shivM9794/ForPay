package in.forpay.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.HistoryMenu;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class HistoryMenuAdapter extends RecyclerView.Adapter<HistoryMenuAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<HistoryMenu> mList = new ArrayList<>();
    private ProgressHelper progressHelper;

    public HistoryMenuAdapter(Activity activity, ArrayList<HistoryMenu> list, ProgressHelper progressHelper) {
        this.mActivity = activity;
        this.mList = list;
        this.progressHelper=progressHelper;

    }

    @NonNull
    @Override
    public HistoryMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_history_menu_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.history_menu_title.setText(mList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView history_menu_title;

        ViewHolder(@NonNull View view) {
            super(view);
            history_menu_title=view.findViewById(R.id.history_menu_title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openActivity(mList.get(getBindingAdapterPosition()));
                }
            });
        }
    }

    private void openActivity(HistoryMenu historyMenu){
        Log.d("Clicked",historyMenu.getActivity());
        //progressHelper.show();
        Utility.openActivity(mActivity,historyMenu.getActivity(),null);
    }
}

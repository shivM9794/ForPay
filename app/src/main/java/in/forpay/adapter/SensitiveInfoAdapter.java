package in.forpay.adapter;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.response.GetFirstApiResponse;

public class SensitiveInfoAdapter extends RecyclerView.Adapter<SensitiveInfoAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<GetFirstApiResponse.Data> mList = new ArrayList<>();

    public SensitiveInfoAdapter(Activity mActivity, ArrayList<GetFirstApiResponse.Data> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public SensitiveInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sensitive_adapter, parent, false);
        return new SensitiveInfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensitiveInfoAdapter.ViewHolder holder, int position) {

        //Log.d("asdfgh","text "+mList.get(position).getDesc());
       holder.senstive.setText(Html.fromHtml(mList.get(position).getDesc()), TextView.BufferType.SPANNABLE);


    }

    @Override
    public int getItemCount() {

        Log.d("asdfgh","fhg size "+mList.size());
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView senstive;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            senstive = itemView.findViewById(R.id.senstive);

        }
    }
}

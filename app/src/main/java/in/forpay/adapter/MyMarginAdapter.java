package in.forpay.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.response.GetMyMarginResponse;

public class MyMarginAdapter extends RecyclerView.Adapter<MyMarginAdapter.ViewHolder> implements Filterable {

    private Activity mActivity;
    private ArrayList<GetMyMarginResponse.Data> mList = new ArrayList<>();
    private ArrayList<GetMyMarginResponse.Data> mListFilter = new ArrayList<>();

    public MyMarginAdapter(Activity activity, ArrayList<GetMyMarginResponse.Data> list) {
        this.mActivity = activity;
        this.mList = list;
        this.mListFilter = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_margin_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (position % 2 == 0) {
            viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(mActivity,
                    R.color.light_gray));
        } else {
            viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(mActivity,
                    R.color.white));
        }

        viewHolder.textViewService.setText(mList.get(position).getService());

        String type = mList.get(position).getMarginType();
        String margin = "";
        if (!TextUtils.isEmpty(type)) {
            if (type.equalsIgnoreCase("1")) {
                margin = mList.get(position).getMargin() + "%";
            } else {
                margin = mList.get(position).getMargin() + "txn";
            }
        } else {
            margin = mList.get(position).getMargin() + "%";
        }
        viewHolder.textViewMargin.setText(margin);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mList = mListFilter;
                } else {
                    ArrayList<GetMyMarginResponse.Data> filteredList = new ArrayList<>();
                    for (GetMyMarginResponse.Data row : mListFilter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getService().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    mList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mList = (ArrayList<GetMyMarginResponse.Data>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView textViewService;
        private TextView textViewMargin;

        ViewHolder(@NonNull View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            textViewService = view.findViewById(R.id.textViewService);
            textViewMargin = view.findViewById(R.id.textViewMargin);
        }
    }
}

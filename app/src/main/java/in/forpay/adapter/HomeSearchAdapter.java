package in.forpay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;

public class HomeSearchAdapter extends ArrayAdapter<HomeCategorySearchModel> {

    private Context context;
    private int resourceId;
    private List<HomeCategorySearchModel> items, tempItems, suggestions;

    public HomeSearchAdapter(@NonNull Context context, int resourceId, ArrayList<HomeCategorySearchModel> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            HomeCategorySearchModel model = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.title);
            name.setText(model.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    @Nullable
    @Override
    public HomeCategorySearchModel getItem(int position) {
        return items.get(position);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return shopListFilter;
    }

    public void setData(ArrayList<HomeCategorySearchModel> list) {
        items.clear();
        items.addAll(list);
    }

    private Filter shopListFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            HomeCategorySearchModel model = (HomeCategorySearchModel) resultValue;
            return model.getTitle();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (HomeCategorySearchModel model: tempItems) {
                    if (model.getTitle().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(model);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<HomeCategorySearchModel> tempValues = (ArrayList<HomeCategorySearchModel>) filterResults.values;
            if (filterResults.count > 0) {
                clear();
                for (HomeCategorySearchModel model : tempValues) {
                    add(model);
                }
                notifyDataSetChanged();
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}

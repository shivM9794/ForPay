package in.forpay.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private Activity mActivity;
    private ArrayList<String> mList = new ArrayList<>();
    private ArrayList<String> mListFilter = new ArrayList<>();
    private ArrayFilter mFilter;

    public AutoCompleteAdapter(Activity activity, int resource,
                               int textViewResourceId, ArrayList<String> list) {

        super(activity, resource, textViewResourceId, list);
        this.mActivity = activity;
        this.mList = list;
        this.mListFilter = new ArrayList<>(list);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mListFilter == null) {
                synchronized (lock) {
                    mListFilter = new ArrayList<String>(mList);
                }
            }

            if (prefix == null || prefix.length() == 0) {

                    synchronized (lock) {
                        ArrayList<String> list = new ArrayList<String>(mListFilter);
                        results.values = list;
                        results.count = list.size();

                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<String> values = mListFilter;
                int count = values.size();

                ArrayList<String> newValues = new ArrayList<String>(count);

                for (int i = 0; i < count; i++) {
                    String item = values.get(i);
                    if (item.toLowerCase().contains(prefixString)) {
                        newValues.add(item);
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values != null) {
                mList = (ArrayList<String>) results.values;
            } else {
                mList = new ArrayList<String>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}

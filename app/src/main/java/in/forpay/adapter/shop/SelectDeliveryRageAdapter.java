package in.forpay.adapter.shop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.forpay.R;

public class SelectDeliveryRageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public SelectDeliveryRageAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view =View.inflate(context, R.layout.select_delivery_range_main, null);
        TextView textView = view.findViewById(R.id.main);
        return textView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view =View.inflate(context, R.layout.select_delivery_range_dropdown, null);
        TextView textView = view.findViewById(R.id.dropdown);
        textView.setText(list.get(position));

        return view;
    }
}

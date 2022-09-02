package in.forpay.adapter.shop;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.model.shop.ShopCategorySearchModel;

public class ShopSearchAdapter extends ArrayAdapter<ShopCategorySearchModel> {

    private Context context;
    private int resourceId;
    private List<ShopCategorySearchModel> items, tempItems, suggestions;

    public ShopSearchAdapter(@NonNull Context context, int resourceId, ArrayList<ShopCategorySearchModel> items) {
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
            ShopCategorySearchModel model = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.title);
            ImageView imageView = view.findViewById(R.id.image);
            Glide.with(context).load(model.getImage()).into(imageView);
            name.setText(model.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    @Nullable
    @Override
    public ShopCategorySearchModel getItem(int position) {
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
    private Filter shopListFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ShopCategorySearchModel model = (ShopCategorySearchModel) resultValue;
            return model.getTitle();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (ShopCategorySearchModel model: tempItems) {
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
            try {
                ArrayList<ShopCategorySearchModel> tempValues = (ArrayList<ShopCategorySearchModel>) filterResults.values;
                if (filterResults.count > 0) {
                    clear();
                    for (ShopCategorySearchModel model : tempValues) {
                        add(model);
                    }
                    notifyDataSetChanged();
                } else {
                    clear();
                    notifyDataSetChanged();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}

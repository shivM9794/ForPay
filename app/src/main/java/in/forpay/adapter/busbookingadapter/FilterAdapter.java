package in.forpay.adapter.busbookingadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

import in.forpay.R;
import in.forpay.databinding.AdapterFilterBinding;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

    private Context context;
    private LinkedHashMap<String, Boolean> linkedHashMap;
    private String[] mKeys;
    private int lastAdapterPos = -1;

    public FilterAdapter(Context context, LinkedHashMap<String, Boolean> linkedHashMap) {
        this.context = context;
        this.linkedHashMap = linkedHashMap;
        mKeys = linkedHashMap.keySet().toArray(new String[linkedHashMap.size()]);

    }

    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_filter, parent, false);
        return new FilterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterHolder holder, int position) {
        holder.bindData(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return linkedHashMap.size();
    }

    public class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterFilterBinding binding;

        public FilterHolder(@NonNull ViewDataBinding itemView) {
            super(itemView.getRoot());

            binding = (AdapterFilterBinding) itemView;

            binding.linFilterItem.setOnClickListener(this);

           /* binding.checkboxItemFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    binding.linFilterItem.performClick();


                }
            });
*/
        }

        public void bindData(int adapterPos) {


            if (linkedHashMap.get(mKeys[adapterPos])) {
                binding.checkboxItemFilter.setChecked(true);
            } else {
                binding.checkboxItemFilter.setChecked(false);
            }

            binding.txtItemFilter.setText(mKeys[adapterPos]);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.linFilterItem:

                    if (linkedHashMap.get(mKeys[getAdapterPosition()])) {
                        linkedHashMap.put(mKeys[getAdapterPosition()], false);
                    } else {
                        if (lastAdapterPos != -1)
                            linkedHashMap.put(mKeys[lastAdapterPos], false);
                        linkedHashMap.put(mKeys[getAdapterPosition()], true);
                    }
                    lastAdapterPos = getAdapterPosition();
                    notifyDataSetChanged();
                    break;
            }
        }
    }
}

package in.forpay.adapter.shop;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.forpay.databinding.AdapterGenerateNewOrderBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.GenerateNewOrderModel;
import in.forpay.util.Constant;

public class GenerateNewOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<GenerateNewOrderModel> arrayList;
    private int quantity;

    public GenerateNewOrderAdapter(Context context, List<GenerateNewOrderModel> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AdapterGenerateNewOrderBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;

            viewHolder.binding.itemName.setText(arrayList.get(position).getItemName());
            viewHolder.binding.quantity.setText(arrayList.get(position).getQuantity());
            viewHolder.binding.price.setText(arrayList.get(position).getPrice());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterGenerateNewOrderBinding binding;

        MyViewHolder(@NonNull AdapterGenerateNewOrderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.subtract.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.GENERATE_NEW_ORDER_SUBTRACT));
            binding.add.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.GENERATE_NEW_ORDER_ADD));
            binding.remove.setOnClickListener(v->listener.onItemClick(getAdapterPosition(), Constant.GENERATE_NEW_ORDER_REMOVE));

            binding.itemName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    arrayList.get(getAdapterPosition()).setItemName(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            binding.subtract.setOnClickListener(v->{
                quantity=Integer.parseInt(binding.quantity.getText().toString());
                if (quantity!=0) {
                    binding.quantity.setText(String.valueOf(--quantity));
                    arrayList.get(getAdapterPosition()).setQuantity(String.valueOf(quantity));
                    notifyItemChanged(getAdapterPosition());
                }
            });

            binding.add.setOnClickListener(v->{
                quantity=Integer.parseInt(binding.quantity.getText().toString());
                binding.quantity.setText(String.valueOf(++quantity));
                arrayList.get(getAdapterPosition()).setQuantity(String.valueOf(quantity));
                notifyItemChanged(getAdapterPosition());
            });

            binding.price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    arrayList.get(getAdapterPosition()).setPrice(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            binding.remove.setOnClickListener(v->{
                arrayList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }
}

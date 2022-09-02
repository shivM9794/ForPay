package in.forpay.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.forpay.R;
import in.forpay.databinding.AdapterShopChatBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ChatHistoryModel;
import in.forpay.util.TimeAgo;

public class ShopChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ItemClickListener listener;
    private List<ChatHistoryModel.DataBean> arrayList;

    public ShopChatAdapter(Context context, List<ChatHistoryModel.DataBean> arrayList, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_shop_chat, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            ChatHistoryModel.DataBean model= arrayList.get(position);
            viewHolder.binding.name.setText(model.getName());
            if (model.getMsgType().equals("0")) {
                viewHolder.binding.message.setText(model.getMsg());
                viewHolder.binding.icon.setVisibility(View.GONE);
            }else if (model.getMsgType().equals("1")) {
                viewHolder.binding.message.setText("Image");
                viewHolder.binding.icon.setVisibility(View.VISIBLE);
                viewHolder.binding.icon.setImageResource(R.drawable.chat_placeholder);
            }else {
                viewHolder.binding.message.setText("Audio");
                viewHolder.binding.icon.setVisibility(View.VISIBLE);
                viewHolder.binding.icon.setImageResource(R.drawable.audio);
            }
            viewHolder.binding.time.setText(TimeAgo.getTimeAgo(model.getTime()));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterShopChatBinding binding;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterShopChatBinding.bind(itemView);

            binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition(), ""));
        }
    }
}

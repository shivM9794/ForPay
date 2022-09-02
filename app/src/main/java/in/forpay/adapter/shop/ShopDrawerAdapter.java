package in.forpay.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.NavHeaderMainShopBinding;
import in.forpay.databinding.NavRowDrawerShopBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.DrawerModel;
import in.forpay.util.Constant;

public class ShopDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<DrawerModel> drawerArrayList;
    Context context;
    ItemClickListener listener;

    public ShopDrawerAdapter(Context context, ArrayList<DrawerModel> drawerDataList, ItemClickListener listener) {
        this.context=context;
        this.drawerArrayList = drawerDataList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new ViewHeader(NavHeaderMainShopBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
        } else {
            View viewHolder= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.nav_row_drawer_shop, null, false);
            viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(viewHolder);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof ViewHeader) {
            ((ViewHeader) viewHolder).binding.userName.setText("");
            ((ViewHeader) viewHolder).binding.userEmail.setText("");

        } else {
            ((ViewHolder) viewHolder).binding.image.setImageResource(drawerArrayList.get(i).getImage());
            ((ViewHolder) viewHolder).binding.title.setText(drawerArrayList.get(i).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return drawerArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        /*if (position==0){
            return position;
        }else {
            return position;
        }*/
        return 1;
    }

    public class ViewHeader extends RecyclerView.ViewHolder {
        NavHeaderMainShopBinding binding;

        public ViewHeader(@NonNull NavHeaderMainShopBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NavRowDrawerShopBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);

            binding.getRoot().setOnClickListener(v -> {

                switch (getAdapterPosition()){
                    case 0:listener.onItemClick(getAdapterPosition(), Constant.ADD_NEW_SHOP);
                        break;
                    case 1:listener.onItemClick(getAdapterPosition(), Constant.EDIT_YOUR_SHOP);
                        break;
                    case 2:listener.onItemClick(getAdapterPosition(), Constant.SHOP_LOCAL);
                        break;
                    case 3:listener.onItemClick(getAdapterPosition(), Constant.MY_AVAILABLE_PRODUCT);
                        break;
                    case 4:listener.onItemClick(getAdapterPosition(), Constant.MY_PURCHASE_ORDERS);
                        break;
                    case 5:listener.onItemClick(getAdapterPosition(), Constant.MY_SOLD_ORDERS);
                        break;
                    case 6:listener.onItemClick(getAdapterPosition(), Constant.GENERATE_NEW_ORDER);
                        break;
                    case 7:listener.onItemClick(getAdapterPosition(), Constant.MY_RATING);
                        break;
                    case 8:listener.onItemClick(getAdapterPosition(), Constant.MY_CHAT);
                        break;
                    case 9:listener.onItemClick(getAdapterPosition(), Constant.START_DELIVERY);
                        break;
                }
            });
        }
    }
}

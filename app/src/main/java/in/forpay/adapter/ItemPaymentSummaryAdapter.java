package in.forpay.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.GetPayoutBankListModel;
import in.forpay.model.response.GetPaymentSummaryModel;
import in.forpay.util.ProgressHelper;

public class ItemPaymentSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Activity activity;

    private ArrayList<GetPaymentSummaryModel> list;
    private String type="";

    public ItemPaymentSummaryAdapter(Activity activity, ArrayList<GetPaymentSummaryModel> list,String type) {
        this.activity = activity;
        this.list = list;
        this.type=type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = null;

        if(type.equals("orderDetails")){
            adView = LayoutInflater.from(activity).inflate(R.layout.item_payment_summary, parent, false);

        }
        else{
            adView = LayoutInflater.from(activity).inflate(R.layout.item_payment_summary, parent, false);

        }
        return new ItemPaymentSummaryHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof ItemPaymentSummaryHolder) {
            GetPaymentSummaryModel model = (GetPaymentSummaryModel) list.get(position);
            ItemPaymentSummaryHolder itemPaymentSummaryHolder = (ItemPaymentSummaryHolder) holder;


            itemPaymentSummaryHolder.itemTitle.setText(Html.fromHtml(model.getTitle()), TextView.BufferType.SPANNABLE);
            //itemPaymentSummaryHolder.itemTitle.setText(model.getTitle());
            //itemPaymentSummaryHolder.itemSubTtitle.setText(model.getAmount());
            itemPaymentSummaryHolder.itemSubTtitle.setText(Html.fromHtml(model.getAmount()), TextView.BufferType.SPANNABLE);


            itemPaymentSummaryHolder.itemSubTtitle.setOnClickListener(v -> {


                ClipboardManager cManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text",model.getAmount());
                cManager.setPrimaryClip(cData);

                Toast.makeText(activity, "Copied :)", Toast.LENGTH_SHORT).show();
            });



        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemPaymentSummaryHolder extends RecyclerView.ViewHolder {

        private TextView itemTitle,itemSubTtitle;

        public ItemPaymentSummaryHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle=itemView.findViewById(R.id.itemTitle);
            itemSubTtitle=itemView.findViewById(R.id.itemSubTtitle);
        }
    }

    }

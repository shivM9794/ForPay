package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.response.GetSocialListResponse;
import in.forpay.util.Utility;


public class SocialMediaAdapter extends RecyclerView.Adapter<SocialMediaAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<GetSocialListResponse.DataList> mList = new ArrayList<>();


    public SocialMediaAdapter(Activity activity, ArrayList<GetSocialListResponse.DataList> mList) {
        this.activity = activity;
        this.mList = mList;

//        Log.d("SMAdapter","sizeSMA"+mList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_social, parent, false);
//        Log.d("SMAdapter","sizeSMA 1"+mList.size());
        return new SocialMediaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.socialName.setText(mList.get(position).getTitle());
        holder.socialDesc.setText(mList.get(position).getDesc());
        Utility.imageLoader(activity, mList.get(position).getIconUrl(), holder.social_icon);

        holder.cardViewSocial.setOnClickListener(v -> {
            try{
                Uri uri = Uri.parse(mList.get(position).getUrl());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(it);
            }
            catch (Exception e){
                Utility.showToastLatest(activity,e.toString(),"ERROR");
            }

        });

    }

    @Override
    public int getItemCount() {
//        Log.d("dfedfdfdfe","afafef"+mList.size());
        return mList.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewSocial;

        private ImageView social_icon;
        private TextView socialName, socialDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            Log.d("SMAdapter","sizeSMA 2"+mList.size());

            social_icon = itemView.findViewById(R.id.social_icon);
            socialName = itemView.findViewById(R.id.socialName);
            socialDesc = itemView.findViewById(R.id.socialDesc);
            cardViewSocial = itemView.findViewById(R.id.cardViewSocial);

        }
    }
}

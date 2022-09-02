package in.forpay.activity.premiumplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.response.GetProfileResponse;

public class PremiumPlanAdapter extends PagerAdapter {
    Context context;
    ArrayList<GetProfileResponse.PremiumBanner> premiumBanners;
    String premiumHeader;
    LayoutInflater layoutInflater;

    public PremiumPlanAdapter(Context context, String premiumHeader, ArrayList<GetProfileResponse.PremiumBanner> premiumBanners) {
        this.context = context;
        this.premiumBanners = premiumBanners;
        this.premiumHeader=premiumHeader;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return premiumBanners.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.adapter_premium_plan, container, false);
        TextView title = itemView.findViewById(R.id.footer);

        title.setText(premiumBanners.get(position).getText());

        ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
        Glide.with(context)
                .load(premiumBanners.get(position).getUrl())
                .into(imageView);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

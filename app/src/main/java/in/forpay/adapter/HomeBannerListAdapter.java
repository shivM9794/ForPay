package in.forpay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.rd.PageIndicatorView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.util.Utility;

public class HomeBannerListAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<MainRechargeModel.HomeBannerListBean> arrayList;
    private ItemClickListener listener;
    private LayoutInflater layoutInflater;
    PageIndicatorView dotsIndicator;

    public HomeBannerListAdapter(Context context, int images[]) {

    }

    public HomeBannerListAdapter(Context context, ArrayList<MainRechargeModel.HomeBannerListBean> arrayList, PageIndicatorView dotsIndicator, ItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.dotsIndicator = dotsIndicator;
        this.listener=listener;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.layout_home_banner, container, false);

        ImageView imageView = itemView.findViewById(R.id.image);
        Utility.imageLoader((Activity) context,arrayList.get(position).getUrl(),imageView);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
                listener.onItemClick(position,"");
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

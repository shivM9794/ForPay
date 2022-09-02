package in.forpay.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.io.File;
import java.util.List;

import in.forpay.R;

public class MyCustomPagerAdapter extends PagerAdapter {

    Context context;
    int images[];
    List<String> imagesf;

    LayoutInflater layoutInflater;

    public MyCustomPagerAdapter(Context context,  List<String> images2) {
        this.context = context;

        this.imagesf=images2;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imagesf.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        //Log.d("f zise ",""+imagesf.size()+" loc "+imagesf.get(position));
        View itemView = layoutInflater.inflate(R.layout.view_pager_item, container, false);
        //String downloadPath= Environment.getExternalStorageDirectory().getPath()+"/forpay/banner1.png";

        String downloadPath= imagesf.get(position);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        File imgFile = new File(downloadPath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());



            imageView.setImageBitmap(myBitmap);

        }
        else {
            //imageView.setImageResource(images[position]);
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}

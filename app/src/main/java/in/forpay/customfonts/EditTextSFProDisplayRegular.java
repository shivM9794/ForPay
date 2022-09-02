package in.forpay.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

public class EditTextSFProDisplayRegular extends AppCompatEditText {
    public EditTextSFProDisplayRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextSFProDisplayRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextSFProDisplayRegular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SF-Pro-Display-Regular.otf");
            setTypeface(tf);
        }
    }

}

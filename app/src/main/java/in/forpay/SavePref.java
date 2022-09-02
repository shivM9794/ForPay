package in.forpay;

import android.content.Context;
import android.content.SharedPreferences;


public class SavePref {

    private static Context context;

    public SavePref(Context context) {
        this.context = context;
    }

    public final static String PREFS_NAME = "appname_prefs";

    public static void setOperatorInt(int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("OPERATOR", value);
        editor.apply();
    }

    public static int getOperatorInt() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt("OPERATOR", -1);
    }


}
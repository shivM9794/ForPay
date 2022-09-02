package in.forpay.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceConnector {
    public static final String FIRST_RUN = "firstRun";
    public static final String CUSTOMER_NAME = "customerName";
    public static final String CUSTOMER_MOBILE = "customerMobile";
    public static final String BUS_SOURCE = "BUS_SOURCE";
    public static final String BUS_DESTINATION = "BUS_DESTINATION";
    public static final String CUSTOMER_EMAIL = "customerEmail";
    public static final String CUSTOMER_PAN = "pan";
    public static final String CUSTOMER_PINCODE = "pincode";
    public static final String CUSTOMER_REFFERURL = "referralUrl";
    public static final String USER_EMAIL="user_email";
    public static final String USER_LOCAL_NAME="user_local_name";
    public static final String FIRST_API_RESPONSE= "first_api_response";
    public static final String CUSTOMER_REFFERURL_COUNT="referCount";
    public static final String APP_VIEW_COUNT = "appViewCount";
    public static final String APP_RATINGS = "appRatings";
    public static final String HELP_LINE="helpLine";
    public static final String GATEWAY_NAME = "gatewayName";
    public static final String GATEWAY_MID = "gatewayMid";
    public static final String GATEWAY_WEBSITE= "gatewayWebsite";
    public static final String GATEWAY_INDTYPE = "gatewayIndType";
    public static final String SERVICE_DISABLE_MONEYTRANSFER = "isDiabledMoneyTransfer";
    public static final String SERVICE_DISABLE_METRORECHARGE = "isDiabledMetroRecharge";
    public static final String SERVICE_DISABLE_GIFTVOUCHER = "isDiabledGiftVoucher";
    public static final String CUSTOMER_HOMELANDMARK = "customerHomeLandmark";
    public static final String PSA_AMOUNT="psaAmount";
    public static final String DEFAULT_LANGUAGE = "Language";
    public static final String DEVICE_STORAGE = "DeviceStorage";
    public static final String NOTIFICATION_DATA_COUNT = "notification_data_count";
    public static final String GATEWAY_LIMIT = "gatewayLimit";
    public static final String GATEWAY_ORDERID="orderId";
    public static final String GCM_TOKEN = "gcmToken";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String USER_REFERID = "user_refer";
    public static final String USER_IS_PREMIUM = "isPremium";
    public static final String USER_ADDITINAL_DATA="USER_ADDITONAL_DATA";
    private static final String PREF_NAME = "pay_it_now";
    private static final int MODE = Context.MODE_PRIVATE;
    public static final String TRAVEL_DURATION = "TRAVEL_DURATION";
    public static final String BUS_NAME = "BUS_NAME";
    public static final String BUS_TYPE = "BUS_TYPE";
    public static final String BUS_AMOUNT = "BUS_AMOUNT";
    public static final String BUS_CONTACT = "BUS_CONTACT";
    public static final String BUS_SEAT_AMOUNT = "BUS_SEAT_AMOUNT";
    public static final String BUS_SEAT_COMMISSION = "BUS_SEAT_COMMISSION";
    public static final String DEVICE_HEIGHT = "device_height";
    public static final String Passenger_Details = "Passenger_Details";
    public static final String DEVICE_WIDTH = "device_width";
    public static final String USER_LOGIN = "user_login";
    public static final String USER_IS_SHOP="isShop";
    public static final String USER_MOBILE = "user_mobile";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String USER_ID = "user_id";
    public static final String IMEI = "imei";
    public static final String ACTIVE_KEY = "active_key";
    public static final String USER_BALANCE = "user_balance";
    public static final String USER_NAME="user_name";
    public static final String USER_COMMISSION_BALANCE = "user_commission_balance";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_OS = "user_os";
    public static final String USER_ENCKEY="user_encKey";
    public static final String USER_SID="sid";
    public static final String USER_LOGIN_PERMISSION="userLoginPermission";
    public static final String DEVICE_UNIQUE_KEY = "deviceUniqueKey";
    public static final String SERVICE_LIST = "service_list";
    public static final String ORDER_ID_LIST = "order_id_list";
    public static final String CIRCLE_LIST = "circle_list";
    public static final String CONTACT_LIST = "contact_list";
    public static final String USER_TYPE = "user_type";

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);

    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    private static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void remove(Context context, String key) {
        getEditor(context).remove(key);

    }

    public static void clear(Context context) {
        getEditor(context).clear().commit();
        getEditor(context).clear().commit();

    }
}

package in.forpay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constant {

    public static final String BASE_URL = "https://api.forpay.in/";
    public static final String BASE_URL_CHAT = "https://api.forpay.in/";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String PENDING = "PENDING";
    public static final String DISPUTED = "DISPUTED";
    public static final String CODE_200 = "200";
    public static final String CODE_201 = "201";
    public static final String CODE_154 = "154";
    public static final String CODE_123 = "123";
    public static final String CODE_129 = "129";
    public static final String CODE_178 = "178";
    public static final String CODE_402 = "402";
    public static final String CODE_401 = "401";

    public static final String ACTION_OTP_ACTIVITY = "otpActivity";
    public static final String ACTION_PASSWORD_ACTIVITY = "passwordActivity";
    public static final String METHOD_CASHBACK_COUPON_OFFER = "cashbackOffers.php";
    public static final String METHOD_EWALLET_SEND_OTP = "ewalletTopup/sendOtp.php";
    public static final String METHOD_SOCIAL = "social.php";
    public static final String METHOD_DIS_MYRETAILER = "distributor/myRetailer.php";
    public static final String METHOD_DIS_CAILM_MYRETAILER = "distributor/claimRetailer.php";
    public static final String METHOD_SMS_RECHARGE = "smsService.php";
    public static final String METHOD_FORPAY_PREMIER_LEAGUE = "fpl/todayMatch.php";
    public static final String METHOD_MATCH_DETAILS = "fpl/matchDetails.php";
    public static final String METHOD_CREATE_GROUP = "fpl/createGroup.php";
    public static final String METHOD_JOIN_GROUP = "fpl/joinGroup.php";
    public static final String METHOD_FPL_RESULT = "fpl/result.php";
    public static final String METHOD_FPL_PLAYERS = "fpl/players.php";
    public static final String METHOD_FPL_MYCONTESTS = "fpl/myContest.php";
    public static final String METHOD_FPL_MAKETEAM = "fpl/makeTeam.php";


    public static final String SERVICE_ALL = "all";
    public static final String SERVICE_GAS = "gas";
    public static final String SERVICE_MOBILE = "mobile";
    public static final String SERVICE_DTH = "dth";
    public static final String SERVICE_POSTPAID = "postpaid";
    public static final String SERVICE_LANDLINE = "landline";
    public static final String SERVICE_ELECTRICITY = "electricity";
    public static final String SERVICE_INSURANCE = "insurance";
    public static final String SERVICE_WATER = "water";
    public static final String SERVICE_METRO = "metro";
    public static final String SERVICE_DIGITAL_FASHION = "digitalvoucherfashion";
    public static final String SERVICE_DIGITAL_ONLINE_SHOP = "digitalvoucheronlineshop";
    public static final String SERVICE_DIGITAL_ENTERTAINMENT = "digitalvoucherentertainment";
    public static final String SERVICE_DIGITAL_HOTEL = "digitalvoucherhotel";
    public static final String SERVICE_DIGITAL_TRAVEL = "digitalvouchertravel";
    public static final String SERVICE_DIGITAL_HEALTH = "digitalvoucherhealth";

    public static final String STATE_FROM = "from";
    public static final String TITLE_SHOW = "title_show";
    public static final String METHOD_FAV_CONTACT = "favContactv2.php";
    public static final String METHOD_UPDATE_FAV_CONTACT = "updateFavContactv2.php";
    public static final String FAV_MOBILE = "favMobile";
    public static final String FAV_INPUTVALUE1 = "favInputValue1";
    public static final String FAV_INPUTVALUE2 = "favInputValue2";
    public static final String METHOD_REFER_DETAILS = "referDetails.php";


    public static boolean PROFILE_ADDED = false;
    public static final int REQUEST_CODE_RECHARGE = 1001;

    // Fragments name

    public static final String FRAGMENT_HOME = "HomeFragment";
    public static final String FRAGMENT_HISTORY = "TotalHistoryFragment";


    public static String OFFER_DATA_PLAN1 = "";
    public static String OFFER_DATA_PLAN2 = "";
    // Fragments name

    // METHODS
    public static final String METHOD_CC_SEND_OTP = "cc/sendOtp.php";
    public static final String METHOD_PROFILE = "profilev2.php";
    public static final String METHOD_VALIDATE_RECHARGE = "validatev3.php";
    public static final String METHOD_SCRATCH_CARD = "scratchCard.php";
    public static final String METHOD_GET_KEY = "getKey.php";
    public static final String METHOD_LOGIN = "loginv2.php";
    public static final String METHOD_FIRSTAPI = "first.php";
    public static final String METHOD_LOGIN_VALIDATE = "loginValidate.php";
    public static final String METHOD_NOTIFICATION = "notificationv2.php";
    public static final String METHOD_UPDATE_NOTIFICATION = "updateNotification.php";

    public static final String METHOD_REFRESH_BALANCE = "balancev2.php";
    public static final String METHOD_CONTACT_US = "contactv2.php";
    public static final String METHOD_MY_MARGIN = "index.php";

    public static final String METHOD_RECHARGE_NOW = "rechargev3.php";
    public static final String METHOD_VALIDATE_COUPON = "validateCoupon.php";
    public static final String METHOD_GET_OFFER = "data_planv2.php";
    public static final String METHOD_REFUND = "dispute.php";
    public static final String METHOD_RECHARGE_HISTORY = "historyv2.php";
    public static final String METHOD_CARD_BALANCE = "cardBalance.php";
    public static final String METHOD_CASHBACK_TO_COUPON = "cashbacktocoupon.php";


    public static final String METHOD_QR_PAYMENT_GENERATOR = "qrPayment/payment.php";

    public static final String METHOD_GET_BANK_DETAILS = "bankDetailsv2.php";
    public static final String METHOD_CHANGE_PASSWORD = "changePassv2.php";
    public static final String METHOD_CHANGE_PIN = "changePinv2.php";
    public static final String METHOD_UPDATE_STATUS = "api_statusv2.php";
    public static final String METHOD_REQUEST_FUND = "fundReqv2.php";
    public static final String METHOD_REQUEST_FUND_HISTORY = "fundReqHisv2.php";
    public static final String METHOD_BALANCE_HISTORY = "bal_hisv2.php";
    public static final String METHOD_BALANCE_TRANSFER = "balTransv2.php";
    public static final String METHOD_STOCK_HISTORY = "stock_hisv2.php";
    public static final String METHOD_STOCK_UPDATE = "updateStockStatv2.php";
    public static final String METHOD_UPDATE_LANGUAGE = "updageLanguagev2.php";
    public static final String METHOD_CUSTOMER_DETAIL = "mt/cusDetailsv2.php";
    public static final String METHOD_TSHIRT = "tshirt/tshirt.php";
    public static final String METHOD_CUSTOMER_REGISTER = "mt/customerRegistrationv2.php";
    public static final String METHOD_ADD_BENEFICIARY_B_OTP = "mt/addBeneficiaryv2.php";
    public static final String METHOD_ADD_BENEFICIARY_A_OTP = "mt/beneficiaryVerifiyv2.php";
    public static final String METHOD_VERIFY_IFSC_CODE = "mt/validateBeneficiaryv3.php";
    public static final String METHOD_SEND_FUND = "mt/sendMoneyv2.php";
    public static final String METHOD_DELETE_BENEFICIARY = "mt/deleteBeneficiaryv2.php";
    public static final String METHOD_DELETE_BENEFICIARY_VERIFY = "mt/deleteBeneficiaryVerifyv2.php";
    public static final String METHOD_ADD_FAV_CONTACT_RECHARGE = "addFavContact.php";

    public static final String METHOD_HOME_SEARCH = "homeSearchv2.php";
    public static final String INPUT_DATA = "INPUT_DATA";
    public static final String ONCLICKACTIVITY = "ONCLICKACTIVITY";
    public static final String IS_FROM_HOME_ACTIVITY = "IS_FROM_HOME_ACTIVITY";

    public static final String METHOD_PSA_AGENT_REGISTER = "pan/agentRegisterv2.php";
    public static final String METHOD_PSA_AGENT_STATUS = "pan/agentStatusv2.php";
    public static final String METHOD_PSA_AGENT_PASSWORD_RESET = "pan/passwordResetv2.php";

    public static final String METHOD_DAYBOOK = "dayBookv2.php";


    public static final String BUS_LIST = "bus/busListv2.php";
    public static final String BUS_STOPS = "bus/busStopsv2.php";
    public static final String BUS_SEAT_LAYOUT = "bus/seatLayoutv2.php";

    public static final String BUS_SEAT_BLOCK = "bus/blockSeatv2.php";

    public static final String BUS_BOOK = "bus/bookSeatv2.php";
    public static final String BUS_BOOKED_DETAIL = "bus/ticketDetailsv2.php";
    public static final String CANCEL_TICKET = "bus/cancelTicketv2.php";
    public static final String BUS_TICKET_HISTORY = "bus/ticketHistoryv2.php";
    public static final String BUS_ROUTE = "bus/busRoutev2.php";


    public static final String METHOD_PING = "ping.php";
    public static final String METHOD_UPDATE_PROFILE = "updateProfilev2.php";
    public static final String METHOD_ADDFUNDGATEWAY = "addFundGatewayv2.php";
    public static final String METHOD_ADDFUNDSTATUS = "addFundStatusv2.php";
    public static final String METHOD_SERVICELIST = "serviceListv2.php";
    public static final String METHOD_LOGINHISTORY = "loginHistoryv2.php";


    public static final String METHOD_ADDFUND_GATEWAY_RESPONSE = "addFundResponsev2.php";
    public static final String METHOD_LOGOUT = "logoutv2.php";

    public static final String STORAGE_NAME = "oxyme";

    public static final String METHOD_CHAT_LIST = "chat/chatListv2.php";
    public static final String METHOD_CHAT_POSTMESSAGE = "chat/postChatv2.php";

    public static final String METHOD_AEPS_DETAILS = "aeps/aepsDatav2.php";
    public static final String METHOD_AEPS_TRANSACTION = "aeps/aepsTransactionv2.php";
    public static final String METHOD_PAYOUT = "payoutv2.php";
    public static final String METHOD_PAYOUT_BANK_DETAILS = "payout/payoutBankDetails.php";
    public static final String METHOD_PAYOUT_BANK_ADD = "payout/addPayoutBank.php";

    public static final String METHOD_PAYOUT_BANK_VERIFY = "payout/bankVerify.php";
    public static final String METHOD_PAYOUT_BANK_DELETE = "payout/deletePayoutBank.php";


    public static final String METHOD_BBPS_STATUS = "bbps/bbpsStatusv2.php";
    public static final String METHOD_COMPLAIN_STATUS = "bbps/complainStatusv2.php";
    public static final String METHOD_BBPS_SEARCH_TRANSACTION = "bbps/searchTransactionv2.php";
    public static final String METHOD_RAISE_COMPLAINT = "bbps/raiseComplaintv2.php";

    //*************Shop***************//

    public static final String METHOD_SHOP_CATEGORY = "shop/shopCategory.php";
    public static final String METHOD_ADD_SHOP = "shop/addShop.php";
    public static final String METHOD_SHOP_LIST = "shop/shopList.php";
    public static final String METHOD_SHOP_ORDER = "shop/shopOrder.php";
    public static final String METHOD_GET_SHOP_RATING = "shop/getShopRating.php";
    public static final String METHOD_SEARCH_SHOP = "shop/searchShop.php";
    public static final String METHOD_SHOP_SEARCH = "shop/shopSearch.php";
    public static final String METHOD_MY_ORDER_HISTORY = "shop/myOrderHistory.php";
    public static final String METHOD_GENERATE_ORDER = "shop/generateOrder.php";
    public static final String METHOD_GET_ORDER_RATING = "shop/getOrderRating.php";
    public static final String METHOD_RATE_ORDER = "shop/rateOrder.php";
    public static final String METHOD_SHOP_ORDER_DETAILS = "shop/shopOrderDetails.php";
    public static final String METHOD_TRACK_ORDER = "shop/trackOrder.php";
    public static final String METHOD_SET_DELIVERY_LOCATION = "shop/setDeliveryLocation.php";
    public static final String METHOD_PAY_ORDER = "shop/payOrder.php";
    public static final String METHOD_MY_SOLD_ORDER_HISTORY = "shop/mySoldOrderHistory.php";
    public static final String METHOD_GET_USER_RATING = "shop/getUserRating.php";
    public static final String METHOD_RATE_USER = "shop/rateUser.php";
    public static final String METHOD_GET_SHOP_CHAT_HISTORY = "shop/getShopChatHistory.php";
    public static final String METHOD_POST_SHOP_CHAT = "shop/postShopChat.php";
    public static final String METHOD_GET_CHAT_HISTORY = "shop/getChatHistory.php";
    public static final String METHOD_ORDER_STATUS = "shop/orderStatus.php";
    public static final String METHOD_UPDATE_ORDER_STATUS = "shop/updateOrderStatus.php";
    public static final String METHOD_GENERATE_TRACKING_ID = "shop/generateTrackingId.php";
    public static final String METHOD_START_DELIVERY = "shop/startDelivery.php";
    public static final String METHOD_GET_MY_RATING = "shop/getMyRating.php";
    public static final String METHOD_MY_AVAILABLE_PRODUCT = "shop/myAvailableProduct.php";
    public static final String METHOD_PRODUCTION_SUGGESTION = "shop/productSuggestion.php";
    public static final String METHOD_UPDATE_AVAILABLE_PRODUCT = "shop/updateAvailableProduct.php";
    public static final String METHOD_SHOP_ADD_ADDRESS = "shop/addAddress.php";
    public static final String METHOD_SHOP_ADDRESS_LIST = "shop/addressList.php";


    public static final String ADD_NEW_SHOP = "Add New Shop";
    public static final String EDIT_YOUR_SHOP = "Edit your Shop";
    public static final String SHOP_LOCAL = "Shop Local";
    public static final String MY_AVAILABLE_PRODUCT = "My Available Product";
    public static final String MY_PURCHASE_ORDERS = "My Purchase Orders";
    public static final String MY_SOLD_ORDERS = "My Sold Orders";
    public static final String GENERATE_NEW_ORDER = "Generate New Orders";
    public static final String MY_RATING = "My Rating";
    public static final String MY_CHAT = "My Chat";
    public static final String START_DELIVERY = "Start Delivery";

    public static final String SHOP_CATEGORY_SELECT = "SHOP_CATEGORY_SELECT";
    public static final String SHOP_CATEGORY_INFO = "SHOP_CATEGORY_INFO";
    public static final String SHOP_CATEGORY_ID = "SHOP_CATEGORY_ID";
    public static final String SHOP_SUB_CATEGORY_LIST = "SHOP_SUB_CATEGORY_LIST";
    public static final String SHOP_CATEGORY_LIST = "SHOP_CATEGORY_LIST";
    public static final String SHOP_ID = "SHOP_ID";
    public static final String TO_USER_ID = "TO_USER_ID";
    public static final String IS_FROM_SHOP_CHAT = "IS_FROM_SHOP_CHAT";
    public static final String SHOP_ORDER_MODEL = "SHOP_ORDER_MODEL";
    public static final String SHOP_LIST = "SHOP_LIST";
    public static final String SHOP_RATING = "SHOP_RATING";
    public static final String SHOP_DISTANCE = "SHOP_DISTANCE";

    public static final String VIEW_ORDER = "VIEW_ORDER";
    public static final String RATING = "RATING";
    public static final String ORDER_ID = "ORDER_ID";
    public static final String STATUS = "STATUS";
    public static final String LAST_ACTIVE = "LAST_ACTIVE";
    public static final String UNIQUE_KEY = "uniqueKey";

    public static final String SHOP_DIRECTORY_NAME = "ShopChat";
    public static final String SHOP_IMAGE_DIRECTORY_NAME = "ShopImage";
    public static final String SHOP_AUDIO_DIRECTORY_NAME = "Audio";
    public static final String IS_FROM_EDIT_YOUR_SHOP = "IS_FROM_EDIT_YOUR_SHOP";
    public static final String IS_FROM_SHOP = "IS_FROM_SHOP";
    public static final String ORDER_HISTORY_MODEL = "ORDER_HISTORY_MODEL";
    public static final String ITEMS_MODEL = "ITEMS_MODEL";
    public static final String ITEMS_LIST = "ITEMS_LIST";
    public static final String MODEL = "MODEL";
    public static final String POSITION = "POSITION";
    public static final String TRACKING_ID = "TRACKING_ID";
    public static final String IS_FROM_START_DELIVERY = "IS_FROM_START_DELIVERY";
    public static final String IS_FROM_ORDER_DETAIL = "IS_FROM_ORDER_DETAIL";
    public static final String IS_FROM_BUYER = "IS_FROM_BUYER";
    public static final String IS_FROM_SELLER = "IS_FROM_SELLER";


    public static final String SOURCE_LATITUDE = "SOURCE_LATITUDE";
    public static final String SOURCE_LONGITUDE = "SOURCE_LONGITUDE";
    public static final String DESTINATION_LATITUDE = "DESTINATION_LATITUDE";
    public static final String DESTINATION_LONGITUDE = "DESTINATION_LONGITUDE";

    public static final String IS_FROM_MY_PRODUCT_ACTIVITY = "IS_FROM_MY_PRODUCT_ACTIVITY";
    public static final String IMAGE = "IMAGE";
    public static final String IMAGES_LIST = "IMAGES_LIST";
    public static final String PRODUCT_SUGGESTION_ID = "PRODUCT_SUGGESTION_ID";

    //-*-*-*-*-*-*-Shop-*-*-*-*-*-*-*-//

    //*************New Recharge***************//
    public static final String METHOD_HOME_UPDATE = "homeUpdatesv3.php";
    public static final String METHOD_SERVICE_LIST = "serviceListv2.php";
    public static final String METHOD_EWALLET_LIST = "ewalletTopup/walletList.php";
    public static final String METHOD_FAV_CONTACT_OPERATOR = "favContactOperator.php";


    public static final String METHOD_GIFTVOUCHER_LIST = "giftVoucher/voucherList.php";
    //public static final String METHOD_DATA_PLAN = "data_plan.php";

    public static final String HOME_LIST = "HOME_LIST";
    public static final String HOME_UPDATE_API_RESPONSE = "HOME_UPDATE_API_RESPONSE";
    public static final String SERVICE_LIST_RESPONSE = "SERVICE_LIST_RESPONSE";
    public static final String IS_60_MINUTES_COMPLETE = "IS_60_MINUTES_COMPLETE";
    public static final String DATE_MILLISECOND = "DATE_MILLISECOND";
    public static final String IS_FROM_NEW_RECHARGE = "IS_FROM_NEW_RECHARGE";
    public static final String SERVICE_ARRAY_LIST = "SERVICE_ARRAY_LIST";
    public static final String PARAMS_ARRAY_LIST = "PARAMS_ARRAY_LIST";
    public static final String VALUE_ARRAY_LIST = "VALUE_ARRAY_LIST";
    public static final String POPUPDATA = "POPUPDATA";
    public static final String INSUFFICIENTDATA = "INSUFFICIENTDATA";
    public static final String LISTENER = "LISTENER";
    public static final String TYPE = "TYPE";
    public static final String HOME_UPDATE_API_TIME_MILLISECOND = "HOME_UPDATE_API_TIME_MILLISECOND";

    public static final String CONTACT_UPDATE_API_TIME_MILLISECOND = "CONTACT_UPDATE_API_TIME_MILLISECOND";
    public static final String IS_DAILY_SUMMARY_API_CALL = "IS_DAILY_SUMMARY_API_CALL";

    public static final String MAIN_RECHARGE_SERVICE_MODEL = "MAIN_RECHARGE_SERVICE_MODEL";

    //-*-*-*-*-*-*-New Recharge-*-*-*-*-*-*-*-//

    public static final String METHOD_KYC = "kyc/kycv2.php";
    public static final String METHOD_KEY_UPLOAD = "kyc/uploadKycv2.php";
    public static final String METHOD_KYC_UPDATE = "kyc/updateKycv2.php";
    public static final String METHOD_PINCODE = "pincodev2.php";


    public static final String METHOD_AADHAAR_KYC = "kyc/aadhaarKyc.php";


    public static final String SOURCE_DESTINATION = "SOURCE_DESTINATION";
    public static final String SOURCE = "SOURCE";
    public static final String DESTINATION = "DESTINATION";
    public static final String SOURCE_CITY = "SOURCE_CITY";
    public static final String DATE_OF_JOURNEY = "DATE_OF_JOURNEY";
    public static final String BUS_AVAILABLE_ON_ROUTE = "BUS_AVAILABLE_ON_ROUTE";
    public static final String NO_OF_SEATS = "NO_OF_SEATS";
    public static final String TRAVEL = "TRAVEL";
    public static final String BOARDING = "BOARDING";
    public static final String DROPPING = "DROPPING";
    public static final String FILTER_TAG_LIST = "FILTER_TAG_LIST";
    public static final String BUS_STOP_LIST = "BUS_STOP_LIST";
    public static final String IS_SPLASH_OPEN = "IS_SPLASH_OPEN";
    public static final String CLEAR_FILTER_ITEM = "CLEAR_FILTER_ITEM";
    public static final String TODAY_DATE_MILLI = "TODAY_DATE_MILLI";
    public static final String CALL_BUS_STOP_API = "CALL_BUS_STOP_API";
    public static final String IS_AC_FILTER_SELECTED = "IS_AC_FILTER_SELECTED";
    public static final String IS_NON_AC_FILTER_SELECTED = "IS_NON_AC_FILTER_SELECTED";
    public static final String IS_SLEEPER_FILTER_SELECTED = "IS_SLEEPER_FILTER_SELECTED";
    public static final String IS_SEATER_FILTER_SELECTED = "IS_SEATER_FILTER_SELECTED";
    public static final String BUS_ID = "BUS_ID";
    public static final String BOOKED_ID = "BOOKED_ID";
    public static final String BOOKED_STATUS = "BOOKED_STATUS";
    public static final String TICKET_ID = "TICKET_ID";
    public static final String BLOCKED_ID = "BLOCKED_ID";
    public static final String PASSENGER_INFO = "PASSENGER_INFO";
    public static final String BOARDING_POINT = "BOARDING_POINT";
    public static final String DROPPING_POINT = "DROPPING_POINT";
    public static final String KEY_CANCELLATION_POLICY = "CANCELLATION_POLICY";
    public static final String BOOKING_TIME = "BOOKING_TIME";
    public static final String BOOKING_AMOUNT = "BOOKING_AMOUNT";
    public static final String DROPPING_TIME = "DROPPING_TIME";
    public static String CONTACT_NUMBER = "";
    public static String PassengerDetails = "PassengerDetails";

    public static final String GENERATE_NEW_ORDER_REMOVE = "GENERATE_NEW_ORDER_REMOVE";
    public static final String GENERATE_NEW_ORDER_SUBTRACT = "GENERATE_NEW_ORDER_SUBTRACT";
    public static final String GENERATE_NEW_ORDER_ADD = "GENERATE_NEW_ORDER_ADD";
    public static final String METHOD_MISS_CALL_RECHARGE = "missCallRecharge.php";
    public static final String METHOD_COUPON_TO_POINTS = "couponToPoints.php";

    public static final int ACTIVITY_RESULT_IMAGE_SHOP = 77;
    public static final int ACTIVITY_RESULT_ADD_LOCATION = 78;
    public static final int ACTIVITY_RESULT_LOCATION_SELECTOR = 79;
    public static final int ACTIVITY_RESULT_GPS = 80;
    public final static int permission_Recording_audio = 790;
    public final static int permission_write_data = 788;

    public static final String LOCATION_ADDRESS = "LOCATION_ADDRESS";
    public static final String LOCATION_LAT_LNG = "LOCATION_LAT_LNG";
    public static final String LOCATION_CITY = "LOCATION_CITY";
    public static final String LOCATION_SUB_LOCALITY = "LOCATION_SUB_LOCALITY";
    public static final String LOCATION_LATITUDE = "LOCATION_LATITUDE";
    public static final String LOCATION_LONGITUDE = "LOCATION_LONGITUDE";


    public static final String SHOP_IMAGE = "SHOP_IMAGE";
    public static final String SHOP_NAME = "SHOP_NAME";
    public static final String SHOP_CONTACT_NO = "SHOP_CONTACT_NO";
    public static final String SHOP_DELIVERY_RANGE = "SHOP_DELIVERY_RANGE";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String IS_FROM_PURCHASE = "IS_FROM_PURCHASE";
    public static final String DELIVERY_CHARGE = "DELIVERY_CHARGE";
    public static final String TOTAL_COST = "TOTAL_COST";


    public static String getDateFormat(String s, String dateFromApi) {
        //2020-02-03 17:30:00
        SimpleDateFormat dateFormatApi = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());

        SimpleDateFormat dateFormat = new SimpleDateFormat(s, Locale.getDefault());
        try {
            Date date = dateFormatApi.parse(dateFromApi);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getHour(String s, String dateFromApi) {

        String[] separated = dateFromApi.split(":");

        return separated[0];

        /*
        Log.d("HourBus","s "+s+" date "+dateFromApi);
        SimpleDateFormat dateFormatApi = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        SimpleDateFormat dateFormat = new SimpleDateFormat(s, Locale.getDefault());
        try {
            Date date = dateFormatApi.parse(dateFromApi);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";

         */
    }

    public static String getMins(String s, String dateFromApi) {

        String[] separated = dateFromApi.split(":");

        return separated[1];
        /*
        SimpleDateFormat dateFormatApi = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat(s, Locale.getDefault());
        try {
            Date date = dateFormatApi.parse(dateFromApi);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";

         */
    }


    public static long getTimeInMilli(String travelDuration, String dateFormatApi) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(dateFormatApi, Locale.getDefault());
        try {
            Date date = simpleDateFormat1.parse(travelDuration);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String getTimeDateFormat(String createdDate) {
        String displayValue = "";
        try {
            // Get date from string
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = dateFormatter.parse(createdDate);

            // Get time from date
            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            displayValue = timeFormatter.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return displayValue;
    }

    // METHODS
}

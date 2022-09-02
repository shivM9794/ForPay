package in.forpay.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.forpay.model.ContactList;
import in.forpay.model.OrderLocal;
import in.forpay.model.request.BalanceHistory;
import in.forpay.model.request.BusList;
import in.forpay.model.request.DayBook;
import in.forpay.model.request.LanguageList;
import in.forpay.model.request.NotificationModel;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetLoginResponse;
import in.forpay.model.response.GetLoginValidateResponse;
import in.forpay.model.response.GetServiceListResponse;
import in.forpay.model.supportchatModel.ChatListModel;
import in.forpay.model.supportchatModel.MessageSendModel;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "oxyme_db";




    //For Audit Bal

    public static final  String TABLE_AUDIT="audit";
    private static final String AUTO_ID = "id";
    private static final String AUDIT_OP_BAL="openingBal";
    private static final String AUDIT_CLOSING_BAL="closingBal";
    private static final String AUDIT_DATE="date";
    private static final String AUDIT_TYPE="type";
    private static final String AUDIT_ORDERID="orderId";
    private static final String AUDIT_STATUS="status";
    private static final String AUDIT_MOBILE="mobile";
    private static final String AUDIT_SERVICE="service";
    private static final String AUDIT_BANKACCOUNT="bankAccount";
    private static final String AUDIT_BANKNAME="bankName";
    private static final String AUDIT_AMOUNT="amount";

    private static final String CREATE_TABLE_AUDIT=
            "CREATE TABLE IF NOT EXISTS " + TABLE_AUDIT + "("
                    + AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + AUDIT_OP_BAL + " VARCHAR(15),"
                    + AUDIT_CLOSING_BAL + " VARCHAR(15),"
                    + AUDIT_DATE + " VARCHAR(30),"
                    + AUDIT_TYPE + " VARCHAR(20),"
                    + AUDIT_ORDERID + " VARCHAR(15),"
                    + AUDIT_STATUS + "  VARCHAR(15),"
                    + AUDIT_MOBILE + " VARCHAR(20),"
                    + AUDIT_SERVICE + " VARCHAR(20),"
                    + AUDIT_BANKACCOUNT + " VARCHAR(30),"
                    + AUDIT_AMOUNT + " VARCHAR(15),"
                    + AUDIT_BANKNAME + " VARCHAR(15)"

                    + ")";
    // For service type
    private static final String TABLE_SERVICE_TYPE = "service_type";


    private static final String SERVICE_ID = "serviceId";
    private static final String SERVICE_NAME = "serviceName";
    private static final String SERVICE_TYPE = "serviceType";
    private static final String SMS_CODE = "smsCode";

    private static final String CREATE_TABLE_SERVICE_TYPE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SERVICE_TYPE + "("
                    + AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + SERVICE_ID + " VARCHAR(10),"
                    + SERVICE_NAME + " VARCHAR(30),"
                    + SERVICE_TYPE + " VARCHAR(10),"
                    + SMS_CODE + " VARCHAR(10)"
                    + ")";


    private static final String TABLE_LANGUAGE = "languageList";

    private static final String LANGUAGE_CODE = "code";
    private static final String LANGUAGE_LANGUAGE = "language";

    private static final String CREATE_TABLE_LANGUAGELIST =
            "CREATE TABLE IF NOT EXISTS " + TABLE_LANGUAGE + "("
                    + LANGUAGE_CODE + " VARCHAR(10) PRIMARY KEY,"
                    + LANGUAGE_LANGUAGE + " VARCHAR(20)"

                    + ")";


    // For service type
    private static final String TABLE_DATA_PLANS = "dataplan_offerss";
    private static final String DATAID = "dataidd";
    private static final String TYPE = "type";
    private static final String AMOUNT = "amount";
    private static final String DETAIL = "detail";
    private static final String VALIDITY = "validity";
    private static final String TALKTIME = "talktime";
    private static final String EXTRA_OFFFER = "extraOffer";
    private static final String COMMISSION = "commission";
    private static final String DATA = "data";
    private static final String CREATE_TABLE_DATA_PLANS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_DATA_PLANS + "("

                    + TYPE + " VARCHAR(10),"
                    + AMOUNT + " VARCHAR(10) PRIMARY KEY,"
                    + DETAIL + " VARCHAR(300),"
                    + VALIDITY + " VARCHAR(10),"
                    + TALKTIME + " VARCHAR(10),"
                    + EXTRA_OFFFER + " VARCHAR(10),"
                    + COMMISSION + " VARCHAR(10),"
                    + DATA + " VARCHAR(10)"
                    + ")";


    // For order id
    /*
    private static final String TABLE_ORDER_ID = "order_id";
    private static final String ORDER_AUTO_ID = "id";
    private static final String ORDER_ID = "orderId";
    private static final String ORDER_STATUS = "orderStatus";

    private static final String CREATE_TABLE_ORDER_ID =
            "CREATE TABLE " + TABLE_ORDER_ID + "("
                    + ORDER_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ORDER_ID + " TEXT,"
                    + ORDER_STATUS + " TEXT"
                    + ")";
*/
    // For recharge history
    private static final String TABLE_RECHARGE_HISTORY = "recharge_history";
    private static final String RH_AUTO_ID = "id";
    private static final String RH_ORDER_ID = "orderId";
    private static final String RH_OPERATOR_ID = "operatorId";
    private static final String RH_STATUS = "status";
    private static final String RH_SERVICE = "service";
    private static final String RH_TRANS_ID = "TransId";
    private static final String RH_MOBILE = "mobile";
    private static final String RH_AMOUNT = "amount";
    private static final String RH_CREDIT_USED = "creditUsed";
    private static final String RH_CUS_CREDIT_USED="cusCreditUsed";
    private static final String RH_MARGIN_AMOUNT = "marginAmount";
    private static final String RH_BENEFICIARY_ACCOUNT_NUMBER = "beneficiaryAccountNumber";
    private static final String RH_BENEFICIARY_NAME = "beneficiaryName";
    private static final String RH_REQ_TIME = "reqTime";

    private static final String CREATE_TABLE_RECHARGE_HISTORY =
            "CREATE TABLE IF NOT EXISTS " + TABLE_RECHARGE_HISTORY + "("

                    + RH_ORDER_ID + " INTEGER PRIMARY KEY,"
                    + RH_OPERATOR_ID + " VARCHAR(10) ,"
                    + RH_STATUS + " VARCHAR(10),"
                    + RH_SERVICE + " TEXT,"
                    + RH_TRANS_ID + " TEXT,"
                    + RH_MOBILE + " TEXT,"
                    + RH_AMOUNT + " TEXT,"
                    + RH_CREDIT_USED + " TEXT,"
                    + RH_CUS_CREDIT_USED+ " VARCHAR(10),"
                    + RH_MARGIN_AMOUNT + " TEXT,"
                    + RH_BENEFICIARY_ACCOUNT_NUMBER + " VARCHAR(50),"
                    + RH_BENEFICIARY_NAME + " VARCHAR(50),"
                    + RH_REQ_TIME + " TEXT"
                    + ")";

    // For Chat Support
    private static final String TABLE_CHAT_SUPPORT = "chat_support";
    private static final String CS_AUTO_ID = "id";
    private static final String CS_CHAT_ID = "chatId";
    private static final String CS_USER_ID = "userId";
    private static final String CS_CHAT_MESSAGE = "chatMsg";
    private static final String CS_CHAT_TYPE = "chatType";
    private static final String CS_MSG_TYPE = "msgType";
    private static final String CS_TIME = "time";
    private static final String CS_CREATED_DATE = "createdDate";
    private static final String CS_REPLY_BY = "replyBy";

    private static final String CREATE_TABLE_CHAT_SUPPORT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CHAT_SUPPORT + "("

                    + CS_CHAT_ID + " VARCHAR(10),"
                    + CS_USER_ID + " VARCHAR(10) ,"
                    + CS_CHAT_MESSAGE + " TEXT,"
                    + CS_CHAT_TYPE + " VARCHAR(10),"
                    + CS_MSG_TYPE + " VARCHAR(10),"
                    + CS_TIME + " TEXT,"
                    + CS_CREATED_DATE + " TEXT,"
                    + CS_REPLY_BY + " TEXT"
                    + ")";


    // For retailer
    public static final String TABLE_RETAILERS = "retailers";

    private static final String RETAILER_USERNAME = "username";
    private static final String RETAILER_MOBILE = "mobile";
    private static final String RETAILER_NAME = "name";
    private static final String RETAILER_BALANCE = "rbal";
    private static final String RETAILER_COMMISSION_BALANCE = "rcommissionBal";
    private static final String RETAILER_STATUS = "status";
    private static final String RETAILER_PACKAGE_ID = "packageId";
    private static final String RETAILER_PACKAGE_NAME = "packageName";
    private static final String RETAILER_USER_TYPE = "userType";

    private static final String PROFILE_TABEL = "profile_table_new";
    private static final String PROFILE_IMAGE = "profile_image";

    private static final String CREATE_TABLE_PROFILE =
            "CREATE TABLE IF NOT EXISTS " + PROFILE_TABEL + " ("
                    + PROFILE_IMAGE + " blob" + ")";


    //for notification
    private static final String TABLE_NOTIFICATION="notification";
    private static final String NOTIFICATION_ID="id";
    private static final String NOTIFICATION_MSG="msg";
    private static final String NOTIFICATION_DATETIME="dateTime";
    private static final String NOTIFICATION_CLICKABLE="clickable";
    private static final String NOTIFICATION_ACTIVITY="activity";
    private static final String NOTIFICATION_ACTIVITYDATA="activityData";
    private static final String NOTIFICATION_TYPE="type";
    private static final String NOTIFICATION_STATUS="status";
    private static final String CREATE_NOTIFICATION_TABLE=
            "CREATE TABLE IF NOT EXISTS "+TABLE_NOTIFICATION+"("
                    +NOTIFICATION_ID+" INTEGER PRIMARY KEY, "
                    +NOTIFICATION_MSG+" VARCHAR(200) ,"
                    +NOTIFICATION_DATETIME+" VARCHAR(50) ,"
                    +NOTIFICATION_CLICKABLE+" VARCHAR(3) ,"
                    +NOTIFICATION_ACTIVITY+" VARCHAR(20) ,"
                    +NOTIFICATION_ACTIVITYDATA+" VARCHAR(150) ,"
                    +NOTIFICATION_TYPE+" VARCHAR(10) ,"
                    +NOTIFICATION_STATUS+" VARCHAR(5)"
                    +")";

    private static final String CREATE_TABLE_RETAILER =
            "CREATE TABLE IF NOT EXISTS " + TABLE_RETAILERS + "("

                    + RETAILER_USERNAME + " VARCHAR(30) PRIMARY KEY,"
                    + RETAILER_MOBILE + " TEXT,"
                    + RETAILER_NAME + " TEXT,"
                    + RETAILER_BALANCE + " TEXT,"
                    + RETAILER_COMMISSION_BALANCE + " TEXT,"
                    + RETAILER_STATUS + " TEXT,"
                    + RETAILER_PACKAGE_ID + " TEXT,"
                    + RETAILER_PACKAGE_NAME + " TEXT,"
                    + RETAILER_USER_TYPE + " TEXT"
                    + ")";

    // For package
    private static final String TABLE_PACKAGE = "packages";
    private static final String PACKAGE_AUTO_ID = "id";
    private static final String PACKAGE_USERNAME = "username";
    private static final String PACKAGE_NAME = "packageName";
    private static final String PACKAGE_ID = "packageId";

    private static final String CREATE_TABLE_PACKAGE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_PACKAGE + "("
                    + PACKAGE_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + PACKAGE_USERNAME + " TEXT,"
                    + PACKAGE_NAME + " TEXT,"
                    + PACKAGE_ID + " TEXT"
                    + ")";


    private static final String TABLE_TMPCONTACT="tmpContact";
    private static final String TMPCONTACT="number";
    private static final String TMPNAME="name";
    private static final String TMPTYPE="_type";
    private static final String CREATE_TABLE_TMPCONTACT=
            "CREATE TABLE IF NOT EXISTS "+ TABLE_TMPCONTACT+ "("
            +TMPCONTACT+" VARCHAR(20) PRIMARY KEY,"
            +TMPNAME+" VARCHAR(30),"
            +TMPTYPE+" VARCHAR(15)"
            +")";

    private static final String TABLE_DAYBOOK="daybook";

    private static final String DAYBOOK_DATE = "date";
    private static final String DAYBOOK_OPERATORID = "operatorId";
    private static final String DAYBOOK_SUCCESS = "success";
    private static final String DAYBOOK_PENDING = "pending";
    private static final String DAYBOOK_DISPUTED = "disputed";
    private static final String DAYBOOK_CREDITUSED = "creditUsed";
    private static final String DAYBOOK_CUSCREDITUSED = "cusCreditUsed";
    private static final String DAYBOOK_PROFIT="profit";

    private static final String CREATE_TABLE_DAYBOOK =
            "CREATE TABLE IF NOT EXISTS " + TABLE_DAYBOOK + "("
                    + DAYBOOK_DATE + " VARCHAR(20) PRIMARY KEY,"
                    + DAYBOOK_OPERATORID + " VARCHAR(5),"
                    + DAYBOOK_SUCCESS + " VARCHAR(10),"
                    + DAYBOOK_PENDING + " VARCHAR(10),"
                    + DAYBOOK_DISPUTED + " VARCHAR(10),"
                    + DAYBOOK_CREDITUSED + " VARCHAR(10),"
                    + DAYBOOK_CUSCREDITUSED + " VARCHAR(10),"
                    + DAYBOOK_PROFIT +" VARCHAR(10)"

                    + ")";

    //For Buslist
    public static final String TABLE_BUSLIST="busList";
    private static final String BUSLIST_AC="ac";
    private static final String BUSLIST_SLEEPER="sleeper";
    private static final String BUSLIST_PUSHBACK="pushback";
    private static final String BUSLIST_LIVETRACKINGAVAILABLE="livetrackingavailable";
    private static final String BUSLIST_CHARGINGPOINT="chargingpoint";
    private static final String BUSLIST_ARRIVALTIME="arrivaltime";
    private static final String BUSLIST_AVLSEATS="avlseats";
    private static final String BUSLIST_AVLWINDOWSSEATS="avlwindowsseats";
    private static final String BUSLIST_BUSTYPE="bustype";
    private static final String BUSLIST_BUSNAME="busname";
    private static final String BUSLIST_BOOKINGID="bookingid";
    private static final String BUSLIST_CANCELPOLICY="cancelpolicy";
    private static final String BUSLIST_PARTIALCANCELALLOWED="partialcancelallowed";
    private static final String BUSLIST_DEPARTURETIME="departureTimee";
    private static final String BUSLIST_TRAVELDURATION="travelDuration";
    private static final String BUSLIST_FARE="fare";

    private static final String CREATE_TABLE_BUSLIST=
            "CREATE TABLE IF NOT EXISTS "+TABLE_BUSLIST+ "("
                    + BUSLIST_BOOKINGID + " VARCHAR(10) PRIMARY KEY,"
                    + BUSLIST_AC + " VARCHAR(10),"
                    + BUSLIST_SLEEPER + " VARCHAR(10),"
                    + BUSLIST_PUSHBACK + " VARCHAR(10),"
                    + BUSLIST_LIVETRACKINGAVAILABLE + " VARCHAR(10),"
                    + BUSLIST_CHARGINGPOINT + " VARCHAR(10),"
                    + BUSLIST_ARRIVALTIME + " VARCHAR(20),"
                    + BUSLIST_AVLSEATS + " VARCHAR(10),"
                    + BUSLIST_AVLWINDOWSSEATS + " VARCHAR(10),"
                    + BUSLIST_BUSTYPE + " VARCHAR(50),"
                    + BUSLIST_BUSNAME + " VARCHAR(50),"
                    + BUSLIST_CANCELPOLICY + " VARCHAR(80),"
                    + BUSLIST_DEPARTURETIME + " VARCHAR(20),"
                    + BUSLIST_TRAVELDURATION + " VARCHAR(20),"
                    + BUSLIST_FARE + " VARCHAR(10),"
                    + BUSLIST_PARTIALCANCELALLOWED + " VARCHAR(10)"
                    + ")";


    public final String TABLE_BUS_bpdpPoint="bpdpPoint";

    Context context;
    Activity mActivity;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }



    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context);

        return instance;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DATA_PLANS);

        // create service type table
        db.execSQL(CREATE_TABLE_SERVICE_TYPE);
        //db.execSQL(CREATE_TABLE_ORDER_ID);
        db.execSQL(CREATE_TABLE_RECHARGE_HISTORY);
        db.execSQL(CREATE_TABLE_CHAT_SUPPORT);
        db.execSQL(CREATE_TABLE_RETAILER);
        db.execSQL(CREATE_TABLE_PACKAGE);
        db.execSQL(CREATE_TABLE_AUDIT);
        db.execSQL(CREATE_TABLE_LANGUAGELIST);

        db.execSQL(CREATE_TABLE_BUSLIST);
        db.execSQL(CREATE_NOTIFICATION_TABLE);
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_DAYBOOK);
        db.execSQL(CREATE_TABLE_TMPCONTACT);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(CREATE_TABLE_LANGUAGELIST);
        db.execSQL(CREATE_TABLE_BUSLIST);
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_DAYBOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        db.execSQL(CREATE_NOTIFICATION_TABLE);
        db.execSQL(CREATE_TABLE_TMPCONTACT);
        // Create tables again
        onCreate(db);
    }

    public void insertContactTmp(String number,String name,String type){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TABLE_TMPCONTACT);
        ContentValues values = new ContentValues();
        values.put(TMPCONTACT,number);
        values.put(TMPNAME,name);
        values.put(TMPTYPE,type);
        long id=0;
        try {
            id =db.insertWithOnConflict(TABLE_TMPCONTACT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        catch (Exception e){

        }
        Log.d("Chatlist","Mobile inserted "+id);
    }

    public ArrayList<ContactList> getConactListTmp(String type , String value) {
        ArrayList<ContactList> list = new ArrayList<>();


        // Select All Query

           //String selectQuery = "SELECT  * FROM " + TABLE_TMPCONTACT + " WHERE "+TMPCONTACT+" LIKE %"+value+"%";
        String selectQuery = "SELECT  * FROM " + TABLE_TMPCONTACT;
        if(!value.equals("")){
            selectQuery = "SELECT  * FROM " + TABLE_TMPCONTACT+" WHERE "+TMPTYPE+" ='"+value+"'";
        }


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndex(TMPCONTACT));
                String name = cursor.getString(cursor.getColumnIndex(TMPNAME));

                ContactList contactList = new ContactList(name, number);
                list.add(contactList);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        cursor.close();
        // return list
        Log.d("Chatlist","query "+selectQuery+ "size "+list.size());
        return list;
    }

    public void insertDaybook(String operatorId,String success,String pending,String disputed,String creditUsed,String cusCreditUsed,String date,String profit){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(CREATE_TABLE_DAYBOOK);
        ContentValues values = new ContentValues();
        values.put(DAYBOOK_OPERATORID,operatorId);
        values.put(DAYBOOK_SUCCESS, success);
        values.put(DAYBOOK_PENDING, pending);
        values.put(DAYBOOK_DISPUTED, disputed);
        values.put(DAYBOOK_CREDITUSED, creditUsed);
        values.put(DAYBOOK_CUSCREDITUSED,cusCreditUsed);
        values.put(DAYBOOK_DATE,date);
        values.put(DAYBOOK_PROFIT,profit);

        long id = db.insertWithOnConflict(TABLE_DAYBOOK, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("Database"," Inserted "+id);
        // close db connection

        db.close();

    }

    public void insertNotification(String notificationId,String msg,String date,String clickable,String activity,
                                   String activityData,String type,String status){

        int notificationIdTmp = 0;

        try {
            notificationIdTmp = Integer.parseInt(notificationId);
        } catch(Exception nfe) {
            ///System.out.println("Could not parse " + nfe);
        }

        if(notificationIdTmp>0){

            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL(CREATE_NOTIFICATION_TABLE);
            ContentValues values = new ContentValues();


            values.put(NOTIFICATION_ID, notificationIdTmp);
            values.put(NOTIFICATION_MSG, msg);
            values.put(NOTIFICATION_DATETIME, date);
            values.put(NOTIFICATION_CLICKABLE, clickable);
            values.put(NOTIFICATION_ACTIVITY,activity);
            values.put(NOTIFICATION_ACTIVITYDATA, activityData);
            values.put(NOTIFICATION_TYPE, type);
            values.put(NOTIFICATION_STATUS, status);


            // insert row
            long id = db.insertWithOnConflict(TABLE_NOTIFICATION, null, values,SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Chatlist"," Inserted "+id+" status "+status);
            // close db connection

            db.close();
        }

    }
    public void insertBusList(String ac,String sleeper,String pushBack,String liveTracingAvailable,String chargingPoint,
                              String arrivalTime,String avlSeats,String avlWindowsSeats,String busType,String busName,
                              String bookingId,String cancelPolicy,String partialCancelAllowed,String departureTime,String travelDuration,String fare){
        String selectQuery="INSERT OR REPLACE INTO "+TABLE_BUSLIST+"("+BUSLIST_AC+","+
                BUSLIST_SLEEPER+","+
                BUSLIST_PUSHBACK+","+
                BUSLIST_LIVETRACKINGAVAILABLE+","+
                BUSLIST_CHARGINGPOINT+","+
                BUSLIST_ARRIVALTIME+","+
                BUSLIST_AVLSEATS+","+
                BUSLIST_AVLWINDOWSSEATS+","+
                BUSLIST_BUSTYPE+","+
                BUSLIST_BUSNAME+","+
                BUSLIST_BOOKINGID+","+
                BUSLIST_CANCELPOLICY+","+
                BUSLIST_DEPARTURETIME+","+
                BUSLIST_TRAVELDURATION+","+
                BUSLIST_FARE+","+
                BUSLIST_PARTIALCANCELALLOWED+
                ") values (" +
                "'"+ac+"'," +
                "'"+sleeper+"'," +
                "'"+pushBack+"'," +
                "'"+liveTracingAvailable+"'," +
                "'"+chargingPoint+"'," +
                "'"+arrivalTime+"'," +
                "'"+avlSeats+"'," +
                "'"+avlWindowsSeats+"'," +
                "'"+busType+"'," +
                "'"+busName+"'," +
                "'"+bookingId+"'," +
                "'"+cancelPolicy+"'," +
                "'"+departureTime+"'," +
                "'"+travelDuration+"'," +
                "'"+fare+"'," +
                "'"+partialCancelAllowed+"'" +
                ") ";
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(CREATE_TABLE_BUSLIST);
        db.execSQL(selectQuery);

        //db.rawQuery(selectQuery, null);
        Log.d("Database inserted ",selectQuery);
        db.close();


    }

    /**
     * Insert service type data
     */

    public void insertServiceList(GetServiceListResponse.Service model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        int ServiceIdTmp = 1;

        try {
            ServiceIdTmp = Integer.parseInt(model.getId());
        } catch(Exception nfe) {
            ///System.out.println("Could not parse " + nfe);
        }
        values.put(AUTO_ID, ServiceIdTmp);
        values.put(SERVICE_ID, model.getId());
        values.put(SERVICE_NAME, model.getService());
        values.put(SERVICE_TYPE, model.getType());
        values.put(SMS_CODE, model.getSmsCode());

        // insert row
        try{

            long id=db.insertWithOnConflict(TABLE_SERVICE_TYPE, null, values,SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Database","inserted id "+id);
        }
        catch (Exception e){
            Utility.showToastLatest(context,"Service list not inserted "+e.toString(),"ERROR");
        }


        // close db connection

        db.close();

    }


    public void insertServiceType(GetLoginValidateResponse.Service model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        int ServiceIdTmp = 1;

        try {
            ServiceIdTmp = Integer.parseInt(model.getId());
        } catch(Exception nfe) {
            ///System.out.println("Could not parse " + nfe);
        }
        values.put(AUTO_ID, ServiceIdTmp);
        values.put(SERVICE_ID, model.getId());
        values.put(SERVICE_NAME, model.getService());
        values.put(SERVICE_TYPE, model.getType());
        values.put(SMS_CODE, model.getSmsCode());

        // insert row
        try{

            long id=db.insertWithOnConflict(TABLE_SERVICE_TYPE, null, values,SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Database","inserted id "+id);
        }
        catch (Exception e){
            Utility.showToastLatest(context,"Service list not inserted "+e.toString(),"ERROR");
        }


        // close db connection

        db.close();

    }
    /**
     * Get list of service type
     */
    public ArrayList<GetLoginValidateResponse.Service> getServiceList(String type) {
        ArrayList<GetLoginValidateResponse.Service> list = new ArrayList<>();

        String selectQuery = "";
        if (type.equals(Constant.SERVICE_ALL)) {
            selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE + " where " + SERVICE_TYPE + "='" + type + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GetLoginValidateResponse.Service service = new GetLoginValidateResponse.Service();
                service.setId(cursor.getString(cursor.getColumnIndex(SERVICE_ID)));
                service.setService(cursor.getString(cursor.getColumnIndex(SERVICE_NAME)));
                service.setType(cursor.getString(cursor.getColumnIndex(SERVICE_TYPE)));
                service.setSmsCode(cursor.getString(cursor.getColumnIndex(SMS_CODE)));

                list.add(service);
            } while (cursor.moveToNext());
        }

        // close db connection
        cursor.close();
        db.close();

        // return list
        return list;
    }

    /**
     * Delete service type table
     */
    public void deleteServiceTypeTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SERVICE_TYPE);
        db.execSQL("vacuum");
        db.close();
    }

    public void deleteTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("delete from " + tableName);
            db.execSQL("vacuum");
            db.close();
        }
        catch (Exception e){

        }

    }
    /**
     * Insert order id
     */
    /*
    public void insertOrderData(OrderLocal model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ORDER_ID, model.getOrderId());
        values.put(ORDER_STATUS, model.getStatus());

        // insert row
        db.insert(TABLE_ORDER_ID, null, values);

        // close db connection
        db.close();
    }
*/

    /**
     * Get order id list
     */
    /*
    public ArrayList<OrderLocal> getOrderIdList() {
        ArrayList<OrderLocal> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ORDER_ID));
                String status = cursor.getString(cursor.getColumnIndex(ORDER_STATUS));

                OrderLocal orderLocal = new OrderLocal(id, status);
                list.add(orderLocal);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return list
        return list;
    }
*/
    public ArrayList<OrderLocal> getOrderIdListToCheck(String type , String value) {
        ArrayList<OrderLocal> list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY + " WHERE status='PENDING' or status='DISPUTED'";

        if(type.equals("orderId") && !value.isEmpty()){
            selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY + " WHERE "+RH_ORDER_ID+"="+value;

        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(RH_ORDER_ID));
                String status = cursor.getString(cursor.getColumnIndex(RH_STATUS));

                OrderLocal orderLocal = new OrderLocal(id, status);
                list.add(orderLocal);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        cursor.close();
        // return list
        return list;
    }

    /**
     * Delete order id table
     */
    /*
    public void deleteOrderIdTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_ORDER_ID);
        db.close();
    }
*/
    /**
     * Update recharge history table
     */
    /*
    public void updateOrderIdData(String orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ORDER_STATUS, status);

        // updating row
        db.update(TABLE_ORDER_ID, values, ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
    }
*/
    public void insertNotification(NotificationModel model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

       // values.put(NOTIFICATION_ID, model.getId());
        values.put(NOTIFICATION_MSG,model.getDetail());
        values.put(NOTIFICATION_DATETIME, model.getDate());
        values.put(NOTIFICATION_TYPE,model.getType());


        // insert row
        //db.insert(TABLE_RECHARGE_HISTORY, null, values);
        long rowId = db.insertWithOnConflict(TABLE_NOTIFICATION, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        // close db connection
        db.close();
    }


    public void updateNotification(String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query="";

           ContentValues cv = new ContentValues();
           cv.put(NOTIFICATION_STATUS,value);
           //query = "UPDATE " + TABLE_NOTIFICATION + " SET  " + NOTIFICATION_STATUS + " = "+"'"+value+"' WHERE "+AUTO_ID+">0";
           //int rows=db.update(TABLE_NOTIFICATION,cv,null,null);

           db.update(TABLE_NOTIFICATION, cv, NOTIFICATION_ID + " = ?",
                   new String[]{String.valueOf(key)});
           //Log.d("Database","query -"+query+" rows "+rows);
           //db.execSQL(query);
            Log.d("Chatlist","Db updated value"+value+" order id "+key);

       db.close();


        // updating row

    }


    public ArrayList<NotificationModel> getNotificationList(String type, String key, String value) {
        ArrayList<NotificationModel> list = new ArrayList<>();

        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE + " where " + SERVICE_TYPE + "='" + type + "'";
        //String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY;
        //ORDER BY post_datetime DESC
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION + " ORDER BY " + NOTIFICATION_DATETIME + " DESC LIMIT 100";

        if(type.equals("search") && !value.isEmpty() && key.equals("status")){
            selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION + " WHERE "+NOTIFICATION_STATUS+"="+"'"+value+"'";

        }

        Log.d("Database","query search -"+selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setDetail(cursor.getString(cursor.getColumnIndex(NOTIFICATION_MSG)));
                notificationModel.setDate(cursor.getString(cursor.getColumnIndex(NOTIFICATION_DATETIME)));
                notificationModel.setType(cursor.getString(cursor.getColumnIndex(NOTIFICATION_TYPE)));
                notificationModel.setClickable(cursor.getString(cursor.getColumnIndex(NOTIFICATION_CLICKABLE)));
                notificationModel.setActivityValue(cursor.getString(cursor.getColumnIndex(NOTIFICATION_ACTIVITY)));
                notificationModel.setActivityData(cursor.getString(cursor.getColumnIndex(NOTIFICATION_ACTIVITYDATA)));
                notificationModel.setStatus(cursor.getString(cursor.getColumnIndex(NOTIFICATION_STATUS)));
                notificationModel.setId(cursor.getString(cursor.getColumnIndex(NOTIFICATION_ID)));

                list.add(notificationModel);
            } while (cursor.moveToNext());
        }

        // close db connection
        cursor.close();
        db.close();

        // return list
        return list;
    }

    public void insertImage(String link) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TABLE_PROFILE);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(link);
            byte[] imgbyte = new byte[fileInputStream.available()];
            fileInputStream.read(imgbyte);
            ContentValues values = new ContentValues();

            values.put(PROFILE_IMAGE, imgbyte);
            db.execSQL("delete from "+ PROFILE_TABEL);
            long rowId = db.insertWithOnConflict(PROFILE_TABEL, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            // close db connection
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public Bitmap getProfileImage() {
        String link = "";
        String selectQuery = "SELECT  * FROM " + PROFILE_TABEL;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TABLE_PROFILE);
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndex(PROFILE_IMAGE));
            cursor.close();
            db.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (!cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        // close db connection


        // return list
        return null;
    }


    /**
     * Insert recharge history data
     */
    public void insertRechargeHistory(RechargeHistory model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(RH_OPERATOR_ID, model.getOperatorId());
        values.put(RH_ORDER_ID,model.getOrderId());
        values.put(RH_STATUS, model.getStatus());
        values.put(RH_SERVICE, model.getService());
        values.put(RH_TRANS_ID, model.getTransId());
        values.put(RH_MOBILE, model.getMobile());
        values.put(RH_AMOUNT, model.getAmount());
        values.put(RH_CREDIT_USED, model.getCreditUsed());
        values.put(RH_CUS_CREDIT_USED,model.getCusCreditUsed());
        values.put(RH_MARGIN_AMOUNT, model.getProfit());
        values.put(RH_BENEFICIARY_ACCOUNT_NUMBER, model.getBeneficiaryAccountNumber());
        values.put(RH_BENEFICIARY_NAME, model.getBeneficiaryName());
        values.put(RH_REQ_TIME, model.getReqTime());

        // insert row
        //db.insert(TABLE_RECHARGE_HISTORY, null, values);
        long rowId = db.insertWithOnConflict(TABLE_RECHARGE_HISTORY, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        // close db connection
        db.close();
    }

    /**
     * Insert chat support data
     */
    public void insertChatSupportHistory(ChatListModel.DataBean model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        try {
            String query = "SELECT  * FROM " + TABLE_CHAT_SUPPORT + " WHERE " + CS_CHAT_ID + "=" + model.getId();

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                //Update Data
                values.put(CS_CHAT_ID, model.getId());
                values.put(CS_USER_ID, model.getUserId());
                values.put(CS_CHAT_MESSAGE, model.getChatMsg());
                values.put(CS_CHAT_TYPE, model.getChatType());
                values.put(CS_MSG_TYPE, model.getMsgType());
                values.put(CS_TIME, model.getTime());
                values.put(CS_CREATED_DATE, model.getCreatedDate());
                values.put(CS_REPLY_BY, model.getReplyBy());
                db.update(TABLE_CHAT_SUPPORT, values, CS_CHAT_ID + " = ?", new String[]{String.valueOf(model.getId())});
            } else {
                //Insert Data
                values.put(CS_CHAT_ID, model.getId());
                values.put(CS_USER_ID, model.getUserId());
                values.put(CS_CHAT_MESSAGE, model.getChatMsg());
                values.put(CS_CHAT_TYPE, model.getChatType());
                values.put(CS_MSG_TYPE, model.getMsgType());
                values.put(CS_TIME, model.getTime());
                values.put(CS_CREATED_DATE, model.getCreatedDate());
                values.put(CS_REPLY_BY, model.getReplyBy());

                // insert row
                //db.insert(TABLE_CHAT_SUPPORT, null, values);
                long rowId = db.insertWithOnConflict(TABLE_CHAT_SUPPORT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // close db connection
        db.close();
    }

    public void insertAuditData(BalanceHistory model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(AUDIT_OP_BAL, model.getOpeningBal());
        values.put(AUDIT_CLOSING_BAL, model.getClosingBal());
        values.put(AUDIT_DATE, model.getDate());
        values.put(AUDIT_TYPE, model.getType());
        values.put(AUDIT_ORDERID, model.getOrderId());
        values.put(AUDIT_STATUS, model.getStatus());
        values.put(AUDIT_MOBILE, model.getMobile());
        values.put(AUDIT_SERVICE,model.getService());
        values.put(AUDIT_BANKACCOUNT,model.getBankAccount());
        values.put(AUDIT_BANKNAME,model.getBankName());
        values.put(AUDIT_AMOUNT,model.getAmount());




        // insert row
        //db.insert(TABLE_RECHARGE_HISTORY, null, values);

        try{
            long id = db.insert(TABLE_AUDIT, null, values);

        }
        catch (Exception e){

        }

        // close db connection
        db.close();
    }

    public void insertDataPlans(DataPlansModel model) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TYPE, model.getType());
        values.put(AMOUNT, model.getAmount());
        values.put(DETAIL, model.getDetail());
        values.put(VALIDITY, model.getValidity());
        values.put(TALKTIME, model.getTalktime());
        values.put(EXTRA_OFFFER, model.getExtraOffer());
        values.put(COMMISSION, model.getCommission());
        values.put(DATA,model.getData());

        Log.e("Datainserted", "Inserting");

        // insert row
        //db.insert(TABLE_RECHARGE_HISTORY, null, values);

        try{
            long id = db.insert(TABLE_DATA_PLANS, null, values);

        }
        catch (Exception e){

        }

        // close db connection
        db.close();
    }


    public ArrayList<DataPlansModel> getDataPlans(String talktimeamt) {
        ArrayList<DataPlansModel> list = new ArrayList<>();


     /*   String selectQuery = "SELECT  * FROM " + TABLE_DATA_PLANS + " ORDER BY " + TALKTIME + " DESC";

        if (talktimeamt != "" && talktimeamt != null) {

            selectQuery = "SELECT  * FROM " + TABLE_DATA_PLANS +
                    " where (" + TALKTIME + " EQUALS '" + talktimeamt +"')";

        }

*/



        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_DATA_PLANS+" WHERE AMOUNT='" + talktimeamt + "' LIMIT 1",null);

        if(talktimeamt.equals("0")){
            cursor = db.rawQuery("SELECT * FROM "+TABLE_DATA_PLANS,null);

        }






        if (cursor.moveToFirst()) {
            do {
                DataPlansModel dataPlansModel = new DataPlansModel();

                dataPlansModel.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                dataPlansModel.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
                dataPlansModel.setDetail(cursor.getString(cursor.getColumnIndex(DETAIL)));
                dataPlansModel.setValidity(cursor.getString(cursor.getColumnIndex(VALIDITY)));
                dataPlansModel.setTalktime(cursor.getString(cursor.getColumnIndex(TALKTIME)));
                dataPlansModel.setExtraOffer(cursor.getString(cursor.getColumnIndex(EXTRA_OFFFER)));
                dataPlansModel.setCommission(cursor.getString(cursor.getColumnIndex(COMMISSION)));
                dataPlansModel.setData(cursor.getString(cursor.getColumnIndex(DATA)));
                list.add(dataPlansModel);
            } while (cursor.moveToNext());
        }

        // close db connection
        cursor.close();
        db.close();

        // return list
        return list;
    }


public ArrayList<BusList> getBusList(){
        ArrayList<BusList> list = new ArrayList<>();

    // Select All Query
    //String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE + " where " + SERVICE_TYPE + "='" + type + "'";
    //String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY;
    //ORDER BY post_datetime DESC
    String selectQuery = "SELECT  * FROM " + TABLE_BUSLIST + " ORDER BY " + BUSLIST_BOOKINGID + " DESC LIMIT 100";


    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
        do {
            BusList busList = new BusList();
            busList.setAc(cursor.getString(cursor.getColumnIndex(BUSLIST_AC)));
            busList.setBusType(cursor.getString(cursor.getColumnIndex(BUSLIST_BUSTYPE)));
            busList.setBusName(cursor.getString(cursor.getColumnIndex(BUSLIST_BUSNAME)));
            busList.setBookingId(cursor.getString(cursor.getColumnIndex(BUSLIST_BOOKINGID)));
            busList.setAvlWindowsSeats(cursor.getString(cursor.getColumnIndex(BUSLIST_AVLWINDOWSSEATS)));
            busList.setAvlSeats(cursor.getString(cursor.getColumnIndex(BUSLIST_AVLSEATS)));
            busList.setArrivalTime(cursor.getString(cursor.getColumnIndex(BUSLIST_ARRIVALTIME)));
            busList.setChargingPoint(cursor.getString(cursor.getColumnIndex(BUSLIST_CHARGINGPOINT)));
            busList.setCancelPolicy(cursor.getString(cursor.getColumnIndex(BUSLIST_CANCELPOLICY)));
            busList.setLiveTracingAvailable(cursor.getString(cursor.getColumnIndex(BUSLIST_LIVETRACKINGAVAILABLE)));
            busList.setPartialCancelAllowed(cursor.getString(cursor.getColumnIndex(BUSLIST_PARTIALCANCELALLOWED)));
            busList.setPushBack(cursor.getString(cursor.getColumnIndex(BUSLIST_PUSHBACK)));
            busList.setDepartureTime(cursor.getString(cursor.getColumnIndex(BUSLIST_DEPARTURETIME)));
            busList.setTravelDuration(cursor.getString(cursor.getColumnIndex(BUSLIST_TRAVELDURATION)));
            busList.setFare(cursor.getString(cursor.getColumnIndex(BUSLIST_FARE)));


            list.add(busList);
        } while (cursor.moveToNext());
    }
    cursor.close();
    // close db connection
    db.close();

    // return list
    return list;

}

    public ArrayList<DayBook> getDayBookList(String mobileSearch , String searchType) {
        ArrayList<DayBook> list = new ArrayList<>();

        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE + " where " + SERVICE_TYPE + "='" + type + "'";
        //String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY;
        //ORDER BY post_datetime DESC
        String selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK + " ORDER BY " + DAYBOOK_DATE + " DESC LIMIT 10";


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_TABLE_DAYBOOK);
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DayBook dayBook= new DayBook();
                dayBook.setDate(cursor.getString(cursor.getColumnIndex(DAYBOOK_DATE)));
                dayBook.setOperatorId(cursor.getString(cursor.getColumnIndex(DAYBOOK_OPERATORID)));
                dayBook.setSuccess(cursor.getString(cursor.getColumnIndex(DAYBOOK_SUCCESS)));
                dayBook.setPending(cursor.getString(cursor.getColumnIndex(DAYBOOK_PENDING)));
                dayBook.setDisputed(cursor.getString(cursor.getColumnIndex(DAYBOOK_DISPUTED)));
                dayBook.setCreditUsed(cursor.getString(cursor.getColumnIndex(DAYBOOK_CREDITUSED)));
                dayBook.setCusCreditUsed(cursor.getString(cursor.getColumnIndex(DAYBOOK_CUSCREDITUSED)));
                dayBook.setProfit(cursor.getString(cursor.getColumnIndex(DAYBOOK_PROFIT)));



                list.add(dayBook);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // close db connection
        db.close();

        // return list
        return list;
    }


    /**
     * Get list of recharge history
     */
    public ArrayList<RechargeHistory> getRechargeHistoryList(String mobileSearch , String searchType) {
        ArrayList<RechargeHistory> list = new ArrayList<>();

        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE + " where " + SERVICE_TYPE + "='" + type + "'";
        //String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY;
        //ORDER BY post_datetime DESC
        String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY + " ORDER BY " + RH_ORDER_ID + " DESC LIMIT 100";

        if (mobileSearch != "" && mobileSearch != null) {

            selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY +
                    " where (" + RH_MOBILE + " LIKE '%" + mobileSearch +
                    "%' or " + RH_SERVICE + " LIKE '%" + mobileSearch +
                    "%' or " + RH_STATUS + " LIKE '%" + mobileSearch +
                    "%' or " + RH_BENEFICIARY_ACCOUNT_NUMBER + " LIKE '%" + mobileSearch +
                    "%') ORDER BY " + RH_ORDER_ID + " DESC LIMIT 100";
        }

        if(searchType.equals("orderId")){

            selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY +
                    " where " + RH_ORDER_ID + " = " + mobileSearch+" LIMIT 1";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RechargeHistory rechargeHistory = new RechargeHistory();
                rechargeHistory.setOrderId(cursor.getString(cursor.getColumnIndex(RH_ORDER_ID)));
                rechargeHistory.setOperatorId(cursor.getString(cursor.getColumnIndex(RH_OPERATOR_ID)));
                rechargeHistory.setStatus(cursor.getString(cursor.getColumnIndex(RH_STATUS)));
                rechargeHistory.setService(cursor.getString(cursor.getColumnIndex(RH_SERVICE)));
                rechargeHistory.setTransId(cursor.getString(cursor.getColumnIndex(RH_TRANS_ID)));
                rechargeHistory.setMobile(cursor.getString(cursor.getColumnIndex(RH_MOBILE)));
                rechargeHistory.setAmount(cursor.getString(cursor.getColumnIndex(RH_AMOUNT)));
                rechargeHistory.setCreditUsed(cursor.getString(cursor.getColumnIndex(RH_CREDIT_USED)));
                rechargeHistory.setCusCreditUsed(cursor.getString(cursor.getColumnIndex(RH_CUS_CREDIT_USED)));
                rechargeHistory.setMarginAmount(cursor.getString(cursor.getColumnIndex(RH_MARGIN_AMOUNT)));
                rechargeHistory.setBeneficiaryAccountNumber(cursor.getString(cursor.getColumnIndex(RH_BENEFICIARY_ACCOUNT_NUMBER)));
                rechargeHistory.setBeneficiaryName(cursor.getString(cursor.getColumnIndex(RH_BENEFICIARY_NAME)));
                rechargeHistory.setReqTime(cursor.getString(cursor.getColumnIndex(RH_REQ_TIME)));
                rechargeHistory.setProfit(cursor.getString(cursor.getColumnIndex(RH_MARGIN_AMOUNT)));

                list.add(rechargeHistory);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // close db connection
        db.close();

        // return list
        return list;
    }


    /**
     * Get list of chat support
     */
    public ArrayList<ChatListModel.DataBean> getChatSupportList() {
        ArrayList<ChatListModel.DataBean> list = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CHAT_SUPPORT ;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatListModel.DataBean chatHistory = new ChatListModel.DataBean();
                chatHistory.setId(cursor.getString(cursor.getColumnIndex(CS_CHAT_ID)));
                chatHistory.setUserId(cursor.getString(cursor.getColumnIndex(CS_USER_ID)));
                chatHistory.setChatMsg(cursor.getString(cursor.getColumnIndex(CS_CHAT_MESSAGE)));
                chatHistory.setChatType(cursor.getString(cursor.getColumnIndex(CS_CHAT_TYPE)));
                chatHistory.setMsgType(cursor.getString(cursor.getColumnIndex(CS_MSG_TYPE)));
                chatHistory.setTime(cursor.getString(cursor.getColumnIndex(CS_TIME)));
                chatHistory.setCreatedDate(cursor.getString(cursor.getColumnIndex(CS_CREATED_DATE)));
                chatHistory.setReplyBy(cursor.getString(cursor.getColumnIndex(CS_REPLY_BY)));

                list.add(chatHistory);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // close db connection
        db.close();

        // return list
        return list;
    }

    public ArrayList<BalanceHistory> getBalanceHistoryList(String mobileSearch , String searchType) {
        ArrayList<BalanceHistory> list = new ArrayList<>();

        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE + " where " + SERVICE_TYPE + "='" + type + "'";
        //String selectQuery = "SELECT  * FROM " + TABLE_RECHARGE_HISTORY;
        //ORDER BY post_datetime DESC
        String selectQuery = "SELECT  * FROM " + TABLE_AUDIT + " ORDER BY " + AUTO_ID + " DESC";

        if (mobileSearch != "" && mobileSearch != null) {

            selectQuery = "SELECT  * FROM " + TABLE_AUDIT +
                    " where (" + AUDIT_DATE + " LIKE '%" + mobileSearch +
                    "%' or " + AUDIT_TYPE + " LIKE '%" + mobileSearch +
                    "%' or " + AUDIT_ORDERID + " LIKE '%" + mobileSearch +
                    "%' or " + AUDIT_STATUS + " LIKE '%" + mobileSearch +
                    "%' or " + AUDIT_MOBILE + " LIKE '%" + mobileSearch +
                    "%' or " + AUDIT_SERVICE + " LIKE '%" + mobileSearch +
                    "%' or " + AUDIT_BANKACCOUNT + " LIKE '%" + mobileSearch +
                    "%' or " + AUDIT_BANKNAME + " LIKE '%" + mobileSearch +
                    "%') ORDER BY " + AUTO_ID + " DESC";
        }


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Log.d("Found result ","yes ");
                BalanceHistory balanceHistory = new BalanceHistory();
                //Log.e("Data fetched", "Date "+cursor.getString(cursor.getColumnIndex(AUDIT_DATE)));
                balanceHistory.setDate(cursor.getString(cursor.getColumnIndex(AUDIT_DATE)));
                balanceHistory.setOpeningBal(cursor.getString(cursor.getColumnIndex(AUDIT_OP_BAL)));
                balanceHistory.setClosingBal(cursor.getString(cursor.getColumnIndex(AUDIT_CLOSING_BAL)));
                balanceHistory.setType(cursor.getString(cursor.getColumnIndex(AUDIT_TYPE)));
                balanceHistory.setAmount(cursor.getString(cursor.getColumnIndex(AUDIT_AMOUNT)));

                balanceHistory.setOrderId(cursor.getString(cursor.getColumnIndex(AUDIT_ORDERID)));
                balanceHistory.setStatus(cursor.getString(cursor.getColumnIndex(AUDIT_STATUS)));
                balanceHistory.setMobile(cursor.getString(cursor.getColumnIndex(AUDIT_MOBILE)));


                balanceHistory.setService(cursor.getString(cursor.getColumnIndex(AUDIT_SERVICE)));
                balanceHistory.setBankName(cursor.getString(cursor.getColumnIndex(AUDIT_BANKNAME)));
                balanceHistory.setBankAccount(cursor.getString(cursor.getColumnIndex(AUDIT_BANKACCOUNT)));







                list.add(balanceHistory);
            } while (cursor.moveToNext());
        }

        // close db connection
        cursor.close();
        db.close();

        // return list
        return list;
    }

    public ArrayList<LanguageList> getLanguageList() {
        ArrayList<LanguageList> list = new ArrayList<>();

        LanguageList languageList = new LanguageList();
        //Log.e("Data fetched", "Date "+cursor.getString(cursor.getColumnIndex(AUDIT_DATE)));
        languageList.setCode("en");
        languageList.setLanguage("English");
        languageList.setLanguageLocal("English");
        list.add(languageList);
        LanguageList languageList1 = new LanguageList();
        languageList1.setCode("hi");
        languageList1.setLanguage("Hindi");
        languageList1.setLanguageLocal("हिंदी");
        list.add(languageList1);

        /*
        String selectQuery = "SELECT  * FROM " + TABLE_LANGUAGE + " ORDER BY " + LANGUAGE_LANGUAGE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //Log.d("Found result ","yes ");
                LanguageList languageList = new LanguageList();
                //Log.e("Data fetched", "Date "+cursor.getString(cursor.getColumnIndex(AUDIT_DATE)));
                languageList.setCode(cursor.getString(cursor.getColumnIndex(LANGUAGE_CODE)));
                languageList.setLanguage(cursor.getString(cursor.getColumnIndex(LANGUAGE_LANGUAGE)));

                list.add(languageList);
            } while (cursor.moveToNext());

        }
*/
        // return list
        return list;
    }


    /**
     * Update recharge history table
     */
    public void updateRechargeHistory(String orderId, String status, String transId, String creditUsed , String cusCreditUsed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (!status.equalsIgnoreCase("no")) {
            values.put(RH_STATUS, status);
        }

        if (!transId.equalsIgnoreCase("no")) {
            values.put(RH_TRANS_ID, transId);
        }

        if (!creditUsed.equalsIgnoreCase("no")) {
            values.put(RH_CREDIT_USED, creditUsed);
        }
        if (!cusCreditUsed.equalsIgnoreCase("no")) {
            values.put(RH_CUS_CREDIT_USED, cusCreditUsed);
        }

        // updating row
        db.update(TABLE_RECHARGE_HISTORY, values, RH_ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
    }

    /**
     * Delete recharge history table
     */
    public void deleteRechargeHistoryTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_RECHARGE_HISTORY);
        db.close();
    }





    public synchronized void addMultipleRetailer(JSONArray retailer_array) throws JSONException {
        //JSONArray history_array = new JSONArray(history_string);
        //Log.d(TAG,history_array.toString());
        if (retailer_array == null) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        int count = retailer_array.length();

        for (int i = 0; i < count; i++) {


            JSONObject history = retailer_array.getJSONObject(i);
            ContentValues values = new ContentValues();


            if (history.getString(RETAILER_USERNAME) == "" || history.getString(RETAILER_USERNAME) == null) {

                continue;
            }

            try {

                values.put(RETAILER_USERNAME, history.getString(RETAILER_USERNAME));

                values.put(RETAILER_MOBILE, history.getString(RETAILER_MOBILE));
                values.put(RETAILER_NAME, history.getString(RETAILER_NAME));
                values.put(RETAILER_BALANCE, history.getString(RETAILER_BALANCE));
                values.put(RETAILER_COMMISSION_BALANCE, history.getString(RETAILER_COMMISSION_BALANCE));
                values.put(RETAILER_STATUS, history.getString(RETAILER_STATUS));
                values.put(RETAILER_PACKAGE_ID, history.getString(RETAILER_PACKAGE_ID));
                values.put(RETAILER_PACKAGE_NAME, history.getString(RETAILER_PACKAGE_NAME));
                values.put(RETAILER_USER_TYPE, history.getString(RETAILER_USER_TYPE));


                long rowId = db.insertWithOnConflict(TABLE_RETAILERS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            } catch (Exception e) {

            }

        }


        db.close();

    }


    /**
     * Delete retailer table
     */
    public void deleteRetailerTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_RETAILERS);
        db.execSQL("vacuum");
        db.close();
    }

    public void emptyTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + tableName);
        db.execSQL("vacuum");
        db.close();

    }



    /**
     * Delete package table
     */
    public void deletePackageTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PACKAGE);
        db.execSQL("vacuum");
        db.close();
    }


    public void deleteDataPlansTable() {
        Log.e("TABLE" ,"deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_DATA_PLANS);
        db.execSQL("vacuum");
        db.close();
    }



    /*public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }*/

    /*public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE, note.getNote());

        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }*/
}

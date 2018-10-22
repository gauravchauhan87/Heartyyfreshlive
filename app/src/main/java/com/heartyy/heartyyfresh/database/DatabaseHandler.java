package com.heartyy.heartyyfresh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.errors.AddressError;
import com.heartyy.heartyyfresh.errors.CardError;
import com.heartyy.heartyyfresh.errors.ChangePasswordError;
import com.heartyy.heartyyfresh.errors.LoginError;
import com.heartyy.heartyyfresh.errors.SignupError;
import com.heartyy.heartyyfresh.errors.SupportError;
import com.heartyy.heartyyfresh.errors.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dheeraj on 12/21/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "heartyfreshDBManager";
    private static final String TABLE_SIGNUP_ERROR = "signup_error_table";
    private static final String TABLE_LOGIN_ERROR = "login_error_table";
    private static final String TABLE_ADDRESS_ERROR = "address_error_table";
    private static final String TABLE_CARD_ERROR = "card_error_table";
    private static final String TABLE_CHANGE_PASSWORD_ERROR = "change_password_error_table";
    private static final String TABLE_SUPPORT_ERROR = "support_error_table";
    private static final String TABLE_ORDER = "order_table";
    private static final String TABLE_SUPPLIER = "supplier_table";
    private static final String TABLE_LIKE = "like_table";

    //Order Table Strings
    private static final String SUPPLIER_ITEM_ID = "supplier_item_id";
    private static final String ITEM_NAME = "item_name";
    private static final String BRAND_NAME = "brand_name";
    private static final String SIZE = "size";
    private static final String UOM = "uom";
    private static final String SHIPPING_WEIGHT = "shipping_weight";
    private static final String ORDER_QUANTITY = "order_quantity";
    private static final String UNIT_PRICE = "unit_price";
    private static final String FINAL_PRICE = "final_price";
    private static final String APPLICABLE_SALES_DESCRIPTION = "applicable_sale_description";
    private static final String SUPPLIER_ID = "supplier_id";
    private static final String THUMBNAIL = "thumbnail";
    private static final String SUBSTITUTION = "substitution";
    private static final String IS_TAXABLE = "is_taxable";
    private static final String REGULAR_PRICE = "regular_price";
    private static final String TAX_AMOUNT = "tax_amount";
    private static final String MAX_QUANTITY = "max_quantity";
    //EndOrder Table Strings

    //Order Table Strings
    private static final String SUPPLIER_NAME = "item_name";
    //EndOrder Table Strings


    private static final String PROFILE_NAME_REQUIRED = "profile_name_required";
    private static final String PROFILE_NAME_LENGTH = "profile_name_length";
    private static final String PROFILE_LAST_NAME_REQUIRED = "profile_last_name_required";
    private static final String PROFILE_LAST_NAME_LENGTH = "profile_last_name_length";
    private static final String PROFILE_EMAIL_REQUIRED = "profile_email_required";
    private static final String PROFILE_EMAIL_INVALID = "profile_email_invalid";
    private static final String PROFILE_PASSWORD_REQUIRED = "profile_password_required";
    private static final String PROFILE_PASSWORD_INVALID = "profile_password_invalid";
    private static final String PROFILE_PASSWORD_LENGTH = "profile_password_length";
    private static final String PROFILE_PHONE_REQUIRED = "profile_phone_required";
    private static final String PROFILE_PHONE_INVALID = "profile_phone_invalid";
    private static final String PROFILE_ZIP_REQUIRED = "profile_zip_required";
    private static final String PROFILE_ZIP_INVALID = "profile_zip_invalid";
    private static final String PROFILE_ZIP_LENGTH = "profile_zip_length";


    private static final String LOGIN_PASSWORD_REQUIRED = "login_password_required";
    private static final String LOGIN_EMAIL_REQUIRED = "login_email_required";
    private static final String LOGIN_EMAIL_INVALID = "login_email_invalid";


    private static final String NO_RESULT = "no_result";
    private static final String STREET_REQUIRED = "street_required";
    private static final String STREET_LENGTH = "street_length";
    private static final String APT_REQUIRED = "apt_required";
    private static final String APT_LENGTH = "apt_length";
    private static final String CITY_REQUIRED = "city_required";
    private static final String CITY_LENGTH = "city_length";
    private static final String STATE_REQUIRED = "state_required";
    private static final String STATE_LENGTH = "state_length";
    private static final String ADDRESS_ZIP_REQUIRED = "address_zip_required";
    private static final String ADDRESS_ZIP_INVALID = "address_zip_invalid";
    private static final String ADDRESS_ZIP_LENGTH = "address_zip_length";


    private static final String CARD_NUMBER_REQUIRED = "card_number_required";
    private static final String CARD_NUMBER_LENGTH = "card_number_length";
    private static final String CVV_REQUIRED = "cvv_required";
    private static final String CVV_LENGTH = "cvv_length";
    private static final String MONTH_REQUIRED = "month_required";
    private static final String MONTH_LENGTH = "month_length";
    private static final String YEAR_REQUIRED = "year_required";
    private static final String YEAR_LENGTH = "year_length";
    private static final String CARD_ZIP_REQUIRED = "card_zip_required";
    private static final String CARD_ZIP_LENGTH = "card_zip_length";


    private static final String CURRENT_PASSWORD_REQUIRED = "current_password_required";
    private static final String NEW_PASSWORD_REQUIRED = "new_password_required";
    private static final String SAME_PASSWORD = "same_password";
    private static final String PASSWORD_LENGTH = "password_length";

    private static final String COMMENT_REQUIRED = "comment_required";

    private static final String TABLE_LOCATION = "location_table";
    private static final String TABLE_CARD = "card_table";

    private static final String USER_DELIVERY_LOCATION_ID = "user_delivery_location_id";
    private static final String USER_ID = "user_id";
    private static final String LOCATION_TYPE = "location_type";
    private static final String ADDRESS1 = "address1";
    private static final String ADDRESS2 = "address2";
    private static final String LOCATION_NAME = "location_name";
    private static final String DELIVERY_INSTRUCTIONS = "delivery_instructions";
    private static final String ZIP_CODE = "zipcode";
    private static final String IS_PRMARY_LOCATION = "is_primary_location";
    private static final String CITY = "city";
    private static final String STATE = "state";
    private static final String COUNTRY = "country";
    private static final String APT_SUIT_UNIT = "apt_suite_unit";
    private static final String TAX_RATE = "tax_rate";


    private static final String PAYMENT_GATEWAY_CUSTOMER_ID = "payment_gateway_customer_id";
    private static final String CC_NUMBER = "cc_number";
    private static final String CARD_ISSUER = "card_issuer";
    private static final String EXPIRATION_DATE = "expiration_date";
    private static final String LAST_4_DIGITS = "last_4_digits";
    private static final String TOKEN = "token";
    private static final String GIVEN_CARD_NAME = "given_card_name";
    private static final String IS_PRIMARY = "is_primary   ";
    private static final String USER_PEYMENT_METHOD_ID = "user_payment_method_id";

    private static final String SUPPLIER_AVAILABLE = "supplier_available";


    private static final String CREATE_SIGNUP_ERROR_TABLE = "create table "
            + TABLE_SIGNUP_ERROR + "(" + PROFILE_NAME_REQUIRED + " text,"
            + PROFILE_NAME_LENGTH + " text, " + PROFILE_EMAIL_REQUIRED + " text, " + PROFILE_EMAIL_INVALID
            + " text, " + PROFILE_PASSWORD_REQUIRED + " text, " + PROFILE_PASSWORD_INVALID + " text, " + PROFILE_PASSWORD_LENGTH
            + " text, " + PROFILE_PHONE_REQUIRED + " text, " + PROFILE_PHONE_INVALID + " text, " + PROFILE_ZIP_REQUIRED
            + " text, " + PROFILE_ZIP_INVALID + " text, " + PROFILE_ZIP_LENGTH + " text, " + PROFILE_LAST_NAME_REQUIRED + " text, " + PROFILE_LAST_NAME_LENGTH + " text" + ")";

    private static final String CREATE_LOGIN_ERROR_TABLE = "create table "
            + TABLE_LOGIN_ERROR + "(" + LOGIN_PASSWORD_REQUIRED + " text,"
            + LOGIN_EMAIL_REQUIRED + " text, " + LOGIN_EMAIL_INVALID + " text" + ")";


    private static final String CREATE_ADDRESS_ERROR_TABLE = "create table "
            + TABLE_ADDRESS_ERROR + "(" + NO_RESULT + " text,"
            + STREET_REQUIRED + " text, " + STREET_LENGTH + " text, " + APT_REQUIRED
            + " text, " + APT_LENGTH + " text, " + CITY_REQUIRED + " text, " + CITY_LENGTH
            + " text, " + STATE_REQUIRED + " text, " + STATE_LENGTH + " text, " + ADDRESS_ZIP_REQUIRED
            + " text, " + ADDRESS_ZIP_INVALID
            + " text, " + ADDRESS_ZIP_LENGTH + " text" + ")";

    private static final String CREATE_CARD_ERROR_TABLE = "create table "
            + TABLE_CARD_ERROR + "(" + CARD_NUMBER_REQUIRED + " text,"
            + CARD_NUMBER_LENGTH + " text, " + CVV_REQUIRED + " text, " + CVV_LENGTH
            + " text, " + MONTH_REQUIRED + " text, " + MONTH_LENGTH + " text, " + YEAR_REQUIRED
            + " text, " + YEAR_LENGTH + " text, " + CARD_ZIP_REQUIRED + " text, " + CARD_ZIP_LENGTH
            + " text" + ")";

    private static final String CREATE_CHANGE_PASSWORD_ERROR_TABLE = "create table "
            + TABLE_CHANGE_PASSWORD_ERROR + "(" + CURRENT_PASSWORD_REQUIRED + " text,"
            + NEW_PASSWORD_REQUIRED + " text, " + SAME_PASSWORD + " text, "
            + PASSWORD_LENGTH + " text"
            + ")";

    private static final String CREATE_SUPPORT_ERROR_TABLE = "create table "
            + TABLE_SUPPORT_ERROR + "(" + COMMENT_REQUIRED + " text"
            + ")";


    private static final String CREATE_LOCATION_TABLE = "create table "
            + TABLE_LOCATION + "(" + USER_DELIVERY_LOCATION_ID + " text,"
            + USER_ID + " text, " + LOCATION_TYPE + " text, " + ADDRESS1
            + " text, " + ADDRESS2 + " text, " + LOCATION_NAME + " text, " + DELIVERY_INSTRUCTIONS
            + " text, " + ZIP_CODE + " text, " + IS_PRMARY_LOCATION + " text, " + CITY
            + " text, " + STATE
            + " text, " + COUNTRY + " text, " + APT_SUIT_UNIT + " text, " + TAX_RATE + " text " + ")";

    private static final String CREATE_CARD_TABLE = "create table "
            + TABLE_CARD + "(" + PAYMENT_GATEWAY_CUSTOMER_ID + " text, " + CC_NUMBER + " text, " + CARD_ISSUER
            + " text, " + EXPIRATION_DATE + " text, " + LAST_4_DIGITS + " text, " + TOKEN
            + " text, " + GIVEN_CARD_NAME + " text, " + IS_PRIMARY + " text, " + USER_PEYMENT_METHOD_ID + " text " + ")";

    private static final String CREATE_ORDER_TABLE = "create table "
            + TABLE_ORDER + "(" + SUPPLIER_ITEM_ID + " text,"
            + ITEM_NAME + " text, " + BRAND_NAME + " text, " + SIZE
            + " text, " + UOM + " text, " + SHIPPING_WEIGHT + " text, " + ORDER_QUANTITY
            + " text, " + UNIT_PRICE + " text, " + FINAL_PRICE + " text, " + APPLICABLE_SALES_DESCRIPTION
            + " text, " + SUPPLIER_ID
            + " text, " + THUMBNAIL
            + " text, " + SUBSTITUTION
            + " text, " + IS_TAXABLE
            + " text, " + REGULAR_PRICE
            + " text, " + TAX_AMOUNT
            + " text, " + MAX_QUANTITY
            + " text " + ")";

    private static final String CREATE_SUPPLIER_TABLE = "create table "
            + TABLE_SUPPLIER + "(" + SUPPLIER_ID + " text," + SUPPLIER_NAME + " text," + SUPPLIER_AVAILABLE + " text" + ")";

    private static final String CREATE_LIKE_TABLE = "create table "
            + TABLE_LIKE + "(" + SUPPLIER_ITEM_ID + " text" + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SIGNUP_ERROR_TABLE);
        sqLiteDatabase.execSQL(CREATE_LOGIN_ERROR_TABLE);
        sqLiteDatabase.execSQL(CREATE_ADDRESS_ERROR_TABLE);
        sqLiteDatabase.execSQL(CREATE_CARD_ERROR_TABLE);
        sqLiteDatabase.execSQL(CREATE_CHANGE_PASSWORD_ERROR_TABLE);
        sqLiteDatabase.execSQL(CREATE_SUPPORT_ERROR_TABLE);
        sqLiteDatabase.execSQL(CREATE_LOCATION_TABLE);
        sqLiteDatabase.execSQL(CREATE_CARD_TABLE);
        sqLiteDatabase.execSQL(CREATE_ORDER_TABLE);
        sqLiteDatabase.execSQL(CREATE_SUPPLIER_TABLE);
        sqLiteDatabase.execSQL(CREATE_LIKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNUP_ERROR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_ERROR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS_ERROR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD_ERROR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANGE_PASSWORD_ERROR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPORT_ERROR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);

        onCreate(db);

    }

    public void addLikeItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues supplierValues = new ContentValues();
        supplierValues.put(SUPPLIER_ITEM_ID, id);
        db.insert(TABLE_LIKE, null, supplierValues);
        db.close();
    }


    public List<String> getAllLikeItems() {
        List<String> stringArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LIKE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String str = cursor.getString(0);
                stringArrayList.add(str);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return stringArrayList;
    }

    public String getLikeItem(String supplierItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_LIKE, new String[]{SUPPLIER_ITEM_ID}, SUPPLIER_ITEM_ID + "=?",

                new String[]{supplierItemId}, null, null, null, null);
        String str = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                str = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();
        return str;
    }

    public void deleteLikeItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIKE, SUPPLIER_ITEM_ID + "=? ",
                new String[]{id});
        db.close();
    }

    public void deleteAllLike() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_LIKE);
        db.close();
    }

    //Order Table Operations
    public void addOrderInCart(OrderBean orderBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        orderValues.put(SUPPLIER_ITEM_ID, orderBean.getSupplierItemId());
        orderValues.put(ITEM_NAME, orderBean.getItemName());
        orderValues.put(BRAND_NAME, orderBean.getBrandName());
        orderValues.put(SIZE, orderBean.getSize());
        orderValues.put(UOM, orderBean.getUom());
        orderValues.put(SHIPPING_WEIGHT, orderBean.getShippingWeight());
        orderValues.put(ORDER_QUANTITY, orderBean.getOrderQuantity());
        orderValues.put(UNIT_PRICE, orderBean.getUnitPrice());
        orderValues.put(FINAL_PRICE, orderBean.getFinalPrice());
        orderValues.put(APPLICABLE_SALES_DESCRIPTION, orderBean.getApplicableSaleDescription());
        orderValues.put(SUPPLIER_ID, orderBean.getSupplierId());
        orderValues.put(THUMBNAIL, orderBean.getThumbnail());
        orderValues.put(SUBSTITUTION, orderBean.getSubstitutionWith());
        orderValues.put(IS_TAXABLE, orderBean.getIsTaxable());
        orderValues.put(REGULAR_PRICE, orderBean.getRegularPrice());
        orderValues.put(TAX_AMOUNT, orderBean.getTaxAmount());
        orderValues.put(MAX_QUANTITY,orderBean.getMaxQuantity());
        db.insert(TABLE_ORDER, null, orderValues);
        db.close();
    }

    public OrderBean getOrder(String supplierItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_ORDER, new String[]{SUPPLIER_ITEM_ID,
                        ITEM_NAME, BRAND_NAME, SIZE, UOM, SHIPPING_WEIGHT, ORDER_QUANTITY, UNIT_PRICE, FINAL_PRICE, APPLICABLE_SALES_DESCRIPTION, SUPPLIER_ID, THUMBNAIL, SUBSTITUTION, IS_TAXABLE, REGULAR_PRICE, TAX_AMOUNT,MAX_QUANTITY}, SUPPLIER_ITEM_ID + "=?",

                new String[]{supplierItemId}, null, null, null, null);
        OrderBean orderBean = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                orderBean = new OrderBean();
                orderBean.setSupplierItemId(cursor.getString(0));
                orderBean.setItemName(cursor.getString(1));
                orderBean.setBrandName(cursor.getString(2));
                orderBean.setSize(cursor.getString(3));
                orderBean.setUom(cursor.getString(4));
                orderBean.setShippingWeight(cursor.getString(5));
                orderBean.setOrderQuantity(cursor.getString(6));
                orderBean.setUnitPrice(cursor.getString(7));
                orderBean.setFinalPrice(cursor.getString(8));
                orderBean.setApplicableSaleDescription(cursor.getString(9));
                orderBean.setSupplierId(cursor.getString(10));
                orderBean.setThumbnail(cursor.getString(11));
                orderBean.setSubstitutionWith(cursor.getString(12));
                orderBean.setIsTaxable(cursor.getString(13));
                orderBean.setRegularPrice(cursor.getString(14));
                orderBean.setTaxAmount(cursor.getDouble(15));
                orderBean.setMaxQuantity(cursor.getString(16));
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return orderBean;
    }

    public int getOrdersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ORDER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }


    public List<OrderBean> getAllOrders() {
        List<OrderBean> orderBeanList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                OrderBean order = new OrderBean();
                order.setSupplierItemId(cursor.getString(0));
                order.setItemName(cursor.getString(1));
                order.setBrandName(cursor.getString(2));
                order.setSize(cursor.getString(3));
                order.setUom(cursor.getString(4));
                order.setShippingWeight(cursor.getString(5));
                order.setOrderQuantity(cursor.getString(6));
                order.setUnitPrice(cursor.getString(7));
                order.setFinalPrice(cursor.getString(8));
                order.setApplicableSaleDescription(cursor.getString(9));
                order.setSupplierId(cursor.getString(10));
                order.setThumbnail(cursor.getString(11));
                order.setSubstitutionWith(cursor.getString(12));
                order.setIsTaxable(cursor.getString(13));
                order.setRegularPrice(cursor.getString(14));
                order.setTaxAmount(cursor.getDouble(15));
                order.setMaxQuantity(cursor.getString(16));
                orderBeanList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderBeanList;
    }

    public List<OrderBean> getAllOrders(SuppliersBean suppliersBean) {
        List<OrderBean> orderBeanList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_ORDER, new String[]{SUPPLIER_ITEM_ID,
                        ITEM_NAME, BRAND_NAME, SIZE, UOM, SHIPPING_WEIGHT, ORDER_QUANTITY, UNIT_PRICE, FINAL_PRICE, APPLICABLE_SALES_DESCRIPTION, SUPPLIER_ID, THUMBNAIL, SUBSTITUTION, IS_TAXABLE, REGULAR_PRICE, TAX_AMOUNT,MAX_QUANTITY}, SUPPLIER_ID + "=?",
                new String[]{suppliersBean.getSupplierId()}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                OrderBean order = new OrderBean();
                order.setSupplierItemId(cursor.getString(0));
                order.setItemName(cursor.getString(1));
                order.setBrandName(cursor.getString(2));
                order.setSize(cursor.getString(3));
                order.setUom(cursor.getString(4));
                order.setShippingWeight(cursor.getString(5));
                order.setOrderQuantity(cursor.getString(6));
                order.setUnitPrice(cursor.getString(7));
                order.setFinalPrice(cursor.getString(8));
                order.setApplicableSaleDescription(cursor.getString(9));
                order.setSupplierId(cursor.getString(10));
                order.setThumbnail(cursor.getString(11));
                order.setSubstitutionWith(cursor.getString(12));
                order.setIsTaxable(cursor.getString(13));
                order.setRegularPrice(cursor.getString(14));
                order.setTaxAmount(cursor.getDouble(15));
                order.setMaxQuantity(cursor.getString(16));
                orderBeanList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return orderBeanList;
    }

    public void deleteOrder(OrderBean orderBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER, SUPPLIER_ITEM_ID + "=? ",
                new String[]{orderBean.getSupplierItemId()});
        db.close();
    }

    public void updateOrder(OrderBean orderBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDER_QUANTITY, orderBean.getOrderQuantity());
        values.put(FINAL_PRICE, orderBean.getFinalPrice());
        values.put(SUBSTITUTION, orderBean.getSubstitutionWith());
        values.put(TAX_AMOUNT, orderBean.getTaxAmount());

        // updating row
        db.update(TABLE_ORDER, values, SUPPLIER_ITEM_ID + "=?",
                new String[]{orderBean.getSupplierItemId()});
        db.close();
    }
//End Order Table Operations


    //Supplier table operations
    public void addSupplier(SuppliersBean supplierBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues supplierValues = new ContentValues();
        supplierValues.put(SUPPLIER_ID, supplierBean.getSupplierId());
        supplierValues.put(SUPPLIER_NAME, supplierBean.getSupplierName());
        supplierValues.put(SUPPLIER_AVAILABLE, "yes");
        db.insert(TABLE_SUPPLIER, null, supplierValues);
        db.close();
    }

    public SuppliersBean getSupplier(String supplierId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SUPPLIER, new String[]{SUPPLIER_ID,
                        SUPPLIER_NAME, SUPPLIER_AVAILABLE}, SUPPLIER_ID + "=?",
                new String[]{supplierId}, null, null, null, null);
        SuppliersBean suppliersBean = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                suppliersBean = new SuppliersBean();
                suppliersBean.setSupplierId(cursor.getString(0));
                suppliersBean.setSupplierName(cursor.getString(1));
                suppliersBean.setSupplierAvailable(cursor.getString(2));
            }
            cursor.close();
        }

        db.close();
        return suppliersBean;
    }

    public List<SuppliersBean> getAllSuppliers() {
        List<SuppliersBean> suppliersBeanList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SUPPLIER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SuppliersBean suppliersBean = new SuppliersBean();
                suppliersBean.setSupplierId(cursor.getString(0));
                suppliersBean.setSupplierName(cursor.getString(1));
                suppliersBean.setSupplierAvailable(cursor.getString(2));
                suppliersBeanList.add(suppliersBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return suppliersBeanList;
    }

    public void updateSupplier(SuppliersBean suppliersBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUPPLIER_AVAILABLE, suppliersBean.getSupplierAvailable());

        // updating row
        db.update(TABLE_SUPPLIER, values, SUPPLIER_ID + "=?",
                new String[]{suppliersBean.getSupplierId()});
        db.close();
    }

    public void deleteSupplier(SuppliersBean supplierBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUPPLIER, SUPPLIER_ID + "=? ",
                new String[]{supplierBean.getSupplierId()});
        db.close();
    }

    public int getSuppliersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SUPPLIER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    //End of Supplier table operations


    public void addErrors(ValidationError error) {
        deleteAllErrors();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues signupValues = new ContentValues();
        SignupError signupError = error.getSignupError();
        LoginError loginError = error.getLoginError();
        AddressError addressError = error.getAddressError();
        ChangePasswordError changePasswordError = error.getChangePasswordError();
        CardError cardError = error.getCardError();
        SupportError supportError = error.getSupportError();


        signupValues.put(PROFILE_NAME_REQUIRED, signupError.getNameRequired());
        signupValues.put(PROFILE_NAME_LENGTH, signupError.getNameLength());
        signupValues.put(PROFILE_EMAIL_REQUIRED, signupError.getEmailRequired());
        signupValues.put(PROFILE_EMAIL_INVALID, signupError.getEmailInvalid());
        signupValues.put(PROFILE_PASSWORD_REQUIRED, signupError.getPasswordRequired());
        signupValues.put(PROFILE_PASSWORD_INVALID, signupError.getPasswordInvalid());
        signupValues.put(PROFILE_PASSWORD_LENGTH, signupError.getPasswordLength());
        signupValues.put(PROFILE_PHONE_REQUIRED, signupError.getPhoneRequired());
        signupValues.put(PROFILE_PHONE_INVALID, signupError.getPhoneInvalid());
        signupValues.put(PROFILE_ZIP_REQUIRED, signupError.getZipcodeRequired());
        signupValues.put(PROFILE_ZIP_INVALID, signupError.getZipcodeInvalid());
        signupValues.put(PROFILE_ZIP_LENGTH, signupError.getZipcodeLength());
        signupValues.put(PROFILE_LAST_NAME_REQUIRED, signupError.getLastNameRequired());
        signupValues.put(PROFILE_LAST_NAME_LENGTH, signupError.getLastNameLength());

        ContentValues loginValues = new ContentValues();
        loginValues.put(LOGIN_PASSWORD_REQUIRED, loginError.getPasswordRequired());
        loginValues.put(LOGIN_EMAIL_REQUIRED, loginError.getEmailRequired());
        loginValues.put(LOGIN_EMAIL_INVALID, loginError.getEmailInvalid());


        ContentValues addressValues = new ContentValues();
        addressValues.put(NO_RESULT, addressError.getNoResult());
        addressValues.put(STREET_REQUIRED, addressError.getStreetRequired());
        addressValues.put(STREET_LENGTH, addressError.getStreetLength());
        addressValues.put(APT_REQUIRED, addressError.getAptRequired());
        addressValues.put(APT_LENGTH, addressError.getAptLength());
        addressValues.put(CITY_REQUIRED, addressError.getCityRequired());
        addressValues.put(CITY_LENGTH, addressError.getCityLength());
        addressValues.put(STATE_REQUIRED, addressError.getStateRequired());
        addressValues.put(STATE_LENGTH, addressError.getStateLength());
        addressValues.put(ADDRESS_ZIP_REQUIRED, addressError.getZipcodeRequired());
        addressValues.put(ADDRESS_ZIP_INVALID, addressError.getZipcodeInvalid());
        addressValues.put(ADDRESS_ZIP_LENGTH, addressError.getZipcodeLength());


        ContentValues cardValues = new ContentValues();
        cardValues.put(CARD_NUMBER_REQUIRED, cardError.getNumberRequired());
        cardValues.put(CARD_NUMBER_LENGTH, cardError.getNumberLength());
        cardValues.put(CVV_REQUIRED, cardError.getCvvRequired());
        cardValues.put(CVV_LENGTH, cardError.getCvvLength());
        cardValues.put(MONTH_REQUIRED, cardError.getMonthRequired());
        cardValues.put(MONTH_LENGTH, cardError.getMonthLength());
        cardValues.put(YEAR_REQUIRED, cardError.getYearRequired());
        cardValues.put(YEAR_LENGTH, cardError.getYearLength());
        cardValues.put(CARD_ZIP_REQUIRED, cardError.getZipcodeRequired());
        cardValues.put(CARD_ZIP_LENGTH, cardError.getZipcodeLength());


        ContentValues changePasswordValues = new ContentValues();
        changePasswordValues.put(CURRENT_PASSWORD_REQUIRED, changePasswordError.getCurrentPasswordRequired());
        changePasswordValues.put(NEW_PASSWORD_REQUIRED, changePasswordError.getNewPasswordrequired());
        changePasswordValues.put(SAME_PASSWORD, changePasswordError.getSame());
        changePasswordValues.put(PASSWORD_LENGTH, changePasswordError.getPasswordLength());


        ContentValues supportValues = new ContentValues();
        supportValues.put(COMMENT_REQUIRED, supportError.getCommentRequired());


        db.insert(TABLE_SIGNUP_ERROR, null, signupValues);
        db.insert(TABLE_LOGIN_ERROR, null, loginValues);
        db.insert(TABLE_ADDRESS_ERROR, null, addressValues);
        db.insert(TABLE_CARD_ERROR, null, cardValues);
        db.insert(TABLE_CHANGE_PASSWORD_ERROR, null, changePasswordValues);
        db.insert(TABLE_SUPPORT_ERROR, null, supportValues);
        db.close();
    }

    private void deleteAllErrors() {

        deleteSignupError();
        deleteLoginError();
        deleteAddressError();
        deleteCardnError();
        deleteChangePasswordError();
        deleteSupportError();
    }

    public SignupError getSignupError() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SIGNUP_ERROR;
        Cursor cursor = db.rawQuery(selectQuery, null);
        SignupError signupError = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                signupError = new SignupError();
                signupError.setNameRequired(cursor.getString(0));
                signupError.setNameLength(cursor.getString(1));
                signupError.setEmailRequired(cursor.getString(2));
                signupError.setEmailInvalid(cursor.getString(3));
                signupError.setPasswordRequired(cursor.getString(4));
                signupError.setPasswordInvalid(cursor.getString(5));
                signupError.setPasswordLength(cursor.getString(6));
                signupError.setPhoneRequired(cursor.getString(7));
                signupError.setPhoneInvalid(cursor.getString(8));
                signupError.setZipcodeRequired(cursor.getString(9));
                signupError.setZipcodeInvalid(cursor.getString(10));
                signupError.setZipcodeLength(cursor.getString(11));
                signupError.setLastNameRequired(cursor.getString(12));
                signupError.setLastNameLength(cursor.getString(13));
            }
            cursor.close();
        }

        db.close();

        return signupError;

    }

    public LoginError getLoginError() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN_ERROR;
        Cursor cursor = db.rawQuery(selectQuery, null);
        LoginError loginError = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                loginError = new LoginError();
                loginError.setPasswordRequired(cursor.getString(0));
                loginError.setEmailRequired(cursor.getString(1));
                loginError.setEmailInvalid(cursor.getString(2));
            }
            cursor.close();
        }

        db.close();

        return loginError;
    }

    public AddressError getAddressError() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS_ERROR;
        Cursor cursor = db.rawQuery(selectQuery, null);
        AddressError addressError = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                addressError = new AddressError();
                addressError.setNoResult(cursor.getString(0));
                addressError.setStreetRequired(cursor.getString(1));
                addressError.setStreetLength(cursor.getString(2));
                addressError.setAptRequired(cursor.getString(3));
                addressError.setAptLength(cursor.getString(4));
                addressError.setCityRequired(cursor.getString(5));
                addressError.setCityLength(cursor.getString(6));
                addressError.setStateRequired(cursor.getString(7));
                addressError.setStateLength(cursor.getString(8));
                addressError.setZipcodeRequired(cursor.getString(9));
                addressError.setZipcodeInvalid(cursor.getString(10));
                addressError.setZipcodeLength(cursor.getString(11));
            }
            cursor.close();
        }

        db.close();

        return addressError;
    }

    public CardError getCardError() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CARD_ERROR;
        Cursor cursor = db.rawQuery(selectQuery, null);
        CardError cardError = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cardError = new CardError();
                cardError.setNumberRequired(cursor.getString(0));
                cardError.setNumberLength(cursor.getString(1));
                cardError.setCvvRequired(cursor.getString(2));
                cardError.setCvvLength(cursor.getString(3));
                cardError.setMonthRequired(cursor.getString(4));
                cardError.setMonthLength(cursor.getString(5));
                cardError.setYearRequired(cursor.getString(6));
                cardError.setYearLength(cursor.getString(7));
                cardError.setZipcodeRequired(cursor.getString(8));
                cardError.setZipcodeLength(cursor.getString(9));
            }
            cursor.close();
        }

        db.close();

        return cardError;
    }

    public ChangePasswordError getChangePasswordError() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CHANGE_PASSWORD_ERROR;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ChangePasswordError changePasswordError = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                changePasswordError = new ChangePasswordError();
                changePasswordError.setCurrentPasswordRequired(cursor.getString(0));
                changePasswordError.setNewPasswordrequired(cursor.getString(1));
                changePasswordError.setSame(cursor.getString(2));
                changePasswordError.setPasswordLength(cursor.getString(3));
            }
            cursor.close();
        }

        db.close();

        return changePasswordError;
    }

    public SupportError getSupportError() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CHANGE_PASSWORD_ERROR;
        Cursor cursor = db.rawQuery(selectQuery, null);
        SupportError supportError = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                supportError = new SupportError();
                supportError.setCommentRequired(cursor.getString(0));
            }
            cursor.close();
        }

        db.close();

        return supportError;
    }

    private void deleteSignupError() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SIGNUP_ERROR);
        db.close();
    }

    private void deleteLoginError() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_LOGIN_ERROR);
        db.close();
    }

    private void deleteAddressError() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_ADDRESS_ERROR);
        db.close();
    }

    private void deleteCardnError() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CARD_ERROR);
        db.close();
    }

    private void deleteChangePasswordError() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CHANGE_PASSWORD_ERROR);
        db.close();
    }

    private void deleteSupportError() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SUPPORT_ERROR);
        db.close();
    }


    public void addDeliveryAddress(List<LocationBean> deliveryApiAddressBeanList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (LocationBean addressBean : deliveryApiAddressBeanList) {
            ContentValues values = new ContentValues();
            values.put(USER_DELIVERY_LOCATION_ID, addressBean.getUserDeliveryLocationId());
            values.put(USER_ID, addressBean.getUserId());
            values.put(LOCATION_TYPE, addressBean.getLocationType());
            values.put(ADDRESS1, addressBean.getAddress1());
            values.put(ADDRESS2, addressBean.getAddress2());
            values.put(LOCATION_NAME, addressBean.getLocationName());
            values.put(DELIVERY_INSTRUCTIONS, addressBean.getDeliveryInstructions());
            values.put(ZIP_CODE, addressBean.getZipcode());
            values.put(IS_PRMARY_LOCATION, addressBean.getIsPrimaryLocation());
            values.put(CITY, addressBean.getCity());
            values.put(STATE, addressBean.getState());
            values.put(COUNTRY, addressBean.getCountry());
            values.put(APT_SUIT_UNIT, addressBean.getAptSuitUnit());
            values.put(TAX_RATE, addressBean.getTaxRate());

            db.insert(TABLE_LOCATION, null, values);
        }

        db.close();

    }

    public List<LocationBean> getAllDeliveryaddress() {
        List<LocationBean> deliveryApiAddressBeanList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LocationBean addressBean = new LocationBean();
                addressBean.setUserDeliveryLocationId(cursor.getString(0));
                addressBean.setUserId(cursor.getString(1));
                addressBean.setLocationType(cursor.getString(2));
                addressBean.setAddress1(cursor.getString(3));
                addressBean.setAddress2(cursor.getString(4));
                addressBean.setLocationName(cursor.getString(5));
                addressBean.setDeliveryInstructions(cursor.getString(6));
                addressBean.setZipcode(cursor.getString(7));
                addressBean.setIsPrimaryLocation(cursor.getString(8));
                addressBean.setCity(cursor.getString(9));
                addressBean.setState(cursor.getString(10));
                addressBean.setCountry(cursor.getString(11));
                addressBean.setAptSuitUnit(cursor.getString(12));
                addressBean.setTaxRate(cursor.getString(13));
                deliveryApiAddressBeanList.add(addressBean);


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return deliveryApiAddressBeanList;
    }

    public int getDeliveryAddressCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        db.close();
        return count;
    }

    public void deleteDeliveryAddress() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_LOCATION);
        db.close();
    }

    public void updateDeliveryAddress(LocationBean addressBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_DELIVERY_LOCATION_ID, addressBean.getUserDeliveryLocationId());
        values.put(USER_ID, addressBean.getUserId());
        values.put(LOCATION_TYPE, addressBean.getLocationType());
        values.put(ADDRESS1, addressBean.getAddress1());
        values.put(ADDRESS2, addressBean.getAddress2());
        values.put(LOCATION_NAME, addressBean.getLocationName());
        values.put(DELIVERY_INSTRUCTIONS, addressBean.getDeliveryInstructions());
        values.put(ZIP_CODE, addressBean.getZipcode());
        values.put(IS_PRMARY_LOCATION, addressBean.getIsPrimaryLocation());
        values.put(CITY, addressBean.getCity());
        values.put(STATE, addressBean.getState());
        values.put(COUNTRY, addressBean.getCountry());
        values.put(APT_SUIT_UNIT, addressBean.getAptSuitUnit());

        // updating row
        db.update(TABLE_LOCATION, values, USER_DELIVERY_LOCATION_ID + "=?",
                new String[]{addressBean.getUserDeliveryLocationId()});
        db.close();


    }


    public void addAllPaymentCards(List<PaymentCardBean> paymentCardApiBeanList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (PaymentCardBean card : paymentCardApiBeanList) {
            ContentValues values = new ContentValues();
            values.put(PAYMENT_GATEWAY_CUSTOMER_ID, card.getPaymentGatewayCustomerId());
            values.put(CC_NUMBER, card.getCardCvvNumber());
            values.put(CARD_ISSUER, card.getCardIssuer());
            values.put(EXPIRATION_DATE, card.getCardExpiratoinDate());
            values.put(LAST_4_DIGITS, card.getCardLastFourDigit());
            values.put(TOKEN, card.getToken());
            values.put(GIVEN_CARD_NAME, card.getCardGivenName());
            values.put(IS_PRIMARY, card.getIsPrimary());
            values.put(USER_PEYMENT_METHOD_ID, card.getUserPaymentMethodId());

            db.insert(TABLE_CARD, null, values);
        }
        db.close();
    }

    public List<PaymentCardBean> getAllPaymentCards() {
        List<PaymentCardBean> paymentCardApiBeanList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CARD;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PaymentCardBean card = new PaymentCardBean();
                card.setPaymentGatewayCustomerId(cursor.getString(0));
                card.setCardCvvNumber(cursor.getString(1));
                card.setCardIssuer(cursor.getString(2));
                card.setCardExpiratoinDate(cursor.getString(3));
                card.setCardLastFourDigit(cursor.getString(4));
                card.setToken(cursor.getString(5));
                card.setCardGivenName(cursor.getString(6));
                card.setIsPrimary(cursor.getString(7));
                card.setUserPaymentMethodId(cursor.getString(8));

                paymentCardApiBeanList.add(card);


            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return paymentCardApiBeanList;
    }

    public int getPaymentCardsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CARD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        db.close();
        return count;
    }

    public void deletePaymentCards() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CARD);
        db.close();
    }

    public void deleteAllOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_ORDER);
        db.close();
    }

    public void deleteAllSuppliers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SUPPLIER);
        db.close();
    }
}

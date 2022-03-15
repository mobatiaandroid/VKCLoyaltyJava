/**
 *
 */
package com.vkc.loyaltyapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;

/**
 * @author mobatia-user
 */

/*
 * Bibin Comment 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
 */
public class AppPrefenceManager {

    public static final String PREFS_NAME = "VKC";
    public static final String PREFS_KEY_TYPE = "type";
    public static final String PREFS_KEY_OFFER = "offer";
    public static final String PREFS_KEY_SIZE = "size";
    public static final String PREFS_KEY_COLOR = "color";
    public static final String PREFS_KEY_PRICE = "price_range";
    public static final String PREFS_REG_ID = "Regid";
    public static final String PREFS_FILTER_DATACATEGORY = "category";
    public static final String PREFS_FILTER_DATASIZE = "sizefilter";
    public static final String PREFS_FILTER_DATAOFFER = "offerfilter";
    public static final String PREFS_FILTER_DATABRAND = "brandfilter";
    public static final String PREFS_FILTER_DATAPRICE = "pricefilter";
    public static final String PREFS_FILTER_DATACOLOR = "colorfilter";
    public static final String PREFS_BANNE_RESPONSE = "BannerResponse";

    public static final String PREFS_TOP_SLIDER = "top_slider";
    public static final String PREFS_BOTTOM_SLIDER = "bottom_slider";

    public static final String IDS_FOR_OFFER = "idsforoffer";
    public static final String OFFER_IDS = "offer_ids";

    public static final String LISTING_OPTION = "lising_option";

    public static final String BRAND_BANNER = "BrandBanner";

    public static final String LIST_TYPE = "ListType";

    public static final String BRAND_ID_FOR_SEARCH = "brand_id_for_search";

    public static final String MAIN_CATEGORY = "MAIN_CATEGORY";
    public static final String SUB_CATEGORY_ID = "SUB_CATEGORY_ID";

    public static final String PRODUCT_LIST_SORTOPTION = "PRODUCT_LIST_SORTOPTION";

    public static final String USERTYPE = "usertype";

    public static final String USERID = "userid";

    public static final String USERNAME = "username";

    public static boolean getAgreeTerms(Context context) {
        SharedPreferences settings;
        boolean text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getBoolean("terms", false);
        return text;
    }

    public static void saveAgreeTerms(Context context, boolean text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean("terms", text);
        editor.commit();
    }

    public static String getProductListSortOption(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString(PRODUCT_LIST_SORTOPTION, "0");
        return text;
    }

    public static void saveProductListSortOption(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PRODUCT_LIST_SORTOPTION, text.toString());
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String text = settings.getString("token", "");
        return text;
    }

    public static void saveToken(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("token", text);
        editor.commit();
    }

    public static String getSubCategoryId(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString(SUB_CATEGORY_ID, "");
        return text;
    }


    public static void saveSubCategoryId(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(SUB_CATEGORY_ID, text.toString());
        editor.commit();
    }

    public static String getMainCategory(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString(MAIN_CATEGORY, "");
        return text;
    }

    public static String getDealerListCount(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("dealer_count", "");
        return text;
    }

    public static void saveDealerListCount(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("dealer_count", text.toString());
        editor.commit();
    }

    public static void saveMainCategory(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(MAIN_CATEGORY, text.toString());
        editor.commit();
    }

    // From Filter :2
    // From Offer Banner:1
    // From Home menu :0
    // For Search : 3
    // For Brand : 4
    public static void saveListingOption(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(LISTING_OPTION, text); // 3
        editor.commit(); // 4
    }

    public static String getListingOption(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(LISTING_OPTION, null); // 2
        return text;
    }

    // Brand Id for listing
    public static void saveBrandIdForSearch(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(BRAND_ID_FOR_SEARCH, text); // 3
        editor.commit(); // 4
    }

    public static String getBrandIdForSearch(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(BRAND_ID_FOR_SEARCH, null); // 2
        return text;
    }

    //

    public static void saveOfferIDs(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(OFFER_IDS, text); // 3
        editor.commit(); // 4
    }

    public static String getOfferIDs(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(OFFER_IDS, ""); // 2
        return text;
    }

    public static void saveIDsForOffer(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(IDS_FOR_OFFER, text); // 3
        editor.commit(); // 4
    }

    public static String getIDsForOffer(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(IDS_FOR_OFFER, null); // 2
        return text;
    }

    public static void saveJsonOfferResponse(Context context, JSONArray text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_KEY_OFFER, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getJsonOfferResponse(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_KEY_OFFER, null); // 2
        return text;
    }

    public static void saveJsonTypeResponse(Context context, JSONArray text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_KEY_TYPE, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getJsonTypeResponse(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_KEY_TYPE, null); // 2
        return text;
    }

    public static void saveJsonSizeResponse(Context context, JSONArray text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_KEY_SIZE, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getJsonSizeResponse(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_KEY_SIZE, null); // 2
        return text;
    }

    public static void saveJsonColorResponse(Context context, JSONArray text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_KEY_COLOR, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getJsonColorResponse(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_KEY_COLOR, null); // 2
        return text;
    }

    public static void saveJsonPriceResponse(Context context, JSONArray text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_KEY_PRICE, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getJsonPriceResponse(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_KEY_PRICE, null); // 2
        return text;
    }

    /**
     * Save regid.
     *
     * @param Regid    the regid
     * @param mContext the m context
     */
    public static void saveRegid(String Regid, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();

        editor.putString(PREFS_REG_ID, Regid);
        editor.commit();
    }

    /**
     * Gets the regid.
     *
     * @param mContext the m context
     * @return the regid
     */
    public static String getRegid(Context mContext) {
        String value = "";
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        value = prefs.getString(PREFS_REG_ID, "");
        return value;
    }

    public static String getFilterDataCategory(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_FILTER_DATACATEGORY, ""); // 2
        return text;
    }

    public static void saveFilterDataCategory(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_FILTER_DATACATEGORY, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getFilterDataSize(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_FILTER_DATASIZE, ""); // 2
        return text;
    }

    public static void saveFilterDataSize(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_FILTER_DATASIZE, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getFilterDataOffer(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_FILTER_DATAOFFER, ""); // 2
        return text;
    }

    public static void saveFilterDataOffer(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_FILTER_DATAOFFER, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getFilterDataBrand(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_FILTER_DATABRAND, ""); // 2
        return text;
    }

    public static void saveFilterDataBrand(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_FILTER_DATABRAND, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getFilterDataPrice(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_FILTER_DATAPRICE, ""); // 2
        return text;
    }

    public static void saveFilterDataPrice(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_FILTER_DATAPRICE, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getFilterDataColor(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_FILTER_DATACOLOR, ""); // 2
        return text;
    }

    public static void saveFilterDataColor(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_FILTER_DATACOLOR, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getNewArrivalBannerResponse(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_BANNE_RESPONSE, ""); // 2
        return text;
    }

    public static void saveNewArrivalBannerResponse(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_BANNE_RESPONSE, text.toString()); // 3
        editor.commit(); // 4
    }

    // public static String getTopSliderResponse(Context context) {
    // SharedPreferences settings;
    // String text;
    // settings = context.getSharedPreferences(PREFS_NAME,
    // Context.MODE_PRIVATE); // 1
    // text = settings.getString(PREFS_TOP_SLIDER, ""); // 2
    // return text;
    // }
    //
    // public static void saveTopSliderResponse(Context context, String text) {
    // SharedPreferences settings;
    // Editor editor;
    // settings = context.getSharedPreferences(PREFS_NAME,
    // Context.MODE_PRIVATE); // 1
    // editor = settings.edit(); // 2
    //
    // editor.putString(PREFS_TOP_SLIDER, text.toString()); // 3
    // editor.commit(); // 4
    // }

    public static String getPopularProductSliderResponse(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(PREFS_BOTTOM_SLIDER, ""); // 2
        return text;
    }

    public static void savePopularProductSliderResponse(Context context,
                                                        String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(PREFS_BOTTOM_SLIDER, text.toString()); // 3
        editor.commit(); // 4
    }

    // //////
    public static String getBrandBannerResponse(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(BRAND_BANNER, ""); // 2
        return text;
    }

    public static void saveBrandBannerResponse(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(BRAND_BANNER, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getListType(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        text = settings.getString(LIST_TYPE, ""); // 2
        return text;
    }

    public static void saveListType(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE); // 1
        editor = settings.edit(); // 2

        editor.putString(LIST_TYPE, text.toString()); // 3
        editor.commit(); // 4
    }

    public static String getUserType(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString(USERTYPE, "");
        return text;
    }

    public static void saveUserType(Context context, String text) { // 1-SalesPerson,2-Dealer,3-Retailer
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(USERTYPE, text.toString());
        editor.commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString(USERID, "");
        return text;
    }

    public static void saveUserId(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(USERID, text.toString());
        editor.commit();
    }

    public static String getCustomerId(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("custId", "");
        return text;
    }

    public static void saveCustomerId(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("custId", text.toString());
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString(USERNAME, "");
        return text;
    }

    public static void saveUserName(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(USERNAME, text.toString());
        editor.commit();
    }

    public static String getStateCode(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("statecode", "");
        return text;
    }

    public static void saveStateCode(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("statecode", text.toString());
        editor.commit();
    }

    public static String getName(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("name", "");
        return text;
    }

    public static void saveName(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("name", text.toString());
        editor.commit();
    }

    public static String getLoginStatusFlag(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("loginstatus", "no");
        return text;
    }

    public static void saveLoginStatusFlag(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("loginstatus", text.toString());
        editor.commit();
    }

    public static String getLoginCustomerName(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("customer_name", "");
        return text;
    }

    public static void setLoginCustomerName(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("customer_name", text.toString());
        editor.commit();
    }

    public static String getLoginPlace(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("log_place", "");
        return text;
    }

    public static void setLoginPlace(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("log_place", text.toString());
        editor.commit();
    }

    public static int getDealerCount(Context context) {
        SharedPreferences settings;
        int text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getInt("dealer_count", 0);
        return text;
    }

    public static void saveDealerCount(Context context, int text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putInt("dealer_count", text);
        editor.commit();
    }

    public static String getIsCredit(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("is_credit", "");
        return text;
    }

    public static void saveIsCredit(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("is_credit", text.toString());
        editor.commit();
    }

    public static String getCustomerCategory(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("cust_cat", "");
        return text;
    }

    public static void saveCustomerCategory(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("cust_cat", text.toString());
        editor.commit();
    }

    public static String getSelectedUserName(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("sel_user", "");
        return text;
    }

    public static void saveSelectedUserName(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("sel_user", text.toString());
        editor.commit();
    }


    public static String getSelectedUserId(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("sel_user_id", "");
        return text;
    }

    public static void saveSelectedUserId(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("sel_user_id", text.toString());
        editor.commit();
    }

    public static String getFillTable(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("set_fill_table", "");
        return text;
    }

    public static void setFillTable(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("set_fill_table", text.toString());
        editor.commit();
    }

    public static boolean getIsCallPendingAPI(Context context) {
        SharedPreferences settings;
        boolean text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getBoolean("call_pending", true);
        return text;
    }

    public static void setIsCallPendingAPI(Context context, boolean text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean("call_pending", text);
        editor.commit();
    }

    public static String getDate(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("current_date", "");
        return text;
    }

    public static void setDate(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("current_date", text);
        editor.commit();
    }

    public static String getIsVerifiedOTP(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("otp_status", "no");
        return text;
    }

    public static void setIsVerifiedOTP(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("otp_status", text);
        editor.commit();
    }

    public static String getMobile(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = settings.getString("mobile", "0");
        return text;
    }

    public static void setMobile(Context context, String text) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("mobile", text);
        editor.commit();
    }

}

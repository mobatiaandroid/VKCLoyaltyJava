package com.vkc.loyaltyapp.constants;

public interface VKCUrlConstants {

    // Dev URL
    //public String BASE_URL = "http://dev.mobatia.com/vkc2.5/";
//    public String BASE_URL = "http://ec2-18-213-204-92.compute-1.amazonaws.com/vkc/";
    // Live URL
    public String BASE_URL = "https://mobile.walkaroo.in/vkc/";
    public String GET_NEWS = BASE_URL + "apiv4/news";
    public String GET_NEWS_DETAIL = BASE_URL + "apiv4/newslist";
    public String GET_GIFTS = BASE_URL + "apiv2/getGifts";
    public String REDEEM_GIFTS = BASE_URL + "apiv2/gifts_redeem";
    public String REDEEM_HISTORY = BASE_URL + "apiv2/redeem_history";
    public String GET_USER_DATA = BASE_URL + "apiv2/getuserdetailswithMobile";
    public String REGISTER_URL = BASE_URL + "apiv2/registration";
    public String OTP_VERIFY_URL = BASE_URL + "apiv2/OTP_verification";
    public String GET_DEALERS = BASE_URL + "apiv2/getDealerswithState";
    public String ASSIGN_DEALERS = BASE_URL + "apiv2/assignDealers";
    public String GET_LOYALTY_POINTS = BASE_URL + "apiv2/getLoyalityPoints";
    public String GET_POINTS_HISTORY = BASE_URL + "apiv2/transaction_history";
    public String GET_PROFILE = BASE_URL + "apiv2/getProfile";
    public String GET_RETAILERS = BASE_URL + "apiv2/getRetailerswithState";
    public String UPLOAD_IMAGE = BASE_URL + "apiv2/upload_shop_images";
    public String SUBMIT_POINTS = BASE_URL + "apiv2/issueLoyalityPoints";
    public String UPLOADED_IMAGE = BASE_URL + "apiv2/last_uploaded_image";
    public String GET_MY_DEALERS = BASE_URL + "apiv2/myDealers";
    public String UPDATE_PROFILE = BASE_URL + "apiv2/profile_updation";
    public String UPDATE_MOBILE = BASE_URL + "apiv2/phoneUpdateOTP";
    public String NEW_REGISTER = BASE_URL + "apiv2/newRegRequest";
    public String GET_STATE = BASE_URL + "apiv2/getstate";
    public String GET_DISTRICT = BASE_URL + "apiv2/getdistrict";
    public String OTP_RESEND_URL = BASE_URL + "apiv2/resend_otp";
    public String GET_APP_VERSION_URL = BASE_URL + "apiv2/loyalty_appversion";
    public String GET_DATA = BASE_URL + "apiv2/fetchUserData";
    public String DEVICE_REGISTRATION_API = BASE_URL + "apiv2/device_registration";
    public String ADD_TO_CART = BASE_URL + "apiv2/addGiftsCartItem";
    public String GET_CART_COUNT = BASE_URL + "apiv2/giftCartCount";
    public String GET_MY_CART = BASE_URL + "apiv2/GiftcartList";
    public String EDIT_MY_CART = BASE_URL + "apiv2/updateGiftCart";
    public String DELETE_MY_CART = BASE_URL + "apiv2/deleteGiftCart";
    public String PLACE_ORDER = BASE_URL + "apiv2/giftsPlaceorder";
    public String REDEEM_LIST_URL = BASE_URL + "apiv2/redeem_history";
    public String GET_NOTIFICATIONS = BASE_URL + "apiv2/NotificationsList";
    public String GET_IMAGE_HISTORY = BASE_URL + "apiv2/uploaded_images_history";
    public String DELETE_IMAGE = BASE_URL + "apiv2/delete_uploaded_images";
    public String GET_DEALERS_SUBDEALER = BASE_URL + "apiv2/myDealers_Subdealers";
    public String GET_SUBDEALER_RETAILERS = BASE_URL + "apiv2/subDealersGiftRedeemedList";
    public String PLACE_ORDER_SUBDEALER = BASE_URL + "apiv2/giftsPlaceorderBySubDealer";
    public String REDEEM_HISTORY_SUBDEALER = BASE_URL + "apiv2/redeem_historyForSubdealer";
    //
}

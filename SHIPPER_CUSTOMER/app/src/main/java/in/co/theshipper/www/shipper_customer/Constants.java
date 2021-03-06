package in.co.theshipper.www.shipper_customer;

public final class Constants {
    public static final class Config{
        protected static final String ROOT_PATH = "http://theshipper.ml/";
//        public static final String ROOT_PATH = "http://52.77.190.248/";
//        public static final String ROOT_PATH = "http://192.168.0.100/loader_mobile/";
//        public static final String ROOT_PATH = "http://192.168.0.101/loader_mobile/";
//        public static final String ROOT_PATH = "http://192.168.0.102/loader_mobile/";
//        public static final String ROOT_PATH = "http://192.168.0.103/loader_mobile/";
//        public static final String ROOT_PATH = "http://192.168.0.104/loader_mobile/";
//        public static final String ROOT_PATH = "http://192.168.1.5/loader_mobile/";

        protected static final int UPDATE_CUSTOMER_LOCATION_DELAY = 0*10000;
        protected static final int UPDATE_CUSTOMER_LOCATION_PERIOD = 30*1000;
        protected static final int GET_DRIVER_LOCATION_DELAY = 0*10000;
        protected static final int GET_DRIVER_LOCATION_PERIOD = 30*1000;


        protected static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
        protected static final long MIN_TIME_BW_UPDATES = 0;
        protected static final long MIN_DATE_DURATION = 1*1000;
        protected static final long MAX_DATE_DURATION = 6*24*60*60*1000;
        protected static final String SUPPORT_CONTACT = "08276097972";
        protected static final int NAME_FIELD_LENGTH = 50;
        protected static final int ADDRESS_FIELD_LENGTH = 50;
        protected static final int DELAY_AFTER_TOAST = 10;
        protected static final int DELAY_LOCATION_CHECK = 1*1000;
        protected static final float MAP_HIGH_ZOOM_LEVEL = 14;
        protected static final float MAP_MID_ZOOM_LEVEL = 13;
        protected static final float MAP_SMALL_ZOOM_LEVEL = 12;
        protected static final String CURRENT_FRAG_TAG = "current_fragment";
        protected static final int FLASH_TO_MAIN_DELAY = 2*1000;
        protected static final int GPS_INTERVAL = 2*1000;
        protected static final int GPS_FASTEST_INTERVAL = 1*1000;
        protected static final int PROGRESSBAR_DELAY = 2*1000;
    }
    public static final class Message{
        protected static final String NEW_USER_ENTER_DETAILS = "Please enter your details";
        protected static final String NO_CURRENT_BOOKING = "No Current Booking";
        protected static final String VEHICLE_ALLOCATION_PENDING = "Vehicle Allocation Pending";
        protected static final String DRIVER_FOUND = "Driver Found";
        protected static final String NETWORK_ERROR = "Unable to connect to server.Check your Internet Connection";
        protected static final String SERVER_ERROR = "Server not responding to request";
        protected static final String GPS_NOT_ENABLED = "GPS not enabled !!";
        protected static final String INTERNET_NOT_ENABLED = "Internet not enabled!!";
        protected static final String CONNECTING = "Connecting...";
        protected static final String LOADING = "Loading...";
        protected static final String OTP_VERIFICATION_ERROR = "OTP could not be verified";
        protected static final String FORM_ERROR = "Form contains error";
    }
    public static final class Title{
        protected static final String NETWORK_ERROR = "NETWORK ERROR";
        protected static final String SERVER_ERROR = "SERVER ERROR";
        protected static final String OTP_VERIFICATION_ERROR = "VERIFICATION ERROR";

    }
}

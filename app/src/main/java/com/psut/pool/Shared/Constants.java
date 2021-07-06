package com.psut.pool.Shared;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Constants {
    //Errors:
    public static final String VALID_PHONE_NUMBER = "Please Enter a Valid Phone Number";
    public static final String LIMIT_EXCEEDED = "Limit Exceeded";
    public static final String INVALID_CODE = "Invalid Code";
    public static final String WENT_WRONG = "Something Went Wrong";
    public static final String NOT_VALID_INPUT = "Please Enter a Valid value";

    //Keys:
    public static final String INTENT_PHONE_NUMBER_KEY = "Phone Number";
    public static final String INTENT_ID = "phonenumber";
    public static final String INTENT_ID_STRING = "isDriver";
    public static final String SHARED_ID = "ID";
    public static final Integer ERROR_DIALOG_REQUEST = 9001;
    public static final Integer LOCATION_PERMISSION_REQUEST_CODE = 9002;
    public static final Integer LOCATION_AUTO_COMPLETE_REQUEST_CODE = 1010;
    public static final String COUNTRY_ID = "jo";
    public static final String MARKER_LAYOUT = "marker layout";
    public static final String MARKER_ID = "marker id";
    public static final String KEY = "Key";
    public static final String API_KEY = "AIzaSyAviOlzcFhIac8VYwlXJ8g__oLjoVlfE2w";
    public static final String PLEASE_RATE_TRIPS = "Please Rate Trips";
    //Maps:
    public static final Float DEFAULT_ZOOM = 15f;

    //Texts:
    public static final String TRUN_LOCATION_ON = "Please Turn on Your Location";
    public static final String WELCOME = "Welcome";
    public static final String NEAREST_DRIVER = "Nearest Driver";
    public static final String DRIVER_REQUEST = "Request";
    public static final String REQUEST_SENT = "Request Sent to ";
    public static final String RESPONSE_DENYED = "Response Denyed";
    public static final String REQUEST_CONFIRMED = "Request Confirmed";
    public static final String REQUEST_DENYED = "Request Denyed by Driver";
    public static final String DRIVER_IS_ON_HIS_WAY = "Driver is on his Way";
    public static final String DELETE_REQUEST = "Delete Request";
    public static final String DELETED_REQUEST = "Request Deleted ";
    public static final String DENY_REQUEST = "Request Denyed";
    public static final String DENYED = "Denyed";
    public static final String CONFIRMED = "Confirmed";
    public static final String DESTINATION = "Destination";
    public static final String CONFIRM_RIDE = "Confirm Ride";
    public static final String YOU_ARE_HERE = "You're Here";
    public static final String JUST_A_MIN = "Just a Moment and Try Again";
    public static final String TRUE = Boolean.valueOf(true).toString();
    public static final String FALSE = Boolean.valueOf(false).toString();
    public static final String COST_WILL_BE = "Cost Will Be = ";
    public static final String JD = " JD";
    public static final String SPECIAL = "Special";
    public static final String NO_ROUTE_EXIST = "No routes exist";
    public static final String STATUS_DRIVING_MOVING = "Moving";
    public static final String STATUS_DRIVING_STARTING = "Starting";
    public static final String STATUS_DRIVING_WAITING = "Waiting";
    public static final String DRIVER_DRIVING = "Driving";
    public static final String STATUS_USER_OFFLINE = "Offline";
    public static final String STATUS_USER_ONILINE = "Online";
    public static final String NO_HISTORY = "No History Was Found";
    public static final String PLEASE_RATE_THE_TRIP = "Please Rate The Trip";
    public static final String YOU_HAVE_REQUEST = "You have a request";

    //Requests:
    public static final String CUSTOMER_ID = "Customer ID";
    public static final String CUSTOMER_PHONE_NUMBER = "Customer Phone Number";
    public static final String CUSTOMER_CURRENT_LOCATION = "Customer Location";
    public static final String CUSTOMER_CURRENT_LOCATION_PICKUP_NAME = "Customer Location Pick up Name";
    public static final String CUSTOMER_CURRENT_LOCATION_DROP_NAME = "Customer Location Drop Off Name";
    public static final String CUSTOMER_CURRENT_LATITUDE = "Customer Latitude";
    public static final String CUSTOMER_CURRENT_LONGITIUDE = "Customer Longitude";
    public static final String DESTINATION_LATITUDE = "Destination Latitude";
    public static final String DESTINATION_LONGITIUDE = "Destination Longitude";
    public static final String CUSTOMER_NAME = "Customer Name";
    public static final String DRIVER_ID = "Driver ID";
    public static final String DRIVER_PHONE_NUMBER = "Driver Phone Number";
    public static final String DRIVER_CURRENT_LOCATION = "Driver Location";

    //Database:
    public static final String DATABASE_USERS = "users";
    public static final String DATABASE_NAME = "name";
    public static final String DATABASE_EMAIL = "email";
    public static final String DATABASE_DATE = "date";
    public static final String DATABASE_UNI_ID = "university_id";
    public static final String DATABASE_ADDRESS = "address";
    public static final String DATABASE_PREFERRED = "preferred";
    public static final String DATABASE_GENDER = "gender";
    public static final String DATABASE_PHONE_NUMBER = "phone_number";
    public static final String DATABASE_IS_DRIVER = "driver";
    public static final String DATABASE_USER_STATUS = "status";
    public static final String DATABASE_TRIP_STATUS = "trip_status";
    public static final String DATABASE_USER_CURRENT_LOCATION = "current_location";
    public static final String DATABASE_USER_CURRENT_LATITUDE = "current_latitude";
    public static final String DATABASE_USER_CURRENT_LONGITUDE = "current_longitude";
    public static final String DATABASE_DRIVERS_LOCATIONS = "drivers_locations";
    public static final String DATABASE_TRIP = "trip";
    public static final String DATABASE_DRIVER_NAME = "driver_name";
    public static final String DATABASE_DRIVER_ID = "driver_id";
    public static final String DATABASE_DRIVER_CAR_ID = "car_id";
    public static final String DATABASE_CAR_TYPE = "car_type";
    public static final String DATABASE_CAR_MODEL = "car_model";
    public static final String DATABASE_CAR_COLOR = "car_color";
    public static final String DATABASE_REQUESTS = "requests";
    public static final String DATABASE_RESPONSE = "response";
    public static final String DATABASE_AMOUNT = "amount";
    public static final String DATABASE_TRIP_RANK = "trip_rank";
    public static final String DATABASE_PICK_UP_LOCATION = "pick_up_location";
    public static final String DATABASE_DROP_OFF_LOCATION = "drop_off_location";
    public static final String DATABASE_DISTANCE = "distance";
    public static final String DATABASE_DURATION = "duration";
    public static final String DATABASE_DESCRIPTION = "description";
    public static final String DATABASE_REQUEST = "request";
    public static final String DATABASE_RATING = "rating";
    public static final String DATABASE_NUMBER = "number_of_pepole ";
    public static final String DATABASE_COST = "cost";
    public static final String DATABASE_VALUE = "value";
    public static final String DATABASE_ACCOUNT_TYPE = "account_type";
    public static final String DATABASE_TRIP_NUMBER = "trip_number";

    //Uni MapSetup:
    public static final Float UNI_MAP_DEFAULT_ZOOM = 17.5f;
    public static final LatLng UNI_MAP_MAIN = new LatLng(32.02324, 35.87611);
    public static final LatLng UNI_MAP_KING_ABDULLAH_II_SCHOOL_FOR_ELECTRICAL_ENGINEERING = new LatLng(32.02331, 35.87672);
    private static final LatLng UNI_MAP_RESTRICT1 = new LatLng(32.02375, 35.87918);
    private static final LatLng UNI_MAP_RESTRICT2 = new LatLng(32.02138, 35.87676);
    private static final LatLng UNI_MAP_RESTRICT3 = new LatLng(32.02301, 35.87337);
    private static final LatLng UNI_MAP_MAIN_ENTRANCE = new LatLng(32.02253, 35.87535);
    private static final LatLng UNI_MAP_STUDENT_AFFAIR = new LatLng(32.02249, 35.87614);
    private static final LatLng UNI_MAP_AL_WAFAA_BUILDING = new LatLng(32.02278, 35.87626);
    public static final LatLng UNI_MAP_KING_HUSSEIN_SCHOOL_FOR_INFORMATION_TECHNOLOGY = new LatLng(32.02353, 35.87596);
    private static final LatLng UNI_MAP_PSUT_PRESIDENTIAL_BUILDING = new LatLng(32.02347, 35.87631);
    public static final LatLng UNI_MAP_PSUT_EL_HASSAN_LIBRARY = new LatLng(32.02407, 35.8765);
    public static final LatLng UNI_MAP_BUILDING_B = new LatLng(32.02357, 35.87539);
    private static final LatLng UNI_MAP_IT_ENTER = new LatLng(32.02442, 35.87635);
    private static final LatLng UNI_MAP_ENG_ENTER = new LatLng(32.02191, 35.87646);
    public static final LatLng UNI_MAP_BUILDING_D = new LatLng(32.02375, 35.87567);
    //University Map:
    private static final LatLng UNI_MAP_RESTRICT0 = new LatLng(32.02483, 35.87548);

    public static Map<String, LatLng> toLocationMap() {
        HashMap<String, LatLng> places = new HashMap<>();
        places.put("Main Entrance", UNI_MAP_MAIN_ENTRANCE);
        places.put("Princess Sumaya University for Technology", UNI_MAP_MAIN);
        places.put("Students Affairs Deanship", UNI_MAP_STUDENT_AFFAIR);
        places.put("Al Wafa'a Building", UNI_MAP_AL_WAFAA_BUILDING);
        places.put("King Abdullah II School for Electrical Engineering", UNI_MAP_KING_ABDULLAH_II_SCHOOL_FOR_ELECTRICAL_ENGINEERING);
        places.put("PSUT Presidential Building", UNI_MAP_PSUT_PRESIDENTIAL_BUILDING);
        places.put("King Hussein School for Information Technology", UNI_MAP_KING_HUSSEIN_SCHOOL_FOR_INFORMATION_TECHNOLOGY);
        places.put("2nd Entrance", UNI_MAP_ENG_ENTER);
        places.put("PSUT El Hassan Library", UNI_MAP_PSUT_EL_HASSAN_LIBRARY);
        places.put("IT Entrance", UNI_MAP_IT_ENTER);
        places.put("Builing B", UNI_MAP_BUILDING_B);
        places.put("Builing D", UNI_MAP_BUILDING_D);
        return places;
    }

    public static ArrayList<LatLng> restrictionList() {
        ArrayList<LatLng> restrict = new ArrayList<>();
        restrict.add(UNI_MAP_RESTRICT0);
        restrict.add(UNI_MAP_RESTRICT1);
        restrict.add(UNI_MAP_RESTRICT2);
        restrict.add(UNI_MAP_RESTRICT3);
        return restrict;
    }

    public static String description(String oirgin, String dest) {
        return "Ride From : " + oirgin + " to " + dest + ":::" + DateTime.now().toString();
    }

}

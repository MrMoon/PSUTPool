package com.psut.pool.Shared;

import com.google.android.gms.maps.model.LatLng;

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
    public static final String SHARED_ID = "ID";
    public static final Integer ERROR_DIALOG_REQUEST = 9001;
    public static final Integer LOCATION_PERMISSION_REQUEST_CODE = 9002;
    public static final Integer LOCATION_AUTO_COMPLETE_REQUEST_CODE = 1010;

    //Maps:
    public static final Float DEFAULT_ZOOM = 15f;

    //Texts:
    public static final String TRUN_LOCATION_ON = "Please Turn on Your Location";
    public static final String WELCOME = "Welcome";
    public static final String NEAREST_DRIVER = "Nearest Driver";
    public static final String DESTINATION = "Destination";

    //Database:
    public static final String DATABASE_USERS = "Users";
    public static final String DATABASE_NAME = "Name";
    public static final String DATABASE_EMAIL = "E-Mail";
    public static final String DATABASE_UNI_ID = "University ID";
    public static final String DATABASE_ADDRESS = "Address";
    public static final String DATABASE_PREFERRED = "Preferred";
    public static final String DATABASE_GENDER = "Gender";
    public static final String DATABASE_PHONE_NUMBER = "Phone Number";
    public static final String DATABASE_IS_DRIVER = "Driver";
    public static final String DATABASE_USER_STATUS = "Status";
    public static final String DATABASE_USER_CURRENT_LOCATION = "Current Location";
    public static final String DATABASE_USER_CURRENT_LATITUDE = "Current Latitude";
    public static final String DATABASE_USER_CURRENT_LONGITUDE = "Current Longitude";
    public static final String DATABASE_DRIVERS_LOCATIONS = "Drivers Locations";
    public static final String DATABASE_DRIVER_CAR_ID = "Car ID";
    public static final String DATABASE_CAR_TYPE = "Car Type";
    public static final String DATABASE_CAR_MODEL = "Car Model";
    public static final String DATABASE_CAR_COLOR = "Car Color";
    //Uni Map:
    public static final Float UNI_MAP_DEFAULT_ZOOM = 17.5f;
    public static final LatLng UNI_MAP_MAIN = new LatLng(32.02324, 35.87611);
    //Restriction:
    private static final LatLng UNI_MAP_RESTRICT0 = new LatLng(32.02483, 35.87548);
    private static final LatLng UNI_MAP_RESTRICT1 = new LatLng(32.02375, 35.87918);
    private static final LatLng UNI_MAP_RESTRICT2 = new LatLng(32.02138, 35.87676);
    private static final LatLng UNI_MAP_RESTRICT3 = new LatLng(32.02301, 35.87337);
    private static final LatLng UNI_MAP_MAIN_ENTRANCE = new LatLng(32.02253, 35.87535);
    private static final LatLng UNI_MAP_STUDENT_AFFAIR = new LatLng(32.02249, 35.87614);
    private static final LatLng UNI_MAP_AL_WAFAA_BUILDING = new LatLng(32.02278, 35.87626);
    private static final LatLng UNI_MAP_KING_ABDULLAH_II_SCHOOL_FOR_ELECTRICAL_ENGINEERING = new LatLng(32.02331, 35.87672);
    private static final LatLng UNI_MAP_PSUT_PRESIDENTIAL_BUILDING = new LatLng(32.02347, 35.87631);
    private static final LatLng UNI_MAP_KING_HUSSEIN_SCHOOL_FOR_INFORMATION_TECHNOLOGY = new LatLng(32.02353, 35.87596);
    private static final LatLng UNI_MAP_PSUT_EL_HASSAN_LIBRARY = new LatLng(32.02407, 35.8765);
    private static final LatLng UNI_MAP_IT_ENTER = new LatLng(32.02442, 35.87635);
    private static final LatLng UNI_MAP_ENG_ENTER = new LatLng(32.02191, 35.87646);

    public static Map<String, LatLng> toLocationMap() {
        HashMap<String, LatLng> places = new HashMap<>();
        places.put("Main Entrance", Constants.UNI_MAP_MAIN_ENTRANCE);
        places.put("Princess Sumaya University for Technology", Constants.UNI_MAP_MAIN);
        places.put("Students Affairs Deanship", Constants.UNI_MAP_STUDENT_AFFAIR);
        places.put("Al Wafa'a Building", Constants.UNI_MAP_AL_WAFAA_BUILDING);
        places.put("King Abdullah II School for Electrical Engineering", Constants.UNI_MAP_KING_ABDULLAH_II_SCHOOL_FOR_ELECTRICAL_ENGINEERING);
        places.put("PSUT Presidential Building", Constants.UNI_MAP_PSUT_PRESIDENTIAL_BUILDING);
        places.put("King Hussein School for Information Technology", Constants.UNI_MAP_KING_HUSSEIN_SCHOOL_FOR_INFORMATION_TECHNOLOGY);
        places.put("2nd Entrance", Constants.UNI_MAP_ENG_ENTER);
        places.put("PSUT El Hassan Library", Constants.UNI_MAP_PSUT_EL_HASSAN_LIBRARY);
        places.put("IT Entrance", Constants.UNI_MAP_IT_ENTER);
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

}

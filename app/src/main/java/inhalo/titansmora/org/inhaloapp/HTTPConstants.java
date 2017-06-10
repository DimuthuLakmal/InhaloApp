package inhalo.titansmora.org.inhaloapp;

/**
 * Created by kjtdi on 6/8/2017.
 */
public class HTTPConstants {

    private static final String ROOT_URL = "http://192.168.175.1:3000/";

    public static final String URL_REGISTER = ROOT_URL+"users/adduser";
    public static final String URL_LOGIN = ROOT_URL+"users/login";
    public static final String URL_REREIVE_USER_DATA = ROOT_URL+"users/basic/userId/";
    public static final String URL_UPDATE_BASIC_USER_DATA = ROOT_URL+"users/basic/update";
    public static final String URL_UPDATE_HIGHESTPEF_USER_DATA = ROOT_URL+"users/highestPEF/update";

    public static final String URL_ADD_CONTACTS = ROOT_URL+"contacts/add";
    public static final String URL_RETREIVE_CONTACTS = ROOT_URL+"contacts/userId/";
    public static final String URL_UPDATE_CONTACTS = ROOT_URL+"contacts/update";
    public static final String URL_DELETE_CONTACTS = ROOT_URL+"contacts/delete";

    public static final String URL_ADD_ALLERGENS = ROOT_URL+"allergens/add";
    public static final String URL_RETREIVE_ALLERGENS = ROOT_URL+"allergens/userId/";
    public static final String URL_UPDATE_ALLERGENS = ROOT_URL+"allergens/update";
    public static final String URL_DELETE_ALLERGENS = ROOT_URL+"allergens/delete";

    public static final String URL_ADD_INHALER = ROOT_URL+"inhalers/add";
    public static final String URL_RETREIVE_INHALER = ROOT_URL+"inhalers/userId/";
    public static final String URL_UPDATE_INHALER = ROOT_URL+"inhalers/update";
    public static final String URL_DELETE_INHALER = ROOT_URL+"inhalers/delete";

    public static final String URL_ADD_DAILY_DATA_BASIC = ROOT_URL+"dailydata/add/basic";
    public static final String URL_RETREIVE_DAILY_DATA = ROOT_URL+"dailydata/userId/";
    public static final String URL_UPDATE_DATA_BASIC = ROOT_URL+"dailydata/update/basic";

    public static final String URL_ADD_DAILY_DATA_PEF = ROOT_URL+"dailydata/update/pef";

}

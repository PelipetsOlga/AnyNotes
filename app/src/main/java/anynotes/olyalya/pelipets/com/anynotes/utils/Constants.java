package anynotes.olyalya.pelipets.com.anynotes.utils;

public class Constants {
    public final static String TAG = "anynotes_tag";
    public static final String EXTRA_ACTION_TYPE = "action_type";
    public static final String EXTRA_NOTE = "note";
    public static final int EXTRA_ACTION_NEW_NOTE = 33;
    public static final int EXTRA_ACTION_EDIT_NOTE = 66;
    public static final String EXTRA_TIME_DATE = "time_date";
    public static final String EXTRA_REPEAT = "repeat";
    public static final String EXTRA_OPEN_CURRENT_NOTE = "open_current_note";

    public static final int STATUS_DRAFT = 1;
    public static final int STATUS_DRAFT_DELETED = 5;
    public static final int STATUS_ACTUAL = 2;
    public static final int STATUS_IMPORTANT = 3;
    public static final int STATUS_DELETED = 4;

    public static final int MODE_SORT_ALL = 1;
    public static final int MODE_SORT_IMPORTANTS = 2;
    public static final int MODE_SORT_ACTUALS = 3;
    public static final int MODE_SORT_DRAFTS = 4;
    public static final int MODE_SORT_DELETED = 5;
    public static final int MODE_SORT_ALARMS = 6;
    public static final int MODE_SORT_SEARCH = 7;

    public static final int MODE_ORDERED_UNSORTED = 1;
    public static final int MODE_ORDERED_SORT_ALPHA_ASC = 2;
    public static final int MODE_ORDERED_SORT_ALPHA_DESC = 3;
    public static final int MODE_ORDERED_SORT_DATE_ASC = 4;
    public static final int MODE_ORDERED_SORT_DATE_DESC = 5;

    public static final String TEST_DEVICE_A = "4028FDD84874526BA9EC2EDDF86BD389";//7562
    public static final String TEST_DEVICE_B = "EC974FC89B02C966F878083846A7AE5B"; //5110
    public static final String TEST_DEVICE_C = "8ABA2DDDFF7842874E9FBAC5B668DDBC"; //M700
    public static final String TEST_DEVICE_D = "C47B0844075E4129BF541BE3792DCD3D";//lenovo tab2
    public static final String TEST_DEVICE_E = "B686CDA2AA5466E0F4C9B63DE9E8B368";//nexus5
    public static final String TEST_DEVICE_G = "86D72F707C2EDC7D0A97028DC211974B";//samsung G360H

    public static final String BAAS_APP_ID = "88936601-BFF8-0D16-FF3D-721E9334BF00";
    public static final String BAAS_APP_SECRET = "1679EEF9-0050-E314-FFEB-38620E00EC00";
    public static final String BAAS_APP_VERSION = "v1";
    public static final String BAAS_USER_EXIST_ERROR = "3033";

    public static final String PREF_RINGTONE = "ringtone";
    public static final String PREF_FONT_SIZE = "font_size";
    public static final String PREF_BRIGHTNESS = "brightness";
    public static final String PREFS_NAME = "anynotes_prefs";
    public static final String PREF_VIBRO = "vibro";
    public static final String PREF_SORT = "sort";
    public static final String PREF_SEARCH = "search";
    public static final String PREF_IS_LOGINED = "is_logined";
    public static final String PREF_USER_OBJECT_ID = "user_object_id";
    public static final String PREF_LOGIN = "login";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_LAST_SYNCH = "last_synch";
    public static final int PREF_SORT_UNSORT = 100;
    public static final int PREF_SORT_AL_ASC = 101;
    public static final int PREF_SORT_AL_DESC = 102;
    public static final int PREF_SORT_NUM_ASC = 103;
    public static final int PREF_SORT_NUM_DESC = 104;

    public static final int BRIGHTNESS = 200;
    public static final int SIZE_FONT = 18;
    public static final int LENGTH_TEXT = 300;
    public static final int TITLE_LENGTH = 80;
    public static final String FORMAT_ALARM = "dd/MM/yyyy, kk:mm";
    public static final String FORMAT_DATE_CREATING = "yyyy-MM-dd kk:mm";
}

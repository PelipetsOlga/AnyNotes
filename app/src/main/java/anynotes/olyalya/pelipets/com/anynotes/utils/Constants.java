package anynotes.olyalya.pelipets.com.anynotes.utils;

/**
 * Created by Olga on 26.12.2015.
 */
public class Constants {
    public final static String TAG = "tag";
    public static final String EXTRA_ACTION_TYPE = "action_type";
    public static final String EXTRA_NOTE = "note";
    public static final int EXTRA_ACTION_NEW_NOTE = 33;
    public static final int EXTRA_ACTION_EDIT_NOTE = 66;
    public static final String EXTRA_LISTENER = "listener";
    public static final String EXTRA_CREATING = "creating";
    public static final String EXTRA_NOTE_CONTENT = "note_content";
    public static final String EXTRA_NOTE_TITLE = "note_title";
    public static final String EXTRA_TIME_DATE = "time_date";
    public static final String EXTRA_REPEAT = "repeat";

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

    public static final int MODE_ORDERED_UNSORTED = 1;
    public static final int MODE_ORDERED_SORT_ALPHA_ASC = 2;
    public static final int MODE_ORDERED_SORT_ALPHA_DESC = 3;
    public static final int MODE_ORDERED_SORT_DATE_ASC = 4;
    public static final int MODE_ORDERED_SORT_DATE_DESC = 5;

    public static final String TEST_DEVICE_A = "B605DFC1B0C341FC81584E6AED7DE278";//7562
    public static final String TEST_DEVICE_B = "EC974FC89B02C966F878083846A7AE5B"; //5110
    public static final String TEST_DEVICE_C = "DB2A12672D0ED448B37FCB499FFF50B7"; //M700
    public static final String TEST_DEVICE_D = "C47B0844075E4129BF541BE3792DCD3D";//lenovo tab2
    public static final String TEST_DEVICE_E = "B686CDA2AA5466E0F4C9B63DE9E8B368";//nexus5
    public static final String TEST_DEVICE_G = "86D72F707C2EDC7D0A97028DC211974B";//samsung G360H


    public static final String PREF_RINGTONE = "ringtone";
    public static final String PREF_FONT_SIZE = "font_size";
    public static final String PREF_BRIGHTNESS = "brightness";
    public static final String PREFS_NAME = "anynotes_prefs";
    public static final String PREF_VIBRO = "vibro";
    public static final int BRIGHTNESS = 200;
    public static final int SIZE_FONT = 18;
    public static final int LENGTH_TEXT = 300;
    public static final int TITLE_LENGTH = 80;
    public static final String FORMAT_ALARM = "d/M/yyyy, HH:mm";
}

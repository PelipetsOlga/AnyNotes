package anynotes.olyalya.pelipets.com.anynotes.storage;

/**
 * Created by Olga on 26.12.2015.
 */
public class DBSchema {
    public static final String TABLE = "NOTES";

    public static final String ID = "_id";
    public static final String CREATING = "creating";
    public static final String LAST_SAVING = "last_saving";
    public static final String PRE_LAST_SAVING = "pre_last_saving";
    public static final String TEXT = "text";


    public static final String CREATE_SCREAPT = "CREATE TABLE IF NOT EXISTS " + TABLE + "("
            + ID + " integer primary key autoincrement, "
            + CREATING + " integer, "
            + LAST_SAVING + " integer, "
            + PRE_LAST_SAVING + " integer, "
            + TEXT + " text);";

}

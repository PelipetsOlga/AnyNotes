package anynotes.olyalya.pelipets.com.anynotes.storage;

/**
 * Created by Olga on 26.12.2015.
 */
public class DBSchema {
    public static final String TABLE = "NOTES";

    public static final String ID = "_id";
    public static final String OBJECT_ID = "objectId";
    public static final String OWNER_ID = "ownerId";
    public static final String CREATING = "creating";
    public static final String LAST_SAVING = "last_saving";
    public static final String STATUS = "status";
    public static final String TEXT = "text";
    public static final String TITLE = "title";
    public static final String ALARM = "alarm";
    public static final String REPEAT = "repeat";


    public static final String CREATE_SCREAPT = "CREATE TABLE IF NOT EXISTS " + TABLE + "("
            + ID + " integer primary key autoincrement, "
            + OBJECT_ID + " text, "
            + OWNER_ID + " text, "
            + CREATING + " integer, "
            + LAST_SAVING + " integer, "
            + STATUS + " integer, "
            + TITLE + " text, "
            + ALARM + " text, "
            + REPEAT + " integer, "
            + TEXT + " text);";

}

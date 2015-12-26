package anynotes.olyalya.pelipets.com.anynotes.models;

/**
 * Created by Olga on 26.12.2015.
 */
public class Note {
    private long id;
    private long creating;
    private long lastSaving;
    private long preLastSaving;
    private String text;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreating() {
        return creating;
    }

    public void setCreating(long creating) {
        this.creating = creating;
    }

    public long getLastSaving() {
        return lastSaving;
    }

    public void setLastSaving(long lastSaving) {
        this.lastSaving = lastSaving;
    }

    public long getPreLastSaving() {
        return preLastSaving;
    }

    public void setPreLastSaving(long preLastSaving) {
        this.preLastSaving = preLastSaving;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package anynotes.olyalya.pelipets.com.anynotes.models;

import java.io.Serializable;

public class Note implements Serializable {
    private long id;
    private long creating;
    private long lastSaving;
    private int status;
    private String title;
    private String text;
    private String alarm;
    private long repeat;
    private String objectId;
    private String ownerId;

    public Note() {
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public long getRepeat() {
        return repeat;
    }

    public void setRepeat(long repeat) {
        this.repeat = repeat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreating() {
        return creating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
